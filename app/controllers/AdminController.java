package controllers;

import models.User;
import models.UserType;

/**
 * Created by Thijs on 14-9-2016.
 */
public class AdminController {

    public AdminController()
    {
    }

    public boolean ChangeUserToPhotographer(User user)
    {
        //Check wether the user actually is of the type requestedPhotographer
        if(user.getUserType() == UserType.requestedPhotographer)
        {
            user.changeUserType(UserType.Photographer);
            return true;
        }
        else
        {
            //Let the user know the user role cant be changed.
            return false;
        }
    }
}
