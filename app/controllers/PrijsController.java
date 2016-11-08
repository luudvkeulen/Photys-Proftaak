package controllers;

import models.Product;
import play.mvc.Controller;
import play.db.Database;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Thijs on 8-11-2016.
 */
public class PrijsController extends Controller {

    private  Database db;


    @Inject
    public PrijsController(Database db)
    {
        this.db = db;
    }

    //Constructor for Unit testing.
    public PrijsController()
    {}

    public double CalcTotalPrice(ArrayList<Product> productsInCart)
    {

        double totalPrice = 0;

        for(Product p : productsInCart) {

            totalPrice += p.getPrice();
        }

        return totalPrice;
    }

}
