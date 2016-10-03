package controllers;

import models.Photo;
import models.User;
import play.db.DB;
import play.mvc.*;

import views.html.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement("select p.*, u.first_name, u.last_name, u.emailadres, a.name as album_name from picture p left join `user` u on p.photographer_id = u.id left join album a on a.id = p.album_id where album_id in (select id from album where private = 0) order by p.date desc;");
        ResultSet result = statement.executeQuery();

        ArrayList<Photo> photos = new ArrayList<>();
        while(result.next()){
            Photo photo = new Photo(result.getInt("id"),
                    result.getString("name"),
                    new User(result.getInt("photographer_id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            result.getString("emailadres")),
                    result.getInt("file_size"),
                    result.getDate("date"),
                    result.getString("album_name"),
                    result.getString("file_location"));
            photos.add(photo);
        }

        connection.close();
        return ok(index.render(photos));
    }

}
