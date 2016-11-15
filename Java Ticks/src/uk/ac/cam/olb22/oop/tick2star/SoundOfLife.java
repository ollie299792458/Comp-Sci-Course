package uk.ac.cam.olb22.oop.tick2star;

import uk.ac.cam.acr31.sound.AudioSequence;
import uk.ac.cam.acr31.sound.SineWaveSound;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by oliver on 14/11/16.
 */
public class SoundOfLife {
    private static final double DEFAULT_TIMESLOT_DURATION_SECONDS = 0.01666;
    private static final int MAX_GENERATIONS = 1200;
    private static final int FREQUENCY_CONSTANT = 60;
    private static final double AMPLITUDE_CONSTANT = 0.5;
    private World mWorld;
    private AudioSequence mAudioSequence;
    private String mFileName = "soundOfLifeOut.wav";

    public SoundOfLife(World w) {
        mWorld = w;
        mAudioSequence = new AudioSequence(DEFAULT_TIMESLOT_DURATION_SECONDS);
    }

    public SoundOfLife(World w, String fileName) {
        mWorld = w;
        mAudioSequence = new AudioSequence(DEFAULT_TIMESLOT_DURATION_SECONDS);
        mFileName = fileName;
    }

    public void play() throws IOException {
        while (mWorld.getGenerationCount() <= MAX_GENERATIONS) {
            addSounds();
            mWorld.nextGeneration();
        }
        mAudioSequence.write(new FileOutputStream(mFileName));
    }

    public void addSounds() {
        mAudioSequence.advance();
        for (int row = 0; row < mWorld.getHeight(); row++) {
            for (int col = 0; col < mWorld.getWidth(); col++) {
                if (mWorld.getCell(col, row)) {
                    mAudioSequence.addSound(new SineWaveSound(row*col*FREQUENCY_CONSTANT, AMPLITUDE_CONSTANT));
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        try {
            if (args.length != 0){
                throw new PatternFormatException("Invalid number of command line arguments");
            }
            World w = new ArrayWorld("R-Pentomino:John Conway feat. Oliver Black:100:100:48:48:011 110 010");
            String fileName = "soundOfLifeOut.wav";
            SoundOfLife sol = new SoundOfLife(w, fileName);
            sol.play();
            System.out.println("Finished");
        }
        catch(PatternFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}
