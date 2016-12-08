package models;

public class RenderPhoto {
    private byte[] photobytes;
    private int photoId;

    public byte[] getPhotobytes() {
        return photobytes;
    }

    public int getPhotoId() {
        return photoId;
    }

    public RenderPhoto(int photoId, byte[] photobytes) {
        this.photobytes = photobytes;
        this.photoId = photoId;
    }
}
