package uk.ac.cam.olb22.oop.tick2;

import java.io.IOException;

/**
 * Created by oliver on 14/11/16.
 */
public class GameOfLife {

    private World mWorld;

    public GameOfLife(World w) {
        mWorld = w;
    }

    public void play() throws IOException {
        int userResponse = 0;
        while (userResponse != 'q') {
            print();
            userResponse = System.in.read();
            mWorld.nextGeneration();
        }
    }

    public void print() {
        System.out.println("- "+mWorld.getGenerationCount());
        for (int row = 0; row < mWorld.getHeight(); row++) {
            for (int col = 0; col < mWorld.getWidth(); col++) {
                System.out.print(mWorld.getCell(col, row) ? "#" : "_");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) throws IOException {
        //args = new String[]{"Glider:Richard Guy:9:7:1:1:010 001 111"};
        try {
            World w = null;
            if (args.length == 2) {
                if (args[0].equals("--packed")) {
                    w = new PackedWorld(args[1]);
                } else if (args[0].equals("--array")) {
                    w = new ArrayWorld(args[1]);
                } else {
                    throw new PatternFormatException("Invalid command line option");
                }
            } else if (args.length == 1){
                w = new ArrayWorld(args[0]);
            } else {
                throw new PatternFormatException("Invalid number of command line arguments");
            }
            GameOfLife gol = new GameOfLife(w);
            gol.play();
        }
        catch(PatternFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}
