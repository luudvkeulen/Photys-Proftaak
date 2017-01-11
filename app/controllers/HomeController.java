package controllers;

import com.typesafe.config.ConfigFactory;
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
    private static ArrayList<RenderPhoto> renderPhotos;

    @Inject
    public HomeController(Database db) {
        HomeController.db = db;
    }

    public Result index() throws SQLException {
        renderPhotos = new ArrayList<>();
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

        getBytePhotos(photos);

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

    private void getBytePhotos(ArrayList<Photo> photos) {
        byte[] result;
        FTPClient client = new FTPClient();
        try {
            client.connect(ConfigFactory.load().getString("ftp.ip"), ConfigFactory.load().getInt("ftp.port"));
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream stream;
            RenderPhoto renderPhoto;
            for (Photo p : photos) {
                stream = client.retrieveFileStream(p.getFileLocation());
                result = IOUtils.toByteArray(stream);
                stream.close();
                while (!client.completePendingCommand()) ;
                renderPhoto = new RenderPhoto(p.getId(), result);
                renderPhotos.add(renderPhoto);
            }
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Result getRenderPhoto(int id) {
        byte[] result = new byte[0];
        for (RenderPhoto rp : renderPhotos) {
            if (rp.getPhotoId() == id) {
                result = rp.getPhotobytes();
            }
        }
        if (result.length < 1) return ok().as("image");
        return ok(result).as("image");
    }
}
