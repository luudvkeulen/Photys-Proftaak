package controllers;

import logic.BinaryLogic;
import logic.OrderLogic;
import logic.PhotographerLogic;
import logic.UserLogic;
import models.*;
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

    public Result loadProfilePicture(){
        return ok(userLogic.getUserProfilePicture(session("user"))).as("image");
    }

    public Result changeAvatar() {
        changeProfilePicture();
        return ok(myavatar.render());
    }

    public Result index() {
        User currentUser = getAccountInfo(session("user"));
        ArrayList<Order> orders = getAccountOrders();

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (Order o : orders) {
            orderItems.addAll(getOrderItems(o.getId() + ""));
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

    private User getAccountInfo(String email) {
        return userLogic.getAccountInfo(email);
    }

    private List<OrderItem> getOrderItems(String orderId) {
        return orderLogic.getOrderItems(orderId);
    }

    private ArrayList<Order> getAccountOrders() {
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

    public void changeProfilePicture() {
        DynamicForm bindedForm = factory.form().bindFromRequest();
        String picName = bindedForm.get("profilepic");
        System.out.println("picname: " + picName);
        if(picName != null) {
            userLogic.setProfilePicture(picName, session("user"));
        }

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
                User currentUser = getAccountInfo(session("user"));
                ArrayList<Order> orders = getAccountOrders();

                ArrayList<OrderItem> orderItems = new ArrayList<>();

                for (Order o : orders) {
                    orderItems.addAll(getOrderItems(o.getId() + ""));
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

        User updatedUser = userLogic.getAccountInfo(userEmail);

        if (select.equals("email")) {
            if (userLogic.checkEmailAvailable(userEmail)) {
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

        userLogic.updateAccountInfo(updatedUser, userEmail);
        User currentUser = getAccountInfo(updatedUser.getEmailAdress());
        flash("notice", "Account information was updated");
        ArrayList<Order> orders = getAccountOrders();

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        for (Order o : orders) {
            orderItems.addAll(getOrderItems(o.getId() + ""));
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