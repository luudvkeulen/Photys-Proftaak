package controllers;

import logic.BinaryLogic;
import logic.PhotographerLogic;
import logic.ProductLogic;
import models.CartItem;
import models.Product;
import play.db.Database;
import play.mvc.Controller;
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

    private Database db;
    private static PrijsController prc;
    private static List<Product> fullProducts;

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
        fullProducts = getAllCartProducts();
        List<CartItem> cartItems = new ArrayList<>();
        if(request().cookie("cart") == null) {
            return ok(cart.render(new ArrayList<>()));
        }
        String cookie = request().cookie("cart").value();
        if(cookie.isEmpty() || cookie == null) {
            return ok(cart.render(new ArrayList<>()));
        }

        if(cookie != null){
            cartItems = BinaryLogic.binaryToObject(cookie);
        }else{
            System.out.println("EMPTY COOKIE");
        }

        List<Product> products = getAllCartProducts();
        List<Product> fullProducts;

        for(CartItem carty : cartItems){
            // Laat het bestelde foto aantallen met ID zien.
            System.out.println("\nPRODUCT SIZE: " + carty.getProducts().size());
            System.out.println("PICTURE ID: " + carty.getPictureId());

            fullProducts = new ArrayList<>();

            // Laat de producten zien die gekoppeld zijn aan de gekozen foto
            for(Product pr : carty.getProducts()){
                Product fullProduct = null;
                for(Product pr2 : products) {
                    if(pr2.getID() == pr.getID()) {
                        fullProduct = pr2;
                    }
                }

                if(fullProduct == null) continue;

                fullProducts.add(fullProduct);

                //System.out.println("\nPRODUCT ID: " + fullProduct.getID());
                //System.out.println("PRODUCT NAME: " + fullProduct.getName());
                //System.out.println("PRODUCT AMOUNT: " + fullProduct.getAmount());
                //System.out.println("PRODUCT DESCRIPTION: " + fullProduct.getDescription());
                //System.out.println("PRODUCT PRICE: " + fullProduct.getPrice());
            }


            //System.out.println("TOTALE PRIJS: " + totalPrice);
            carty.setProducts(fullProducts);
        }

        return ok(cart.render(cartItems));
    }

    public static int addItem(int product){
        //TODO
        //Product zoeken aan de hand van id
        //fullProducts.add(productID);
        System.out.println("ADDED NEW PRODUCT WITH ID: " + product);
        //System.out.println("NEW SIZE PRODUCT AFTER ADD: " + fullProducts.size());
        int totalItems = getTotalItems();
        return totalItems;
    }

    public Result getToCartPage(int productID){
        addItem(productID);
        return redirect("/cart");
    }

    public List<Product> getAllCartProducts() {
        List<Product> result = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select p.* from `product` p");

            ResultSet set = statement.executeQuery();
            while(set.next()) {
                result.add(
                        new Product(
                                set.getInt("id"),
                                set.getString("name"),
                                set.getString("description"),
                                set.getDouble("price")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // berekenen van de totaal prijs van alle producten in de winkelwagen
    public static double countTotalPrice() {
        if(getCartCookie().equals("")) return 0.00;
        List<CartItem> cartItems = BinaryLogic.binaryToObject(getCartCookie());
        if(cartItems == null) return 0;
        double counter = 0;

        for(CartItem cartItem : cartItems) {
            for(Product p : cartItem.getProducts()) {
                double price = 0;
                for(Product p2 : fullProducts) {
                    if(p2.getID() == p.getID()) {
                        price = p2.getPrice();
                    }
                }
                counter += price;
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
    public CartController(play.db.Database db) {;
        this.prc = new PrijsController(db);
        this.db = db;
    }
}
