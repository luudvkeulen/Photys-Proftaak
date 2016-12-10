package models;

public class OrderProduct {
    private String name;
    private String description;
    private int amount;
    private double price;

    public OrderProduct(String name, String description, int amount, double price) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
    }
}
