package models;


public class Album {

    private final int ID;
    private final String name;
    private final int photographer_id;
    private String photographer_name;
    private final String description;
    private final boolean accessibility;
    private final String albumURL;


    public String getPhotographer_name() { return photographer_name; }

    public String getAlbumURL() { return albumURL; }

    public int getPhotographer_id() {
        return photographer_id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccessible() {
        return accessibility;
    }

    public int getID() {

        return ID;
    }

    public String getName() {

        return name;
    }

    public Album(int ID, String name, int photographer_id, String photographer_name, String description, boolean accessibility, String albumURL) {
        this.ID = ID;
        this.name = name;
        this.photographer_id = photographer_id;
        this.photographer_name = photographer_name;
        this.description = description;
        this.accessibility = accessibility;
        this.albumURL = albumURL;
    }

    public Album(int ID, String name, int photographer_id, String description, boolean accessibility, String albumURL) {
        this.ID = ID;
        this.name = name;
        this.photographer_id = photographer_id;
        this.description = description;
        this.accessibility = accessibility;
        this.albumURL = albumURL;
    }

    @Override
    public String toString() {
        if (isAccessible()){
            return name;
        } else {
            return name + " (Private album)";
        }
    }
}
