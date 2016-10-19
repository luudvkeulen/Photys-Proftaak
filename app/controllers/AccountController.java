package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class AccountController extends Controller {

    public Result index() {
        return ok(account.render());
    }
}

//
