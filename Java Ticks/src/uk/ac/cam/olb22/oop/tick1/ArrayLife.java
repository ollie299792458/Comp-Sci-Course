package uk.ac.cam.olb22.oop.tick1;
        
public class ArrayLife {

    private boolean[][] mWorld;
    private Pattern pattern;

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

    public ArrayLife(String format) throws PatternFormatException{
        pattern = new Pattern(format);
        mWorld = new boolean[pattern.getHeight()][pattern.getWidth()];
        pattern.initialise(mWorld);
    }

    public static void main(String[] args) throws Exception {
        //args = new String[]{"Glider:Richard Guy:9:7:1:1:010 001 111"};
        try {
            ArrayLife al = new ArrayLife(args[0]);

            al.play();
        } catch (PatternFormatException e){
            System.out.println(e.getMessage());

        }
    }
}