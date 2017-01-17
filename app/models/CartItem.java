package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartItem implements Serializable {

    private final Photo photo;
    private final Filter filter;
    private List<Product> products;

    public void removeProduct(ArrayList<Product> product){
        products.removeAll(product);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {this.products = products;}

    public Photo getPhoto() {
        return this.photo;
    }

    public CartItem(Photo photo, Filter filter, List<Product> products) {
        this.photo = photo;
        this.products = products;
        this.filter = filter;
    }

    public double getPrice() {
        double price = photo.getPrice();

        for (Product p : products) {
            price += p.getPrice();
        }
        return price;
    }
}
