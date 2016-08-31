package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
    public Result index() {
        return ok(testpage.render("test"));
    }
}
