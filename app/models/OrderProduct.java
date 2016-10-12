package models;

import play.api.libs.json.JsValue;
import play.api.libs.json.Writes;
import scala.Function1;

import java.io.Serializable;

/**
 * Created by bramd on 12-10-2016.
 */
public class OrderProduct implements Serializable{
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
