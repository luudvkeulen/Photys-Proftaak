package models;

import java.util.List;

public class CartItem {
    private Integer pictureId;
    private List<Product> products;

    public Integer getPictureId() {
        return this.pictureId;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public CartItem(Integer pictureId, List<Product> products) {
        this.pictureId = pictureId;
        this.products = products;
    }
}
