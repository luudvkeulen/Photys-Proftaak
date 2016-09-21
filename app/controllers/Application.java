package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
    public Result index() {
        return ok(index.render());
    }

    public Result nextpage() {
        return ok(nexttestpage.render());
    }

    public Result logout() {
        session().clear();
        flash("info", "You've been logged out");
        return redirect(
                routes.Application.index()
        );
    }
}

//Commentaar
