package logic;

import com.typesafe.config.ConfigFactory;
import models.Photo;
import models.User;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import play.Logger;
import play.db.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PhotoLogic {

    private final Database db;
    private final PhotographerLogic pgL;
    private final AlbumLogic aL;

    public PhotoLogic(Database db){
        this.db = db;
        pgL = new PhotographerLogic(db);
        aL = new AlbumLogic(db);
        }

    private Photo GetPhotoByID(int photoID)
    {
        Logger.info("GetPhotoByID is called!");
        Photo photo = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try{
            connection = db.getConnection();
            Logger.info("Received DB connection");
            statement = connection.prepareStatement("SELECT * FROM `picture` WHERE `id` = ?");
            statement.setInt(1, photoID);
            Logger.info("Querying");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Logger.info("Getting photo properties");
                String photoName = resultSet.getString("name");

                int photographerId = resultSet.getInt("photographer_id");
                User photographer = pgL.getPhotographerById(photographerId);

                int albumId = resultSet.getInt("album_id");
                String albumName = aL.getAlbumNameById(albumId);

                int fileSize = resultSet.getInt("file_size");
                Date date = resultSet.getDate("date");
                String fileLocation = resultSet.getString("file_location");
                Logger.info("Photo file location is: " + fileLocation);
                Double price = resultSet.getDouble("price");
                String url = resultSet.getString("url");

                photo = new Photo(photoID, photoName, photographer, fileSize, date, albumName, fileLocation, price, url);
            }
        }
        catch(SQLException ex){
            Logger.info("Could not retrieve photo!");
            ex.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return photo;
    }

    public ArrayList<Photo> getPhotosByAlbumID(int albumId) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;
            ArrayList<Photo> photos = new ArrayList<>();

            statement = connection.prepareStatement("SELECT * FROM `picture` WHERE `album_id` = ?");
            statement.setInt(1, albumId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int photoID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String fileLocation = resultSet.getString("file_location");
                photos.add(new Photo(photoID, name, fileLocation));
            }
            return photos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deletePhotoById(int photoId)
    {
        PreparedStatement statement;
        Boolean ftpSucces;
        Boolean dbSucces = false;

        //Delete photo on ftp
        FTPClient client = new FTPClient();
        try{
            client.connect(ConfigFactory.load().getString("ftp.ip"), ConfigFactory.load().getInt("ftp.port"));
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setSoTimeout(10000);


            Logger.info("Succesfully connected to the ftp client!");
            Logger.info("Getting photo by ID...");
            Photo photo = this.GetPhotoByID(photoId);
            Logger.info("Recieved photo by id! id:" + photoId);
            Logger.info("Photo file location : " + photo.getFileLocation());
            ftpSucces =  client.deleteFile(photo.getFileLocation());

        }
        catch(IOException ex){
            Logger.info("Couldnt connect to the ftp server");
            ex.printStackTrace();
            return false;
        }

        //Delete photo reference on database
        try(Connection connection = db.getConnection()) {
            String sql = "DELETE FROM `picture` WHERE `id` = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, photoId);
            dbSucces = statement.execute();
        }catch(SQLException ex){
            Logger.info("Something went wrong with the database");
            ex.printStackTrace();
        }

        return (dbSucces && ftpSucces);
    }

}
