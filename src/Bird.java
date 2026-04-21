import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Bird {
    int x = 50;
    double y = 120;
    double height = y;
    int maxRotation = 25, rotationVelocity = 20;
    int animationTime = 5;
    int imageCount = 0;
    double velocity = 0;
    int time = 0;
    double angle = 0;
    GamePanel gp;
    List<BufferedImage> birdImages = new ArrayList<>();
    BufferedImage image;

    Bird(GamePanel gp) throws IOException {
        this.gp = gp;

        for (int i = 0; i < 3; i++) {
            birdImages.add(ImageIO.read(new BufferedInputStream(
                    Objects.requireNonNull(getClass().getResourceAsStream("/images/bird-" + i + ".png"))
            )));
        }

        image = birdImages.get(imageCount);
    }

    BufferedImage rotatedImage(BufferedImage image, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int width = image.getWidth(), height = image.getHeight();
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
        g2d.rotate(rads, width / 2.0, height / 2.0);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    void jump() {
        velocity = -10.5;
        time = 0;
        height = y;
    }

    void draw(Graphics2D g2d) {
        imageCount++;

        if (imageCount < animationTime) {
            image = birdImages.getFirst();
        } else if (imageCount < animationTime * 2) {
            image = birdImages.get(1);
        } else if (imageCount < animationTime * 3) {
            image = birdImages.get(2);
        } else if (imageCount < animationTime * 4) {
            image = birdImages.get(1);
        } else if (imageCount < animationTime * 5) {
            image = birdImages.getFirst();
            imageCount = 0;
        }

        if (angle < -80) {
            image = birdImages.get(1);
            imageCount = animationTime * 2;
        }

        BufferedImage rotated = rotatedImage(image, -angle);
        int centerX = x + image.getWidth() / 2, centerY = (int) y + image.getHeight() / 2;
        int drawX = centerX - rotated.getWidth() / 2, drawY = centerY - rotated.getHeight() / 2;

        g2d.drawImage(rotated, drawX, drawY, null);
    }

    void update() {
        time++;

        double displacement = 1.5 * Math.pow(time, 2) + velocity * time;

        if (displacement > 16) {
            displacement = 16;
        } else if (displacement < 0) {
            displacement -= 2;
        }

        y += displacement;

        if (displacement < 0 || y < height + 50) {
            if (angle < maxRotation) {
                angle = maxRotation;
            }
        } else {
            if (angle > -90) {
                angle -= rotationVelocity;
            }
        }
    }
}
