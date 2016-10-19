package controllers;

import com.typesafe.config.ConfigFactory;
import logic.PhotographerLogic;
import models.User;
import play.api.Logger;
import play.api.Play;
import play.api.Play.*;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by bramd on 21-9-2016.
 */
public class AlbumsController extends Controller {

    public Result index() {
        return ok(albums.render());
    }

    public String GenerateAlbumURL()
    {
        String albumURL = UUID.randomUUID().toString();

        return albumURL;
    }

    private Boolean InsertAlbumIntoDatabase(String name, String description, int privateAlbum)
    {

        Connection connection = DB.getConnection();

        PreparedStatement statement = null;
        try
        {
            statement = connection.prepareStatement("INSERT INTO ALBUM (`name`, `photographer_id`, `description`, `private`, `AlbumURL`) VALUES (?,?,?,?,?)");
            statement.setString(1, name);
            statement.setInt(2, PhotographerLogic.findPhotographerId(session("user")));
            statement.setString(3, description);
            statement.setInt(4, privateAlbum);
            statement.setString(5, GenerateAlbumURL());

            Boolean result = statement.execute();
            connection.close();
            return result;

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
