package controllers;

import logic.AdminLogic;
import logic.PhotographerLogic;
import logic.ProductLogic;
import models.Product;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminController extends Controller {

    private final PhotographerLogic photographerLogic;
    private final ProductLogic productLogic;
    private final Database db;
    private final FormFactory factory;

    @Inject
    public AdminController(play.db.Database db, FormFactory factory) {
        photographerLogic = new PhotographerLogic(db);
        productLogic = new ProductLogic(db);

        this.db = db;
        this.factory = factory;
    }

    public Result index() {
        AdminLogic adminLogic = new AdminLogic(db);
        if (!adminLogic.isAdmin(session("user"))) {
            flash("error", "You are not an admin!");
            return redirect("/");
        }
        List<User> acceptedUsers = photographerLogic.getAllPhotographers(true);
        List<User> pendingUsers = photographerLogic.getAllPhotographers(false);
        List<User> customers = photographerLogic.getAllCustomers();
        List<Product> products = productLogic.getAllProducts();
        return ok(admin.render(acceptedUsers, pendingUsers, customers, products));
    }

    public Result accept() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String id = dynamicForm.get("id");
        String sql = "UPDATE `user` SET `type`='2' WHERE `id`=?";

        try (Connection connection = db.getConnection()) {
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return redirect("/admin");
    }

    public Result suspend() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String id = dynamicForm.get("id");
        String sql = "UPDATE `user` SET `type`='-1' WHERE `id`=?";

        try (Connection connection = db.getConnection()) {
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            flash("error", "Something went wrong!");
        }

        return redirect("/admin");
    }

    public Result addProduct() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String productname = dynamicForm.get("productName");
        String productdescription = dynamicForm.get("productDescription");
        String productprice = dynamicForm.get("productPrice");

        insertProduct(productname, productdescription, productprice);
        return redirect("/admin");
    }

    private boolean insertProduct(String productName, String productDescription, String productPrice) {
        PreparedStatement prepared = null;
        String sql = "INSERT INTO `product` (`name`, description, price) VALUES (?, ?, ?)";

        try (Connection connection = db.getConnection()) {
            prepared = connection.prepareStatement(sql);
            prepared.setString(1, productName);
            prepared.setString(2, productDescription);
            prepared.setString(3, productPrice);
            prepared.execute();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Result updateProduct() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String id = dynamicForm.get("productid");
        String action = dynamicForm.get("action");


        if (action.equals("remove")) {
            try (Connection connection = db.getConnection()) {
                String sql = "UPDATE `product` SET `active`=0 WHERE `id`=?";
                PreparedStatement prepared = connection.prepareStatement(sql);
                prepared.setString(1, id);
                prepared.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (action.equals("save")) {
            String name = dynamicForm.get("tbname");
            String price = dynamicForm.get("tbprice");
            
            try (Connection connection = db.getConnection()) {
                String sql = "UPDATE `product` SET `name`=?, price=? WHERE `id`=?";
                PreparedStatement prepared = connection.prepareStatement(sql);
                prepared.setString(1, name);
                prepared.setString(2, price);
                prepared.setString(3, id);
                prepared.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                flash("error", "Something went wrong!");
            }
        }

        return redirect("/admin");
    }
}
