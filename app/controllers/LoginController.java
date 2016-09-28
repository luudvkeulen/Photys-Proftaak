package controllers;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.*;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.data.Form;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController extends Controller {

    private Form<User> formFactory;
    private Form<User> userForm;

    public Result index() {
        userForm = formFactory.form(User.class);
        return ok(login.render());
    }

    public Result login() throws SQLException {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        if(checkCredentials(bindedForm.get("email"),bindedForm.get("password"))) {
            flash("success", "You've been logged in");
            return redirect(routes.HomeController.index());
        } else {
            flash("danger", "Your email address or password is wrong");
            return ok(login.render());
        }
    }

    private boolean checkCredentials(String email, String password) {
        String hashed = getEncryptedPassword(email);
        if(hashed.isEmpty()) return false;
        boolean correct = BCrypt.checkpw(password, hashed);
        if(correct) {
            Logger.info("correct");
            session("user", email);
            Logger.info(session("user"));
            return true;
        } else {
            Logger.info("false");
            return false;
        }
    }

    private String getEncryptedPassword(String email) {
        Connection connection = DB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM `user` WHERE emailadres=?");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getString(1);
            } else {
                return "";
            }
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return "";
        }
    }
}
