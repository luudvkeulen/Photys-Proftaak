package controllers;

import com.typesafe.config.ConfigFactory;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;
import org.apache.commons.mail.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class RegisterController extends Controller {

    @Inject
    FormFactory factory;
    private Database db;
    private final MessagesApi messagesApi;

    public Result index() {
        return ok(register.render());
    }

    public Result register() {
        DynamicForm bindedForm = factory.form().bindFromRequest();
        String firstname = bindedForm.get("firstname");
        String lastname = bindedForm.get("lastname");
        String emailaddress = bindedForm.get("email");
        String password = bindedForm.get("password");
        String passwordAgain = bindedForm.get("passwordvalidation");
        String zipcode = bindedForm.get("zipcode");
        String street = bindedForm.get("street");
        String housenumber = bindedForm.get("housenr");
        String phone = bindedForm.get("phonenr");
        int type = (Boolean.parseBoolean(bindedForm.get("box1"))) ? 1 : 0;
        if(!password.equals(passwordAgain)) {
            return badRequest(register.render());
        } else {
            String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
            String uuid = UUID.randomUUID().toString();
            //sendEmail(emailaddress, uuid);
            play.i18n.Lang lang = Http.Context.current().lang();
            if(insertRegisterDetails(firstname, lastname, emailaddress, hashedPw, zipcode, street, housenumber, phone, type, uuid)) {
                flash("success", messagesApi.get(lang, "flash.registered"));
            }else {
                flash("danger", messagesApi.get(lang, "flash.emailinuse"));
            }
            return redirect("/");
        }
    }

    private boolean insertRegisterDetails(String firstname, String lastname, String email, String password, String zipcode, String street, String housenumber, String phone, int type, String uuid) {
        Connection connection = db.getConnection();
        PreparedStatement prepared = null;
        try {
            prepared = connection.prepareStatement("INSERT INTO `user` (`first_name`, last_name, emailadres, password, zipcode, street, housenr, phonenr, `type`, email_verified, verify_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?)");
            prepared.setString(1, firstname);
            prepared.setString(2, lastname);
            prepared.setString(3, email);
            prepared.setString(4, password);
            prepared.setString(5, zipcode);
            prepared.setString(6, street);
            prepared.setInt(7, Integer.parseInt(housenumber));
            prepared.setString(8, phone);
            prepared.setInt(9, type);
            prepared.setString(10, uuid);
            prepared.execute();
            connection.close();
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private void sendEmail(String email, String uuid) {
        SimpleEmail mail = new SimpleEmail();
        try {
            mail.setHostName(ConfigFactory.load().getString("mail.hostname"));
            mail.setSmtpPort(ConfigFactory.load().getInt("mail.port"));
            mail.setAuthenticator(new DefaultAuthenticator(ConfigFactory.load().getString("mail.username"), ConfigFactory.load().getString("mail.password")));
            mail.setDebug(true);
            mail.setMsg("Test");
            mail.setSocketConnectionTimeout(3000);
            mail.setSocketTimeout(3000);
            mail.setFrom("photys2016@gmail.com");
            mail.addTo(email);
            mail.setSubject("Photys - Activate your email");
            mail.setMsg("To activate your email go to: photys.nl/activate?id=" + uuid);
            mail.send();
        } catch (EmailException e) {
            Logger.error(e.getMessage());
        }
    }

    @Inject
    public RegisterController(Database db, MessagesApi messagesApi) {
        this.db = db;
        this.messagesApi = messagesApi;
    }
}
