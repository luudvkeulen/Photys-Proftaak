package logic;

import play.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLogic {
    private final Database db;

    public AdminLogic(Database db) {
        this.db = db;
    }

    public boolean isAdmin(String email) {
        Boolean result = false;
        if(email == null) return result;


        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT `type` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if(set.next() && set.getInt("type") >= 3) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
