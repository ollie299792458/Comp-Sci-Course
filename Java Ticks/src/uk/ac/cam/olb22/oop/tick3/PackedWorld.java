package uk.ac.cam.olb22.oop.tick3;

/**
 * Created by oliver on 13/11/16.
 */
public class PackedWorld extends World {

    private long mWorld;

    @Override
    public boolean getCell(int col, int row) {
        if (!((col >= getHeight()) || (row >= getHeight()) || (col<0) || (row<0))) {
            return getFromLong(mWorld, col + row * getWidth());
        } else {
            return false;
        }
    }

    @Override
    public void nextGenerationImpl() {
        long newWorld = 0;
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                newWorld = setInLong(newWorld, col + row * getWidth(), computeCell(col, row));
            }
        }
        mWorld = newWorld;
    }

    @Override
    public void setCell(int col, int row, boolean val) {
        if (!((col >= getWidth()) || (row >= getHeight()) || (col<0) || (row<0))) {
            mWorld = setInLong(mWorld, col + row * getWidth(), val);
        }
    }

    public PackedWorld(String serial) throws PatternFormatException {
        super(serial);
        if ((getWidth()*getHeight())>64){
            throw new PatternFormatException("World size too big");
        }
        mWorld = 0;
        getPattern().initialise(this);
    }

    public PackedWorld(Pattern pattern) throws PatternFormatException {
        super(pattern);
        if ((getWidth()*getHeight())>64){
            throw new PatternFormatException("World size too big");
        }
        mWorld = 0;
        getPattern().initialise(this);
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