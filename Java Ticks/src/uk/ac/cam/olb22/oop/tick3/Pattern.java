package uk.ac.cam.olb22.oop.tick3;

public class Pattern implements Comparable<Pattern>{

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

    public void initialise(World world) throws PatternFormatException {
        for (int row = 0; row < world.getHeight(); row++) {
            for (int col = 0; col < world.getWidth(); col++) {
                world.setCell(row, col, false);
            }
        }

        String[] subStrings = mCells.split(" ");

        PatternFormatException pException = new PatternFormatException("Invalid pattern format: Malformed pattern '"
                + mCells + "'.");

        try {
            for (int row = getStartRow(); row < getStartRow() + subStrings.length; row++) {
                char[] chars = subStrings[row - getStartRow()].toCharArray();
                for (int col = getStartCol(); col < getStartCol() + chars.length; col++) {
                    int index = col - getStartCol();
                    if (chars[index] == '1') {
                        world.setCell(col, row, true);
                    } else if (chars[index] == '0') {
                        world.setCell(col, row, false);
                    } else {
                        throw pException;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw pException;
        }
    }

    @Override
    public int compareTo(Pattern p) {
        return getName().compareTo(p.getName());
    }
}