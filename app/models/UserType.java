package models;

/**
 * Created by Thijs on 14-9-2016.
 */
public enum UserType {

    User(0) , Photographer(1), requestedPhotographer(2), Admin(3);

    private final int id;
    UserType(int id) {this.id = id; }
    public int getValue() {return id; }

}