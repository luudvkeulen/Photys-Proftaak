package models;


import java.io.Serializable;

public class Product implements Serializable {

    private final int ID;
    private final String name;
    private String description;
    private final Double price;
    private Integer amount;

    public Product(int ID, String name, String description, double price) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(int ID, int amount, String name, double price) {
        this.ID = ID;
        this.amount = amount;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getAmount() { return amount; }

    public double getTotalPrice(){
        return (this.price * this.amount);
    }

    public void addOne() {
        this.amount++;
    }

    public void substractOne() {
        if (this.amount > 0) {
            this.amount--;
        }
    }
}
