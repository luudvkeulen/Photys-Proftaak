package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.albums;

/**
 * Created by bramd on 21-9-2016.
 */
public class AlbumsController extends Controller {
    public Result index() {
        return ok(albums.render());
    }
}
