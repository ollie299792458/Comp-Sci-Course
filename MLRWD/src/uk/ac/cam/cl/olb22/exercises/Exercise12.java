package uk.ac.cam.cl.olb22.exercises;

import com.sun.javafx.geom.Edge;
import uk.ac.cam.cl.mlrwd.exercises.social_networks.IExercise12;

import java.util.*;

/**
 * Created by oliver on 06/03/17.
 */
public class Exercise12 implements IExercise12 {
    private final Double fuzzyComparison = 1e-06;

    /**
     * Compute graph clustering using the Girvan-Newman method. Stop algorithm when the
     * minimum number of components has been reached (your answer may be higher than
     * the minimum).
     *
     * @param graph             {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *                          loaded graph
     * @param minimumComponents {@link int} The minimum number of components to reach.
     * @return {@link List}<{@link Set}<{@link Integer}>>
     * List of components for the graph.
     */
    @Override
    public List<Set<Integer>> GirvanNewman(Map<Integer, Set<Integer>> graph, int minimumComponents) {
        List<Set<Integer>> result = getComponents(graph);
        Map<Integer, Map<Integer, Double>> betweennesss = getEdgeBetweenness(graph);
        while (result.size() < minimumComponents) {
            Set<Edge> maxEdges = getMaxEdges(betweennesss);
            for (Edge maxEdge : maxEdges) {
                removeEdge(graph, betweennesss, maxEdge.from, maxEdge.to);
                //removeEdge(graph, betweennesss, maxEdge.to, maxEdge.from);
            }
            result = getComponents(graph);
            betweennesss = getEdgeBetweenness(graph);
        }
        return result;
    }

    private void removeEdge(Map<Integer, Set<Integer>> graph, Map<Integer, Map<Integer, Double>> betweeness, int from, int to) {
        Set<Integer> fromChildren = graph.get(from);
        Map<Integer, Double> betweenessChildren = betweeness.get(from);
        if (fromChildren != null) {
            fromChildren.remove(to);
            graph.put(from, fromChildren);
        }
        if (betweenessChildren != null) {
            betweenessChildren.remove(to);
            betweeness.put(from, betweenessChildren);
        }
    }

    private Set<Edge> getMaxEdges(Map<Integer, Map<Integer, Double>> betweennesss) {
        Set<Edge> maxEdges = new HashSet<>();
        Edge maxEdge = new Edge();
        maxEdges.add(maxEdge);
        for (Map.Entry<Integer, Map<Integer, Double>> fromNodeEntry : betweennesss.entrySet()) {
            int from = fromNodeEntry.getKey();
            for (Map.Entry<Integer, Double> toNodeEntry : fromNodeEntry.getValue().entrySet()) {
                int to = toNodeEntry.getKey();
                if (maxEdge.weightDouble < toNodeEntry.getValue()-fuzzyComparison) {
                    maxEdges.clear();
                    maxEdge.to = to;
                    maxEdge.from = from;
                    maxEdge.weightDouble = toNodeEntry.getValue();
                    maxEdges.add(maxEdge);
                } else if (maxEdge.weightDouble < toNodeEntry.getValue()+fuzzyComparison) {
                    Edge newEdge = new Edge();
                    newEdge.to = to;
                    newEdge.from = from;
                    newEdge.weightDouble = toNodeEntry.getValue();
                    maxEdges.add(newEdge);
                }
            }
        }
        return maxEdges;
    }

    /**
     * Find the number of edges in the graph.
     *
     * @param graph {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *              loaded graph
     * @return {@link Integer}> Number of edges.
     */
    @Override
    public int getNumberOfEdges(Map<Integer, Set<Integer>> graph) {
        int count = 0;
        for (Map.Entry<Integer, Set<Integer>> entry : graph.entrySet())
            for (Integer edge : entry.getValue()) {
                count++;
            }
        return count/2;
    }

