package controllers;

import logic.BinaryLogic;
import models.CartItem;
import models.Product;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.*;
import views.html.*;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreviewController extends Controller {

    @Inject
    FormFactory factory;

    Database db;

    public Result index(Integer id) {
        String album = "";
        String name = "";
        String location = "";
        try(Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM picture WHERE id = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                album = set.getString("album_id");
                name = set.getString("name");
                location = set.getString("file_location");
            } else {
                flash("error", "this photo does not exist");
                return redirect("/");
            }
        } catch(SQLException e) {
            Logger.info(e.getMessage());
        }
        String prevUrl = request().getHeader("referer");
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = db.getConnection()) {
            ResultSet result = connection.prepareStatement("SELECT * FROM product ORDER BY id DESC").executeQuery();
            while(result.next()) {
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
        return ok(preview.render(products, prevUrl, name, album, location, id));
    }

    public Result addToCart() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = db.getConnection()) {
            ResultSet result = connection.prepareStatement("SELECT * FROM product").executeQuery();
            while(result.next()) {
                Integer id = result.getInt("id");
                Integer amount = Integer.valueOf(dynamicForm.get(id.toString()));
                if(amount < 1) continue;
                Product product = new Product(
                        id,
                        amount
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CartItem cartItem = new CartItem(Integer.parseInt(dynamicForm.get("id")), products);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        String cookieText;
        if (request().cookie("cart") != null) {
            cookieText = BinaryLogic.addToExisting(request().cookie("cart").value(), cartItems);
        } else {
            cookieText = BinaryLogic.objectsToBinary(cartItems);
        }

        response().setCookie(new Http.Cookie("cart", cookieText, null, "/", "", false, false));

        return ok();
    }

    @Inject
    public PreviewController(Database db) {
        this.db = db;
    }
}
