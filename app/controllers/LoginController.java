package controllers;

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

    public Result index() {
        return ok(login.render());
    }

    public Result login() throws SQLException {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        checkCredentials(bindedForm.get("email"),bindedForm.get("password"));
        return ok();
    }

    private void checkCredentials(String email, String password) throws SQLException {
        Logger.info(BCrypt.hashpw(password, BCrypt.gensalt()));
        String hashed = getEncryptedPassword(email);
        if(hashed.isEmpty()) return;
        boolean correct = BCrypt.checkpw(password, hashed);
        if(correct) {
            Logger.info("correct");
            session("user", email);
            Logger.info(session("user"));
        } else {
            Logger.info("false");
        }
    }

    private String getEncryptedPassword(String email) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM `user` WHERE emailadres=?");
        statement.setString(1, email);
        ResultSet result = statement.executeQuery();

        if(result.next()) {
            return result.getString(1);
        } else {
            return "";
        }
    }
}
