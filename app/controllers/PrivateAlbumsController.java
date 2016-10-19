package controllers;

import models.Album;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import java.util.List;

/**
 * Created by hp on 19-10-2016.
 */
public class PrivateAlbumsController extends Controller {

    public Result index() {
        AlbumsController ac = new AlbumsController();
        List<Album> albums = ac.GetAvailableAlbums();
        return ok(privatealbums.render(albums));
    }
}