package controllers;

import com.typesafe.config.ConfigFactory;
import play.api.Play;
import play.api.Play.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.*;

import static play.mvc.Http.Context.current;

public class UploadController extends Controller {

    private static String server = "137.74.163.54";
    private static int port = 21;


    public Result index() {
        return ok(upload.render());
    }

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        if(picture != null)
        {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();

            FTPClient ftpClient = new FTPClient();
            try
            {
                ftpClient.connect(server, port);
                ftpClient.login(ConfigFactory.load().getString("db.default.ftpPassword"), ConfigFactory.load().getString("db.default.ftpUser"));

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                ftpClient.setSoTimeout(10000);
                ftpClient.enterLocalPassiveMode();

                FileInputStream fs = new FileInputStream(file);
                Boolean result = ftpClient.storeFile(server + "/Porno/" + fileName, fs);

            }
            catch(IOException ex)
            {

            }

            return ok("File uploaded");
        }
        else{
            flash("error", "Missing file");
            return badRequest();
        }
    }


}
