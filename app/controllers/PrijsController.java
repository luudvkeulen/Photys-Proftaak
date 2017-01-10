package controllers;

import models.Product;
import play.mvc.Controller;
import play.db.Database;

import javax.inject.Inject;
import java.util.List;


public class PrijsController extends Controller {

    @SuppressWarnings("FieldCanBeLocal")
    private  Database db;


    @Inject
    public PrijsController(Database db)
    {
        this.db = db;
    }

    //Constructor for Unit testing.
    public PrijsController()
    {}

    public double CalcTotalPrice(List<Product> productsInCart)
    {

        double totalPrice = 0;

        for(Product p : productsInCart) {
            if(p.getPrice() != null) {
                totalPrice += p.getPrice();
            }
        }

        return totalPrice;
    }

}
