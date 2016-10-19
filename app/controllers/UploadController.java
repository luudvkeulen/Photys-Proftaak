package controllers;

import com.typesafe.config.ConfigFactory;
import logic.PhotographerLogic;
import models.Album;
import play.data.DynamicForm;
import play.data.Form;
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
import java.util.ArrayList;
import org.apache.commons.net.ftp.*;

public class UploadController extends Controller {

    private static String server = "137.74.163.54";
    private static int port = 21;
    private Boolean result;


    public Result index() {
        if(!PhotographerLogic.isPhotographer(session("user"))) {
            flash("warning", "You need to be logged in as a photographer to upload pictures.");
            return redirect("/");
        }
        return ok(upload.render(GetAlbums()));
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
                return ok(upload.render(GetAlbums()));
            }

            String email = session("user");
            int photographerID = PhotographerLogic.findPhotographerId(email);
            DynamicForm bindedForm = Form.form().bindFromRequest();
            String newAlbumString = bindedForm.get("rbExisting");
            int albumid = -1;
            
            if (newAlbumString == null)
            {
                boolean privateAlbum = false;
                if (bindedForm.get("albumPrivate") != null){
                    privateAlbum = true;
                }

                AlbumsController AC = new AlbumsController();

                albumid = insertAlbumDetails(bindedForm.get("albumName"),
                        photographerID,
                        bindedForm.get("albumDescr"),
                        privateAlbum,
                        AC.GenerateAlbumURL());
            } else
            {
                albumid = Integer.parseInt(bindedForm.get("albumSelect"));
            }

            if(albumid > 0){
                if(fileName.substring(index + 1).equals("png") || fileName.substring(index + 1).equals("jpg") || fileName.substring(index + 1).equals("JPEG"))
                {
                    insertFileDetails(fileName, PhotographerLogic.findPhotographerId(email), albumid, (int)(file.getTotalSpace() / 1000000), email);
                    return connectWithFTP(file, fileName);
                } else {
                    flash("danger", "Please upload a legit file type.");
                    return ok(upload.render(GetAlbums()));
                }
            } else
            {
                flash("danger", "Please fill out all the album information correctly.");
                return badRequest();
            }

        }
        else{
            flash("error", "Missing file.");
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
            ftpClient.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
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

    private int insertAlbumDetails(String name, int photographer_id, String description, boolean accessibility, String albumURL)
    {
        Connection connection = DB.getConnection();
        PreparedStatement prepared = null;
        try
        {
            prepared = connection.prepareStatement("INSERT INTO `album` (`name`, photographer_id, description, private, albumURL) VALUES (?,?,?,?,?)");
            prepared.setString(1, name);
            prepared.setInt(2, photographer_id);
            prepared.setString(3, description);
            prepared.setInt(4, (accessibility) ? 1 : 0);
            prepared.setString(5, albumURL);
            Boolean result;
            result = prepared.execute();

            prepared = connection.prepareStatement("SELECT id FROM `album` ORDER BY id DESC LIMIT 1");
            ResultSet result2 = prepared.executeQuery();
            int albumID = result2.getInt("id");
            connection.close();
            return albumID;
        } catch (SQLException e) {
            play.Logger.error(e.getMessage());
            return -1;
        }
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

    private ArrayList<Album> GetAlbums()
    {
        Connection connection = DB.getConnection();
        PreparedStatement statement = null;

        ArrayList<Album> albums = new ArrayList<>();

        try
        {
            statement = connection.prepareStatement("SELECT * FROM `album` WHERE `photographer_id` = ?");
            statement.setInt(1, PhotographerLogic.findPhotographerId(session("user")));

            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                int id = result.getInt("id");
                String name = result.getString("name");
                int photographer_id = result.getInt("photographer_id");
                String description = result.getString("description");
                Boolean available = result.getBoolean("private");
                String url = result.getString("AlbumURL");

                Album album = new Album(id,name,photographer_id,description,available,url);

                albums.add(album);

            }

            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println(albums.size());
        return albums;
    }

    private ArrayList<String> GenerateTestList()
    {
        ArrayList<String> testList = new ArrayList<>();
        testList.add("Swag 1");
        testList.add("Swag 2");
        testList.add("Swag 3");

        return testList;
    }
}
