package controllers;

import com.typesafe.config.ConfigFactory;
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

    private String fileLocation = "Deze file heeft geen fileLocatie. Swag.";


    public Result index() {
        return ok(upload.render());
    }

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();

            int index = fileName.lastIndexOf(".");
            System.out.println(fileName.substring(index + 1));

            if (file.length() > 10000000) {
                flash("danger", "This file is too big to upload!");
                return ok(upload.render());
            }


            if (fileName.substring(index + 1).equals("png")) {
                return connectWithFTP(file, fileName);
            } else if (fileName.substring(index + 1).equals("jpg")) {
                return connectWithFTP(file, fileName);
            } else if (fileName.substring(index + 1).equals("JPEG")) {
                return connectWithFTP(file, fileName);
            } else {
                flash("danger", "please upload a legit file tpye");
                return ok(upload.render());
            }


        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    private int getPhotographerId() {
        String userEmail = session("user");

        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try {
            prepared = connection.prepareStatement("SELECT id FROM `user` WHERE emailadres = ?");
            prepared.setString(1, userEmail);

            ResultSet result = prepared.executeQuery();

            int resultId = 0;

            while (result.next()) {
                resultId = result.getInt("id");
            }

            System.out.println(resultId);

            return resultId;

        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return -1;
        }

    }

    private boolean insertFileDetails(String fileName, int photographerId, int albumId, int fileSize) {
        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try {
            prepared = connection.prepareStatement("INSERT INTO `picture` (`name` , photographer_id, album_id, file_size, file_location) VALUES (?,?,?,?,?)");
            prepared.setString(1, fileName);
            prepared.setInt(2, photographerId);
            prepared.setInt(3, albumId);
            prepared.setInt(4, fileSize);
            prepared.setString(5, fileLocation);


            return prepared.execute();

        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return false;
        }
    }

    public Result connectWithFTP(File file, String fileName) {
        String userEmail = session("user");
        System.out.println(userEmail + " FTP");

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            //System.out.println(ConfigFactory.load().getString("db.default.ftpPassword") + ConfigFactory.load().getString("db.default.ftpUser"));
            ftpClient.login(ConfigFactory.load().getString("db.default.ftpUser"), ConfigFactory.load().getString("db.default.ftpPassword"));
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.setSoTimeout(10000);

            FileInputStream fs = new FileInputStream(file);

            if (!ftpClient.changeWorkingDirectory("/Photographers/" + userEmail)) {
                ftpClient.makeDirectory("/Photographers/" + userEmail);
                fileLocation = "/Photographers/" + userEmail + "/" + fileName;
            }
            else
            {
                result = ftpClient.storeFile("/Photographers/" + userEmail + "/" + fileName, fs);
                fileLocation = "/Photographers/" + userEmail + "/" + fileName;
            }




            System.out.println(ftpClient.getStatus());


            fs.close();

            int fileSize = (int) (file.length() / 1000);

            insertFileDetails(fileName, getPhotographerId(), 1, fileSize);


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return ok("File uploaded" + fileName + " " + result);
    }


}
