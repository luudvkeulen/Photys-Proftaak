package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class AlbumsController extends Controller {
    public Result index() {
        return ok(albums.render());
    }
}
