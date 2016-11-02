package logic;

import models.Product;
import play.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class JsonLogic {

    private static String textToJsonFormat(Integer id, List<Product> products) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"").append(id.toString()).append("\"").append(":{");
        for(Product p : products) {
            sb.append("\"").append(p.getID()).append("\":{");
            sb.append("\"amount\":").append(p.getAmount()).append("},");
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("}}");
        Logger.info(sb.toString());
        return sb.toString();
    }

    public static String addTextToJson(String cookie, Integer id, List<Product> products) {
        try {
            if(cookie == "") {
                textToJsonFormat(id, products);
            } else {
                String existingCookie = URLDecoder.decode(cookie, "UTF-8");
                Logger.info("Cookie exists");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "test";
    }

    public static List<Product> jsonToProductList(String json) {
        List<Product> products = new ArrayList();
        Logger.info(json);
        return products;
    }
}
