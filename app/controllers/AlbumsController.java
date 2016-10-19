package controllers;

import com.typesafe.config.ConfigFactory;
import logic.PhotographerLogic;
import models.Album;
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
import java.util.ArrayList;
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

    public String GenerateAlbumURL()
    {
        String albumURL = UUID.randomUUID().toString();

        return albumURL;
    }

    @Inject
    public AlbumsController(Database db) {
        this.db = db;
    }

    public ArrayList<Album> GetAllAlbums(int userID) {
        ArrayList<Album> albums = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection;
        //Get each album with the album ID's in the availableAlbumIDs list
        try {
            connection = DB.getConnection();
            statement = connection.prepareStatement("SELECT * FROM `album` WHERE `id` in (select `album_id` FROM `useralbum` WHERE `user_id` = ?)");
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int photographer_id = resultSet.getInt("photographer_id");
                String description = resultSet.getString("description");
                Boolean available = (resultSet.getInt("private") != 1);
                String url = resultSet.getString("AlbumURL");
                Album album = new Album(id, name, photographer_id, description, available, url);
                albums.add(album);
            }
            connection.close();
            return albums;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int GetUserID(String email)
    {
        int userID;
        Connection connection = DB.getConnection();
        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `email` = ?");
            statement.setString(1, email);

            ResultSet result = statement.executeQuery();
            userID = result.getInt("id");
            connection.close();

            return userID;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Album> GetAvailableAlbums()
    {
        ArrayList<Album> albums = new ArrayList<>();
        //Get the user id
        int userID = GetUserID(session("user"));
        //Get all album id's that are available for the user with user id
        albums = GetAllAlbums(userID);


        return albums;
    }
}
