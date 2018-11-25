package uk.ac.cam.olb22.Algorithms.Tick2Star;

import uk.ac.cam.rkh23.Algorithms.Tick2.LCSFinder;

import java.util.Arrays;

/**
 * Created by oliver on 11/02/17.
 */
public class LCSTopDownRecursive extends LCSFinder {
    public LCSTopDownRecursive(String s1, String s2) {
        super(s1, s2);
    }

    @Override
    public int getLCSLength() {

        int widthi = mString1.length();
        int heightj = mString2.length();
        mTable = new int[widthi][heightj];

        for (int[] row : mTable) {
            Arrays.fill(row, -1);
        }

        return getLCSLengthRec(widthi-1,heightj-1);
    }

    private int getLCSLengthRec(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        } else if (mTable[i][j] < 0) {
            if (mString1.charAt(i) == mString2.charAt(j)) {
                mTable[i][j] = 1 + getLCSLengthRec(i-1, j-1);
            } else {
                mTable[i][j] = Math.max(getLCSLengthRec(i-1, j), getLCSLengthRec(i,j-1));
            }
        }
        return mTable[i][j];
    }

    @Override
    public String getLCSString() {
        String result = "";
        int i = mString1.length() - 1;
        int j = mString2.length() - 1;
        while (i >= 0 && j >= 0) {
            if (mString1.charAt(i) == mString2.charAt(j)) {
                //char in String
                result = mString1.charAt(i) + result;
                //move diagonally down
                i--;
                j--;
            } else {
                int upj = 0;
                int righti = 0;
                if (i > 0) {
                    righti = mTable[i-1][j];
                }
                if (j > 0) {
                    upj = mTable[i][j-1];
                }
                if (righti == upj) {
                    i--;
                } else if (righti > upj) {
                    i--;
                } else {
                    j--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        LCSFinder lcsFinder = (LCSFinder) new LCSTopDownRecursive("ABBA", "CACA");
        System.out.println(lcsFinder.getLCSLength());
        System.out.println(Arrays.deepToString(lcsFinder.getLCSLengthTable()));
        System.out.println(lcsFinder.getLCSString());
    }
}