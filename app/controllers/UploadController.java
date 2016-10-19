package controllers;

import com.typesafe.config.ConfigFactory;
import models.Photo;
import models.User;
import org.joda.time.DateTime;
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
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.net.ftp.*;

import static play.mvc.Http.Context.current;

public class UploadController extends Controller {

    private static String server = "137.74.163.54";
    private static int port = 21;
    private Boolean result;


    public Result index() {
        if (!isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in to upload pictures");
            return redirect("/");
        }
        return ok(upload.render());
    }

    public Result uploads() {
        ArrayList<Photo> photos = new ArrayList<>();
        if (!isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to view upload history");
            return redirect("/");
        }
        photos = retrieveUploadHistory();
        if (photos.size() != 0) {
            flash("You haven't uploaded any files yet.");
        }
        return ok(myuploads.render(photos));
    }

    private boolean isPhotographer(String email) {
        Boolean result = false;
        if (email == null) return result;
        Connection connection = DB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `type` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                if (set.getInt("type") >= 2) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private Integer findPhotographerId(String email) {
        Integer result = -1;
        if (email == null) return result;
        Connection connection = DB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE emailadres = ?");
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt("id");
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
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

            if (fileName.substring(index + 1).equals("png") || fileName.substring(index + 1).equals("jpg") || fileName.substring(index + 1).equals("JPEG")) {
                String email = session("user");
                //Fix album id pl0x
                insertFileDetails(fileName, findPhotographerId(email), 1, (int) (file.getTotalSpace() / 1000), email);
                return connectWithFTP(file, fileName);
            } else {
                flash("danger", "please upload a legit file type");
                return ok(upload.render());
            }
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    public Result connectWithFTP(File file, String fileName) {
        String userEmail = session("user");

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
            }

            result = ftpClient.storeFile("/Photographers/" + userEmail + "/" + fileName, fs);

            System.out.println(ftpClient.getStatus());

            fs.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return ok("File uploaded" + fileName + " " + result);
    }

    private boolean insertFileDetails(String fileName, int photographerId, int albumId, int fileSize, String email) {
        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try {
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

    public ArrayList<Photo> retrieveUploadHistory() {
        ArrayList<Photo> uploads = null;
        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try {
            uploads = new ArrayList<>();
            int photographerId = findPhotographerId(session("user"));
            prepared = connection.prepareStatement("SELECT picture.name as pname, album.name as aname, picture.date, picture.price FROM `picture`, `album` WHERE picture.photographer_id = ? AND album.id = picture.album_id");
            prepared.setInt(1, photographerId);
            ResultSet rs = prepared.executeQuery();
            while (rs.next()) {
                String name = rs.getString("pname");
                String albumName = rs.getString("aname");
                Date dt = rs.getDate("date");
                BigDecimal dcm = rs.getBigDecimal("price");
                uploads.add(new Photo(photographerId, name, dt, dcm, albumName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return uploads;
    }
}
