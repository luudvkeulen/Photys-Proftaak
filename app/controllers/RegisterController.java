package controllers;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.sql.SQLException;

public class RegisterController extends Controller {

    public Result index() {
        return ok(register.render());
    }

    public Result register() throws SQLException {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        Logger.info("Username is: " + bindedForm.get("username"));
        Logger.info("Password 1 is: " + bindedForm.get("password1"));
        Logger.info("Password 2 is: " + bindedForm.get("password2"));
        Logger.info("Photographer: " + bindedForm.get("photographer"));
        String password1 = bindedForm.get("password1");
        String password2 = bindedForm.get("password2");
        String username = bindedForm.get("username");
        if(!password1.equals(password2)) return ok();
        return ok();
    }
}
