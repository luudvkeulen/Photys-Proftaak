package models;

import java.util.Date;

public class Photo implements java.io.Serializable {

    private int id;
    private String name;
    private User photographer;
    private int fileSize;
    private Double price;
    private Date date;
    private String albumName;
    private String fileLocation;
    private String url;
    private String albumDescription;

    //Album description
    public void setPrice(double price){
        this.price = price;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }
    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }
    //Url
    public String getUrl() {
        return url;
    }
    //Id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //Date
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    //Photographer
    public User getPhotographer() {
        return photographer;
    }
    public void setPhotographer(User photographer) {
        this.photographer = photographer;
    }
    //FileSize
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    //Photo name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //Album name
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    //File location
    public String getFileLocation() {
        return fileLocation;
    }
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    //Price
    public Double getPrice() {
        return this.price;
    }

    public Photo(int id, String name, User photographer, int fileSize, Date date, String albumName, String fileLocation, double price, String url) {
        this.id = id;
        this.name = name;
        this.photographer = photographer;
        this.fileSize = fileSize;
        this.date = date;
        this.albumName = albumName;
        this.fileLocation = fileLocation;
        this.price = price;
        this.url = url;
    }

    public Photo(int id, String name, int fileSize, Date date, String albumName, String albumDescription, String fileLocation, double price, String url) {
        this.id = id;
        this.name = name;
        this.photographer = null;
        this.fileSize = fileSize;
        this.date = date;
        this.albumName = albumName;
        this.fileLocation = fileLocation;
        this.price = price;
        this.url = url;
        this.albumDescription = albumDescription;
    }

    public Photo(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Photo(int id, String name, Date date, double price, String albumName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.albumName = albumName;
    }

    public Photo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Photo(int id, String name, String fileLocation) {
        this.id = id;
        this.name = name;
        this.fileLocation = fileLocation;
    }

}
