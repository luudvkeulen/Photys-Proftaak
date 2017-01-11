package logic;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogic {

    private final Database db;
    private Connection connection;

    public UserLogic(Database db) {
        this.db = db;
    }

    public User getUserByEmail(String email) {
        User user = null;
        PreparedStatement statement;
        try {
            connection = db.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM `user` WHERE `emailadres` = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String zipcode = resultSet.getString("zipcode");
                String street = resultSet.getString("street");
                String houseNr = resultSet.getString("housenr");
                String phoneNr = resultSet.getString("phonenr");
                String profilePicture = resultSet.getString("profile_picture");
                int userType = resultSet.getInt("type");
                int isBanned = resultSet.getInt("isBanned");

                user = new User(userId, firstName, lastName, email, zipcode, street, houseNr, phoneNr, userType, isBanned, profilePicture);
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

    public Boolean updateAccountInfo(User user, String email) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;

            String sql = "UPDATE `user` SET emailadres=?,first_name=?,last_name=?,`password`=?,zipcode=?,street=?,housenr=?,phonenr=? WHERE emailadres=?";
            statement = connection.prepareStatement(sql);
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

    private String getEncryptedPassword(String email) {
        String resultString = "";

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT PASSWORD FROM `user` WHERE emailadres=?";
            PreparedStatement statement = connection.prepareStatement(sql);
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

    public User getAccountInfo(String email) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;

            String sql = "SELECT first_name, last_name, emailadres, `password`, zipcode, street, housenr, phonenr, `type` FROM `user` where emailadres = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("emailadres"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("zipcode"),
                        rs.getString("street"),
                        rs.getString("housenr"),
                        rs.getString("phonenr"),
                        rs.getInt("type"));
            }

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkBanStatus(String email) {
        boolean status = true;
        PreparedStatement statement;
        try {
            connection = db.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String sql = "SELECT `isBanned` FROM `user` WHERE `emailadres` = ?";
            statement = connection.prepareStatement(sql);
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

    public boolean checkEmailAvailable(String email) {
        boolean status = true;


        try (Connection connection = db.getConnection()) {
            PreparedStatement statement;

            String sql = "SELECT * FROM `user` WHERE `emailadres` = ?";
            statement = connection.prepareStatement(sql);
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