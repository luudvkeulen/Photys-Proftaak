package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.sql.SQLException;

public class PrivateAlbumsController extends Controller {

    public Result index() {
        return ok(privatealbums.render());
    }
}
