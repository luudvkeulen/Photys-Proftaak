package controllers;

import logic.PhotographerLogic;
import models.User;
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
    public AccountController(Database db) {
        this.db = db;
        pl = new PhotographerLogic(db);
    }

    private Database db;

    private PhotographerLogic pl;

    public boolean isPhotographer() {
        return pl.isPhotographer(session("user"));
    }

    public Result index() {
        User user = new User();

        String email = session("user");

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT first_name, last_name, zipcode, street, housenr, phonenr FROM `user` where emailadres = ?");

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                user.setEmailAdress(email);
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ok(account.render(user));
    }

    public String GetAccountInfo(String email) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;

            statement = connection.prepareStatement("SELECT first_name, last_name, zipcode, street, housenr, phonenr, `type` FROM `user` where emailadres = ?");

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                rs.getString("");
            }

            return "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}