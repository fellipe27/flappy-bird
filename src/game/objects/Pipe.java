package game.objects;

import game.GamePanel;
import game.managers.ImageManager;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Pipe {
    GamePanel gamePanel;
    ImageManager imageManager = new ImageManager();
    int distance = 170;
    int velocity = 5;
    BufferedImage basePipeImage;
    public BufferedImage topPipeImage;
    public int x;
    int height;
    int topPos, basePos;
    public boolean birdPassed = false;
    Random random = new Random();

    public Pipe(GamePanel gamePanel, int x) throws IOException {
        this.gamePanel = gamePanel;
        this.x = x;
        basePipeImage = imageManager.setImage("/pipe.png");
        topPipeImage = flipVertical(basePipeImage);
        defineHeight();
    }

    boolean checkPixelPerfectCollision(BufferedImage image0, int x0, int y0, BufferedImage image1, int x1, int y1) {
        int left = Math.max(x0, x1);
        int right = Math.min(x0 + image0.getWidth(), x1 + image1.getWidth());
        int top = Math.max(y0, y1);
        int bottom = Math.min(y0 + image0.getHeight(), y1 + image1.getHeight());

        if (right <= left || bottom <= top) {
            return false;
        }

        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                int image0X = x - x0, image1X = x - x1;
                int image0Y = y - y0, image1Y = y - y1;
                int pixel0 = image0.getRGB(image0X, image0Y), pixel1 = image1.getRGB(image1X, image1Y);
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
        int min = 50, max = 250;
        height = random.nextInt(max - min + 1) + min;
        topPos = height - topPipeImage.getHeight();
        basePos = height + distance;
    }

    public boolean collide(Bird bird) {
        return checkPixelPerfectCollision(
                bird.image, bird.x, (int) bird.y, topPipeImage, x, topPos
        ) || checkPixelPerfectCollision(bird.image, bird.x, (int) bird.y, basePipeImage, x, basePos);
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(topPipeImage, x, topPos, null);
        g2d.drawImage(basePipeImage, x, basePos, null);
    }

    public void update() {
        x -= velocity;
    }
}
