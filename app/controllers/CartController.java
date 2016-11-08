package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class CartController extends Controller {
    public Result index() {
        return ok(cart.render());
    }
}
