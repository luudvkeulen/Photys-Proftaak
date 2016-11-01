package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.handler.codec.base64.Base64;
import models.OrderProduct;
import play.Logger;
import play.api.libs.json.Json;
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
        String cookie = null;
        try{
            cookie = response().cookie("photyscart").get().value();
        }catch (Exception e){

        }
        if(cookie != null){
            ArrayList<OrderProduct> items = DeserializeObject(cookie);
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

    public ArrayList<OrderProduct> GetCart(){
        String cookie = response().cookie("photyscart").get().value();
        if(cookie != null) {
            return DeserializeObject(cookie);
        }
        return null;
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
            Logger.info(e.getMessage());
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
            Logger.error(e.getMessage());
            return null;
        }
        return arr;
    }
}
