package uk.ac.cam.olb22.Algorithms.Tick2;

import uk.ac.cam.rkh23.Algorithms.Tick2.LCSFinder;

import java.util.Arrays;

/**
 * Created by oliver on 11/02/17.
 */
public class LCSBottomUp extends LCSFinder {
    public LCSBottomUp(String s1, String s2) {
        super(s1, s2);
    }

    @Override
    public int getLCSLength() {
        int widthi = mString1.length();
        int heightj = mString2.length();
        mTable = new int[widthi][heightj];
        for (int i = 0; i < widthi; i++) {
            for (int j = 0; j < heightj; j++) {
                int cellValue = 0;
                if (mString1.charAt(i)==mString2.charAt(j)) {
                    cellValue += 1;
                    if (i > 0 && j > 0) {
                        cellValue += mTable[i-1][j-1];
                    }
                } else {
                    int left = 0;
                    int up = 0;

                    if (i > 0) {
                        left = mTable[i - 1][j];
                    }
                    if (j > 0) {
                        up = mTable[i][j - 1];
                    }

                    cellValue += Math.max(left, up);
                }

                mTable[i][j] = cellValue;
            }
        }
        if (widthi > 0 && heightj > 0) {
            return mTable[widthi - 1][heightj - 1];
        } else {
            return 0;
        }
    }

    @Override
    public String getLCSString() {
        String result = "";
        int i = 0;
        int j = 0;
        while (i < mString1.length() && j < mString2.length()) {
            if (mString1.charAt(i)==mString2.charAt(j)) {
                //char in String
                result += mString1.charAt(i);
                //move diagonally down
                i++;
                j++;
            } else {
                int upj = 0;
                int righti = 0;
                if (i < mString1.length() - 1) {
                    righti = mTable[i+1][j];
                }
                if (j < mString2.length() - 1) {
                    upj = mTable[i][j+1];
                }
                if (righti >= upj) {
                    i++;
                } else {
                    j++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        LCSFinder lcsFinder = (LCSFinder) new LCSBottomUp("XMJYAUZ", "MZJAWXU");
        System.out.println(lcsFinder.getLCSLength());
        System.out.println(Arrays.deepToString(lcsFinder.getLCSLengthTable()));
        System.out.println(lcsFinder.getLCSString());
    }
}
