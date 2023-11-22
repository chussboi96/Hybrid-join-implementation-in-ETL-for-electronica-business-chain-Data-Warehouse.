public class MasterData {
    private int productID;
    private String productName;
    private double productPrice;
    private int supplierID;
    private String supplierName;
    private int storeID;
    private String storeName;

    // Constructor
    public MasterData(int productID, String productName, double productPrice, int supplierID, String supplierName, int storeID, String storeName) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.storeID = storeID;
        this.storeName = storeName;
    }

    // Getters
    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getStoreID() {
        return storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    // Optional: Override toString() for easy printing/debugging
    @Override
    public String toString() {
        return "MasterData{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", supplierID=" + supplierID +
                ", supplierName='" + supplierName + '\'' +
                ", storeID=" + storeID +
                ", storeName='" + storeName + '\'' +
                '}';
    }
    public void printMasterData(MasterData masterData) {
        if (masterData != null) {
            System.out.println("MasterData for ProductID " + masterData.getProductID() + ": " + masterData);
        } else {
            System.out.println("MasterData not found for given ProductID.");
        }
    }

}