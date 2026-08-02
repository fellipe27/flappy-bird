package game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

class Main {
    void main() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        JFrame window = new JFrame();
        GamePanel gp = new GamePanel();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Flappy bird");
        window.setVisible(true);
        window.add(gp);
        window.pack();
        window.setLocationRelativeTo(null);
    }
}
