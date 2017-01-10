package controllers;

import logic.OrderLogic;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import javax.inject.Inject;

public class OrderController extends Controller {
    private final Database db;
    private final OrderLogic orderLogic;

    @Inject
    public OrderController(Database db) {
        this.db = db;
        orderLogic = new OrderLogic(db);
    }

    public Result index(String orderId) {
        return ok(order.render(orderLogic.getOrderItems(orderId), orderLogic.getTotalOrderPrice(orderId), orderLogic.getPictureCosts(orderId), orderId));
    }
}
