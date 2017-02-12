package uk.ac.cam.olb22.Algorithms.Tick2Star;

import uk.ac.cam.rkh23.Algorithms.Tick2.LCSFinder;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by oliver on 11/02/17.
 */
public class LCSTopDownNonRecursive extends LCSFinder {
    private Stack<Problem> problemStack = new Stack<>();

    public LCSTopDownNonRecursive(String s1, String s2) {
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

        problemStack.add(new Problem(widthi-1, heightj-1));

        while (problemStack.size() != 0) {
            Problem currentProblem = problemStack.peek();
            solveCurrentProblem(currentProblem);
        }

        if (widthi <= 0 || heightj <= 0) {
            return 0;
        } else {
            return mTable[widthi-1][heightj-1];
        }
    }

    private void solveCurrentProblem(Problem currentProblem) {
        int i = currentProblem.i;
        int j = currentProblem.j;
        if (i < 0 || j < 0) {
            problemStack.pop();
        } else if (mTable[i][j] < 0) {
            if (mString1.charAt(i) == mString2.charAt(j)) {
                if (pushOrTrue(i-1, j-1)) {
                    int diagdown = getOrDefault(i-1, j-1);
                    mTable[i][j] = 1 + diagdown;
                    problemStack.pop();
                }
            } else {
                if (pushOrTrue(i, j-1) && pushOrTrue(i-1, j)) {
                    int down = getOrDefault(i, j - 1);
                    int left = getOrDefault(i - 1, j);
                    mTable[i][j] = Math.max(down, left);
                    problemStack.pop();
                }
            }
        }
    }

    private boolean pushOrTrue(int i, int j) {
        if (i < 0 || j < 0 || i >= mString1.length() || j >= mString2.length()) {
            return true;
        } else if (mTable[i][j] == -1) {
            problemStack.push(new Problem(i,j));
            return false;
        } else {
            return true;
        }
    }

    private int getOrDefault(int i, int j) {
        if (i < 0 || j < 0 || i >= mString1.length() || j >= mString2.length()) {
            return 0;
        } else {
            return mTable[i][j];
        }
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
        LCSFinder lcsFinder = (LCSFinder) new LCSTopDownNonRecursive("aba", "baa");
        System.out.println(lcsFinder.getLCSLength());
        System.out.println(Arrays.deepToString(lcsFinder.getLCSLengthTable()));
        System.out.println(lcsFinder.getLCSString());
    }

    private class Problem {
        public int i;
        public int j;

        public Problem(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Problem(Problem problem) {
            this.i = problem.i;
            this.i = problem.i;
        }
    }
}
