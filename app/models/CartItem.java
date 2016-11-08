package models;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
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
