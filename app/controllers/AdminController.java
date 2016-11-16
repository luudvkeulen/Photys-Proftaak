package controllers;

import logic.AdminLogic;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.DB;
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

public class AdminController extends Controller {

    Database db;

    @Inject
    FormFactory factory;

    public Result index() {
        AdminLogic adminLogic = new AdminLogic(db);
        if(!adminLogic.isAdmin(session("user"))) {
            flash("error", "You are not an admin!");
            return redirect("/");
        }
        List<User> acceptedUsers = getPhotographers(true);
        List<User> pendingUsers = getPhotographers(false);
        return ok(admin.render(acceptedUsers, pendingUsers));
    }

    public Result accept() {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        String id = dynamicForm.get("id");
        try (Connection con = db.getConnection()) {
            PreparedStatement prep = con.prepareStatement("UPDATE `user` SET `type`='2' WHERE `id`=?");
            prep.setString(1, id);
            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return redirect("/admin");
    }

    @Inject
    public AdminController(play.db.Database db) {
        this.db = db;
    }

    private List<User> getPhotographers(boolean accepted) {
        List<User> users = new ArrayList<>();

        PreparedStatement statement;
        try (Connection connection = db.getConnection()) {
            if(accepted) {
                statement = connection.prepareStatement("SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=2");
            } else {
                statement = connection.prepareStatement("SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=1");
            }
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                User tempUser = new User();
                tempUser.setId(result.getInt(1));
                tempUser.setFirstName(result.getString(2));
                tempUser.setLastName(result.getString(3));
                tempUser.setEmailAdress(result.getString(4));
                users.add(tempUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void ChangeUserBannedStatus(User user, boolean value)
    {
        user.ChangeUserBan(value);
    }


}
