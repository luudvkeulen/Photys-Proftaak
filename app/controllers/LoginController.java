package controllers;

import business.DatabaseController;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.sql.Connection;

public class LoginController extends Controller {
    public Result index() {
        testdb();
        return ok(login.render());
    }

    private void testdb() {
        DatabaseController dbc = new DatabaseController();
        Connection con = dbc.getConnection();
    }
}
