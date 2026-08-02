package game.managers;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class SoundManager {
    Clip backgroundSound;
    Clip successSound;

    public SoundManager() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream backgroundStream = getSoundStream("/background.wav");
        AudioInputStream successStream = getSoundStream("/success.wav");

        backgroundSound = AudioSystem.getClip();
        successSound = AudioSystem.getClip();

        backgroundSound.open(backgroundStream);
        successSound.open(successStream);
    }

    AudioInputStream getSoundStream(String path) throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/sounds" + path))
        ));
    }

    public void playSuccessSound() {
        if (successSound.isRunning()) {
            successSound.stop();
        }

        successSound.setFramePosition(0);
        successSound.start();
    }

    public void playBackgroundLoop() {
        backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
