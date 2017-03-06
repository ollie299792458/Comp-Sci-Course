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

        private List<Integer> getNeighbours(Integer v) {
            List<Integer> neighbours = new LinkedList<>();
            for (int j = 0; j < getNumVertices(); j++) {
                if (j != v && mAdj[v][j] > 0) {
                    neighbours.add(j);
                }
            }
            return neighbours;
        }

        @Override
        public MaxFlowNetwork getMaxFlow(int s, int t) {
            try {
                int maxFlow = 0;
                Graph f = new Graph(getEmptyMatrix());

                while (true) {
                    Set<Integer> S = new HashSet<>();
                    S.add(s);
                    Graph gS = addAllVerticesInWhileLoop(S, f);
                    if (!S.contains(t)) break;
                    List<Integer> p = gS.getFewestEdgesPath(s, t);
                    int delta = Integer.MAX_VALUE;
                    for (Map.Entry<Integer, Integer> e : getEdges(p)) {
                        int vi = e.getKey();
                        int vi1 = e.getValue();
                        if (mAdj[vi][vi1] > 0) {
                            delta = Math.min(mAdj[vi][vi1] - f.mAdj[vi][vi1], delta);
                        } else {
                            delta = Math.min(mAdj[vi1][vi], delta);
                        }
                    }
                    assert delta > 0;
                    for (Map.Entry<Integer, Integer> e : getEdges(p)) {
                        int vi = e.getKey();
                        int vi1 = e.getValue();
                        if (mAdj[vi][vi1] > 0) {
                            f.mAdj[vi][vi1] = f.mAdj[vi][vi1] + delta;
                        } else {
                            f.mAdj[vi][vi1] = f.mAdj[vi][vi1] - delta;
                        }
                    }
                }

                maxFlow = calculateMaxFlow(f, s);
                MaxFlowNetwork result = new MaxFlowNetwork(maxFlow, f);
                return result;
            } catch (TargetUnreachable e) {
                return new MaxFlowNetwork(0, new Graph(new int[0][0]));
            }
        }

        private List<Map.Entry<Integer, Integer>> getEdges(List<Integer> p) {
            List<Map.Entry<Integer,Integer>> result = new LinkedList<>();
            for (int i = 0; i < p.size()-1;i++) {
                result.add(new AbstractMap.SimpleEntry<Integer, Integer>(p.get(i), p.get(i+1)));
            }
            return result;
        }

        private int calculateMaxFlow(Graph f, int s) {
            int result = 0;
            for (Integer n : f.getNeighbours(s)) {
                result += f.mAdj[s][n];
            }
            return result;
        }

        private Graph addAllVerticesInWhileLoop(Set<Integer> S, Graph f) {
            Graph result = new Graph(getEmptyMatrix());
            boolean thereare = true;
            while (thereare) {
                thereare = false;
                Set<Integer> newS = new HashSet<>();
                for (Integer v : S) {
                    for (Integer u : getNeighbours(v)) {
                        if (!S.contains(u)) {
                            if ((f.mAdj[v][u] < mAdj[v][u])) {
                                newS.add(u);
                                result.mAdj[v][u] = mAdj[v][u];
                            } else if (f.mAdj[u][v] > 0) {
                                newS.add(u);
                                result.mAdj[u][v] = mAdj[u][v];
                            }
                        }
                    }
                }
                S.addAll(newS);
                if (!newS.isEmpty()) {
                    thereare=true;
                }
            }
            return result;
        }

        private int[][] getEmptyMatrix() {
            int[][] result = new int[getNumVertices()][getNumVertices()];
            for(int i = 0; i < getNumVertices(); i++) {
                for (int j = 0; j < getNumVertices(); j++) {
                    try {
                        if (getWeight(i,j) > 0) {
                            result[i][j] = 0;
                        } else {
                            result[i][j] = -1;
                        }
                    } catch (InvalidEdgeException e) {
                        throw new RuntimeException("Error in graphbase implementation");
                    }
                }
            }
            return result;
        }

        public static void main(String[] args) throws IOException, TargetUnreachable {
            Graph graph1 = new Graph("in/maxflow_test.01.in");
            System.out.println("Graph 1:");
            System.out.println(Arrays.deepToString(graph1.mAdj));
            Graph graph2 = new Graph("in/maxflow_test.02.in");
            System.out.println("Graph 2:");
            System.out.println(Arrays.deepToString(graph2.mAdj));
            Graph graph3 = new Graph("in/maxflow_test.03.in");
            System.out.println("Graph 3:");
            System.out.println(Arrays.deepToString(graph3.mAdj));
            System.out.println();

            int tgt1 = 3, src1 = 0;
            System.out.println("Fewest edges path from " + src1 + " to " + tgt1 + " in graph 1: "+graph1.getFewestEdgesPath(src1,tgt1));
            int tgt2 = 2, src2 = 0;
            System.out.println("Fewest edges path from " + src2 + " to " + tgt2 + " in graph 2: "+graph2.getFewestEdgesPath(src2,tgt2));
            int tgt3 = 5, src3 = 0;
            System.out.println("Fewest edges path from " + src3 + " to " + tgt3 + " in graph 3: "+graph3.getFewestEdgesPath(src3,tgt3));
            System.out.println();

            MaxFlowNetwork maxFlowGraph1 = graph1.getMaxFlow(src1,tgt1);
            System.out.println("Maxflow from " + src1 + " to " + tgt1 + " in graph 1: "+maxFlowGraph1.getFlow());
            MaxFlowNetwork maxFlowGraph2 = graph2.getMaxFlow(src2,tgt2);
            System.out.println("Maxflow from " + src2 + " to " + tgt2 + " in graph 2: "+maxFlowGraph2.getFlow());
            MaxFlowNetwork maxFlowGraph3 = graph3.getMaxFlow(src3,tgt3);
            System.out.println("Maxflow from " + src3 + " to " + tgt3 + " in graph 3: "+maxFlowGraph3.getFlow());
            System.out.println();
        }
    }
