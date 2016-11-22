package controllers;

import models.Album;
import models.Photo;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Http;
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

    public Result RenderAlbum(String albumUrl) {
        AlbumsController ac = new AlbumsController(db);
        int albumId = ac.GetAlbumIdByURL(albumUrl, session("user"));
        List<Photo> pictures = ac.GetPhotosInAlbum(albumId);
        return ok(albumpreview.render(pictures));
    }

    @Inject
    public PrivateAlbumsController(Database db) {
        this.db = db;
    }
}