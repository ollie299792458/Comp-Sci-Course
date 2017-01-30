package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise4;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 30/01/17.
 */
public class Exercise4 implements IExercise4 {
    @Override
    public Map<Path, Sentiment> magnitudeClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        Map<String, Sentiment> lexiconSentiment = new HashMap<>();
        Set<String> lexiconStrong = new HashSet<>();

        BufferedReader reader = new BufferedReader(new FileReader(lexiconFile.toFile()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] splitSpace = line.split(" ");
            String name = null;
            Sentiment sentiment = null;
            boolean strong = false;
            boolean weak = false;
            for (String equation : splitSpace) {
                String[] words = equation.split("=");
                if (equation.startsWith("word1=")) {
                    name = words[1];
                } else if (equation.startsWith("priorpolarity=")) {
                    if (words[1].equals("positive")){
                        sentiment = Sentiment.POSITIVE;
                    } else if (words[1].equals("negative")){
                        sentiment = Sentiment.NEGATIVE;
                    }
                } else if (equation.startsWith("type=")) {
                    if (words[1].equals("strongsubj")) {
                        strong = true;
                    } else if (words[1].equals("weaksubj")) {
                        weak = true;
                    }
                }
            }
            if (sentiment != null) {
                lexiconSentiment.put(name, sentiment);
                if (strong) {
                    lexiconStrong.add(name);
                } else if (weak) {
                    if (lexiconStrong.contains(name)) {
                        lexiconStrong.remove(name);
                    }
                }
            }
        }

        Map<Path, Sentiment> calculatedSentiments = new HashMap<>(testSet.size());

        for (Path path:testSet) {
            List<String> tokens = Tokenizer.tokenize(path);
            int posScore = 0;
            int negScore = 0;
            for (String token : tokens) {
                if (lexiconSentiment.containsKey(token)) {
                    Sentiment sentiment = lexiconSentiment.get(token);
                    switch (sentiment) {
                        case POSITIVE:
                            if (lexiconStrong.contains(token)) {
                                posScore++;
                            }
                            posScore++;
                            break;
                        case NEGATIVE:
                            if (lexiconStrong.contains(token)) {
                                negScore++;
                            }
                            negScore++;
                            break;
                    }
                }
            }
            Sentiment sentiment = Sentiment.NEGATIVE;
            if (posScore - negScore >= 0) {
                sentiment = Sentiment.POSITIVE;
            }
            calculatedSentiments.put(path, sentiment);
        }

        return calculatedSentiments;
    }

    @Override
    public double signTest(Map<Path, Sentiment> actualSentiments, Map<Path, Sentiment> classificationA, Map<Path, Sentiment> classificationB) {
        int pluss = 0;
        int minuss = 0;
        int nulls = 0;
        for (Map.Entry<Path, Sentiment> actualSentiment: actualSentiments.entrySet()) {
            if (classificationA.get(actualSentiment.getKey())==classificationB.get(actualSentiment.getKey())) {
                nulls++;
            } else if (classificationA.get(actualSentiment.getKey())==actualSentiment.getValue()) {
                pluss++;
            } else {
                minuss++;
            }
        }
        int n = pluss+minuss+nulls;
        int k = Math.min(pluss, minuss) + nulls/2;
        double q = 0.5;
        double p = 0;
        for (int i = 0; i<=k; i++) {
            p += (binomial(n,i).multiply(BigDecimal.valueOf(2*Math.pow(q,i)*Math.pow(1-q,n-i)))).doubleValue();
        }
        return p;
    }

    private static BigDecimal binomial(final int N, final int K) {
        BigDecimal ret = BigDecimal.ONE;
        for (int k = 0; k < K; k++) {
            ret = ret.multiply(BigDecimal.valueOf(N-k))
                    .divide(BigDecimal.valueOf(k+1));
        }
        return ret;
    }
}
