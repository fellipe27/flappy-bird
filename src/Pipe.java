import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

class Pipe {
    int distance = 170;
    int velocity = 5;
    BufferedImage basePipeImage, topPipeImage;
    int x;
    GamePanel gp;
    int height, topPos, basePos;
    boolean birdPassed = false;
    Random rand = new Random();

    Pipe(GamePanel gp, int x) throws IOException {
        this.gp = gp;
        this.x = x;
        basePipeImage = ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/pipe.png")
        )));
        topPipeImage = flipVertical(basePipeImage);
        defineHeight();
    }

    boolean checkPixelPerfectCollision(BufferedImage img0, int x0, int y0, BufferedImage img1, int x1, int y1) {
        int left = Math.max(x0, x1);
        int right = Math.min(x0 + img0.getWidth(), x1 + img1.getWidth());
        int top = Math.max(y0, y1);
        int bottom = Math.min(y0 + img0.getHeight(), y1 + img1.getHeight());

        if (right <= left || bottom <= top) {
            return false;
        }

        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                int img0X = x - x0, img1X = x - x1;
                int img0Y = y - y0, img1Y = y - y1;
                int pixel0 = img0.getRGB(img0X, img0Y), pixel1 = img1.getRGB(img1X, img1Y);
                boolean opaque0 = ((pixel0 >> 24) & 0xff) != 0, opaque1 = ((pixel1 >> 24) & 0xff) != 0;

                if (opaque0 && opaque1) {
                    return true;
                }
            }
        }

        return false;
    }

    BufferedImage flipVertical(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();

        BufferedImage rotated = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform transform = AffineTransform.getScaleInstance(1, -1);

        transform.translate(0, -height);
        g2d.drawImage(image, transform, null);
        g2d.dispose();

        return rotated;
    }

    void defineHeight() {
        int MIN = 50, MAX = 250;

        height = rand.nextInt(MAX - MIN + 1) + MIN;
        topPos = height - topPipeImage.getHeight();
        basePos = height + distance;
    }

    boolean collide(Bird bird) {
        return checkPixelPerfectCollision(
                bird.image, bird.x, (int) bird.y, topPipeImage, x, topPos
        ) || checkPixelPerfectCollision(bird.image, bird.x, (int) bird.y, basePipeImage, x, basePos);
    }

    void draw(Graphics2D g2d) {
        g2d.drawImage(topPipeImage, x, topPos, null);
        g2d.drawImage(basePipeImage, x, basePos, null);
    }

    void update() {
        x -= velocity;
    }
}
