package models;

public class OrderItem {
    private String pictureName;
    private String productName;
    private double productPrice;
    private double picturePrice;
    private int productAmount;

    public OrderItem(String pictureName, String productName, double productPrice, int productAmount, double picturePrice) {
        this.pictureName = pictureName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.picturePrice = picturePrice;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getProductName() {
        return productName;
    }

    public double getTotalPrice() {
        //return (picturePrice + (productPrice * productAmount));
        return ((productPrice * productAmount));
    }

    public double getPicturePrice() {
        return picturePrice;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public double getProductPrice() {
        return productPrice;
    }
}
