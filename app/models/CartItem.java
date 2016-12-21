package models;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
    
    private Photo photo;
    private Filter filter;
    private List<Product> products;

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
}
