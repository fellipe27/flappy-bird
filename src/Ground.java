import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

class Ground {
    int velocity = 5;
    int width;
    BufferedImage groundImage;
    GamePanel gp;
    int x0 = 0;
    int x1;
    int y;

    Ground(GamePanel gp) throws IOException {
        this.gp = gp;
        groundImage = ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/ground.png")
        )));
        width = groundImage.getWidth();
        x1 = width;
        y = gp.height - groundImage.getHeight();
    }

    void draw(Graphics2D g2d) {
        g2d.drawImage(groundImage, x0, y, null);
        g2d.drawImage(groundImage, x1, y, null);
    }

    void update() {
        x0 -= velocity;
        x1 -= velocity;

        if (x0 + width < 0) {
            x0 = x1 + width;
        }
        if (x1 + width < 0) {
            x1 = x0 + width;
        }
    }
}
