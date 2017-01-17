package models;

public class OrderProduct {
    private final String name;
    private final String description;
    private final int amount;
    private final double price;
    private final double picturePrice;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return (price + picturePrice) * amount;
    }

    public OrderProduct(String name, String description, int amount, double price, double picturePrice) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.picturePrice = picturePrice;
    }
}
