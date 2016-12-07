package controllers;

import logic.BinaryLogic;
import logic.PhotographerLogic;
import logic.ProductLogic;
import models.CartItem;
import models.Product;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartController extends Controller {

    private final Database db;

    private static String getCartCookie() {
        if(request().cookie("cart") == null) {
            return "";
        }
        String cookie = request().cookie("cart").value();
        if(cookie.isEmpty() || cookie == null) {
            return "";
        }
        return cookie;
    }

    public Result index() {
        String cookie = getCartCookie();
        if (cookie.equals("")) return ok(cart.render(new ArrayList<>()));
        //fullProducts = getAllCartProducts();
        List<CartItem> cartItems = BinaryLogic.binaryToObject(cookie);
        return ok(cart.render(cartItems));
    }

    public Result changeAmount(int productID, int pictureID, boolean add) {
        String cookie = getCartCookie();
        if (cookie.equals("")) return redirect("/cart");

        List<CartItem> cartItems = BinaryLogic.binaryToObject(request().cookie("cart").value());
        if (cartItems.size() < 1) return redirect("/cart");

        for (CartItem item : cartItems) {
            if (item.getPictureId() == pictureID) {
                for (Product product : item.getProducts()) {
                    if (product.getID() == productID) {
                        if (add) {
                            product.addOne();
                        } else {
                            product.substractOne();
                        }

                    }
                }
            }
        }
        String newCookie = BinaryLogic.objectsToBinary(cartItems);
        response().setCookie(new Http.Cookie("cart", newCookie, null, "/", "", false, false));

        return redirect("/cart");
    }

    // berekenen van de totaal prijs van alle producten in de winkelwagen
    public static double countTotalPrice() {
        if(getCartCookie().equals("")) return 0.00;
        List<CartItem> cartItems = BinaryLogic.binaryToObject(getCartCookie());
        if(cartItems == null) return 0;
        double counter = 0;

        for(CartItem cartItem : cartItems) {
            for(Product p : cartItem.getProducts()) {
                counter += p.getTotalPrice();
            }
        }
        return counter;
    }

    // Ophalen van alle Items voor de winkelwagen
    public static int getTotalItems(){
        if(getCartCookie().equals("")) return 0;
        List<CartItem> cartItems = BinaryLogic.binaryToObject(getCartCookie());
        int counter = 0;
        for(CartItem ci : cartItems) {
            counter += ci.getProducts().size();
        }
        System.out.println("TOTAL PRODUCTS: " + counter);
        System.out.println("TOTAL CART ITEMS: " + counter);
        return counter;
    }

    @Inject
    public CartController(play.db.Database db) {
        this.db = db;
    }
}
