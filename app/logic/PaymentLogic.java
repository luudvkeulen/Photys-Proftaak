package logic;

import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaymentLogic {

    private static final boolean LIVE = false;
    private static final String BASE_URL = "http://localhost:9000";
    private static final String CANCEL_URL = BASE_URL + "/paymentcanceled";
    private static final String RETURN_URL = BASE_URL + "/paymentsuccess";
    private static final String CURRENCY = "EUR";
    private static final RedirectUrls REDIRECT_URLS = new RedirectUrls();
    private Transaction transaction;
    private Amount amount;
    private Payer payer;

    public PaymentLogic() {
        REDIRECT_URLS.setCancelUrl(CANCEL_URL);
        REDIRECT_URLS.setReturnUrl(RETURN_URL);
        payer = new Payer();
        payer.setPaymentMethod("paypal");
    }

    public String pay() {
        amount = new Amount();
        amount.setCurrency(CURRENCY);
        amount.setTotal("10");

        transaction = new Transaction();
        transaction.setAmount(amount);

        Item item = new Item();
        item.setName("testproduct");
        item.setDescription("mooi product");
        item.setPrice("10");
        item.setCurrency("EUR");
        item.setQuantity("1");
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        payment.setRedirectUrls(REDIRECT_URLS);

        String mode;
        APIContext apiContext;
        if (LIVE) {
            mode = Constants.LIVE;
            apiContext = new APIContext("AZz7hRLiaFPzLeMgwSchKgG_mBHKCGf5ojbMqLp8qDE61nt9O4FkNkbxHKLv36HKrnut1uQOn39s3__E", "EOPVPxZ0wdosfPbK2Lj458ESqvcQoZpjjPXwReha9SrYFhl8FHi-xkY5x-pBX7IlPMLEjPDxpi2mGSxc", mode);

        } else {
            mode = Constants.SANDBOX;
            apiContext = new APIContext("AUh2gdHH_RDb6Stask5jhJFM2iLOGP8-lvqbHzr1hhMkygP4qesPqXalMID3mXqQIrwsOM2uzFG-qFdR", "EGFd-qDDumwbDqnaOm_xdZvmZi-DhBLVlXfPdyAHf4-qxGT7f64TSMmZKq949-8oJyIGYwvjIQZ_0pMq", mode);
        }

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
}
