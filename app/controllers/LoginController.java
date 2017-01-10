package controllers;

import logic.UserLogic;

import play.data.FormFactory;
import play.db.*;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import javax.inject.Inject;
import java.sql.SQLException;

public class LoginController extends Controller {

    final FormFactory factory;

    final Database db;
    UserLogic ul;

    public Result index() {
        ul = new UserLogic(db);
        return ok(login.render());
    }

    public Result login() throws SQLException {
        UserLogic ul = new UserLogic(db);
        DynamicForm dynamicForm = factory.form().bindFromRequest();
        if(checkCredentials(dynamicForm.get("email"),dynamicForm.get("password"))) {
            //Check if the user is banned
            ul.CheckBanStatus(dynamicForm.get("email"));
            flash("success", "You've been logged in");
            return redirect(routes.HomeController.index());
        } else {
            flash("danger", "Your email address or password is wrong");
            return ok(login.render());
        }
    }

    public Result logout() {
        session().clear();
        flash("info", "You've been logged out");
        return redirect(
                routes.HomeController.index()
        );
    }

    private boolean checkCredentials(String email, String password) {
        boolean correct = ul.checkPassword(email, password);
        if(correct) {
            session("user", email);
            return true;
        } else {
            return false;
        }
    }



    @Inject
    public LoginController(Database db, FormFactory factory) {
        this.db = db;
        this.factory = factory;
    }
}
