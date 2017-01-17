package models;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
    private final int id;
    private int order_id;
    private final String pictureName;
    private final double picturePrice;
    private final List<OrderProduct> products;
    public OrderItem(int id, String pictureName, double picturePrice) {
        this.id = id;
        this.pictureName = pictureName;
        this.picturePrice = picturePrice;
        this.products = new ArrayList<>();
    }
    public int getOrderid() {
        return order_id;
    }
    public OrderItem(int id, int order_id, String pictureName, double picturePrice) {
        this.id = id;
        this.order_id = order_id;
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
