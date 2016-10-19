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

    //Gets all albums that the user with userID is allowed to look at
    private ArrayList<Album> GetAllAlbums(int userID)
    {
        ArrayList<Album> albums = new ArrayList<>();
        ArrayList<Integer> availableAlbumIDs = new ArrayList<>();

        PreparedStatement statement = null;
        Connection connection;

        //Get all album ID's that are available for the user with userID
        try
        {
            connection = DB.getConnection();

            statement = connection.prepareStatement("SELECT `album_id` FROM `useralbum` WHERE `user_id` = ?");
            statement.setInt(1, userID);

            ResultSet albumIDs = statement.executeQuery();


            while(albumIDs.next())
            {
                int albumID = albumIDs.getInt("album_id");
                availableAlbumIDs.add(albumID);
            }

            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }



        //Get each album with the album ID's in the availableAlbumIDs list
        try
        {
            connection = DB.getConnection();

            Array sqlArray = connection.createArrayOf("INT", availableAlbumIDs.toArray());

            statement = connection.prepareStatement("SELECT * FROM ALBUM WHERE `id` IN ?");
            statement.setArray(1, sqlArray);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {

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
        }
        catch(SQLException e) {
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

    private ArrayList<Album> GetAvailableAlbums()
    {
        ArrayList<Album> albums = new ArrayList<>();
        //Get the user id
        int userID = GetUserID(session("user"));
        //Get all album id's that are available for the user with user id
        albums = GetAllAlbums(userID);


        return albums;
    }
}
