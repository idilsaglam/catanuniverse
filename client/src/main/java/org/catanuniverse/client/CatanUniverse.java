/*
	22015094 - Idil Saglam*/
package org.catanuniverse.client;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CatanUniverse {

    public static void main(String[] args)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        MainFrame frame = new MainFrame();

        AudioInputStream audioIn =
                AudioSystem.getAudioInputStream(CatanUniverse.class.getResource("/catanmenu.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        // clip.start();
    }
}
