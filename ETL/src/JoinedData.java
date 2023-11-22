import java.math.BigDecimal;
import java.util.Date;

public class JoinedData {
    private int orderID;
    private BigDecimal totalSale;
    private String orderDate;
    private int productID;
    private String productName;
    private double productPrice;
    private int customerID;
    private String customerName;
    private String gender;
    private int quantityOrdered;
    private int supplierID;
    private String supplierName;
    private int storeID;
    private String storeName;

    public JoinedData(
            int orderID,
            String orderDate,
            int productID,
            String productName,
            double productPrice,
            int customerID,
            String customerName,
            String gender,
            int quantityOrdered,
            int supplierID,
            String supplierName,
            int storeID,
            String storeName,
            BigDecimal totalSale
    ) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.customerID = customerID;
        this.customerName = customerName;
        this.gender = gender;
        this.quantityOrdered = quantityOrdered;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.storeID = storeID;
        this.storeName = storeName;
        this.totalSale = totalSale;

    }


    @Override
    public String toString() {
        return "JoinedData{" +
                "orderID=" + orderID +
                ", orderDate='" + orderDate + '\'' +
                ", productID=" + productID +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", customerID=" + customerID +
                ", customerName='" + customerName + '\'' +
                ", gender='" + gender + '\'' +
                ", quantityOrdered=" + quantityOrdered +
                ", supplierID=" + supplierID +
                ", supplierName='" + supplierName + '\'' +
                ", storeID=" + storeID +
                ", storeName='" + storeName + '\'' +
                '}';
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getProductID() {
        return productID;
    }

    public String getGender() {
        return gender;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public int getStoreID() {
        return  storeID;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public BigDecimal getTotalSale() {
        return totalSale;
    }

    public String  getOrderDate() {
        return orderDate;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getStoreName() {
        return storeName;
    }
}