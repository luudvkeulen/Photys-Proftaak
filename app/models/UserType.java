package models;

public enum UserType {

    Suspended(-1), User(0), requestedPhotographer(1), Photographer(2), Admin(3);

    private final int id;
    UserType(int id) {this.id = id; }
    public int getValue() {return id; }

}

