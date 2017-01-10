package logic;

import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import models.OrderItem;
import models.OrderProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PaymentLogic {

    private static final boolean LIVE = false;
    private static final String BASE_URL = "http://localhost:9000";
    private static final String CANCEL_URL = BASE_URL + "/paymentcanceled";
    private static final String RETURN_URL = BASE_URL + "/paymentsuccess";
    private static final String CURRENCY = "EUR";
    private static final RedirectUrls REDIRECT_URLS = new RedirectUrls();
    private final Payer payer;

    public PaymentLogic() {
        REDIRECT_URLS.setCancelUrl(CANCEL_URL);
        REDIRECT_URLS.setReturnUrl(RETURN_URL);
        payer = new Payer();
        payer.setPaymentMethod("paypal");
    }

    public String pay(List<OrderItem> orderItems) {
        List<Item> items = new ArrayList<>();
        Item item;
        Double totalprice = 0.00;
        for (OrderItem oi : orderItems) {
            item = new Item(oi.getPictureName(), "1", String.valueOf(oi.getPicturePrice()), CURRENCY);
            items.add(item);
            totalprice += oi.getTotalPrice();

            for (OrderProduct op : oi.getProducts()) {
                String itemname = oi.getPictureName() + ": " + op.getName();
                item = new Item(itemname, String.valueOf(op.getAmount()), String.valueOf(op.getPrice()), CURRENCY);
                item.setDescription(op.getDescription());
                items.add(item);
            }
        }

        totalprice = roundDouble(totalprice);

        Amount amount = new Amount(CURRENCY, totalprice.toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        ItemList itemList = new ItemList();
        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> transactions = new ArrayList<>(Arrays.asList(transaction));

        Payment payment = new Payment("sale", payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(REDIRECT_URLS);

        String mode;
        String clientId;
        String clientSecret;

        if (LIVE) {
            mode = Constants.LIVE;
            clientId = "AZz7hRLiaFPzLeMgwSchKgG_mBHKCGf5ojbMqLp8qDE61nt9O4FkNkbxHKLv36HKrnut1uQOn39s3__E";
            clientSecret = "EOPVPxZ0wdosfPbK2Lj458ESqvcQoZpjjPXwReha9SrYFhl8FHi-xkY5x-pBX7IlPMLEjPDxpi2mGSxc";

        } else {
            mode = Constants.SANDBOX;
            clientId = "AUh2gdHH_RDb6Stask5jhJFM2iLOGP8-lvqbHzr1hhMkygP4qesPqXalMID3mXqQIrwsOM2uzFG-qFdR";
            clientSecret = "EGFd-qDDumwbDqnaOm_xdZvmZi-DhBLVlXfPdyAHf4-qxGT7f64TSMmZKq949-8oJyIGYwvjIQZ_0pMq";
        }

        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        try {
            Payment createdPayment = payment.create(apiContext);

            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return "/";
    }

    private Double roundDouble(Double d) {
        Double result = d * 100;
        Long roundedLong = Math.round(result);
        return ((double) roundedLong) / 100;
    }
}
