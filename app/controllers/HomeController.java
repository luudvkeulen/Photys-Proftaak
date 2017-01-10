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

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private static Database db;
    private static ArrayList<RenderPhoto> renderPhotos;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() throws SQLException {
        renderPhotos = new ArrayList<>();
        ArrayList<Photo> photos = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select p.*, u.first_name, u.last_name, u.emailadres, a.name as album_name from picture p left join `user` u on p.photographer_id = u.id left join album a on a.id = p.album_id where album_id in (select id from album where private = 0) order by RAND()");
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

    public static String GetUserName() throws SQLException {

        String userName = "";

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT first_name, last_name FROM `user` where emailadres = ?");
            statement.setString(1, session("user"));

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                userName = result.getString("first_name") + " " + result.getString("last_name");
            }
        }

        return userName;
    }

    private void getBytePhotos(ArrayList<Photo> photos) {
        byte[] result;
        FTPClient client = new FTPClient();
        try {
            client.connect("137.74.163.54", 21);
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream stream;
            RenderPhoto rp;
            for (Photo p : photos) {
                stream = client.retrieveFileStream(p.getFileLocation());
                result = IOUtils.toByteArray(stream);
                stream.close();
                while (!client.completePendingCommand()) ;
                rp = new RenderPhoto(p.getId(), result);
                renderPhotos.add(rp);
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

    @Inject
    public HomeController(Database db) {
        HomeController.db = db;
    }
}
