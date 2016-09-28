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
    private static String server = getConfigString("db.default.serverip");
    private static int port = ConfigFactory.load().getInt("db.default.serverport");
    private static String userString = getConfigString("db.default.ftpUser");
    private static String passwordString = getConfigString("db.default.ftpPassword");

    private boolean result;

    public AdminController()
    {
    }

    private static String getConfigString(String input){
        return ConfigFactory.load().getString(input);
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
                ftpClient.login(userString, passwordString);
                ftpClient.enterLocalPassiveMode();

                if(ftpClient.changeWorkingDirectory("/Photographers/" + user.getEmailAdress()))
                {
                    return true;
                }
                else
                {
                    ftpClient.makeDirectory("/Photographers/" + user.getEmailAdress());

                    if (ftpClient.changeWorkingDirectory("/Photographers/" + user.getEmailAdress()))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
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
