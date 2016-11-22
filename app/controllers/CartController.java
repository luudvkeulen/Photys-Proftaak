package controllers;

import logic.BinaryLogic;
import models.CartItem;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class CartController extends Controller {
    public Result index() {
        String cookie = request().cookie("cart").value();
        if(cookie.isEmpty() || cookie == null) {
            return ok(cart.render(new ArrayList<>()));
        }
        List<CartItem> cartItems = BinaryLogic.binaryToObject(cookie);
        return ok(cart.render(cartItems));
    }
}
