package controllers;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import logic.OrderLogic;
import logic.PaymentLogic;
import models.OrderItem;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class PaymentController extends Controller {
    private final PaymentLogic pl = new PaymentLogic();
    private Database db;

    @Inject
    public PaymentController(Database db) {
        this.db = db;
    }

    public Result createPayment(String order_id) {
        OrderLogic ol = new OrderLogic(db);
        List<OrderItem> orderItems = ol.getOrderItems(order_id);
        return redirect(pl.pay(orderItems));
    }

    public Result paymentCancelled() {
        return ok("Payment was cancelled");
    }

    public Result paymentAccepted(String payerID, String paymentId) {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        String mode = Constants.SANDBOX;
        APIContext apiContext = new APIContext("AUh2gdHH_RDb6Stask5jhJFM2iLOGP8-lvqbHzr1hhMkygP4qesPqXalMID3mXqQIrwsOM2uzFG-qFdR", "EGFd-qDDumwbDqnaOm_xdZvmZi-DhBLVlXfPdyAHf4-qxGT7f64TSMmZKq949-8oJyIGYwvjIQZ_0pMq", mode);

        try {
            payment.execute(apiContext, paymentExecution);
            System.out.println("Success");
            return ok("Payment accepted");
        } catch (PayPalRESTException e) {
            System.err.println(e.getMessage());
            return ok("Error with payment");
        }
    }
}
