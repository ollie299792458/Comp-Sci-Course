package uk.ac.cam.rkh23.Algorithms.Tick2;

public abstract class LCSFinder {
	
	protected final String mString1;
	protected final String mString2;
	protected int mTable[][]=null;
	
	public LCSFinder(String s1, String s2) {
		mString1=s1;
		mString2=s2;
	}

	/**
	 * Computes the LCS length table (i.e. fills
	 * the member mTable)
	 * @return the length of the LCS solution
	 */
	public abstract int getLCSLength();
	
	/**
	 * Computes the LCS as a string by analysing 
	 * mTable
	 * @return The LCS string
	 */
	public abstract String getLCSString();
	
	public int[][] getLCSLengthTable() { return mTable;}
}
