package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

    private int id;
    private int userId;
    private Date date;

    public Date getDate() {
        return date;
    }

    public String getSimpleDate() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public int getUserid() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public Order(int id, int userId, Date date) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }
}
