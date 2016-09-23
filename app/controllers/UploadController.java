package controllers;

import com.typesafe.config.ConfigFactory;
import play.api.Logger;
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
    private Boolean result;


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
                //System.out.println(ConfigFactory.load().getString("db.default.ftpPassword") + ConfigFactory.load().getString("db.default.ftpUser"));
                ftpClient.login(ConfigFactory.load().getString("db.default.ftpUser"), ConfigFactory.load().getString("db.default.ftpPassword"));
                ftpClient.enterLocalPassiveMode();

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                ftpClient.setSoTimeout(10000);


                System.out.println(ftpClient.getStatus());

                FileInputStream fs = new FileInputStream(file);
                result = ftpClient.storeFile(fileName, fs);
                fs.close();

            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }

            return ok("File uploaded" + fileName + " " + result);
        }
        else{
            flash("error", "Missing file");
            return badRequest();
        }
    }


}
