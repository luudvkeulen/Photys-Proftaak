package controllers;

import com.typesafe.config.ConfigFactory;
import logic.PhotoLogic;
import models.Photo;
import logic.PhotographerLogic;
import models.Album;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.net.ftp.*;

import javax.inject.Inject;

public class UploadController extends Controller {

    @Inject
    FormFactory factory;

    private Boolean result;
    private Database db;
    private PhotographerLogic pl;

    //Generates a random Album URL
    private String GeneratePictureURL() {
        return UUID.randomUUID().toString();
    }

    @Inject
    public UploadController(Database db) {
        this.db = db;
        pl = new PhotographerLogic(db);
    }

    public Result index() {
        if (!pl.isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to upload pictures.");
            return redirect("/");
        }
        return ok(upload.render(GetAlbums()));
    }

    public Result uploads() {
        ArrayList<Photo> photos = new ArrayList<>();
        if (!pl.isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to view upload history");
            return redirect("/");
        }
        photos = retrieveUploadHistory();
        if (photos.size() < 1) {
            flash("You haven't uploaded any files yet.");
        }
        return ok(myuploads.render(photos));
    }

    //Temporary on void for testing purposes, but should be boolean
    public Result deletePhoto(String PhotoID) {
        int intPhotoID = Integer.parseInt(PhotoID);
        PhotoLogic pL = new PhotoLogic(db);
        pL.DeletePhotoByID(intPhotoID);

        ArrayList<Photo> photos = retrieveUploadHistory();
        if (photos.size() < 1) {
            flash("You haven't uploaded any files yet.");
        }
        return ok(myuploads.render(photos));
    }

