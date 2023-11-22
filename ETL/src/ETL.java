    import java.sql.*;
    import org.apache.commons.collections4.MultiValuedMap;
    import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
    import java.util.*;
    import java.util.Date;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import java.math.BigDecimal;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;

    public class ETL {

        private static final int CHUNK_SIZE = 1000;
        private static final Logger logger = Logger.getLogger(ETL.class.getName());
        private List<MasterData> diskBuffer = new ArrayList<>();
        private customQueue<Transaction> transactionQueue = new customQueue<>();
        private MultiValuedMap<Integer, Transaction> multiHashTable;
        private int totalTransactionsProcessed = 0;

        public ETL() {
            this.multiHashTable = new ArrayListValuedHashMap<>();
        }

        public int loadDateDimension(String orderDate, Connection dwhConn) {
            SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yy HH:mm");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yy");
            int dateID = -1;

            try {
                Date parsedDate = inputFormatter.parse(orderDate);
                String fullDateStr = outputFormatter.format(parsedDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(parsedDate);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                int quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);

                String DateQuery = "INSERT INTO Date (day, week, month, quarter, year, full_date) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = dwhConn.prepareStatement(DateQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setInt(1, day);
                    insertStmt.setInt(2, week);
                    insertStmt.setInt(3, month);
                    insertStmt.setInt(4, quarter);
                    insertStmt.setInt(5, year);
                    insertStmt.setString(6, fullDateStr);
                    insertStmt.executeUpdate();

                    // Retrieve the generated dateID
                    ResultSet rs = insertStmt.getGeneratedKeys();
                    if (rs.next()) {
                        dateID = rs.getInt(1);
                    }
                }
            } catch (ParseException | SQLException e) {
                e.printStackTrace();
            }
            return dateID;
        }

        public void readData() {
            boolean dataAvailable = true;
            int offset = 0;
            String query = "SELECT orderID, OrderDate, ProductID, CustomerID, CustomerName, Gender, quantityOrdered FROM transactions LIMIT ? OFFSET ?";

            while (dataAvailable) {
                try (Connection conn = DatabaseConnector.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, CHUNK_SIZE);
                    stmt.setInt(2, offset);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.isBeforeFirst()) {
                        dataAvailable = false;
                        break;
                    }

                    while (rs.next()) {
                        Transaction transaction = new Transaction(
                                rs.getInt("orderID"),
                                rs.getString("OrderDate"),
                                rs.getInt("ProductID"),
                                rs.getInt("CustomerID"),
                                rs.getString("CustomerName"),
                                rs.getString("Gender"),
                                rs.getInt("QuantityOrdered")
                        );

                        try (Connection dwhConn = DatabaseConnector.getDWHConnection()) {
                            loadDateDimension(transaction.getOrderDate(), dwhConn);
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "Error loading date dimension", e);
                        }

                        System.out.println("Incoming Tuple: " + transaction);
                        transactionQueue.enqueue(transaction.getProductID(), transaction);
                        multiHashTable.put(transaction.getProductID(), transaction);
                        loadDiskBuffer(transaction.getProductID());
                        hybridJoin();
                    }

                    // Increment offset to fetch the next chunk
                    offset += CHUNK_SIZE;
                } catch (SQLException e) {
                    e.printStackTrace();
                    dataAvailable = false;
                }
            }
        }

        public void loadDiskBuffer(int productID) {
            String query = "SELECT * FROM master_data WHERE productID = ? LIMIT 10";

            try (Connection conn = DatabaseConnector.getMDConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, productID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    MasterData md = new MasterData(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getDouble("productPrice"),
                            rs.getInt("supplierID"),
                            rs.getString("supplierName"),
                            rs.getInt("storeID"),
                            rs.getString("storeName")
                    );
                    diskBuffer.add(md);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        private void hybridJoin() {
            List<JoinedData> joinedDataList = new ArrayList<>();

            try (
                 Connection dwhConn = DatabaseConnector.getDWHConnection()) {

                // Loop through each MasterData object in the disk buffer
                for (MasterData md : diskBuffer) {
                    Collection<Transaction> transactionNodes = multiHashTable.get(md.getProductID());
                    if (transactionNodes != null) {
                        for (Transaction transaction : transactionNodes) {
                            JoinedData joinedData = joinData(md, transaction);
                            if (joinedData != null) {
                                System.out.println("MultiHashTable contents for productID " + md.getProductID() + ": " + transactionNodes);
                                joinedDataList.add(joinedData);
                                System.out.println("Joined Data: " + joinedData);
                                totalTransactionsProcessed++;
                                transactionQueue.removeByProductID(md.getProductID());
                            }
                        }
                        multiHashTable.remove(md.getProductID());
                    }
                }

                // Clear the disk buffer after processing all MasterData objects
                diskBuffer.clear();

                loadToDWH(joinedDataList, dwhConn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void printTotalTransactionsProcessed() {
            System.out.println("Total Transactions Processed: " + totalTransactionsProcessed);
        }

        private void loadToDWH(List<JoinedData> joinedDataList, Connection dwhConn) {
            // queries
            String CustomerQuery = "INSERT INTO Customer (customerID, customerName, gender) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE customerName = VALUES(customerName), gender = VALUES(gender)";
            String ProductQuery = "INSERT INTO Product (productID, productName, productPrice) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE productName = VALUES(productName), productPrice = VALUES(productPrice)";
            String SupplierQuery = "INSERT INTO Supplier (supplierID, supplierName) VALUES (?, ?) ON DUPLICATE KEY UPDATE supplierName = VALUES(supplierName)";
            String StoreQuery = "INSERT INTO Store (storeID, storeName) VALUES (?, ?) ON DUPLICATE KEY UPDATE storeName = VALUES(storeName)";
            String SalesQuery = "INSERT INTO Sales (orderID, productID, dateID, customerID, storeID, supplierID, quantityOrdered, totalSale) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement customerStmt = dwhConn.prepareStatement(CustomerQuery);
                 PreparedStatement productStmt = dwhConn.prepareStatement(ProductQuery);
                 PreparedStatement supplierStmt = dwhConn.prepareStatement(SupplierQuery);
                 PreparedStatement storeStmt = dwhConn.prepareStatement(StoreQuery);
                 PreparedStatement salesStmt = dwhConn.prepareStatement(SalesQuery)) {

                for (JoinedData joinedData : joinedDataList) {
                    int dateID = loadDateDimension(joinedData.getOrderDate(), dwhConn);
                    if (dateID == -1) {
                        continue;
                    }
                    customerStmt.setInt(1, joinedData.getCustomerID());
                    customerStmt.setString(2, joinedData.getCustomerName());
                    customerStmt.setString(3, joinedData.getGender());
                    customerStmt.executeUpdate();

                    productStmt.setInt(1, joinedData.getProductID());
                    productStmt.setString(2, joinedData.getProductName());
                    productStmt.setDouble(3, joinedData.getProductPrice());
                    productStmt.executeUpdate();

                    supplierStmt.setInt(1, joinedData.getSupplierID());
                    supplierStmt.setString(2, joinedData.getSupplierName());
                    supplierStmt.executeUpdate();

                    storeStmt.setInt(1, joinedData.getStoreID());
                    storeStmt.setString(2, joinedData.getStoreName());
                    storeStmt.executeUpdate();

                    salesStmt.setInt(1, joinedData.getOrderID());
                    salesStmt.setInt(2, joinedData.getProductID());
                    salesStmt.setInt(3, dateID);
                    salesStmt.setInt(4, joinedData.getCustomerID());
                    salesStmt.setInt(5, joinedData.getStoreID());
                    salesStmt.setInt(6, joinedData.getSupplierID());
                    salesStmt.setInt(7, joinedData.getQuantityOrdered());
                    salesStmt.setBigDecimal(8, joinedData.getTotalSale());

                    salesStmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private JoinedData joinData(MasterData md, Transaction transaction) {
            BigDecimal totalSale = BigDecimal.valueOf(md.getProductPrice())
                    .multiply(BigDecimal.valueOf(transaction.getQuantityOrdered()));

            return new JoinedData(
                    transaction.getOrderID(),
                    transaction.getOrderDate(),
                    md.getProductID(),
                    md.getProductName(),
                    md.getProductPrice(),
                    transaction.getCustomerID(),
                    transaction.getCustomerName(),
                    transaction.getGender(),
                    transaction.getQuantityOrdered(),
                    md.getSupplierID(),
                    md.getSupplierName(),
                    md.getStoreID(),
                    md.getStoreName(),
                    totalSale
            );
        }
    }