package logic;

import models.User;
import play.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotographerLogic {

    private final Database db;

    public boolean isPhotographer(String email) {
        Boolean result = false;
        if (email == null) return result;

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT `type` FROM `user` WHERE emailadres = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = (set.getInt("type") >= 2);
            } else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public User getPhotographerById(Integer id) {
        User user = null;
        PreparedStatement statement;

        try (Connection connection = db.getConnection()) {
            String sql = "SELECT `first_name`, `last_name`, `emailadres` FROM `user` WHERE ID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String email = result.getString("emailadres");

                user = new User(id, firstName, lastName, email);
            }

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer findPhotographerId(String email) {
        Integer result = -1;
        if (email == null) return result;
        try (Connection connection = db.getConnection()) {
            String sql = "SELECT `id` FROM `user` WHERE emailadres = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt("id");
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<User> getAllPhotographers(boolean accepted) {
        List<User> users = new ArrayList<>();

        PreparedStatement statement;
        try (Connection connection = db.getConnection()) {
            String sql;
            if(accepted) {
                sql = "SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=2";
            } else {
                sql = "SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=1";
            }
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            users = resultSetToList(result, users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllCustomers() {
        List<User> users = new ArrayList<>();

        PreparedStatement statement;
        try (Connection connection = db.getConnection()) {
            String sql = "SELECT id, first_name, last_name, emailadres FROM `user` WHERE `type`=0";
            statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            users = resultSetToList(result, users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<User> resultSetToList(ResultSet set, List<User> users) {
        try {
            while(set.next()) {
                User tempUser = new User();
                tempUser.setId(set.getInt(1));
                tempUser.setFirstName(set.getString(2));
                tempUser.setLastName(set.getString(3));
                tempUser.setEmailAdress(set.getString(4));
                users.add(tempUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public PhotographerLogic(Database db) {
        this.db = db;
    }
}
