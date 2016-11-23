package logic;

import com.typesafe.config.ConfigFactory;
import models.Photo;
import models.User;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import play.db.DB;
import play.db.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoLogic {

    private Database db;
    private PhotographerLogic pgL;
    private AlbumLogic aL;

    public PhotoLogic(Database db){
        this.db = db;
        pgL = new PhotographerLogic(db);
        aL = new AlbumLogic(db);
        }

    public Photo GetPhotoByID(int photoID)
    {
        Photo photo = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try{
            connection = db.getConnection();
            statement = connection.prepareStatement("SELECT * FROM `picture` WHERE `id` = ?");
            statement.setInt(1, photoID);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                String photoName = resultSet.getString("name");

                int photographerId = resultSet.getInt("photographer_id");
                User photographer = pgL.GetPhotographerById(photographerId);

                int albumId = resultSet.getInt("album_id");
                String albumName = aL.GetAlbumNameById(albumId);

                int fileSize = resultSet.getInt("file_size");
                Date date = resultSet.getDate("date");
                String fileLocation = resultSet.getString("file_location");
                Double price = resultSet.getDouble("price");
                String url = resultSet.getString("url");

                photo = new Photo(photoID, photoName, photographer, fileSize, date, albumName, fileLocation, price, url);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return photo;
    }

    public boolean DeletePhotoByID(int photoID)
    {
        PreparedStatement statement = null;
        Boolean ftpSucces = false;
        Boolean dbSucces = false;

        //Delete photo on ftp
        FTPClient client = new FTPClient();
        try{
            client.connect("137.74.163.54", 21);
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setSoTimeout(10000);

            Photo photo = this.GetPhotoByID(photoID);

            ftpSucces =  client.deleteFile(photo.getFileLocation());

        }
        catch(IOException ex){
            ex.printStackTrace();
            return false;
        }

        //Delete photo reference on database
        try(Connection connection = db.getConnection()) {
            statement = connection.prepareStatement("DELETE FROM `picture` WHERE `id` = ?");
            statement.setInt(1, photoID);
            dbSucces = statement.execute();
        }catch(SQLException ex){
            ex.printStackTrace();
        }

        if(dbSucces == true && ftpSucces == true) {
            return true;
        }
        else {
            System.out.println("Something went wrong :c");
            return false;
        }
    }

}
