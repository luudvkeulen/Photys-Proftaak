package models;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
    private int id;
    private String pictureName;
    private double picturePrice;
    private List<OrderProduct> products;

    public OrderItem(int id, String pictureName, double picturePrice) {
        this.id = id;
        this.pictureName = pictureName;
        this.picturePrice = picturePrice;
        this.products = new ArrayList<>();
    }

    public String getPictureName() {
        return pictureName;
    }

    public double getTotalPrice() {
        double price = 0.00;
        for (OrderProduct op : products) {
            price += op.getTotalPrice();
        }
        price += picturePrice;
        return price;
    }

    public double getPicturePrice() {
        return picturePrice;
    }

    public int getId() {
        return id;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        products.add(orderProduct);
    }

    public List<OrderProduct> getProducts() {
        return products;
    }
}
