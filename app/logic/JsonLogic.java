package logic;

import models.CartItem;
import models.Product;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

public class JsonLogic {

    private static String cartItemToJson(CartItem item) {
        if(item == null) return "";
        Gson gson = new Gson();
        String json = gson.toJson(item);
        Logger.info(json);
        return gson.toJson(json);
    }

    private static CartItem jsonToCartItem(String json) {
        if(json.equals("")) return null;
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject  jsonObject = jsonElement.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("pictureId");
        Logger.info(jsonObject.getAsString());

        return null;
    }

    public static String addTextToJson(String cookie, Integer id, List<Product> products) {
        String cookieString = "";

        if(cookie == "") {
            CartItem cartItem = new CartItem(id, products);
            cookieString = cartItemToJson(cartItem);
        } else {
            //String existingCookie = URLDecoder.decode(cookie, "UTF-8");
            byte[] cookieBytes = new byte[0];
            try {
                cookieBytes = Base64.decodeBase64(cookie.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String existingCookie = "Decoded: " + new String(cookieBytes);
            Logger.info(existingCookie);
            jsonToCartItem(existingCookie);
            Logger.info("Cookie exists");
            cookieString = "";
        }

        try {
            return URLEncoder.encode(cookieString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Product> jsonToProductList(String json) {
        List<Product> products = new ArrayList();
        Logger.info(json);
        return products;
    }
}
