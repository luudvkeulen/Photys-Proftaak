package controllers;

import com.typesafe.config.ConfigFactory;
import logic.PhotographerLogic;
import models.User;
import play.api.Logger;
import play.api.Play;
import play.api.Play.*;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.net.ftp.*;

import static play.mvc.Http.Context.current;

public class UploadController extends Controller {

    private static String server = "137.74.163.54";
    private static int port = 21;
    private Boolean result;


    public Result index() {
        if(!PhotographerLogic.isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to upload pictures");
            return redirect("/");
        }
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

            int index = fileName.lastIndexOf(".");
            System.out.println(fileName.substring(index + 1));

            if(file.length() > 10000000)
            {
                flash("danger", "This file is too big to upload!");
                return ok(upload.render());
            }

            if(fileName.substring(index + 1).equals("png") || fileName.substring(index + 1).equals("jpg") || fileName.substring(index + 1).equals("JPEG"))
            {
                String email = session("user");
                //Fix album id pl0x
                insertFileDetails(fileName, PhotographerLogic.findPhotographerId(email), 1, (int)(file.getTotalSpace() / 1000), email);
                return connectWithFTP(file, fileName);
            } else {
                flash("danger", "please upload a legit file tpye");
                return ok(upload.render());
            }
        }
        else{
            flash("error", "Missing file");
            return badRequest();
        }
    }

    public Result connectWithFTP(File file, String fileName)
    {
        String userEmail = session("user");

        FTPClient ftpClient = new FTPClient();
        try
        {
            ftpClient.connect(server, port);
            //System.out.println(ConfigFactory.load().getString("db.default.ftpPassword") + ConfigFactory.load().getString("db.default.ftpUser"));
            ftpClient.login(ConfigFactory.load().getString("db.default.ftpUser"), ConfigFactory.load().getString("db.default.ftpPassword"));
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.setSoTimeout(10000);

            FileInputStream fs = new FileInputStream(file);

            if(!ftpClient.changeWorkingDirectory("/Photographers/" + userEmail))
            {
                ftpClient.makeDirectory("/Photographers/" + userEmail);
            }

            result = ftpClient.storeFile("/Photographers/" + userEmail + "/" + fileName, fs);

            System.out.println(ftpClient.getStatus());

            fs.close();

        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return ok("File uploaded" + fileName + " " + result);
    }

    private boolean insertFileDetails(String fileName,int photographerId , int albumId, int fileSize, String email)
    {
        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try
        {
            prepared = connection.prepareStatement("INSERT INTO `picture` (`name` , photographer_id, album_id, file_size, file_location) VALUES (?,?,?,?,?)");
            prepared.setString(1, fileName);
            prepared.setInt(2, photographerId);
            prepared.setInt(3, albumId);
            prepared.setInt(4, fileSize);
            prepared.setString(5, "/Photographers/" + email + "/" + fileName);
            Boolean result;
            result = prepared.execute();
            connection.close();
            return result;
        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return false;
        }
    }
}
