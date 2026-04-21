import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

class Sound {
    Clip backgroundSound;
    Clip successSound;

    Sound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/sounds/background.wav"))
        ));
        AudioInputStream successStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/sounds/success.wav"))
        ));

        backgroundSound = AudioSystem.getClip();
        successSound = AudioSystem.getClip();

        backgroundSound.open(backgroundStream);
        successSound.open(successStream);
    }

    void playSuccessSound() {
        if (successSound.isRunning()) {
            successSound.stop();
        }

        successSound.setFramePosition(0);
        successSound.start();
    }

    void playBackgroundLoop() {
        backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
