package controllers;

import play.Logger;
import play.db.DB;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreviewController extends Controller {

    Database db;

    public Result index(Integer id) {
        String album = "";
        String name = "";
        String location = "";
        try(Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM picture WHERE id = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                album = set.getString("album_id");
                name = set.getString("name");
                location = set.getString("file_location");
            } else {
                flash("error", "this photo does not exist");
                return redirect("/");
            }
        } catch(SQLException e) {
            Logger.info(e.getMessage());
        }
        String prevUrl = request().getHeader("referer");
        return ok(preview.render(prevUrl, name, album, location));
    }

    @Inject
    public PreviewController(Database db) {
        this.db = db;
    }
}
