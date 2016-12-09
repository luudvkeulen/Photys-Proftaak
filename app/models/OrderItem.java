package models;

public class OrderItem {
    private String pictureName;
    private String productName;
    private double productPrice;
    private int productAmount;

    public OrderItem(String pictureName, String productName, double productPrice, int productAmount) {
        this.pictureName = pictureName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getProductName() {
        return productName;
    }

    public double getTotalPrice() {
        return (productPrice * productAmount);
    }
}
