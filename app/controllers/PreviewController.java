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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                Product product = new Product(
                        id,
                        amount
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonLogic.textToJsonFormat(Integer.parseInt(dynamicForm.get("id")), products);
        return ok();
    }

    @Inject
    public PreviewController(Database db) {
        this.db = db;
    }
}
