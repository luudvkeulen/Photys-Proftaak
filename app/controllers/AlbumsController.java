package controllers;

import com.typesafe.config.ConfigFactory;
import logic.AdminLogic;
import logic.PhotographerLogic;
import models.Album;
import models.Photo;
import models.User;
import play.api.Logger;
import play.api.Play;
import play.api.Play.*;
import play.db.DB;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AlbumsController extends Controller {

    private Database db;

    public Result index() {
        return ok(albums.render());
    }

    //Generates a random Album URL
    public String GenerateAlbumURL() {
        String albumURL = UUID.randomUUID().toString();

        return albumURL;
    }

    private String GetAlbumNameById(int albumID) {

        PreparedStatement statement = null;
        Connection connection;
        String albumName = null;

        try {

            connection = db.getConnection();
            statement = connection.prepareStatement("SELECT `name` FROM `album` WHERE `id` = ?");
            statement.setInt(1, albumID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                albumName = resultSet.getString("name");
            }

            return albumName;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private ArrayList<Photo> GetPhotosInAlbum(int albumID) {
        PhotographerLogic photographerLogic = new PhotographerLogic(db);

        ArrayList<Photo> photosInAlbum = new ArrayList<>();

        PreparedStatement statement = null;
        Connection connection;

        try {
            connection = db.getConnection();
            statement = connection.prepareStatement("SELECT * FROM `Photo` WHERE `album_id` = ?");
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


                User user = photographerLogic.GetPhotographerById(photographer_id);
                String albumName = GetAlbumNameById(albumID);
                Photo photo = new Photo(id, name, user, file_size, date, albumName, fileLocation, price);
                photosInAlbum.add(photo);
            }

            return photosInAlbum;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
        Connection connection;

        //Get each album with the album ID's in the availableAlbumIDs list
        try {
            connection = db.getConnection();

            statement = connection.prepareStatement("SELECT A.*, concat(U.first_name, ' ', U.last_name) as `pname` FROM `album` A, `user` U WHERE (A.`id` in (select `album_id` FROM `useralbum` WHERE `user_id` = ?) OR A.photographer_id = ?) AND A.photographer_id = U.id");
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
            connection.close();
            return albums;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Gets the userID based on the email saved in the session
    private int GetUserID(String email) {
        int userID = 0;
        Connection connection = db.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();
            System.out.println(result.toString());
            while (result.next()) {
                userID = result.getInt("id");
            }

            connection.close();

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
