package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class OrderController extends Controller {
    public Result index() {
        return ok(order.render());
    }
}
