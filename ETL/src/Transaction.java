    public class Transaction {
        private int orderID;
        private String orderDate; // Assuming 'Order Date' is stored as a text
        private int productID;
        private int customerID;
        private String customerName;
        private String gender;
        private int quantityOrdered;
        public Transaction(int orderID, String orderDate, int productID, int customerID, String customerName, String gender, int quantityOrdered) {
            this.orderID = orderID;
            this.orderDate = orderDate;
            this.productID = productID;
            this.customerID = customerID;
            this.customerName = customerName;
            this.gender = gender;
            this.quantityOrdered = quantityOrdered;
        }

        public int getOrderID() {
            return orderID;
        }
        public String getOrderDate() {
            return orderDate;
        }
        public int getProductID() {
            return productID;
        }
        public int getCustomerID() {
            return customerID;
        }
        public String getCustomerName() {
            return customerName;
        }
        public String getGender() {
            return gender;
        }

        public int getQuantityOrdered() {
            return quantityOrdered;
        }

        // Optional: Override toString() for easy printing/debugging
        @Override
        public String toString() {
            return "Transaction{" +
                    "orderID=" + orderID +
                    ", orderDate='" + orderDate + '\'' +
                    ", productID=" + productID +
                    ", customerID=" + customerID +
                    ", customerName='" + customerName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", quantityOrdered=" + quantityOrdered +
                    '}';
        }
    }