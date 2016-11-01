package logic;

import models.User;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotographerLogic {
    public static boolean isPhotographer(String email) {
        Boolean result = false;
        if(email == null) return result;
        Connection connection = DB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `type` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                if(set.getInt("type") >= 2) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static User GetPhotographerById(Integer id)
    {
        
    }

    public static Integer findPhotographerId(String email) {
        Integer result = -1;
        if(email == null) return result;
        Connection connection = DB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                result = set.getInt("id");
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
