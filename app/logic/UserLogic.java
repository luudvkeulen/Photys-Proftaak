package logic;

import models.User;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Thijs on 8-11-2016.
 */
public class UserLogic {

    Database db;
    Connection connection;

    public UserLogic(Database db) {
        this.db = db;
    }

    public User GetUserByEmail(String email)
    {
        User user = null;
        PreparedStatement statement;
        try{ connection = db.getConnection();} catch(Exception ex) { ex.printStackTrace();}

        try
        {
            statement = connection.prepareStatement("SELECT * FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                int userID = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String emailAdress = email;
                String zipCode = resultSet.getString("zipcode");
                String street = resultSet.getString("street");
                int houseNr = resultSet.getInt("housenr");
                int phoneNr = resultSet.getInt("phonenr");
                int userType = resultSet.getInt("type");
                int isBanned = resultSet.getInt("isBanned");

                user = new User(userID, firstName, lastName, emailAdress, zipCode, street, houseNr, phoneNr, userType, isBanned);
            }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        finally {
            try{connection.close();} catch(SQLException ex) {ex.printStackTrace();}
        }

        return user;
    }

    public boolean CheckBanStatus(String email)
    {
        boolean status = true;
        PreparedStatement statement;
        try{ connection = db.getConnection();} catch(Exception ex) { ex.printStackTrace();}

        try{
            statement = connection.prepareStatement("SELECT `isBanned` FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                status = (resultSet.getInt("isBanned") != 0);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }finally {
            try{connection.close();} catch(SQLException ex) {ex.printStackTrace();}
        }
        return status;
    }

}