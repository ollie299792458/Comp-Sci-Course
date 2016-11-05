package uk.ac.cam.olb22.oop.tick1star;

import uk.ac.cam.olb22.oop.tick1.Pattern;
import uk.ac.cam.olb22.oop.tick1.PatternFormatException;

import java.io.IOException;

public class AnimatedLife {

    private boolean[][] mWorld;
    private Pattern pattern;
    private OutputAnimatedGif outputAnimatedGif;

    public boolean getCell(int col, int row) {
        if (row < 0 || row > mWorld.length - 1) return false;
        if (col < 0 || col > mWorld[row].length - 1) return false;

        return mWorld[row][col];
    }

    public void setCell(int col, int row, boolean value) {
        if (!(row < 0 || row > mWorld.length - 1)) {
            if (!(col < 0 || col > mWorld[row].length - 1)) {
                mWorld[row][col] = value;
            }
        }
    }

    private int countNeighbours(int col, int row) {
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

    private boolean computeCell(int col, int row) {

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

    public void play(int generations) throws IOException {
        for (int i = generations; i >= 0; i--) {
            outputAnimatedGif.addFrame(mWorld);
            nextGeneration();
        }
        outputAnimatedGif.close();
    }

    public AnimatedLife(String format, String file) throws PatternFormatException, IOException{
        pattern = new Pattern(format);
        mWorld = new boolean[pattern.getHeight()][pattern.getWidth()];
        pattern.initialise(mWorld);
        outputAnimatedGif = new OutputAnimatedGif(file);
    }

    public static void main(String[] args) throws Exception {
        try {
            AnimatedLife al = new AnimatedLife(args[0], args[2]);

            al.play(Integer.valueOf(args[1]));
        } catch (PatternFormatException e){
            System.out.println(e.getMessage());

        }
    }
}