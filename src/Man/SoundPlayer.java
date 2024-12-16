package Man;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private Clip voic;

    // Constructor to load the sound file
    public SoundPlayer(String soundFilePath) {
        try {
            File file = new File(soundFilePath);

            if (!file.exists()) {
                System.err.println("Error: Sound file not found at: " + file.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            voic = AudioSystem.getClip();
            voic.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play the sound
    public void play() {
        if (voic != null) {
            if (voic.isRunning()) {
                voic.stop(); // Stop if already playing
            }
            voic.setFramePosition(0); // Rewind to the beginning
            voic.start();
        }
    }

    // Loop the sound continuously
    // sound in background
    public void loop() {
        if (voic != null) {
            voic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Stop the sound
    public void stop() {
        if (voic != null && voic.isRunning()) {
            voic.stop();
        }
    }

    // Check if the sound is playing
    public boolean isPlaying() {
        return voic != null && voic.isRunning();
    }
}

