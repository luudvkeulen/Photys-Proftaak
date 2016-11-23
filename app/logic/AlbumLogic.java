package logic;

import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Thijs on 22-11-2016.
 */
public class AlbumLogic {

    Database db;

    public AlbumLogic(Database db) {this.db = db;}

    public String GetAlbumNameById(int albumID) {
        try (Connection connection = db.getConnection()) {

            PreparedStatement statement;
            String albumName = null;

            statement = connection.prepareStatement("SELECT `name` FROM `album` WHERE `id` = ?");
            statement.setInt(1, albumID);

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

    public int GetAlbumIdByURL(String albumUrl, String userEmail) {
        try (Connection connection = db.getConnection()) {
            PreparedStatement statement = null;
            int albumId = -1;
            int privateAlbum = -1;

            statement = connection.prepareStatement("SELECT `id` FROM `album` WHERE `albumURL` = ?");
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

                statement = connection.prepareStatement("SELECT a.*, u.emailadres FROM useralbum a join `user` u on u.emailadres = a.user_email WHERE album_id = ?");
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


}
