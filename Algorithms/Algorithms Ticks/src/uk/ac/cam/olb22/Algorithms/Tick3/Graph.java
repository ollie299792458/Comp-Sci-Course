package uk.ac.cam.olb22.Algorithms.Tick3;

import uk.ac.cam.rkh23.Algorithms.Tick3.GraphBase;
import uk.ac.cam.rkh23.Algorithms.Tick3.InvalidEdgeException;
import uk.ac.cam.rkh23.Algorithms.Tick3.MaxFlowNetwork;
import uk.ac.cam.rkh23.Algorithms.Tick3.TargetUnreachable;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by oliver on 04/03/17.
 */
public class Graph extends GraphBase {
    /**
     * Construct graph from a file available at a URL
     *
     * @param url URL for file
     * @throws IOException
     */
    public Graph(URL url) throws IOException {
        super(url);
    }

    /**
     * Construct graph from file
     *
     * @param file Filename
     */
    public Graph(String file) throws IOException {
        super(file);
    }

    /**
     * Construct graph from an adjacency matric
     *
     * @param adj
     */
    public Graph(int[][] adj) {
        super(adj);
    }

    @Override
    public List<Integer> getFewestEdgesPath(int src, int target) throws TargetUnreachable {
        Map<Integer, Integer> predecesor = new HashMap<>(getNumVertices());
        Queue<Integer> toExplore = new LinkedList();

        toExplore.add(src);
        predecesor.put(src, -1);

        boolean targetFound = false;
        while (!toExplore.isEmpty()&&!targetFound) {
            Integer v = toExplore.remove();
            for (Integer w : getNeighbours(v)) {
                if (w.equals(target)) {
                    predecesor.put(w, v);
                    targetFound = true;
                    break;
                } else if (predecesor.getOrDefault(w, -2).equals(-2)) {
                    toExplore.add(w);
                    predecesor.put(w, v);
                }
            }
        }

        if (!targetFound) {
            throw new TargetUnreachable();
        }

        List<Integer> result = new LinkedList<>();
        result.add(target);
        Integer c = target;
        while (!predecesor.getOrDefault(c, -1).equals(-1)) {
            result.add(predecesor.get(c));
            c = predecesor.get(c);
        }

        Collections.reverse(result);

        return result;
    }

    private List<Integer> getNeighbours(Integer v) throws TargetUnreachable {
        List<Integer> neighbours = new LinkedList<>();
        for (int j = 0; j < getNumVertices(); j++) {
            try {
                if (j != v && getWeight(v, j) > 0) {
                    neighbours.add(j);
                }
            } catch (InvalidEdgeException e) {
                throw new RuntimeException("Error in graphbase implementation");
            }
        }
        return neighbours;
    }

    @Override
    public MaxFlowNetwork getMaxFlow(int s, int t) {
        return null;
    }

    public static void main(String[] args) throws IOException, TargetUnreachable {
        Graph graph1 = new Graph("in/maxflow_test.01.in");
        Graph graph2 = new Graph("in/maxflow_test.02.in");
        Graph graph3 = new Graph("in/maxflow_test.03.in");

        int tgt = 2, src = 0;
        System.out.println("Fewest edges path from " + src + " to " + tgt + ": "+graph1.getFewestEdgesPath(src,tgt));
    }
}
