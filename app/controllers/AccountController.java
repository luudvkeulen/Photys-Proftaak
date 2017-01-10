package controllers;

import logic.BinaryLogic;
import logic.OrderLogic;
import logic.PhotographerLogic;
import logic.UserLogic;
import models.CartItem;
import models.Order;
import models.OrderItem;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountController extends Controller {

    private final FormFactory factory;
    private final Database db;
    private final PhotographerLogic photographerLogic;
    private final UserLogic userLogic;
    private final OrderLogic orderLogic;

    @Inject
    public AccountController(Database db, FormFactory factory) {
        this.db = db;
        this.factory = factory;

        photographerLogic = new PhotographerLogic(db);
        userLogic = new UserLogic(db);
        orderLogic = new OrderLogic(db);
    }

    public boolean isPhotographer() {
        return photographerLogic.isPhotographer(session("user"));
    }

    public Result index() {
        User currentUser = GetAccountInfo(session("user"));
        ArrayList<Order> orders = GetAccountOrders();

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (Order o : orders) {
            orderItems.addAll(GetOrderItems(o.getId() + ""));
        }

        List<CartItem> cartItems = new ArrayList<>();
        if (request().cookie("cart") == null) {
            return ok(account.render(currentUser, orders, orderItems, new ArrayList<>()));
        }
        String cookie = request().cookie("cart").value();
        if (cookie.isEmpty() || cookie == null) {
            return ok(account.render(currentUser, orders, orderItems, new ArrayList<>()));
        }

        if (cookie != null) {
            cartItems = BinaryLogic.binaryToObject(cookie);
        } else {
            System.out.println("EMPTY COOKIE");
        }

        //return ok(account.render(currentUser, orders));
        return ok(account.render(currentUser, orders, orderItems, cartItems));
    }

    private User GetAccountInfo(String email) {
        return userLogic.GetAccountInfo(email);
    }

    private List<OrderItem> GetOrderItems(String orderId) {
        return orderLogic.getOrderItems(orderId);
    }

    private ArrayList<Order> GetAccountOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM photys.`order` WHERE user_id = (SELECT id FROM `user` WHERE emailadres = ?)";

        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, session("user"));

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                orders.add(new Order(result.getInt("id"),
                        result.getInt("user_id"),
                        result.getDate("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Result updateAccountInfo() {
        DynamicForm bindedForm = factory.form().bindFromRequest();
        String userEmail = session("user");
        String select = bindedForm.get("infoSelect");
        String input1 = bindedForm.get("inputSelect1");
        String input2 = bindedForm.get("inputSelect2");

        if (select.equals("password")) {
            if (!userLogic.checkPassword(userEmail, input1)) {
                flash("danger", "Incorrect password.");
                User currentUser = GetAccountInfo(session("user"));
                ArrayList<Order> orders = GetAccountOrders();

                ArrayList<OrderItem> orderItems = new ArrayList<>();

                for (Order o : orders) {
                    orderItems.addAll(GetOrderItems(o.getId() + ""));
                }

                List<CartItem> cartItems = new ArrayList<>();
                if (request().cookie("cart") == null) {
                    return ok(cart.render(new ArrayList<>()));
                }
                String cookie = request().cookie("cart").value();
                if (cookie.isEmpty() || cookie == null) {
                    return ok(cart.render(new ArrayList<>()));
                }

                if (cookie != null) {
                    cartItems = BinaryLogic.binaryToObject(cookie);
                } else {
                    System.out.println("EMPTY COOKIE");
                }

                return ok(account.render(currentUser, orders, orderItems, cartItems));
            }
        }

        User updatedUser = userLogic.GetAccountInfo(userEmail);

        if (select.equals("email")) {
            if (userLogic.CheckEmailAvailable(userEmail)) {
                updatedUser.setEmailAdress(input1);
                session("user", updatedUser.getEmailAdress());
            } else {
                flash("warning", "This email is already in use or incorrect.");
            }
        } else if (select.equals("name")) {
            updatedUser.setFirstName(input1);
            updatedUser.setLastName(input2);
        } else if (select.equals("password")) {
            updatedUser.setPassword(input2);
        } else if (select.equals("zipcode")) {
            updatedUser.setZipCode(input1);
        } else if (select.equals("adres")) {
            updatedUser.setStreetName(input1);
            updatedUser.setHouseNr(input2);
        } else if (select.equals("phonenr")) {
            updatedUser.setPhoneNr(input1);
        }

        userLogic.UpdateAccountInfo(updatedUser, userEmail);
        User currentUser = GetAccountInfo(updatedUser.getEmailAdress());
        flash("notice", "Account information was updated");
        ArrayList<Order> orders = GetAccountOrders();

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (Order o : orders) {
            orderItems.addAll(GetOrderItems(o.getId() + ""));
        }

        List<CartItem> cartItems = new ArrayList<>();
        if (request().cookie("cart") == null) {
            return ok(cart.render(new ArrayList<>()));
        }
        String cookie = request().cookie("cart").value();
        if (cookie.isEmpty() || cookie == null) {
            return ok(cart.render(new ArrayList<>()));
        }

        if (cookie != null) {
            cartItems = BinaryLogic.binaryToObject(cookie);
        } else {
            System.out.println("EMPTY COOKIE");
        }

        return ok(account.render(currentUser, orders, orderItems, cartItems));
    }
}