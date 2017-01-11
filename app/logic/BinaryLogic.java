package logic;

import models.CartItem;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
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

        return result;
    }

    public static List<CartItem> binaryToObject(String byteString) {
        byte[] decoded = Base64.decodeBase64(byteString);
        List<CartItem> decodedItems = new ArrayList<>();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(decoded);
            ObjectInputStream is = new ObjectInputStream(in);
            Object o = is.readObject();
            if (o instanceof List<?>) {
                decodedItems = (List<CartItem>) o;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return decodedItems;
    }

    public static String addToExisting(String byteString, List<CartItem> newItems) {
        List<CartItem> existingItems = binaryToObject(byteString);
        existingItems.addAll(newItems);
        return objectsToBinary(existingItems);
    }
}
