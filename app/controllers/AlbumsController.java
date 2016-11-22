package controllers;

import logic.AlbumLogic;
import logic.PhotographerLogic;
import models.Album;
import models.Photo;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AlbumsController extends Controller {

    private Database db;
    private PhotographerLogic pgL;
    private AlbumLogic aL;

    public Result index() {
        return ok(albums.render());
    }

    public Result albums() {
        ArrayList<Album> albums;
        pgL = new PhotographerLogic(db);
        aL = new AlbumLogic(db);

        if (!isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to view album history");
            return redirect("/");
        }
        albums = GetAvailableAlbums();
        if (albums.size() < 1) {
            flash("You haven't created any albums yet.");
        }
        return ok(myalbums.render(albums));
    }

    //Generates a random Album URL
    public String GenerateAlbumURL() {
        String albumURL = UUID.randomUUID().toString();

        return albumURL;
    }


    private boolean isPhotographer(String email) {
        return  pgL.isPhotographer(email);
    }



    public int GetAlbumIdByURL(String albumUrl, String userEmail) {
       return aL.GetAlbumIdByURL(albumUrl, userEmail);
    }

    public ArrayList<Photo> GetPhotosInAlbum(int albumID) {
        PhotographerLogic photographerLogic = new PhotographerLogic(db);

        ArrayList<Photo> photosInAlbum = new ArrayList<>();

        PreparedStatement statement = null;

        try (Connection connection = db.getConnection()) {
            statement = connection.prepareStatement("SELECT p.*, a.name as `album_name`, a.description FROM `picture` p join `album` a on p.album_id = a.id WHERE `album_id` = ?");
            statement.setInt(1, albumID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int photographer_id = resultSet.getInt("photographer_id");
                int album_id = resultSet.getInt("album_id");
                int file_size = resultSet.getInt("file_size");
                java.util.Date date = resultSet.getDate("date");
                String fileLocation = resultSet.getString("file_location");
                double price = resultSet.getDouble("price");
                String url = resultSet.getString("url");
                String albumname = resultSet.getString("album_name");
                String albumDescription = resultSet.getString("description");

                Photo photo = new Photo(id, name, file_size, date, albumname, albumDescription, fileLocation, price, url);
                photosInAlbum.add(photo);
            }

            return photosInAlbum;

        } catch (SQLException e) {
            e.printStackTrace();
            photosInAlbum = new ArrayList<>();
            return photosInAlbum;
        }
    }


    @Inject
    public AlbumsController(Database db) {
        this.db = db;
    }

    //Gets all albums that the user with userID is allowed to look at
    public ArrayList<Album> GetAllAlbums(int userID) {
        ArrayList<Album> albums = new ArrayList<>();

        PreparedStatement statement = null;

        //Get each album with the album ID's in the availableAlbumIDs list
        try (Connection connection = db.getConnection()) {

            statement = connection.prepareStatement("SELECT A.*, concat(U.first_name, ' ', U.last_name) as `pname` FROM `album` A, `user` U WHERE (A.`id` in (select `album_id` FROM `useralbum` WHERE `user_id` = ?) OR A.photographer_id = ? OR A.private = 0) AND A.photographer_id = U.id");
            statement.setInt(1, userID);
            statement.setInt(2, userID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int photographer_id = resultSet.getInt("photographer_id");
                String description = resultSet.getString("description");
                Boolean available = (resultSet.getInt("private") != 1);
                String url = resultSet.getString("AlbumURL");
                String photographer_name = resultSet.getString("pname");
                Album album = new Album(id, name, photographer_id, photographer_name, description, available, url);
                albums.add(album);
            }
            return albums;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Gets the userID based on the email saved in the session
    private int GetUserID(String email) {
        int userID = 0;

        PreparedStatement statement = null;

        try (Connection connection = db.getConnection()) {
            statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();
            System.out.println(result.toString());
            while (result.next()) {
                userID = result.getInt("id");
            }
            return userID;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Album> GetAvailableAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        //Get the user id
        int userID = GetUserID(session("user"));
        //Get all album id's that are available for the user with user id
        albums = GetAllAlbums(userID);


        return albums;
    }
}
