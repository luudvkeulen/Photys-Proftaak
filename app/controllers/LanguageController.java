package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class LanguageController extends Controller {
    public Result changeLangEn() {
        Controller.changeLang("en");
        return redirect(request().getHeader("referer"));
    }

    public Result changeLangNl() {
        Controller.changeLang("nl");
        return redirect(request().getHeader("referer"));
    }

    public Result changeLangDe() {
        Controller.changeLang("de");
        return redirect(request().getHeader("referer"));
    }
}
