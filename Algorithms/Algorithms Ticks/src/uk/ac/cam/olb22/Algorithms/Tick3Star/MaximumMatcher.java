package uk.ac.cam.olb22.Algorithms.Tick3Star;

import uk.ac.cam.rkh23.Algorithms.Tick3.GraphBase;
import uk.ac.cam.rkh23.Algorithms.Tick3.InvalidEdgeException;
import uk.ac.cam.rkh23.Algorithms.Tick3.MaxFlowNetwork;
import uk.ac.cam.rkh23.Algorithms.Tick3Star.Matcher;

import java.util.Arrays;

/**
 * Created by oliver on 07/03/17.
 */
public class MaximumMatcher implements Matcher {
    /**
     * Compute a matching, given weights in a bipartite graph.
     *
     * @param weights matrix of edge weights: weights[i][j] is the weight associated
     *                with the edge from left vertex i to right vertex j,
     *                and weight 0 means there is no edge. Note that this
     *                matrix might not be square.  Observe weights.length is the number
     *                of left-vertices, and weights[0].length the number of right-vertices.
     */
    @Override
    public int[] getMatching(int[][] weights) {
        int[] result;
        try {
            result = new int[weights[0].length];
        } catch (ArrayIndexOutOfBoundsException a) {
            return new int[0];
        }

        int mN = weights.length+weights[0].length+2;
        int[][] mAdj = new int[mN][mN];

        for (int i = 0; i<weights.length; i++) {
            mAdj[0][i+1] = 1;
            for (int j = 0; j<weights[0].length; j++) {
                if (weights[i][j] != 0) {
                    mAdj[i+1][j+weights.length+1] = 1;
                }
            }
        }

        for (int j = 0; j<weights[0].length; j++) {
            mAdj[j+weights.length+1][mN-1] = 1;
        }

        GraphBase graph = new Graph(mAdj);
        MaxFlowNetwork maxFlow = graph.getMaxFlow(0, mN - 1);
        GraphBase network = maxFlow.getNetwork();

        for (int i = 0; i< result.length; i++) {
            result[i] = -1;
        }

        for (int i = 0; i<weights.length;i++) {
            for (int j = 0; j<weights.length;j++) {
                try {
                    if (network.getWeight(i+1, j+weights.length+1) > 0) {
                        result[i] = j;
                    }
                } catch (InvalidEdgeException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        MaximumMatcher matcher = new MaximumMatcher();
        int[][] weights = new int[][]{{10,4,5},{6,8,3},{5,4,0}};
        int[] matching = matcher.getMatching(weights);
        System.out.println("Found matching: "+ Arrays.toString(matching) +", for weights: "+Arrays.deepToString(weights));
    }
}
