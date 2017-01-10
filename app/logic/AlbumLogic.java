package logic;

import models.Album;
import play.Logger;
import play.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlbumLogic {

    private final Database db;

    public AlbumLogic(Database db) {
        this.db = db;
    }

    public String getAlbumNameById(int albumId) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;
            String albumName = null;

            String sql = "SELECT `name` FROM `album` WHERE `id` = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, albumId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                albumName = resultSet.getString("name");
            }

            return albumName;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Album getAlbumById(int albumId) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;
            Album album = null;

            String sql = "SELECT * FROM `album` WHERE `id` = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, albumId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String albumName = resultSet.getString("name");
                int photographerId = resultSet.getInt("photographer_id");
                String description = resultSet.getString("description");
                int accessibility = resultSet.getInt("private");
                String url = resultSet.getString("albumURL");

                boolean access = false;
                if(accessibility == 1) {
                    access = true;
                }
                album = new Album(albumId, albumName, photographerId, description, access, url);
            }
            return album;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getAlbumIdByURL(String albumUrl, String userEmail) {
        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = null;
            int albumId = -1;
            int privateAlbum = -1;

            String sql = "SELECT `id`, `private` FROM `album` WHERE `albumURL` = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, albumUrl);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                albumId = resultSet.getInt("id");
                privateAlbum = resultSet.getInt("private");
            }

            if (privateAlbum == 1) {

                AdminLogic adminLogic = new AdminLogic(db);

                if (adminLogic.isAdmin(userEmail)) {
                    return albumId;
                }

                String sql2 = "SELECT a.*, u.emailadres FROM useralbum a join `user` u on u.emailadres = a.user_email WHERE album_id = ?";
                statement = connection.prepareStatement(sql2);
                statement.setInt(1, albumId);

                resultSet = statement.executeQuery();

                int results = 0;
                while (resultSet.next()) {
                    if (resultSet.getString("emailadres").equals(userEmail)) {
                        return albumId;
                    }
                    results++;
                }

                if (results == 0) {
                    return albumId;
                } else {
                    return -1;
                }
            }
            return albumId;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean deleteAlbum(int albumId) {
        PreparedStatement statement = null;
        Boolean dbSucces = false;

        //Delete album reference from useralbum table in database
        try (Connection connection = db.getConnection()) {
            String sql = "DELETE FROM `useralbum` WHERE `album_id` = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, albumId);
            dbSucces = statement.execute();
        } catch (SQLException ex) {
            Logger.info("Something went wrong while deleting album from the database");
            ex.printStackTrace();
        }

        //Delete album reference from album table in database
        try (Connection connection = db.getConnection()) {
            String sql = "DELETE FROM `album` WHERE `id` = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, albumId);
            dbSucces = statement.execute();
        } catch (SQLException ex) {
            Logger.info("Something went wrong while deleting album from the database");
            ex.printStackTrace();
        }

        return dbSucces;
    }

}