    /**
     * Find the number of components in the graph using a DFS.
     *
     * @param graph {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *              loaded graph
     * @return {@link List}<{@link Set}<{@link Integer}>>
     * List of components for the graph.
     */
    @Override
    public List<Set<Integer>> getComponents(Map<Integer, Set<Integer>> graph) {
        List<Set<Integer>> result = new LinkedList<>();
        Set<Integer> nodesInComponents = new HashSet<>();
        int totalsize = 0;
        for (Map.Entry<Integer, Set<Integer>> nodeEntry : graph.entrySet()) {
            int node = nodeEntry.getKey();
            if (!nodesInComponents.contains(node)) {
                Set<Integer> connectedComponent = getComponent(node, graph, new HashSet<>());
                result.add(connectedComponent);
                nodesInComponents.addAll(connectedComponent);
                totalsize += connectedComponent.size();
            }
        }
        return result;
    }

    private Set<Integer> getComponent(int node, Map<Integer, Set<Integer>> graph, Set<Integer> seen) {
        Set<Integer> result = new HashSet<>();
        seen.add(node);
        for (Integer child : graph.get(node)) {
            if (!seen.contains(child)) {
                seen.addAll(getComponent(child, graph, seen));
            }
        }
        return seen;
    }

    /**
     * Calculate the edge betweenness.
     *
     * @param graph {@link Map}<{@link Integer}, {@link Set}<{@link Integer}>> The
     *              loaded graph
     * @return {@link Map}<{@link Integer},
     * {@link Map}<{@link Integer},{@link Double}>> Edge betweenness for
     * each pair of vertices in the graph
     */
    @Override
    public Map<Integer, Map<Integer, Double>> getEdgeBetweenness(Map<Integer, Set<Integer>> graph) {
        Queue<Integer> Q = new LinkedList<>();
        Stack<Integer> S = new Stack<>();
        Map<Integer, Map<Integer, Double>> cb = new HashMap<>();


        for (Integer s : graph.keySet()) {
            //Single shortest path problem
            //Initialisation
            Map<Integer, Set<Integer>> pred = initHashMapSet(graph.keySet());
            Map<Integer, Integer> dist = initHashMap(graph.keySet(), -1);
            Map<Integer, Integer> sigma = initHashMap(graph.keySet(), 0);

            dist.put(s, 0);
            sigma.put(s, 1);

            Q.add(s);

            while (!Q.isEmpty()) {
                Integer v = Q.remove();
                S.push(v);
                for (Integer w : graph.get(v)) {
                    //Path discovery
                    if (dist.get(w).equals(-1)) {
                        dist.put(w, dist.get(v)+1);
                        Q.add(w);
                    }
                    //Path counting
                    if (dist.get(w).equals(dist.get(v) + 1)) {
                        sigma.put(w, sigma.get(v)+sigma.get(w));
                        Set<Integer> wpred = pred.get(w);
                        wpred.add(v);
                        pred.put(w, wpred);
                    }
                }
            }
            //Accumulation
            Map<Integer, Double> delta = initHashMap(graph.keySet(), 0.0);
            while (!S.isEmpty()) {
                Integer w = S.pop();
                for (Integer v : pred.get(w)) {
                    double c = (((double) sigma.get(v)) / ((double) sigma.get(w))) * (1.0 + delta.get(w));
                    incCb(v, w, c, cb);
                    delta.put(v, delta.get(v) + c);
                }
            }
        }
        /*for (Integer key : cb.keySet()) {
            cb.put(key, cb.get(key)/2.0);
        }*/
        return cb;
    }

    private void incCb(Integer v, Integer w, double c, Map<Integer, Map<Integer, Double>> cb) {
        Map<Integer, Double> innerMap = cb.getOrDefault(v,null);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            cb.put(v, innerMap);
        }
        innerMap.put(w, innerMap.getOrDefault(w, 0.0) + c);
    }


    private Map<Integer,Set<Integer>> initHashMapSet(Set<Integer> keys) {
        Map<Integer, Set<Integer>> result = new HashMap<>(keys.size());
        for (Integer key : keys) {
            result.put(key, new HashSet<>());
        }
        return result;
    }

    private <T> Map<Integer, T> initHashMap(Set<Integer> keys, T value) {
        Map<Integer, T> result = new HashMap<>(keys.size());
        for (Integer key: keys) {
            result.put(key, value);
        }
        return result;
    }

    private class Edge {
        public int from=-1, to=-1, weightInt=0;
        public double weightDouble=0.0;
    }
}
