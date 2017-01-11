package logic;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogic {

    Database db;
    Connection connection;

    public UserLogic(Database db) {
        this.db = db;
    }

    public User GetUserByEmail(String email) {
        User user = null;
        PreparedStatement statement;
        try {
            connection = db.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            statement = connection.prepareStatement("SELECT * FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userID = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String zipCode = resultSet.getString("zipcode");
                String street = resultSet.getString("street");
                String houseNr = resultSet.getString("housenr");
                String phoneNr = resultSet.getString("phonenr");
                int userType = resultSet.getInt("type");
                int isBanned = resultSet.getInt("isBanned");
                String pp_fileLocation = resultSet.getString("profile_picture");

                user = new User(userID, firstName, lastName, email, zipCode, street, houseNr, phoneNr, userType, isBanned, pp_fileLocation);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return user;
    }

    public Boolean UpdateAccountInfo(User user, String email) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;

            statement = connection.prepareStatement("UPDATE `user` SET emailadres=?,first_name=?,last_name=?,`password`=?,zipcode=?,street=?,housenr=?,phonenr=? WHERE emailadres=?");
            statement.setString(1, user.getEmailAdress());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getZipCode());
            statement.setString(6, user.getStreetName());
            statement.setString(7, user.getHouseNr());
            statement.setString(8, user.getPhoneNr());
            statement.setString(9, email);

            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getEncryptedPassword(String email) {
        String resultString = "";

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM `user` WHERE emailadres=?");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                resultString = result.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultString;
    }

    public boolean checkPassword(String email, String password) {
        String hashed = getEncryptedPassword(email);
        if (hashed.isEmpty()) {
            return false;
        } else {
            return BCrypt.checkpw(password, hashed);
        }
    }

    public User GetAccountInfo(String email) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;

            statement = connection.prepareStatement("SELECT first_name, last_name, emailadres, `password`, zipcode, street, housenr, phonenr, `type` FROM `user` where emailadres = ?");

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            User u = null;
            while (rs.next()) {
                u = new User(rs.getString("emailadres"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("zipcode"),
                        rs.getString("street"),
                        rs.getString("housenr"),
                        rs.getString("phonenr"),
                        rs.getInt("type"));
            }

            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean CheckBanStatus(String email) {
        boolean status = true;
        PreparedStatement statement;
        try {
            connection = db.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            statement = connection.prepareStatement("SELECT `isBanned` FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                status = (resultSet.getInt("isBanned") != 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return status;
    }

    public boolean CheckEmailAvailable(String email) {
        boolean status = true;


        try (Connection connection = db.getConnection()) {
            PreparedStatement statement;

            statement = connection.prepareStatement("SELECT * FROM `user` WHERE `emailadres` = ?");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                status = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
}