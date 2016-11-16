package controllers;

import logic.AdminLogic;
import logic.PhotographerLogic;
import logic.ProductLogic;
import models.Product;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.DB;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import scala.reflect.internal.Trees;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {

    PhotographerLogic pl;
    ProductLogic prl;
    Database db;

    @Inject
    FormFactory factory;

    public Result index() {
        AdminLogic adminLogic = new AdminLogic(db);
        if (!adminLogic.isAdmin(session("user"))) {
            flash("error", "You are not an admin!");
            return redirect("/");
        }
        List<User> acceptedUsers = pl.getAllPhotographers(true);
        List<User> pendingUsers = pl.getAllPhotographers(false);
        List<User> customers = pl.getAllCustomers();
        List<Product> products = prl.getAllProducts();
        return ok(admin.render(acceptedUsers, pendingUsers, customers, products));
    }

    public Result accept() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String id = dynamicForm.get("id");

        try (Connection connection = db.getConnection()) {
            PreparedStatement prepared = connection.prepareStatement("UPDATE `user` SET `type`='2' WHERE `id`=?");
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

        try (Connection connection = db.getConnection()) {
            PreparedStatement prepared = connection.prepareStatement("UPDATE `user` SET `type`='-1' WHERE `id`=?");
            prepared.setString(1, id);
            prepared.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return redirect("/admin");
    }

    @Inject
    public AdminController(play.db.Database db) {
        this.pl = new PhotographerLogic(db);
        this.prl = new ProductLogic(db);
        this.db = db;
    }
}
