package controllers;

import business.DatabaseController;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.data.Form;

import java.sql.Connection;

public class LoginController extends Controller {
    public Result index() {
        return ok(login.render());
    }

    public Result login() {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        //Logger.info("Username is: " + bindedForm.get("username"));
        //Logger.info("Password is: " + bindedForm.get("password"));
        checkCredentials(bindedForm.get("username"),bindedForm.get("password"));
        return ok();
    }

    private void checkCredentials(String username, String password) {
        BCrypt.checkpw(password, "");
    }
}