    private Integer findPhotographerId(String email) {
        Integer result = -1;
        if (email == null) return result;

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt("id");
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");

        PhotographerLogic photographerLogic = new PhotographerLogic(db);

        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();

            int index = fileName.lastIndexOf(".");
            System.out.println(fileName.substring(index + 1));

            if (file.length() > 10000000) {
                flash("danger", "This file is too big to upload!");
                return ok(upload.render(GetAlbums()));
            }

            String email = session("user");
            int photographerID = photographerLogic.findPhotographerId(email);
            DynamicForm bindedForm = factory.form().bindFromRequest();
            boolean newAlbum = (bindedForm.get("rbExisting") instanceof String);
            int albumid = -1;

            if (fileName.substring(index + 1).toLowerCase().equals("png") || fileName.substring(index + 1).toLowerCase().equals("jpg") || fileName.substring(index + 1).toLowerCase().equals("JPEG")) {
                if (!newAlbum) {
                    boolean privateAlbum = false;
                    if (bindedForm.get("albumPrivate") != null) {
                        privateAlbum = true;
                    }

                    AlbumsController AC = new AlbumsController(db);

                    albumid = insertAlbumDetails(bindedForm.get("albumName"),
                            photographerID,
                            bindedForm.get("albumDescr"),
                            privateAlbum,
                    AC.GenerateAlbumURL());

                    String emails = bindedForm.get("emails");
                    if (!emails.isEmpty()){
                        emails.substring(0,emails.length()-1);
                        String[] arr = emails.split(",");
                        System.out.print(arr);
                        insertAddUsersToPrivateAlbum(albumid,arr);
                    }
                } else {
                    albumid = Integer.parseInt(bindedForm.get("albumSelect"));
                }

                if (albumid > 0) {
                    insertFileDetails(fileName, bindedForm.get("tbName"), photographerLogic.findPhotographerId(email), albumid, (int) (file.getTotalSpace() / 1000000), email);
                    connectWithFTP(file, fileName);
                    flash("success", "File has been uploaded succesfullly.");
                    return ok(upload.render(GetAlbums()));
                } else {
                    flash("danger", "Album details not filled in correctly.");
                    return ok(upload.render(GetAlbums()));
                }
            } else {
                flash("danger", "Please upload a legit file type.");
                return ok(upload.render(GetAlbums()));
            }
        } else {
            flash("error", "Missing file.");
            return badRequest();
        }
    }


    public Result connectWithFTP(File file, String fileName) {
        String userEmail = session("user");

        FTPClient ftpClient = new FTPClient();
        try {
            int port = 21;
            String server = "137.74.163.54";
            ftpClient.connect(server, port);
            ftpClient.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.setSoTimeout(10000);

            FileInputStream fs = new FileInputStream(file);

            if (!ftpClient.changeWorkingDirectory("/Photographers/" + userEmail)) {
                ftpClient.makeDirectory("/Photographers/" + userEmail);
            }

            result = ftpClient.storeFile("/Photographers/" + userEmail + "/" + fileName, fs);

            System.out.println(ftpClient.getStatus());

            fs.close();
            ftpClient.disconnect();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return ok("File uploaded" + fileName + " " + result);
    }

    private int insertAlbumDetails(String name, int photographer_id, String description, boolean accessibility, String albumURL) {

        PreparedStatement prepared;
        try (Connection connection = db.getConnection()) {
            prepared = connection.prepareStatement("INSERT INTO `album` (`name`, photographer_id, description, private, albumURL) VALUES (?,?,?,?,?)");
            prepared.setString(1, name);
            prepared.setInt(2, photographer_id);
            prepared.setString(3, description);
            prepared.setInt(4, (accessibility) ? 1 : 0);
            prepared.setString(5, albumURL);
            Boolean result;
            result = prepared.execute();

            prepared = null;

            if (!accessibility) {
                System.out.println("if statement");
                prepared = connection.prepareStatement("INSERT INTO `useralbum` (album_id, user_email) VALUES ((SELECT id FROM `album` ORDER BY id DESC LIMIT 1),?)");
                prepared.setString(1, session("user"));
                result = prepared.execute();
            }

            prepared = connection.prepareStatement("SELECT id FROM `album` ORDER BY id DESC LIMIT 1");
            ResultSet resultSet = prepared.executeQuery();
            int albumid = -1;

            while (resultSet.next()) {
                albumid = resultSet.getInt("id");
            }

            return albumid;
        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return -1;
        }
    }

    private boolean insertAddUsersToPrivateAlbum(int albumid, String[] userEmails) {
        PreparedStatement prepared;

        try (Connection connection = db.getConnection()) {

            for (String userEmail : userEmails) {

                prepared = connection.prepareStatement("INSERT INTO useralbum (album_id, user_email) VALUES (?, ?)");
                prepared.setInt(1, albumid);
                prepared.setString(2, userEmail);
                prepared.executeUpdate();
            }

            prepared = connection.prepareStatement("INSERT INTO useralbum (album_id, user_email) VALUES (?, ?)");
            prepared.setInt(1, albumid);
            prepared.setString(2, session("user"));
            prepared.executeUpdate();

            return true;

        } catch (SQLException ex) {
            play.Logger.error(ex.getMessage());
            return false;
        }
    }

    private boolean insertFileDetails(String fileName, String name, int photographerId, int albumId, int fileSize, String email) {
        PreparedStatement prepared = null;

        try (Connection connection = db.getConnection()) {
            prepared = connection.prepareStatement("INSERT INTO `picture` (`name` , photographer_id, album_id, file_size, file_location, url) VALUES (?,?,?,?,?,?)");
            prepared.setString(1, name);
            prepared.setInt(2, photographerId);
            prepared.setInt(3, albumId);
            prepared.setInt(4, fileSize);
            prepared.setString(5, "/Photographers/" + email + "/" + fileName);
            prepared.setString(6, GeneratePictureURL());
            return prepared.execute();
        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return false;
        }
    }

    public ArrayList<Photo> retrieveUploadHistory() {
        ArrayList<Photo> uploads = null;

        PreparedStatement prepared;
        try (Connection connection = db.getConnection()) {
            uploads = new ArrayList<>();
            int photographerId = findPhotographerId(session("user"));
            prepared = connection.prepareStatement("SELECT picture.id, picture.name as pname, album.name as aname, picture.date, picture.price FROM `picture`, `album` WHERE picture.photographer_id = ? AND album.id = picture.album_id");
            prepared.setInt(1, photographerId);
            ResultSet rs = prepared.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("pname");
                String albumName = rs.getString("aname");
                Date dt = rs.getDate("date");
                BigDecimal dcm = rs.getBigDecimal("price");
                uploads.add(new Photo(id, name, dt, dcm.doubleValue(), albumName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uploads;
    }

    private ArrayList<Album> GetAlbums() {
        PreparedStatement statement;

        ArrayList<Album> albums = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            statement = connection.prepareStatement("SELECT * FROM `album` WHERE `photographer_id` = ?");
            PhotographerLogic photographerLogic = new PhotographerLogic(db);
            statement.setInt(1, photographerLogic.findPhotographerId(session("user")));

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int photographer_id = result.getInt("photographer_id");
                String description = result.getString("description");
                Boolean available = (result.getInt("private") != 1);
                String url = result.getString("AlbumURL");

                Album album = new Album(id, name, photographer_id, description, available, url);

                albums.add(album);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(albums.size());
        return albums;
    }
}
