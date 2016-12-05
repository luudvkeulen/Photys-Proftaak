package models;

import java.util.Date;

/**
 * Created by hp on 5-12-2016.
 */
public class Order {

    private int id;
    private int userid;
    private Date date;

    public Order(int id, int userid, Date date) {
        this.id = id;
        this.userid = userid;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public int getUserid() {

        return userid;
    }

    public int getId() {

        return id;
    }
}
