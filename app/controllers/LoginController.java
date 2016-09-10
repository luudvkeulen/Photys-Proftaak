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
        //Logger.info("Username is: " + bindedForm.get("username"));
        //Logger.info("Password is: " + bindedForm.get("password"));
        checkCredentials(bindedForm.get("username"),bindedForm.get("password"));
        return ok();
    }

    private void checkCredentials(String username, String password) throws SQLException {
        String hashed = getEncryptedPassword(username);
        if(hashed.isEmpty()) return;
        boolean correct = BCrypt.checkpw(password, hashed);
        if(correct) {
            Logger.info("correct");
        } else {
            Logger.info("false");
        }
    }

    private String getEncryptedPassword(String username) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM USERS WHERE USERNAME=?");
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        if(result.next()) {
            return result.getString(1);
        } else {
            return "";
        }
    }
}
