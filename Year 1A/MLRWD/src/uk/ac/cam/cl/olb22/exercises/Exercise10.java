package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.social_networks.IExercise10;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 27/02/17.
 */
public class Exercise10 implements IExercise10 {
    @Override
    public Map<Integer, Set<Integer>> loadGraph(Path graphFile) throws IOException {
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

    @Override
    public Map<Integer, Integer> getConnectivities(Map<Integer, Set<Integer>> graph) {
        Map<Integer, Integer> result = new HashMap<>(graph.size());
        for (Map.Entry<Integer, Set<Integer>> entry : graph.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }

    @Override
    public int getDiameter(Map<Integer, Set<Integer>> graph) {
        int currentLongest = 0;
        for (Map.Entry<Integer, Set<Integer>> entry : graph.entrySet()) {
            int longestPath = getLongestPath(entry.getKey(), graph);
            currentLongest = Math.max(currentLongest, longestPath);
        }
        return currentLongest;
    }

    private int getLongestPath(Integer start, Map<Integer, Set<Integer>> graph) {
        int longestPath = 0;
        Map<Integer, Integer> seen = new HashMap<>();
        seen.put(start, 0);

        Queue<Integer> toExplore = new LinkedList<>();
        toExplore.add(start);

        while (!toExplore.isEmpty()) {
            int vertex = toExplore.remove();
            if (graph.containsKey(vertex)) {
                for (int neighbour : graph.get(vertex)) {
                    if (!seen.containsKey(neighbour)) {
                        toExplore.add(neighbour);
                        seen.put(neighbour, seen.get(vertex) + 1);
                        longestPath = Math.max(seen.get(neighbour), longestPath);
                    }
                }
            }
        }
        return longestPath;
    }
}
