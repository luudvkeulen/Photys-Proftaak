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
    private PrijsController prc;
    private double totalPrice;

    public Result index() {
        if(request().cookie("cart") == null) return ok(cart.render(new ArrayList<>()));
        String cookie = request().cookie("cart").value();
        if(cookie.isEmpty() || cookie == null) {
            return ok(cart.render(new ArrayList<>()));
        }

        List<CartItem> cartItems = BinaryLogic.binaryToObject(cookie);
        List<Product> products = getAllCartProducts();

        for(CartItem carty : cartItems){
            // Laat het bestelde foto aantallen met ID zien.
            System.out.println("\nPRODUCT SIZE: " + carty.getProducts().size());
            System.out.println("PICTURE ID: " + carty.getPictureId());

            List<Product> fullProducts = new ArrayList<>();

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

                System.out.println("\nPRODUCT ID: " + fullProduct.getID());
                System.out.println("PRODUCT NAME: " + fullProduct.getName());
                System.out.println("PRODUCT AMOUNT: " + fullProduct.getAmount());
                System.out.println("PRODUCT DESCRIPTION: " + fullProduct.getDescription());
                System.out.println("PRODUCT PRICE: " + fullProduct.getPrice());
                //products = getAllCartProducts(pr.getID());

                totalPrice = prc.CalcTotalPrice(fullProducts);
                fullProduct.setTotalPrice(totalPrice);
            }


            System.out.println("TOTALE PRIJS: " + totalPrice);
            carty.setProducts(fullProducts);
        }

        return ok(cart.render(cartItems));
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

    public static String countCartItems() {
        return "test";
    }

    @Inject
    public CartController(play.db.Database db) {;
        this.prc = new PrijsController(db);
        this.db = db;
    }
}
