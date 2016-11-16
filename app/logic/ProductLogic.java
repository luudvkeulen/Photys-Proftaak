package logic;

import models.Product;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductLogic {
    private Database db;
    public ProductLogic(Database db) {
        this.db = db;
    }

    public List<Product> getAllProducts() {
        List<Product> result = new ArrayList<>();

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                result.add(
                        new Product(
                                set.getInt("id"),
                                set.getString("name"),
                                set.getString("description"),
                                set.getDouble("price")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
