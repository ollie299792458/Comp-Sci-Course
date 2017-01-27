package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;
import uk.ac.cam.cl.mlrwd.utils.BestFit;
import uk.ac.cam.cl.mlrwd.utils.ChartPlotter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by oliver on 27/01/17.
 */
public class Exercise3 {

    static final Path dataDirectory = Paths.get("data/topic1/large_dataset");

    public static void main(String[] args) throws IOException {
        Exercise3 ex = new Exercise3();
        DirectoryStream<Path> files = Files.newDirectoryStream(dataDirectory);
        System.out.println("Got Files");
        List<String> tokens = ex.getAllTokens(files);
        System.out.println("Got Tokens");
        Map<String, Integer> countTokens = ex.countTokens(tokens);
        System.out.println("Counted Tokens");
        List<Map.Entry<String, Integer>> sortedTokens = ex.sortTokens(countTokens);
        System.out.println("Sorted Tokens");
        ex.outputSortedTokens(sortedTokens, "data/topic1/task3/sorted_tokens.txt");
        System.out.println("Outputted Sorted Tokens");
        List<BestFit.Point> points = ex.getFirstNPoints(sortedTokens, 10000);
        System.out.println("Got Points");
        ChartPlotter.plotLines(points);
        System.out.println("Plotted Points");
        Set<String> words = new HashSet<>(
                Arrays.asList(
                        new String[] {"engaging", "exceptional", "marvelous", "fantastic", "hilarious", "awful", "lackluster", "dreadful", "atrocious", "garbage"}));
        List<Map.Entry<String, Integer>> task1SortedWords = ex.getTokensMatching(sortedTokens, words);
        System.out.println("Got Task 1 Frequencies");
        ex.outputSortedTokens(task1SortedWords, "data/topic1/task3/sorted_tokens_task1.txt");
        System.out.println("Outputted Task 1 Frequencies");
        List<BestFit.Point> task1Points = ex.getFirstNPoints(task1SortedWords, task1SortedWords.size());
        ChartPlotter.plotLines(task1Points);
        System.out.println("Plotted Task 1 Frequencies");
        List<BestFit.Point> loglogPoints = ex.getLogLogPoints(points);
        System.out.println("Got log log points");
        Map<BestFit.Point, Double> loglogLineOfBestFitMap = ex.getBestFitMap(loglogPoints);
        BestFit.Line loglogPointsLineOfBestFit = BestFit.leastSquares(loglogLineOfBestFitMap);
        List<BestFit.Point> loglogPointsLineOfBestFitPoints = ex.getPointsFromLineOfBestFine(loglogPointsLineOfBestFit, loglogPoints);
        System.out.println("Got log log line of best fit");
        ChartPlotter.plotLines(loglogPoints, loglogPointsLineOfBestFitPoints);
        System.out.println("Plotted log log points with line best fit");
        System.out.println("Done");
    }

    private List<BestFit.Point> getPointsFromLineOfBestFine(BestFit.Line lineOfBestFit, List<BestFit.Point> points) {
        List<BestFit.Point> result = new ArrayList<>(2);
        result.add(new BestFit.Point(points.get(0).x,
                lineOfBestFit.gradient*points.get(0).x+lineOfBestFit.yIntercept));
        result.add(new BestFit.Point(points.get(points.size()-1).x,
                lineOfBestFit.gradient*points.get(points.size()-1).x+lineOfBestFit.yIntercept));
        return result;
    }

    private Map<BestFit.Point,Double> getBestFitMap(List<BestFit.Point> loglogPoints) {
        Map<BestFit.Point, Double> result = new HashMap<>(loglogPoints.size());
        for (BestFit.Point point : loglogPoints) {
            result.put(point, Math.exp(point.y));
        }
        return result;
    }

    private List<BestFit.Point> getLogLogPoints(List<BestFit.Point> points) {
        List<BestFit.Point> result = new ArrayList<>(points.size());
        for (BestFit.Point point : points) {
            result.add(new BestFit.Point(Math.log(point.x), Math.log(point.y)));
        }
        return result;
    }

    private List<Map.Entry<String,Integer>> getTokensMatching(List<Map.Entry<String, Integer>> sortedTokens, Set<String> words) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>(words.size());
        for (Map.Entry<String , Integer> entry : sortedTokens) {
            if (words.contains(entry.getKey())) {
                result.add(entry);
            }
        }
        return result;
    }

    private void outputSortedTokens(List<Map.Entry<String, Integer>> sortedTokens, String fileName) throws IOException {
        List<String> lines = new ArrayList<>(sortedTokens.size());
        for (Map.Entry entry : sortedTokens) {
            lines.add(entry.getKey() + " " + String.valueOf(entry.getValue()));
        }
        Files.write(Paths.get(fileName), lines);
    }

    private List<BestFit.Point> getFirstNPoints(List<Map.Entry<String, Integer>> sortedTokens, int n) {
        List<BestFit.Point> result = new ArrayList<>(sortedTokens.size());
        for (int i = 1; (i < n)&&(i<sortedTokens.size()); i++) {
            result.add(new BestFit.Point(i, sortedTokens.get(i).getValue()));
        }
        return result;
    }

    private List<Map.Entry<String,Integer>> sortTokens(Map<String, Integer> tokens) {
        ArrayList<Map.Entry<String, Integer>> result = new ArrayList<>(tokens.size());
        result.addAll(tokens.entrySet());
        result.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        return result;
    }

    private Map<String,Integer> countTokens(List<String> tokens) {
        Map<String, Integer> result = new HashMap<>();
        for (String token : tokens) {
            result.put(token, result.getOrDefault(token, 0) + 1);
        }
        return result;
    }

    private List<String> getAllTokens(DirectoryStream<Path> directoryStream) throws IOException {
        List<String> result = new LinkedList<>();
        for (Path path : directoryStream) {
            result.addAll(Tokenizer.tokenize(path));
        }
        return result;
    }
}
