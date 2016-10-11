package controllers;

import com.typesafe.config.ConfigFactory;
import logic.AdminLogic;
import models.User;
import models.UserType;
import org.apache.commons.net.ftp.FTPClient;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    public Result index() {
        if(!AdminLogic.isAdmin(session("user"))) {
            flash("error", "You are not an admin!");
            return redirect("/");
        }
        List<User> acceptedUsers = getPhotographers(true);
        List<User> pendingUsers = getPhotographers(false);
        return ok(admin.render(acceptedUsers, pendingUsers));
    }

    public Result accept() {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        String id = bindedForm.get("id");
        Logger.info(id);
        Connection con = DB.getConnection();
        try {
            PreparedStatement prep = con.prepareStatement("UPDATE `user` SET `type`='2' WHERE `id`=?");
            prep.setString(1, id);
            prep.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return redirect("/admin");
    }

    private List<User> getPhotographers(boolean accepted) {
        List<User> users = new ArrayList<>();
        Connection connection = DB.getConnection();
        PreparedStatement statement;
        try {
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
}
