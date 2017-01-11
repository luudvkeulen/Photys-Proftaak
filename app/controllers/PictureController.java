package controllers;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import play.mvc.Controller;
import play.mvc.Result;
import java.io.IOException;
import java.io.InputStream;

public class PictureController extends Controller {
    public Result renderPhoto(String location) {
        byte[] result = null;
        FTPClient client = new FTPClient();
        try {
            client.connect(ConfigFactory.load().getString("ftp.ip"), ConfigFactory.load().getInt("ftp.port"));
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream stream = client.retrieveFileStream(location);
            result = IOUtils.toByteArray(stream);
            client.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(result).as("image");
    }
}
