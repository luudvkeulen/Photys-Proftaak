package controllers;

import models.Album;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.List;

public class PrivateAlbumsController extends Controller {

    Database db;
    public Result index() {
        AlbumsController ac = new AlbumsController(db);
        List<Album> albums = ac.GetAvailableAlbums();
        return ok(privatealbums.render(albums));
    }

    @Inject
    public PrivateAlbumsController(Database db) {
        this.db = db;
    }
}