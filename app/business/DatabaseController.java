package business;

import play.db.DB;

import java.sql.Connection;

public class DatabaseController {
    public boolean checkCredentials(String username, String encryptedPassword) {
        Connection con = DB.getConnection();
        return false;
    }
}
