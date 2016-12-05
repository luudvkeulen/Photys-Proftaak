package controllers;

import logic.PhotographerLogic;
import logic.UserLogic;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountController extends Controller {

    @Inject
    FormFactory factory;

    @Inject
    public AccountController(Database db) {
        this.db = db;
        pl = new PhotographerLogic(db);
        ul = new UserLogic(db);
    }

    private Database db;

    private PhotographerLogic pl;

    private UserLogic ul;

    public boolean isPhotographer() {
        return pl.isPhotographer(session("user"));
    }

    public Result index() {
        User currentUser = GetAccountInfo(session("user"));
        return ok(account.render(currentUser));
    }

    public User GetAccountInfo(String email) {
        return ul.GetAccountInfo(email);
    }

    public Result UpdateAccountInfo() {
        DynamicForm bindedForm = factory.form().bindFromRequest();
        String userEmail = session("user");
        String select = bindedForm.get("infoSelect");
        String input1 = bindedForm.get("inputSelect1");
        String input2 = bindedForm.get("inputSelect2");

        if (select.equals("password")) {
            if (!ul.checkPassword(userEmail, input1)) {
                flash("danger", "Incorrect password.");
                User currentUser = GetAccountInfo(userEmail);
                return ok(account.render(currentUser));
            }
        }

        User updatedUser = ul.GetAccountInfo(userEmail);

        if (select.equals("email")) {
            if (ul.CheckEmailAvailable(userEmail)) {
                updatedUser.setEmailAdress(input1);
                session("user", updatedUser.getEmailAdress());
            } else {
                flash("warning", "This email is already in use or incorrect.");
            }
        } else if (select.equals("name")) {
            updatedUser.setFirstName(input1);
            updatedUser.setLastName(input2);
        } else if (select.equals("password")) {
            updatedUser.setPassword(input2);
        } else if (select.equals("zipcode")) {
            updatedUser.setZipCode(input1);
        } else if (select.equals("adres")) {
            updatedUser.setStreetName(input1);
            updatedUser.setHouseNr(input2);
        } else if (select.equals("phonenr")) {
            updatedUser.setPhoneNr(input1);
        }

        ul.UpdateAccountInfo(updatedUser, userEmail);
        User currentUser = GetAccountInfo(updatedUser.getEmailAdress());
        flash("notice", "Account information was updated");
        return ok(account.render(currentUser));
    }
}