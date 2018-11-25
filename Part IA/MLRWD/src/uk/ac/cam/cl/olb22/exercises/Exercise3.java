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
    static final String outputFilePath = "data/topic1/task3/";

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
        ex.outputSortedIntegerTokens(sortedTokens, outputFilePath + "sorted_tokens.txt");
        System.out.println("Outputted Sorted Tokens");

        List<BestFit.Point> points = ex.getFirstNPoints(sortedTokens, 10000);
        System.out.println("Got Points");
        ChartPlotter.plotLines(points);
        System.out.println("Plotted Points");

        Set<String> wordsTask1 = new HashSet<>(
                Arrays.asList(
                        new String[] {"engaging", "exceptional", "marvelous", "fantastic", "hilarious", "awful", "lackluster", "dreadful", "atrocious", "garbage"}));
        List<Map.Entry<String, Integer>> task1SortedWords = ex.getTokensMatchingInteger(sortedTokens, wordsTask1);
        System.out.println("Got Task 1 Frequencies");
        ex.outputSortedIntegerTokens(task1SortedWords, outputFilePath+ "sorted_tokens_task1.txt");
        System.out.println("Outputted Task 1 Frequencies");
        List<BestFit.Point> task1Points = ex.getFirstNPoints(task1SortedWords, task1SortedWords.size());
        ChartPlotter.plotLines(task1Points);
        System.out.println("Plotted Task 1 Frequencies");

        List<BestFit.Point> loglogPoints = ex.getLogLogPoints(points);
        System.out.println("Got log log points");
        Map<BestFit.Point, Double> loglogPointsLineOfBestFitMap = ex.getBestFitMap(loglogPoints);
        BestFit.Line loglogPointsLineOfBestFit = BestFit.leastSquares(loglogPointsLineOfBestFitMap);
        List<BestFit.Point> loglogPointsLineOfBestFitPoints = ex.getPointsFromLineOfBestFine(loglogPointsLineOfBestFit, loglogPoints);
        System.out.println("Got log log line of best fit");
        ChartPlotter.plotLines(loglogPoints, loglogPointsLineOfBestFitPoints);
        System.out.println("Plotted log log points with line best fit");

        List<BestFit.Point> expectedFrequencies = ex.getAllExpectedFrequencies(loglogPoints, loglogPointsLineOfBestFit);
        List<Map.Entry<String, Double>> task1ExpectedFrequenciesByWord = ex.getTokensMatchingDouble(ex.groupPointsByWord(sortedTokens, points), wordsTask1);
        System.out.println("Got Expected Frequencies");
        ex.outputSortedDoubleTokens(task1ExpectedFrequenciesByWord, outputFilePath+ "expected_freq_tokens_task1.txt");
        System.out.println("Outputted Expected Frequencies");

        System.out.println("Estimated k: "+ Math.exp(loglogPointsLineOfBestFit.yIntercept)+", and alpha: "+ (-loglogPointsLineOfBestFit.gradient));

        List<BestFit.Point> heapsLawPoints = ex.getHeapsLawPoints(tokens);
        List<BestFit.Point> loglogHeapsLawPoints = ex.getLogLogPoints(heapsLawPoints);
        System.out.println("Got heaps law calculations");
        ChartPlotter.plotLines(loglogHeapsLawPoints);
        System.out.println("Plotted heaps law");

        System.out.println("Done");
    }

    private List<BestFit.Point> getHeapsLawPoints(List<String> tokens) {
        List<BestFit.Point> result = new LinkedList<>();
        Set<String> uniqueWords = new HashSet<>();
        int pow2 = 0;
        long nextcheck = (int) Math.pow(2.0, pow2);
        int wordCount = 0;
        for (String token : tokens) {
            wordCount ++;
            uniqueWords.add(token);
            if (wordCount == nextcheck) {
                result.add(new BestFit.Point(Math.pow(2,pow2),uniqueWords.size()));
                pow2++;
                nextcheck = (long) Math.pow(2.0, pow2);
            }
        }
        return result;
    }

    private List<Map.Entry<String,Double>> groupPointsByWord(List<Map.Entry<String, Integer>> tokens, List<BestFit.Point> points) {
        List<Map.Entry<String, Double>> result = new LinkedList<>();
        int max = tokens.size();
        if (points.size()<tokens.size()) {
            max = points.size();
        }
        for (int i = 0; i < max; i++) {
            result.add(new AbstractMap.SimpleEntry<String, Double>(tokens.get(i).getKey(), points.get(i).y));
        }
        return result;
    }

    private List<BestFit.Point> getAllExpectedFrequencies(List<BestFit.Point> points, BestFit.Line line) {
        List<BestFit.Point> result = new ArrayList<>(points.size());
        for (BestFit.Point point : points) {
            result.add(new BestFit.Point(point.x, getExpectedY(point.x, line)));
        }
        return result;
    }


    private double getExpectedFrequency(int rankToGuess, BestFit.Line loglogPointsLineOfBestFit) {
        return Math.exp(getExpectedY(Math.log(rankToGuess), loglogPointsLineOfBestFit));
    }

    private double getExpectedY(double x, BestFit.Line line) {
        return line.gradient*x+line.yIntercept;
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

    private List<Map.Entry<String, Double>> getTokensMatchingDouble(List<Map.Entry<String, Double>> sortedTokens, Set<String> words) {
        List<Map.Entry<String, Double>> result = new ArrayList<>(words.size());
        for (Map.Entry<String , Double> entry : sortedTokens) {
            if (words.contains(entry.getKey())) {
                result.add(entry);
            }
        }
        return result;
    }

    private List<Map.Entry<String, Integer>> getTokensMatchingInteger(List<Map.Entry<String, Integer>> sortedTokens, Set<String> words) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>(words.size());
        for (Map.Entry<String , Integer> entry : sortedTokens) {
            if (words.contains(entry.getKey())) {
                result.add(entry);
            }
        }
        return result;
    }

    private void outputSortedIntegerTokens(List<Map.Entry<String, Integer>> sortedTokens, String fileName) throws IOException {
        List<String> lines = new ArrayList<>(sortedTokens.size());
        for (Map.Entry entry : sortedTokens) {
            lines.add(entry.getKey() + " " + String.valueOf(entry.getValue()));
        }
        Files.write(Paths.get(fileName), lines);
    }

    private void outputSortedDoubleTokens(List<Map.Entry<String, Double>> sortedTokens, String fileName) throws IOException {
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
