package controllers;

import com.typesafe.config.ConfigFactory;
import models.User;
import models.UserType;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by Thijs on 14-9-2016.
 */
public class AdminController {

    private FTPClient ftpClient;
    private static String server = "137.74.163.54";
    private static int port = 21;

    private boolean result;

    public AdminController()
    {
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

                if(ftpClient.changeWorkingDirectory("/Photographers/" + user.getEmailAdress()))
                {
                    return true;
                }
                else
                {
                    ftpClient.makeDirectory("/Photographers/" + user.getEmailAdress());
                    return true;
                }
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
