package logic;

import models.CartItem;
import org.apache.commons.codec.binary.Base64;
import play.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BinaryLogic {

    public static String objectsToBinary(List<CartItem> cartItems) {
        String result = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject(cartItems);
            result = Base64.encodeBase64URLSafeString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.info("Result:" + result);

        return result;
    }

    public static List<CartItem> binaryToObject(String byteString) {
        byte[] decoded = Base64.decodeBase64(byteString);
        List<CartItem> decodedItems = new ArrayList<>();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(decoded);
            ObjectInputStream is = new ObjectInputStream(in);
            Object obj = is.readObject();
            if (obj instanceof List<?>) return new ArrayList<>();
            decodedItems = (List<CartItem>)is.readObject();
        } catch (Exception e) {

        }
        return decodedItems;
    }

    public static String addToExisting(String byteString, List<CartItem> newItems) {
        List<CartItem> existingItems = binaryToObject(byteString);
        existingItems.addAll(newItems);
        return objectsToBinary(existingItems);
    }
}
