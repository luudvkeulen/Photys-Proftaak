package controllers;

import com.typesafe.config.ConfigFactory;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import org.apache.commons.mail.*;

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
            insertRegisterDetails(firstname, lastname, emailaddress, hashedPw, zipcode, street, housenumber, phone);
            sendEmail(emailaddress);
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

    private void sendEmail(String email) {
        SimpleEmail mail = new SimpleEmail();
        try {
            mail.setHostName(ConfigFactory.load().getString("mail.hostname"));
            mail.setSmtpPort(ConfigFactory.load().getInt("mail.port"));
            mail.setAuthenticator(new DefaultAuthenticator(ConfigFactory.load().getString("mail.username"), ConfigFactory.load().getString("mail.password")));
            mail.setDebug(true);
            mail.setMsg("Test");
            mail.setTLS(true);
            mail.setFrom("photys2016@gmail.com");
            mail.addTo(email);
            mail.setSubject("Registration successful");
            mail.setMsg("You're registration was successful! Welcome to Photys.");
            mail.send();
        } catch (EmailException e) {
            Logger.error(e.getMessage());
        }
    }
}
