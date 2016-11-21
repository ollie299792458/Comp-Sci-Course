package uk.ac.cam.olb22.oop.tick3;

/**
 * Created by oliver on 13/11/16.
 */
public class ArrayWorld extends World {

    private boolean[][] mWorld;

    @Override
    public boolean getCell(int col, int row) {
        if (row < 0 || row > mWorld.length - 1) return false;
        if (col < 0 || col > mWorld[row].length - 1) return false;

        return mWorld[row][col];
    }

    @Override
    public void nextGenerationImpl() {
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
        if (!(row < 0 || row > mWorld.length - 1)) {
            if (!(col < 0 || col > mWorld[row].length - 1)) {
                mWorld[row][col] = val;
            }
        }
    }

    public ArrayWorld(String serial) throws PatternFormatException {
        super(serial);
        mWorld = new boolean[getHeight()][getWidth()];
        getPattern().initialise(this);
    }

    public ArrayWorld(Pattern pattern) throws PatternFormatException {
        super(pattern);
        mWorld = new boolean[getHeight()][getWidth()];
        getPattern().initialise(this);
    }
}