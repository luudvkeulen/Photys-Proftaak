package controllers;

import logic.OrderLogic;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;

public class OrderController extends Controller {
    private final Database db;

    @Inject
    public OrderController(Database db) {
        this.db = db;
    }

    public Result index(String order_id) {
        OrderLogic ol = new OrderLogic(db);
        return ok(order.render(ol.getOrderItems(order_id)));
    }
}
