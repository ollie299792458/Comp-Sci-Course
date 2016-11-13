package uk.ac.cam.olb22.oop.tick2;

import uk.ac.cam.olb22.oop.tick1.PatternFormatException;
import uk.ac.cam.olb22.prejava.ex1.PackedLong;


import java.io.IOException;

/**
 * Created by oliver on 13/11/16.
 */
public class PackedWorld extends World {

    private long mWorld;

    @Override
    public boolean getCell(int col, int row) {
        if (!((col > 7) || (row > 7) || (col<0) || (row<0))) {
            return PackedLong.get(mWorld, col + row * 8);
        } else {
            return false;
        }
    }

    @Override
    public void nextGeneration() {
        boolean[][] nextGeneration = new boolean[mWorld.length][];
        for (int y = 0; y < mWorld.length; ++y) {
            nextGeneration[y] = new boolean[mWorld[y].length];
            for (int x = 0; x < mWorld[y].length; ++x) {
                boolean nextCell = computeCell(x, y);
                nextGeneration[y][x]=nextCell;
            }
        }
        mWorld = nextGeneration;
    }

    @Override
    public void setCell(int col, int row, boolean val) {
        long mask = 1;
        if (value) {
            packed = packed | (mask<<position);
        }
        else {
            packed = packed & ~(mask<<position);
        }
        return packed;
    }

    public PackedWorld(String serial) throws PatternFormatException {
        super(serial);
        mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
        getPattern().initialise(this);
    }

    public void print() {
        System.out.println("-");
        for (int row = 0; row < mWorld.length; row++) {
            for (int col = 0; col < mWorld[row].length; col++) {
                System.out.print(getCell(col, row) ? "#" : "_");
            }
            System.out.println();
        }
    }

    public void play() throws java.io.IOException {
        int userResponse = 0;
        while (userResponse != 'q') {
            print();
            userResponse = System.in.read();
            nextGeneration();
        }
    }

    public static void main(String args[]) throws IOException {
        args = new String[1];
        args[0] = "Glider:Richard Guy:20:20:1:1:010 001 111";
        try {
            ArrayWorld pl = new ArrayWorld(args[0]);
            pl.play();
        }
        catch (PatternFormatException e) {
            System.out.println(e.getMessage());
        }

    }

}