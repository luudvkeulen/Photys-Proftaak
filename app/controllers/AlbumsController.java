package controllers;

import logic.PhotographerLogic;
import play.db.DB;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AlbumsController extends Controller {

    private Database db;

    PhotographerLogic photographerLogic;

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
}
