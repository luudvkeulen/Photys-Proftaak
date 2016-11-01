package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
    public Result logout() {
        session().clear();
        flash("info", "You've been logged out");
        return redirect(
                routes.HomeController.index()
        );
    }
}
