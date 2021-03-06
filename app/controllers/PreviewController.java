package controllers;

import logic.BinaryLogic;
import models.*;
import models.Filter;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreviewController extends Controller {

    private final FormFactory factory;
    private final Database db;

    @Inject
    public PreviewController(Database db, FormFactory factory) {
        this.db = db;
        this.factory = factory;
    }

    public Result index(String url) {
        int id = -1;
        String album = "";
        int albumId = -1;
        String name = "";
        String location = "";
        String albumURL = "";
        double price = 0.00;
        String photographerName = "";
        try (Connection connection = db.getConnection()) {
            String sql = "SELECT p.*, a.name as album_name, a.albumURL as album_url, u.first_name, u.last_name FROM picture p join album a on p.album_id = a.id join `user` u on u.id = p.photographer_id WHERE p.url = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                id = set.getInt("id");
                albumId = set.getInt("album_id");
                name = set.getString("name");
                location = set.getString("file_location");
                album = set.getString("album_name");
                albumURL = set.getString("album_url");
                price = set.getDouble("price");
                photographerName = set.getString("first_name");
                photographerName += " ";
                photographerName += set.getString("last_name");
            } else {
                flash("error", "this photo does not exist");
                return redirect("/");
            }
        } catch (SQLException e) {
            Logger.info(e.getMessage());
        }
        String prevUrl = request().getHeader("referer");
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = db.getConnection()) {
            String sql = "SELECT * FROM product WHERE active = 1 ORDER BY id DESC";
            ResultSet result = connection.prepareStatement(sql).executeQuery();
            while (result.next()) {
                Product product = new Product(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getDouble("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok(preview.render(products, prevUrl, name, album, albumURL, photographerName, price, location, id));
    }

    public Result addToCart() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = db.getConnection()) {
            String sql = "SELECT * FROM product WHERE active = 1";
            ResultSet result = connection.prepareStatement(sql).executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                Double price = result.getDouble("price");
                int amount = Integer.valueOf(dynamicForm.get(id.toString()));
                if (amount < 1) continue;
                Product product = new Product(
                        id,
                        amount,
                        name,
                        price
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Filter selectedFilter;
        switch (dynamicForm.get("filter")) {
            case "NONE":
                selectedFilter = Filter.NONE;
                break;
            case "SEPIA":
                selectedFilter = Filter.SEPIA;
                break;
            case "BW":
                selectedFilter = Filter.BW;
                break;
            case "BLURRED":
                selectedFilter = Filter.BLURRED;
                break;
            case "DARK":
                selectedFilter = Filter.DARK;
                break;
            case "INVERTED":
                selectedFilter = Filter.INVERTED;
                break;
            default:
                selectedFilter = Filter.NONE;
                break;
        }

        Photo photo = new Photo(Integer.parseInt(dynamicForm.get("id")), dynamicForm.get("picturename"));
        photo.setPrice(Double.parseDouble(dynamicForm.get("pictureprice")));
        CartItem cartItem = new CartItem(photo, selectedFilter, products);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        String cookieText;
        if (request().cookie("cart") != null) {
            cookieText = BinaryLogic.addToExisting(request().cookie("cart").value(), cartItems);
        } else {
            cookieText = BinaryLogic.objectsToBinary(cartItems);
        }

        response().setCookie(new Http.Cookie("cart", cookieText, null, "/", "", false, false));
        return redirect("/cart");
    }
}
