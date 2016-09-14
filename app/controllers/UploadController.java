package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;

public class UploadController extends Controller {

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        if(picture != null)
        {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            return ok("File uploaded");
        }
        else{
            flash("error", "Missing file");
            return badRequest();
        }
    }


}
