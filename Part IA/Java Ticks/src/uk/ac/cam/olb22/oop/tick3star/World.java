package uk.ac.cam.olb22.oop.tick3star;

/**
 * Created by oliver on 13/11/16.
 */
public abstract class World {
    public abstract boolean getCell(int col, int row);

    public void nextGeneration() {
        nextGenerationImpl();
        incrementGenerationCount();
    }

    protected abstract void nextGenerationImpl();

    public abstract void setCell(int col, int row, boolean val);

    private int mGeneration = 0;
    private Pattern mPattern;

    public World(String serial) throws PatternFormatException {
        mPattern = new Pattern(serial);
    }

    public World(Pattern pattern) {
        mPattern = pattern;
    }

    public int getWidth() {
        return mPattern.getWidth();
    }

    public int getHeight() {
        return mPattern.getHeight();
    }

    public int getGenerationCount() {
        return mGeneration;
    }

    protected void incrementGenerationCount() {
        mGeneration++;
    }

    public Pattern getPattern() {
        return mPattern;
    }

    public int countNeighbours(int col, int row) {
        int res = 0;
        res += getCell(col-1, row-1) ? 1 : 0;
        res += getCell(col-1, row) ? 1 : 0;
        res += getCell(col-1, row+1) ? 1 : 0;
        res += getCell(col, row-1) ? 1 : 0;
        res += getCell(col, row+1) ? 1 : 0;
        res += getCell(col+1, row-1) ? 1 : 0;
        res += getCell(col+1, row) ? 1 : 0;
        res += getCell(col+1, row+1) ? 1 : 0;
        return res;
    }

    public boolean computeCell(int col, int row) {
        // liveCell is true if the cell at position (col,row) in mWorld is live
        boolean liveCell = getCell(col, row);

        // neighbours is the number of live neighbours to cell (col,row)
        int neighbours = countNeighbours(col, row);

        // we will return this value at the end of the method to indicate whether
        // cell (col,row) should be live in the next generation
        boolean nextCell = false;

        //A live cell with less than two neighbours dies (underpopulation)
        if (neighbours < 2) {
            nextCell = false;
        }

        //A live cell with two or three neighbours lives (a balanced population)
        if (neighbours == 2 || neighbours == 3) {
            nextCell = liveCell;
        }

        //A live cell with with more than three neighbours dies (overcrowding)
        if (neighbours > 3) {
            nextCell = false;
        }

        //A dead cell with exactly three live neighbours comes alive
        if (!liveCell && neighbours == 3) {
            nextCell = true;
        }

        return nextCell;
    }
}
