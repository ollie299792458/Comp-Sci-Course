package uk.ac.cam.olb22.oop.tick2;

import uk.ac.cam.olb22.oop.tick1.PatternFormatException;


import java.io.IOException;

/**
 * Created by oliver on 13/11/16.
 */
public class PackedWorld extends World {

    private long mWorld;

    @Override
    public boolean getCell(int col, int row) {
        if (!((col > 7) || (row > 7) || (col<0) || (row<0))) {
            return getFromLong(mWorld, col + row * 8);
        } else {
            return false;
        }
    }

    @Override
    public void nextGeneration() {
        long newWorld = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                newWorld = setInLong(newWorld, col + row * 8, computeCell(col, row));
            }
        }
        incrementGenerationCount();
        mWorld = newWorld;
    }

    @Override
    public void setCell(int col, int row, boolean val) {
        if (!((col > 7) || (row > 7) || (col<0) || (row<0))) {
            mWorld = setInLong(mWorld, col + row * 8, val);
        }
    }

    public PackedWorld(String serial) throws PatternFormatException {
        super(serial);
        if (getWidth()>8 || getHeight()>8){
            throw new PatternFormatException("World size too big");
        }
        mWorld = 0;
        getPattern().initialise(this);
    }

    public void print() {
        System.out.println("-");
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
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
        args[0] = "Glider:Richard Guy:8:8:1:1:010 001 111";
        try {
            PackedWorld pl = new PackedWorld(args[0]);
            pl.play();
        }
        catch (PatternFormatException e) {
            System.out.println(e.getMessage());
        }

    }

    /*
    * Unpack and return the nth bit from the packed number at index position;
    * position counts from zero (representing the least significant bit)
    * up to 63 (representing the most significant bit).
    */
    private static boolean getFromLong(long packed, int position) {
        // set "check" to equal 1 if the "position" bit in "packed" is set to 1
        long check = (packed>>>position)& 1;

        return (check == 1);
    }

    /*
     * Set the nth bit in the packed number to the value given
     * and return the new packed number
     */
    private static long setInLong(long packed, int position, boolean value) {
        long mask = 1;
        if (value) {
            packed = packed | (mask<<position);
        }
        else {
            packed = packed & ~(mask<<position);
        }
        return packed;
    }
}