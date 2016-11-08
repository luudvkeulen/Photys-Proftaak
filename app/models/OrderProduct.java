package models;

import java.io.Serializable;

public class OrderProduct implements Serializable {
    private Photo photo;
    private Product product;
    private FilterType filterType;

    public Photo getPhoto() {
        return photo;
    }

    public Product getProduct() {
        return product;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public OrderProduct(FilterType filterType, Product product, Photo photo) {
        this.filterType = filterType;
        this.product = product;
        this.photo = photo;
    }

    public OrderProduct() {
    }

}
