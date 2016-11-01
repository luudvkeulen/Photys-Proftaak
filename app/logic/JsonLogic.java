package logic;

import models.Product;
import play.Logger;

import java.util.List;

public class JsonLogic {

    public static String textToJsonFormat(Integer id, List<Product> products) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"").append(id.toString()).append("\"").append(":{");
        for(Product p : products) {
            sb.append("\"").append(p.getID()).append("\":{");
            sb.append("\"amount\":").append(p.getAmount()).append("},");
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("}");
        Logger.info(sb.toString());
        return sb.toString();
    }

    public String addTextToJson(String text) {
        return "";
    }
}
