package controllers;

import logic.BinaryLogic;
import logic.OrderLogic;
import models.CartItem;
import models.Product;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartController extends Controller {

    private final Database db;

    @Inject
    public CartController(play.db.Database db) {
        this.db = db;
    }

    private static String getCartCookie() {
        if (request().cookie("cart") == null) {
            return "";
        }
        String cookie = request().cookie("cart").value();
        if (cookie.isEmpty() || cookie == null) {
            return "";
        }
        return cookie;
    }

    public Result index() {
        String cookie = getCartCookie();
        if (cookie.equals("")) return ok(cart.render(new ArrayList<>()));
        List<CartItem> cartItems = BinaryLogic.binaryToObject(cookie);
        return ok(cart.render(cartItems));
    }

    public Result changeAmount(int productId, int pictureId, boolean add) {
        String cookie = getCartCookie();
        if (cookie.equals("")) return redirect("/cart");

        List<CartItem> cartItems = BinaryLogic.binaryToObject(request().cookie("cart").value());
        if (cartItems.size() < 1) return redirect("/cart");

        ArrayList<CartItem> toRemove = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.getPhoto().getId() == pictureId) {
                for (Product product : item.getProducts()) {
                    if (product.getID() == productId) {
                        if (add) {
                            product.addOne();
                        } else {
                            if(product.getAmount() <= 1) {
                                toRemove.add(item);
                            } else{
                                product.substractOne();
                            }
                        }
                    }
                }
            }
        }
        if(toRemove.size() >= 1) {
            cartItems.removeAll(toRemove);
        }
        String newCookie = BinaryLogic.objectsToBinary(cartItems);
        response().setCookie(new Http.Cookie("cart", newCookie, null, "/", "", false, false));

        return redirect("/cart");
    }

    public Result orderCart() {
        if (session("user") != null){
            String cart = getCartCookie();
            if (cart.equals("")) return badRequest("Cookie is no longer valid");
            List<CartItem> cartItems = BinaryLogic.binaryToObject(cart);
            OrderLogic orderLogic = new OrderLogic(db);
            int orderId = orderLogic.createOrder(cartItems, session("user"));
            return redirect("/order?order_id=" + orderId);
        }else{
            flash("info", "You have to be logged in to place an order.");
            return redirect("/login");
            
        }
    }

    // berekenen van de totaal prijs van alle producten in de winkelwagen
    public static double countTotalPrice() {
        if (getCartCookie().equals("")) return 0.00;
        List<CartItem> cartItems = BinaryLogic.binaryToObject(getCartCookie());
        if (cartItems == null) return 0;
        double counter = 0;

        for (CartItem cartItem : cartItems) {
            for (Product p : cartItem.getProducts()) {
                counter += p.getTotalPrice();
            }
        }

        return counter;
    }

    // Ophalen van alle Items voor de winkelwagen
    public static int getTotalItems() {
        if (getCartCookie().equals("")) return 0;
        List<CartItem> cartItems = BinaryLogic.binaryToObject(getCartCookie());

        int counter = 0;
        for (CartItem ci : cartItems) {
            counter += ci.getProducts().size();
        }

        return counter;
    }
}
