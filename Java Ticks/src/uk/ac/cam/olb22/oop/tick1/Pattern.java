package uk.ac.cam.olb22.oop.tick1;

import java.lang.reflect.Array;

public class Pattern {

    private String mName;
    private String mAuthor;
    private int mWidth;
    private int mHeight;
    private int mStartCol;
    private int mStartRow;
    private String mCells;

    public String getName() {
        return mName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getStartRow() {
        return mStartRow;
    }

    public int getStartCol() {
        return mStartCol;
    }

    public String getCells() {
        return mCells;
    }

    public Pattern(String format) throws PatternFormatException {
        String[] subStrings;
        try {
            subStrings = format.split(":");
        } catch (NullPointerException e) {
            throw new PatternFormatException("Please specify a pattern.");
        }
        if (subStrings.length != 7) {
            throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found "
                    + String.valueOf(subStrings.length) + ").");
        } else {
            mName = subStrings[0];
            mAuthor = subStrings[1];
            try {
                mWidth = Integer.valueOf(subStrings[2]);
            } catch (NumberFormatException e) {
                throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('"
                        + subStrings[2] + "' given).");
            }
            try {
                mHeight = Integer.valueOf(subStrings[3]);
            } catch (NumberFormatException e) {
                throw new PatternFormatException("Invalid pattern format: Could not interpret the height field as a number ('"
                        + subStrings[3] + "' given).");
            }
            try {
                mStartCol = Integer.valueOf(subStrings[4]);
            } catch (NumberFormatException e) {
                throw new PatternFormatException("Invalid pattern format: Could not interpret the startX field as a number ('"
                        + subStrings[4] +"' given).");
            }
            try {
                mStartRow = Integer.valueOf(subStrings[5]);
            } catch (NumberFormatException e) {
                throw new PatternFormatException("Invalid pattern format: Could not interpret the startY field as a number ('"
                        + subStrings[5] +"' given).");

            }
            mCells = subStrings[6];
        }
    }

    public void initialise(boolean[][] world) throws PatternFormatException {
        for (boolean[] col : world) {
            for (boolean cell : col) {
                cell = false;
            }
        }

        String[] subStrings = mCells.split(" ");

        PatternFormatException pException = new PatternFormatException("Invalid pattern format: Malformed pattern '"
                + mCells + "'.");

        try {
            for (int i = getStartRow(); i < getStartRow() + subStrings.length; i++) {
                char[] chars = subStrings[i - getStartRow()].toCharArray();
                for (int j = getStartCol(); j < getStartCol() + chars.length; j++) {
                    int index = j - getStartCol();
                    if (chars[index] == '1') {
                        world[i][j] = true;
                    } else if (chars[index] == '0') {
                        world[i][j] = false;
                    } else {
                        throw pException;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw pException;
        }
    }
}