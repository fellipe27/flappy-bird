package game.managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {
    public BufferedImage setImage(String path) throws IOException {
        return ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images" + path))
        ));
    }
}
