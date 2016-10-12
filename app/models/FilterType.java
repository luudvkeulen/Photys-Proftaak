package models;

/**
 * Created by bramd on 12-10-2016.
 */
public enum FilterType {
    brannan(0) , gotham(1), hefe(2), lordkelvin(3), nashville(4), xpro(5);

    private final int id;
    FilterType(int id) {this.id = id; }
    public int getValue() {return id; }
}
