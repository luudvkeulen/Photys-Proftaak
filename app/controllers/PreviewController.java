package controllers;

import logic.JsonLogic;
import models.Product;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.DB;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.*;
import scala.Int;
import views.html.*;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PreviewController extends Controller {

    @Inject
    FormFactory factory;

    Database db;

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
            PreparedStatement statement = connection.prepareStatement("SELECT p.*, a.name as album_name, a.albumURL as album_url, u.first_name, u.last_name FROM picture p join album a on p.album_id = a.id join `user` u on u.id = p.photographer_id WHERE p.url = ?");
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
            ResultSet result = connection.prepareStatement("SELECT * FROM product ORDER BY id DESC").executeQuery();
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
            ResultSet result = connection.prepareStatement("SELECT * FROM product").executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                Integer amount = Integer.valueOf(dynamicForm.get(id.toString()));
                if (amount < 1) continue;
                Product product = new Product(
                        id,
                        amount
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String jsonText;
        if (request().cookie("cart") != null) {
            jsonText = JsonLogic.addTextToJson(request().cookie("cart").value(), Integer.parseInt(dynamicForm.get("id")), products);
            JsonLogic.jsonToProductList(request().cookie("cart").value());
        } else {
            jsonText = JsonLogic.addTextToJson("", Integer.parseInt(dynamicForm.get("id")), products);
        }

        try {
            response().setCookie(new Http.Cookie("cart", URLEncoder.encode(jsonText, "UTF-8"), null, "/", "", false, false));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ok();
    }

    @Inject
    public PreviewController(Database db) {
        this.db = db;
    }
}
