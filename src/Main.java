import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

void main() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
    JFrame window = new JFrame();
    GamePanel gamePanel = new GamePanel();

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Flappy Bird");
    window.setVisible(true);
    window.add(gamePanel);
    window.pack();
    window.setLocationRelativeTo(null);
}
