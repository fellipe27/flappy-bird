package game.objects;

import game.GamePanel;
import game.managers.ImageManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bird {
    GamePanel gamePanel;
    ImageManager imageManager = new ImageManager();
    public int x = 50;
    public double y = 120;
    double height = y;
    int maxRotation = 25, rotationVelocity = 20;
    int animationTime = 5;
    int imageCount = 0;
    double velocity = 0;
    int time = 0;
    double angle = 0;
    List<BufferedImage> images = new ArrayList<>();
    public BufferedImage image;

    public Bird(GamePanel gamePanel) throws IOException {
        this.gamePanel = gamePanel;

        for (int i = 0; i < 3; i++) {
            images.add(imageManager.setImage("/bird-" + i + ".png"));
        }

        image = images.get(imageCount);
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

    public void jump() {
        velocity = -10.5;
        time = 0;
        height = y;
    }

    public void draw(Graphics2D g2d) {
        imageCount++;

        if (imageCount < animationTime) {
            image = images.getFirst();
        } else if (imageCount < animationTime * 2) {
            image = images.get(1);
        } else if (imageCount < animationTime * 3) {
            image = images.get(2);
        } else if (imageCount < animationTime * 4) {
            image = images.get(1);
        } else {
            image = images.getFirst();
            imageCount = 0;
        }

        if (angle < -80) {
            image = images.get(1);
            imageCount = animationTime * 2;
        }

        BufferedImage rotated = rotatedImage(image, -angle);
        int centerX = x + image.getWidth() / 2, centerY = (int) y + image.getHeight() / 2;
        int drawX = centerX - rotated.getWidth() / 2, drawY = centerY - rotated.getHeight() / 2;

        g2d.drawImage(rotated, drawX, drawY, null);
    }

    public void update() {
        time++;

        double displacement = 1.5 * Math.pow(time, 2) + velocity * time;

        if (displacement > 16) {
            displacement = 16;
        } else {
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
