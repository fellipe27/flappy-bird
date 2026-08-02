package game.objects;

import game.GamePanel;
import game.managers.ImageManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Ground {
    GamePanel gamePanel;
    ImageManager imageManager = new ImageManager();
    int velocity = 5;
    int width;
    BufferedImage image;
    int x0 = 0, x1;
    public int y;

    public Ground(GamePanel gamePanel) throws IOException {
        this.gamePanel = gamePanel;
        image = imageManager.setImage("/ground.png");
        width = image.getWidth();
        x1 = width;
        y = gamePanel.height - image.getHeight();
    }

    public void update() {
        x0 -= velocity;
        x1 -= velocity;

        if (x0 + width < 0) {
            x0 = x1 + width;
        }
        if (x1 + width < 0) {
            x1 = x0 + width;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x0, y, null);
        g2d.drawImage(image, x1, y, null);
    }
}
