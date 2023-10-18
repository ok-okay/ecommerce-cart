package models;

public class Pojo_Product {
    private String productId;
    private String productName;
    private InventoryStatus inventoryStatus;
    private double mrp;
    private double discount;
    private int maxQuantity;

    public Pojo_Product(String productId, String productName, InventoryStatus inventoryStatus, double mrp, double discount, int maxQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.inventoryStatus = inventoryStatus;
        this.mrp = mrp;
        this.discount = discount;
        this.maxQuantity = maxQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }
    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
