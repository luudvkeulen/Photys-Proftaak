package controllers;

import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.*;
import views.html.*;

import static play.data.Form.form;

public class PreviewController extends Controller {

    public Result index(Integer id) {
        String prevUrl = request().getHeader("referer");
        return ok(preview.render(prevUrl));
    }
}
