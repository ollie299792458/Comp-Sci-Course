package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.social_networks.IExercise11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 04/03/17.
 */
public class Exercise11 implements IExercise11 {
    @Override
    public Map<Integer, Double> getNodeBetweenness(Path graphFile) throws IOException {
        Map<Integer, Set<Integer>> graph = loadGraph(graphFile);
        Queue<Integer> Q = new LinkedList<>();
        Stack<Integer> S = new Stack<>();
        Map<Integer, Double> cb = initHashMap(graph.keySet(), 0.0);


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
                    delta.put(v, delta.get(v)+(((double) sigma.get(v)) / ((double) sigma.get(w))) * (1.0 + delta.get(w)));
                }
                if (!w.equals(s)) {
                    cb.put(w, cb.get(w) + delta.get(w));
                }
            }
        }
        for (Integer key : cb.keySet()) {
            cb.put(key, cb.get(key)/2.0);
        }
        return cb;
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

    private Map<Integer, Set<Integer>> loadGraph(Path graphFile) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(graphFile.toFile()))) {
            Map<Integer, Set<Integer>> result = new HashMap<>();
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                String[] numbers = nextLine.split(" ");
                addEdgeUndirected(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]), result);
            }
            return result;
        }
    }

    private void addEdgeUndirected(int vertex1, int vertex2, Map<Integer, Set<Integer>> graph) {
        addEdgeDirected(vertex1, vertex2, graph);
        addEdgeDirected(vertex2, vertex1, graph);
    }

    private void addEdgeDirected(int vertex1, int vertex2, Map<Integer, Set<Integer>> graph) {
        Set<Integer> currentEdges;
        if (graph.containsKey(vertex1)) {
            currentEdges = graph.get(vertex1);
        } else {
            currentEdges = new HashSet<>();
        }
        currentEdges.add(vertex2);
        graph.put(vertex1, currentEdges);
    }
}
