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
import java.sql.Array;
import java.util.ArrayList;
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

        ArrayList<Product> toRemove = new ArrayList<>();
        for (CartItem item : cartItems) {
            for (Product product : item.getProducts()) {
                if (product.getID() == productId && item.getPhoto().getId() == pictureId) {
                    if (add) {
                        product.addOne();
                    } else {
                        if(product.getAmount() <= 1) {
                            toRemove.add(product);
                        } else{
                            product.substractOne();
                        }
                    }
                }
            }
            if(toRemove.size() >= 1) {

                item.removeProduct(toRemove);
            }
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
        List<Integer> picturePriceAdded = new ArrayList<>();
            if (cartItems == null) return 0;
            double counter = 0.0;
        double photoPrice = 0.00;
            for (CartItem cartItem : cartItems) {
                for (Product p : cartItem.getProducts()) {
                    if (!picturePriceAdded.contains(cartItem.getPhoto().getId())) {
                        picturePriceAdded.add(cartItem.getPhoto().getId());
                        photoPrice = cartItem.getPhoto().getPrice();
                    } else {
                        photoPrice = 0.00;
                    }
                    counter += p.getTotalPrice();
                    counter += photoPrice;
                }
        }

        return (double)Math.round(counter*100.0)/100.0;
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
