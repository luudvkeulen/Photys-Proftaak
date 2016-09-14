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
        String firstname = bindedForm.get("firstname");
        String lastname = bindedForm.get("lastname");
        String emailaddress = bindedForm.get("email");
        String password = bindedForm.get("password");
        String zipcode = bindedForm.get("zipcode");
        String street = bindedForm.get("street");
        String housenumber = bindedForm.get("housenr");
        String phone = bindedForm.get("phonenr");
        return ok();
    }

    private void insertRegisterDetails(String firstname, String lastname, String email, String password, String zipcode, String street, String housenumber, String phone) {

    }
}
