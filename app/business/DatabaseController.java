package business;

import play.db.*;

public class DatabaseController {
    public java.sql.Connection getConnection() {
        return DB.getConnection();
    }
}
