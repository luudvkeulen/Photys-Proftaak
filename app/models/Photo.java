package models;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bramd on 27-9-2016.
 */
public class Photo {
    private int id;
    private String name;
    private User photographer;
    private int fileSize;
    private Date date;
    private String albumName;
    private String fileLocation;
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public BigDecimal getPrice() { return this.price; }

    public Photo(int id, String name, User photographer, int fileSize, Date date, String albumName, String fileLocation) {
        this.id = id;
        this.name = name;
        this.photographer = photographer;
        this.fileSize = fileSize;
        this.date = date;
        this.albumName = albumName;
        this.fileLocation = fileLocation;
    }

    public Photo(int id, String name, Date date, BigDecimal price, String albumName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.albumName = albumName;
    }
}
