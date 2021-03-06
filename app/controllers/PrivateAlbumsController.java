package controllers;

import models.Album;
import models.Photo;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.List;

public class PrivateAlbumsController extends Controller {

    private final Database db;

    @Inject
    public PrivateAlbumsController(Database db) {
        this.db = db;
    }

    public Result index() {
        AlbumsController ac = new AlbumsController(db);
        List<Album> albums = ac.GetAvailableAlbums();
        return ok(privatealbums.render(albums));
    }

    public Result renderAlbum(String albumUrl) {
        AlbumsController ac = new AlbumsController(db);
        int albumId = ac.getAlbumIdByURL(albumUrl, session("user"));
        if (albumId > 0) {
            List<Photo> pictures = ac.getPhotosInAlbum(albumId);
            return ok(albumpreview.render(pictures));
        } else {
            flash("danger", "You do not have permission to access this album as it is set to private.");
            return index();
        }
    }
}