package controllers;

import com.typesafe.config.ConfigFactory;
import logic.UserLogic;
import models.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import play.db.*;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeController extends Controller {

    private static Database db;
    private static UserLogic userLogic;

    @Inject
    public HomeController(Database db) {
        this.db = db;
        userLogic = new UserLogic(db);
    }

    public Result index() throws SQLException {
        ArrayList<Photo> photos = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT p.*, u.first_name, u.last_name, u.emailadres, a.name AS album_name FROM picture p LEFT JOIN `user` u ON p.photographer_id = u.id LEFT JOIN album a ON a.id = p.album_id WHERE album_id IN (SELECT id FROM album WHERE private = 0) ORDER BY RAND()";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();


            while (result.next()) {
                Photo photo = new Photo(result.getInt("id"),
                        result.getString("name"),
                        new User(result.getInt("photographer_id"),
                                result.getString("first_name"),
                                result.getString("last_name"),
                                result.getString("emailadres")),
                        result.getInt("file_size"),
                        result.getDate("date"),
                        result.getString("album_name"),
                        result.getString("file_location"),
                        result.getDouble("price"),
                        result.getString("url"));
                photos.add(photo);
            }
        }

        return ok(index.render(photos));
    }

    public static String getUsername() {

        String username = "";

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT first_name, last_name FROM `user` where emailadres = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, session("user"));

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                username = result.getString("first_name") + " " + result.getString("last_name");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return username;
    }

    public Result loadProfilePicture() {
        return ok(userLogic.getUserProfilePicture(session("user"))).as("image");
    }

    public static String getUserType() {

        String userType = "";

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT `type` FROM `user` where emailadres = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, session("user"));

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                switch (result.getInt("type")) {
                    case 0:
                        userType = "Customer";
                        break;
                    case 1:
                        userType = "Pending photographer";
                        break;
                    case 2:
                        userType = "Photographer";
                        break;
                    case 3:
                        userType = "Admin";
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return userType;
    }
}
