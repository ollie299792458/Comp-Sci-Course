package uk.ac.cam.olb22.oop.tick4;

/**
 * Created by oliver on 13/11/16.
 */
public class ArrayWorld extends World implements Cloneable{

    private boolean[][] mWorld;
    private boolean[] mDeadRow;

    @Override
    public ArrayWorld clone() {
        ArrayWorld cloned = (ArrayWorld) super.clone();
        cloned.mDeadRow = mDeadRow;
        cloned.mWorld = new boolean[getHeight()][getWidth()];
        for (int j = 0; j < getHeight(); j++) {
            if (mWorld[j] == mDeadRow) {
                cloned.mWorld[j] = mDeadRow;
            } else {
                for (int i = 0; i < getWidth(); i++) {
                    cloned.setCell(i, j, mWorld[j][i]);
                }
            }
        }
        return cloned;
    }

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
                if (mWorld[row] == mDeadRow && val) {
                    mWorld[row] = new boolean[getWidth()];
                }
                mWorld[row][col] = val;
                if (!val) {
                    boolean dead = true;
                    for (boolean cell : mWorld[row]) {
                        dead = dead & !cell;
                    }
                    if (dead) {
                        mWorld[row] = mDeadRow;
                    }
                }
            }
        }
    }

    public ArrayWorld(String serial) throws PatternFormatException {
        super(serial);
        mDeadRow = new boolean[getWidth()];
        mWorld = new boolean[getHeight()][getWidth()];
        for (boolean[] row : mWorld) {
            row = mDeadRow;
        }
        getPattern().initialise(this);
    }

    public ArrayWorld(Pattern pattern) throws PatternFormatException {
        super(pattern);
        mDeadRow = new boolean[getWidth()];
        mWorld = new boolean[getHeight()][];
        for (boolean[] row : mWorld) {
            row = mDeadRow;
        }
        getPattern().initialise(this);
    }

    public ArrayWorld(ArrayWorld world) {
        mWorld = new boolean[world.getHeight()][world.getWidth()];
        mDeadRow = world.mDeadRow;
        for (int j = 0; j < world.getHeight(); j++) {
            if (world.mWorld[j] == mDeadRow) {
                mWorld[j] = mDeadRow;
            } else {
                for (int i = 0; i < world.getWidth(); i++) {
                    mWorld[j][i] = world.getCell(j,i);
                }
            }
        }
        setPatternAndGeneration(world);
    }
}