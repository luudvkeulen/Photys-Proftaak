package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by bramd on 21-9-2016.
 */
public class PhotoController extends Controller {
    public Result index() {
        return ok(photos.render());
    }
}