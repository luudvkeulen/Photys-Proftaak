package logic;

import models.CartItem;
import models.OrderItem;
import models.Product;
import play.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderLogic {
    private Database db;
    private final PhotographerLogic pl;

    public OrderLogic(Database db) {
        this.db = db;
        this.pl = new PhotographerLogic(db);
    }

    public int createOrder(List<CartItem> cartItems, String email) {
        if (email == null || email.equals("")) return 0;
        int orderid = 0;
        try (Connection connection = db.getConnection()) {
            String sql = "INSERT INTO `order` (`user_id`) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, pl.findPhotographerId(email).toString());
            statement.execute();
            //Get the id of last inserted order
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                orderid = rs.getInt(1);
            }
            statement.clearParameters();
            //sql = "INSERT INTO orderitem (`order_id`,`product_id`,`picture_id`,`amount`) VALUES (?, ?, ?, ?)";
            sql = "INSERT INTO orderitem(order_id, product_id, picture_id,amount,pictureprice,productprice) \n" +
                    "VALUES(?, ?, ?, ?,(SELECT price FROM picture p2 WHERE p2.id = ?) , (SELECT price FROM product p1 WHERE p1.id = ?))";
            for (CartItem ci : cartItems) {
                statement = connection.prepareStatement(sql);
                statement.setInt(3, ci.getPhoto().getId());
                statement.setInt(1, orderid);
                statement.setInt(5, ci.getPhoto().getId());
                for (Product p : ci.getProducts()) {
                    statement.setInt(2, p.getID());
                    statement.setInt(6, p.getID());
                    statement.setInt(4, p.getAmount());
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return orderid;
    }

    public List<OrderItem> getOrderItems(String order_id) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT o.*, oi.*, pr.name as productname, pi.name as picturename \n" +
                "FROM `order` o, orderitem oi, product pr, picture pi \n" +
                "WHERE o.id = oi.order_id \n" +
                "AND pr.id = oi.product_id \n" +
                "AND pi.id = oi.picture_id \n" +
                "AND oi.order_id = ?";
        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, order_id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                OrderItem orderItem = new OrderItem(
                        result.getString("picturename"),
                        result.getString("productname"),
                        result.getDouble("productprice"),
                        result.getInt("amount"),
                        result.getDouble("pictureprice")
                );
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return orderItems;
    }
}
