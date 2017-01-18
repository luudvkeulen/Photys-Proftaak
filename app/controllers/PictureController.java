package controllers;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import play.mvc.Controller;
import play.mvc.Result;
import watermark.Caption;
import watermark.Position;
import watermark.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureController extends Controller {
    public Result renderPhoto(String location) {
        byte[] result = null;
        FTPClient client = new FTPClient();
        BufferedImage watermarked = null;
        byte[] watermarkedbytes = null;
        try {
            client.connect(ConfigFactory.load().getString("ftp.ip"), ConfigFactory.load().getInt("ftp.port"));
            client.login(ConfigFactory.load().getString("ftp.user"), ConfigFactory.load().getString("ftp.password"));
            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream stream = client.retrieveFileStream(location);
            result = IOUtils.toByteArray(stream);

            client.disconnect();

            ByteArrayInputStream bais = new ByteArrayInputStream(result);
            BufferedImage bufferedImage = ImageIO.read(bais);

            String caption = "Photys";
            Font font = new Font("Monospaced", Font.PLAIN, 64);

            Caption filter1 = new Caption(caption, font, Color.white, 0.5f, Positions.CENTER, 0);
            Caption filter2 = new Caption(caption, font, Color.white, 0.5f, Positions.TOP_LEFT, 0);
            Caption filter3 = new Caption(caption, font, Color.white, 0.5f, Positions.TOP_RIGHT, 0);
            Caption filter4 = new Caption(caption, font, Color.white, 0.5f, Positions.BOTTOM_LEFT, 0);
            Caption filter5 = new Caption(caption, font, Color.white, 0.5f, Positions.BOTTOM_RIGHT, 0);
            watermarked = filter1.apply(bufferedImage);
            watermarked = filter2.apply(watermarked);
            watermarked = filter3.apply(watermarked);
            watermarked = filter4.apply(watermarked);
            watermarked = filter5.apply(watermarked);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(watermarked, "jpg", baos);
            watermarkedbytes = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(watermarkedbytes).as("image");
    }
}
