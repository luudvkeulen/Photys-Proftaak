package logic;

import models.CartItem;
import models.Product;
import play.db.Database;

import java.sql.*;
import java.util.List;

public class OrderLogic {
    private Database db;
    private final PhotographerLogic pl;

    public OrderLogic(Database db) {
        this.db = db;
        this.pl = new PhotographerLogic(db);
    }

    public void createOrder(List<CartItem> cartItems, String email) {
        if (email == null || email.equals("")) return;
        try (Connection connection = db.getConnection()) {
            String sql = "INSERT INTO `order` (`user_id`) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, pl.findPhotographerId(email).toString());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            int orderid = 0;
            if (rs.next()) {
                orderid = rs.getInt(1);
            }
            statement.clearParameters();
            sql = "INSERT INTO orderitem (`order_id`,`product_id`,`picture_id`,`amount`) VALUES (?, ?, ?, ?)";
            for (CartItem ci : cartItems) {
                statement = connection.prepareStatement(sql);
                statement.setInt(3, ci.getPhoto().getId());
                statement.setInt(1, orderid);
                for (Product p : ci.getProducts()) {
                    statement.setInt(2, p.getID());
                    statement.setInt(4, p.getAmount());
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
