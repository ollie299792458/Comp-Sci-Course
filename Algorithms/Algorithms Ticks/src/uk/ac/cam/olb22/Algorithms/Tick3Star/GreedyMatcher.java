package uk.ac.cam.olb22.Algorithms.Tick3Star;

import uk.ac.cam.rkh23.Algorithms.Tick3Star.Matcher;

/**
 * Created by oliver on 07/03/17.
 */
public class GreedyMatcher implements Matcher {
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
        Matcher maximumMatcher = new MaximumMatcher();
        return maximumMatcher.getMatching(weights);
    }
}
