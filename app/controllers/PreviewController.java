package controllers;

import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.*;
import play.inject.*;

import static play.data.Form.form;

public class PreviewController extends Controller {

    FormFactory factory;
    public Result index(Integer id) {

        return ok();
    }
}
