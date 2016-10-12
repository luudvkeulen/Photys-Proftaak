package controllers;

import io.netty.handler.codec.base64.Base64;
import models.OrderProduct;
import play.api.mvc.Cookie;
import play.api.mvc.DiscardingCookie;
import play.mvc.Http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.response;

/**
 * Created by bramd on 12-10-2016.
 */
public class CartController {

    public void AddItemToCart(OrderProduct orderProduct){
        String cookie = response().cookie("photyscart").get().value();
        if(cookie != null){
            ArrayList<OrderProduct> items = DeserializeObject(response().cookie("photyscart").get().value());
            if (items != null){
                items.add(orderProduct);
                response().setCookie("photyscart", SerializeObject(items), 9999999);
            }
        }else{
            ArrayList<OrderProduct> singleItem = new ArrayList<>();
            singleItem.add(orderProduct);
            response().setCookie("photyscart", SerializeObject(singleItem), 9999999);
        }
    }

    private String SerializeObject(Object object){
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return serializedObject;
    }

    private ArrayList<OrderProduct> DeserializeObject(String object){
        ArrayList<OrderProduct> arr;
        try {
            byte b[] =  object.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            arr = (ArrayList<OrderProduct>) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return arr;
    }
}
