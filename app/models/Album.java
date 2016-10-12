package models;


public class Album {

    private int ID;
    private String name;
    private int photographer_id;
    private String description;
    private boolean accessibility;
    private String albumURL;

    public String getAlbumURL(){return albumURL;}

    public int getPhotographer_id() {
        return photographer_id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccessibility() {
        return accessibility;
    }

    public int getID() {

        return ID;
    }

    public String getName() {

        return name;
    }

    public Album(int ID, String name, int photographer_id, String description, boolean accessibility, String albumURL) {
        this.ID = ID;
        this.name = name;
        this.photographer_id = photographer_id;
        this.description = description;
        this.accessibility = accessibility;
        this.albumURL = albumURL;
    }
}
