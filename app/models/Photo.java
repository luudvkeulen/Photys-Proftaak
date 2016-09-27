package models;

import org.joda.time.DateTime;

/**
 * Created by bramd on 27-9-2016.
 */
public class Photo {
    private String id;
    private String name;
    private User photographer;
    private int fileSize;
    private DateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public User getPhotographer() {
        return photographer;
    }

    public void setPhotographer(User photographer) {
        this.photographer = photographer;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photo(String id, String name, User photographer, int fileSize, DateTime date) {
        this.id = id;
        this.name = name;
        this.photographer = photographer;
        this.fileSize = fileSize;
        this.date = date;
    }
}
