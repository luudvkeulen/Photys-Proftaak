package controllers;

import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String passwordAgain = bindedForm.get("passwordvalidation");
        String zipcode = bindedForm.get("zipcode");
        String street = bindedForm.get("street");
        String housenumber = bindedForm.get("housenr");
        String phone = bindedForm.get("phonenr");
        if(!password.equals(passwordAgain)) {
            return badRequest(register.render());
        } else {
            String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
            Boolean success = insertRegisterDetails(firstname, lastname, emailaddress, hashedPw, zipcode, street, housenumber, phone);
            Logger.info(success.toString());
            return ok(index.render());
        }
    }

    private boolean insertRegisterDetails(String firstname, String lastname, String email, String password, String zipcode, String street, String housenumber, String phone) throws SQLException {
        Connection connection = DB.getConnection();
        PreparedStatement prepared = connection.prepareStatement("INSERT INTO `user` (`first_name`, last_name, emailadres, password, zipcode, street, housenr, phonenr, `type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Customer')");
        Logger.info(prepared.toString());
        prepared.setString(1, firstname);
        prepared.setString(2, lastname);
        prepared.setString(3, email);
        prepared.setString(4, password);
        prepared.setString(5, zipcode);
        prepared.setString(6, street);
        prepared.setInt(7, Integer.parseInt(housenumber));
        prepared.setString(8, phone);
        Logger.info(prepared.toString());
        return prepared.execute();
    }
}
