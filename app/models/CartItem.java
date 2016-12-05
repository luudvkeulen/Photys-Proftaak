package models;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
    private Integer pictureId;
    private Filter filter;
    private List<Product> products;

    public Integer getPictureId() {
        return this.pictureId;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {this.products = products;}

    public CartItem(Integer pictureId, Filter filter, List<Product> products) {
        this.pictureId = pictureId;
        this.products = products;
        this.filter = filter;
    }
}
