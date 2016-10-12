package models;

/**
 * Created by bramd on 12-10-2016.
 */
public class OderProduct {
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

    public OderProduct(FilterType filterType, Product product, Photo photo) {
        this.filterType = filterType;
        this.product = product;
        this.photo = photo;
    }
}
