package controllers;

import com.typesafe.config.ConfigFactory;
import models.User;
import models.UserType;
import org.apache.commons.net.ftp.FTPClient;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {

    private FTPClient ftpClient;
    private static String server = "137.74.163.54";
    private static int port = 21;

    private boolean result;

    public AdminController()
    {
    }

    public Result index() {
        List<User> acceptedUsers = getPhotographers(true);
        List<User> pendingUsers = getPhotographers(false);
        return ok(admin.render(acceptedUsers, pendingUsers));
    }

    public Result accept() {
        DynamicForm bindedForm = Form.form().bindFromRequest();
        String id = bindedForm.get("id");
        System.out.println(id);
        Logger.info(id);
        return ok();
    }

    private List<User> getPhotographers(boolean accepted) {
        List<User> users = new ArrayList<>();
        Connection connection = DB.getConnection();
        PreparedStatement statement = null;
        try {
            if(accepted) {
                statement = connection.prepareStatement("SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=2");
            } else {
                statement = connection.prepareStatement("SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=1");
            }
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                User tempUser = new User();
                tempUser.setId(result.getInt(1));
                tempUser.setFirstName(result.getString(2));
                tempUser.setLastName(result.getString(3));
                tempUser.setEmailAdress(result.getString(4));
                users.add(tempUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean ChangeUserToPhotographer(User user)
    {
        //Check wether the user actually is of the type requestedPhotographer
        if(user.getUserType() == UserType.requestedPhotographer)
        {
            user.changeUserType(UserType.Photographer);
            //Make directory for user on the FTP server
            try
            {
                ftpClient.connect(server, port);
                //System.out.println(ConfigFactory.load().getString("db.default.ftpPassword") + ConfigFactory.load().getString("db.default.ftpUser"));
                ftpClient.login(ConfigFactory.load().getString("db.default.ftpUser"), ConfigFactory.load().getString("db.default.ftpPassword"));
                ftpClient.enterLocalPassiveMode();

                return true;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                return false;
            }
        }
        else
        {
            //Let the user know the user role cant be changed.
            return false;
        }
    }
}
