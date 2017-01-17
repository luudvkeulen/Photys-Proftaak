package logic;

import models.CartItem;
import models.OrderItem;
import models.OrderProduct;
import models.Product;
import play.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderLogic {
    private final Database db;
    private final PhotographerLogic photographerLogic;

    public OrderLogic(Database db) {
        this.db = db;
        this.photographerLogic = new PhotographerLogic(db);
    }

    public int createOrder(List<CartItem> cartItems, String email) {
        if (email == null || email.equals("")) return 0;
        int orderId = 0;
        try (Connection connection = db.getConnection()) {
            String sql = "INSERT INTO `order` (`user_id`) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, photographerLogic.findPhotographerId(email).toString());
            statement.execute();
            //Get the id of last inserted order
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            statement.clearParameters();
            //sql = "INSERT INTO orderitem (`order_id`,`product_id`,`picture_id`,`amount`) VALUES (?, ?, ?, ?)";
            sql = "INSERT INTO orderitem(order_id, product_id, picture_id,amount,pictureprice,productprice) \n" +
                    "VALUES(?, ?, ?, ?,(SELECT price FROM picture p2 WHERE p2.id = ?) , (SELECT price FROM product p1 WHERE p1.id = ?))";
            for (CartItem ci : cartItems) {
                statement = connection.prepareStatement(sql);
                statement.setInt(3, ci.getPhoto().getId());
                statement.setInt(1, orderId);
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
        return orderId;
    }

    public double getTotalOrderPrice(String orderId) {
        double totalPrice = 0;
        String sql = "SELECT o.picture_id, o.amount, o.pictureprice, o.productprice FROM orderitem o WHERE o.order_id = ?";
        try(Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, orderId);
            ResultSet result = statement.executeQuery();
            ArrayList<Integer> pictureIdList = new ArrayList<>();
            while(result.next()){
                int pictureId = result.getInt("picture_id");
                int amount = result.getInt("amount");
                double productPrice = result.getDouble("productprice");
                double picturePrice = result.getDouble("pictureprice");
                double price = (productPrice * amount);
                totalPrice += price;
                totalPrice += picturePrice;
                if (!pictureIdList.contains(pictureId)) {
                    pictureIdList.add(pictureId);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Math.round(totalPrice * 100) / 100;
    }

    public double getPictureCosts(String orderId) {
        double pictureCosts = 0;
        String sql = "SELECT o.picture_id, o.amount, o.pictureprice, o.productprice FROM orderitem o WHERE o.order_id = ?";
        try(Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, orderId);
            ResultSet result = statement.executeQuery();
            ArrayList<Integer> pictureIdList = new ArrayList<>();
            while(result.next()){
                int pictureId = result.getInt("picture_id");
                double picturePrice = result.getDouble("pictureprice");
                if (!pictureIdList.contains(pictureId)) {
                    pictureCosts += picturePrice;
                    pictureIdList.add(pictureId);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return pictureCosts;
    }

    public List<OrderItem> getOrderItems(String orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT o.*, oi.*, pr.name as productname,pr.description as productdescription, pi.name as picturename \n" +
                "FROM `order` o, orderitem oi, product pr, picture pi \n" +
                "WHERE o.id = oi.order_id \n" +
                "AND pr.id = oi.product_id \n" +
                "AND pi.id = oi.picture_id \n" +
                "AND oi.order_id = ?";
        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, orderId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                OrderItem orderItem = null;
                for (OrderItem oi : orderItems) {
                    if (oi.getId() == result.getInt("picture_id")) {
                        orderItem = oi;
                        break;
                    }
                }

                if (orderItem != null) {
                    orderItem.addOrderProduct(new OrderProduct(
                            result.getString("productname"),
                            result.getString("productdescription"),
                            result.getInt("amount"),
                            result.getDouble("productprice"),
                            result.getDouble("pictureprice")));
                } else {
                    orderItem = new OrderItem(
                            result.getInt("picture_id"),
                            result.getInt("order_id"),
                            result.getString("picturename"),
                            result.getDouble("pictureprice")
                    );
                    orderItem.addOrderProduct(new OrderProduct(
                            result.getString("productname"),
                            result.getString("productdescription"),
                            result.getInt("amount"),
                            result.getDouble("productprice"),
                            result.getDouble("pictureprice")));
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return orderItems;
    }
}
