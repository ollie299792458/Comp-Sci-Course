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
        Map<String, WeightedSentiment> lexiconSentiment = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(lexiconFile.toFile()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] splitSpace = line.split(" ");
            String name = "";
            String polarity = "";
            String weight = "";
            for (String equation : splitSpace) {
                String[] words = equation.split("=");
                if (equation.startsWith("word1=")) {
                    name = words[1];
                } else if (equation.startsWith("priorpolarity=")) {
                    polarity = words[1];
                } else if (equation.startsWith("type=")) {
                    weight = words[1];
                }
            }
            WeightedSentiment sentiment = null;
            if (polarity.equals("positive") && weight.equals("strongsubj")) {
                sentiment = WeightedSentiment.HPOS;
            } else if (polarity.equals("positive") && weight.equals("weaksubj")) {
                sentiment = WeightedSentiment.POS;
            } else if (polarity.equals("negative") && weight.equals("weaksubj")) {
                sentiment = WeightedSentiment.NEG;
            } else if (polarity.equals("negative") && weight.equals("strongsubj")) {
                sentiment = WeightedSentiment.HNEG;
            }

            if (sentiment != null) {
                lexiconSentiment.put(name, sentiment);
            }
        }
        reader.close();

        Map<Path, Sentiment> calculatedSentiments = new HashMap<>(testSet.size());

        for (Path path:testSet) {
            List<String> tokens = Tokenizer.tokenize(path);
            int posScore = 0;
            int negScore = 0;
            for (String token : tokens) {
                if (lexiconSentiment.containsKey(token)) {
                    WeightedSentiment sentiment = lexiconSentiment.get(token);
                    switch (sentiment) {
                        case POS:
                            posScore++;
                            break;
                        case HPOS:
                            posScore+=2;
                            break;
                        case NEG:
                            negScore++;
                            break;
                        case HNEG:
                            negScore+=2;
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

    private enum WeightedSentiment {
        POS, NEG, HPOS, HNEG;
    }
}
