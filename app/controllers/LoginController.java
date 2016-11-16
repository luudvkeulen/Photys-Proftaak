package controllers;

import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.FormFactory;
import play.db.*;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.data.Form;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController extends Controller {

    @Inject
    FormFactory factory;

    Database db;

    public Result index() {
        return ok(login.render());
    }

    public Result login() throws SQLException {
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        if(checkCredentials(dynamicForm.get("email"),dynamicForm.get("password"))) {
            flash("success", "You've been logged in");
            return redirect(routes.HomeController.index());
        } else {
            flash("danger", "Your email address or password is wrong");
            return ok(login.render());
        }
    }

    public Result logout() {
        session().clear();
        flash("info", "You've been logged out");
        return redirect(
                routes.HomeController.index()
        );
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
        Connection connection = db.getConnection();
        String resultString = "";
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM `user` WHERE emailadres=?");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                resultString = result.getString(1);
            }
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return "";
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    @Inject
    public LoginController(Database db) {
        this.db = db;
    }
}
