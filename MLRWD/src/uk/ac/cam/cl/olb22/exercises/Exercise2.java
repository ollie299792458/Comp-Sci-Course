package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise2;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oliver on 23/01/17.
 */
public class Exercise2 implements IExercise2 {
    @Override
    public Map<Sentiment, Double> calculateClassProbabilities(Map<Path, Sentiment> trainingSet) throws IOException {
        Map<Sentiment, Integer> totalReviews = new HashMap<>(2);
        totalReviews.put(Sentiment.NEGATIVE, 0);
        totalReviews.put(Sentiment.POSITIVE, 0);

        for (Map.Entry<Path, Sentiment> entry : trainingSet.entrySet()) {
            totalReviews.put(entry.getValue(), totalReviews.get(entry.getValue())+1);
        }

        Map<Sentiment, Double> classProbabilities= new HashMap<>(totalReviews.size());
        classProbabilities.put(Sentiment.NEGATIVE, ((double) totalReviews.get(Sentiment.NEGATIVE))/((double) trainingSet.size()));
        classProbabilities.put(Sentiment.POSITIVE, ((double) totalReviews.get(Sentiment.POSITIVE))/((double) trainingSet.size()));
        return classProbabilities;
    }

    @Override
    public Map<String, Map<Sentiment, Double>> calculateUnsmoothedLogProbs(Map<Path, Sentiment> trainingSet) throws IOException {
        Map<String, Map<Sentiment, Integer>> countingMap = new HashMap<>();

        Map<Sentiment, Integer> totalWords = new HashMap<>(2);
        totalWords.put(Sentiment.POSITIVE, 0);
        totalWords.put(Sentiment.NEGATIVE, 0);

        for (Map.Entry<Path, Sentiment> entry : trainingSet.entrySet()) {

            List<String> tokens = Tokenizer.tokenize(entry.getKey());
            for (String token : tokens) {

                if (!countingMap.containsKey(token)) {
                    Map<Sentiment, Integer> sentimentMap = new HashMap<>(2);
                    sentimentMap.put(Sentiment.POSITIVE, 0);
                    sentimentMap.put(Sentiment.NEGATIVE, 0);
                    countingMap.put(token, sentimentMap);
                }

                totalWords.put(entry.getValue(), totalWords.get(entry.getValue())+1);
                Map<Sentiment, Integer> sentimentMap = countingMap.get(token);
                sentimentMap.put(entry.getValue(), sentimentMap.get(entry.getValue())+1);
                countingMap.put(token, sentimentMap);
            }
        }

        Map<String, Map<Sentiment, Double>> result = new HashMap<>(countingMap.size());

        for (Map.Entry<String, Map<Sentiment, Integer>> entry : countingMap.entrySet()) {
            Map<Sentiment, Double> sentimentDoubleMap = new HashMap<>(entry.getValue().size());
            double positiveProbability = ((double) entry.getValue().get(Sentiment.POSITIVE))/((double) totalWords.get(Sentiment.POSITIVE));
            double negativeProbability = ((double) entry.getValue().get(Sentiment.NEGATIVE))/((double) totalWords.get(Sentiment.NEGATIVE));
            if (positiveProbability > 0) {
                positiveProbability = Math.log(positiveProbability);
            } else {
                positiveProbability = Math.log(positiveProbability);
            }
            if (negativeProbability > 0) {
                negativeProbability = Math.log(negativeProbability);
            } else {
                negativeProbability = Math.log(negativeProbability);
            }
            sentimentDoubleMap.put(Sentiment.POSITIVE, positiveProbability);
            sentimentDoubleMap.put(Sentiment.NEGATIVE, negativeProbability);
            result.put(entry.getKey(), sentimentDoubleMap);
        }

        return result;
    }

    @Override
    public Map<String, Map<Sentiment, Double>> calculateSmoothedLogProbs(Map<Path, Sentiment> trainingSet) throws IOException {
        Map<String, Map<Sentiment, Integer>> countingMap = new HashMap<>();

        Map<Sentiment, Integer> totalWords = new HashMap<>(2);
        totalWords.put(Sentiment.POSITIVE, 0);
        totalWords.put(Sentiment.NEGATIVE, 0);

        for (Map.Entry<Path, Sentiment> entry : trainingSet.entrySet()) {

            List<String> tokens = Tokenizer.tokenize(entry.getKey());
            for (String token : tokens) {

                if (!countingMap.containsKey(token)) {
                    Map<Sentiment, Integer> sentimentMap = new HashMap<>(2);
                    sentimentMap.put(Sentiment.POSITIVE, 1);
                    sentimentMap.put(Sentiment.NEGATIVE, 1);
                    totalWords.put(Sentiment.POSITIVE, totalWords.get(Sentiment.POSITIVE) + 1);
                    totalWords.put(Sentiment.NEGATIVE, totalWords.get(Sentiment.NEGATIVE) + 1);
                    countingMap.put(token, sentimentMap);
                }

                totalWords.put(entry.getValue(), totalWords.get(entry.getValue())+1);
                Map<Sentiment, Integer> sentimentMap = countingMap.get(token);
                sentimentMap.put(entry.getValue(), sentimentMap.get(entry.getValue())+1);
                countingMap.put(token, sentimentMap);
            }
        }

        Map<String, Map<Sentiment, Double>> result = new HashMap<>(countingMap.size());

        for (Map.Entry<String, Map<Sentiment, Integer>> entry : countingMap.entrySet()) {
            Map<Sentiment, Double> sentimentDoubleMap = new HashMap<>(entry.getValue().size());
            double positiveProbability = ((double) entry.getValue().get(Sentiment.POSITIVE))/((double) totalWords.get(Sentiment.POSITIVE));
            double negativeProbability = ((double) entry.getValue().get(Sentiment.NEGATIVE))/((double) totalWords.get(Sentiment.NEGATIVE));
            if (positiveProbability > 0) {
                positiveProbability = Math.log(positiveProbability);
            } else {
                positiveProbability = Math.log(positiveProbability);
            }
            if (negativeProbability > 0) {
                negativeProbability = Math.log(negativeProbability);
            } else {
                negativeProbability = Math.log(negativeProbability);
            }
            sentimentDoubleMap.put(Sentiment.POSITIVE, positiveProbability);
            sentimentDoubleMap.put(Sentiment.NEGATIVE, negativeProbability);
            result.put(entry.getKey(), sentimentDoubleMap);
        }

        return result;
    }

    @Override
    public Map<Path, Sentiment> naiveBayes(Set<Path> testSet, Map<String, Map<Sentiment, Double>> tokenLogProbs, Map<Sentiment, Double> classProbabilities) throws IOException {
        Map<Path, Sentiment> result = new HashMap<>(testSet.size());

        for (Path path : testSet) {
            Sentiment sentiment = Sentiment.NEGATIVE;
            double positive = Math.log(classProbabilities.get(Sentiment.POSITIVE));
            double negative = Math.log(classProbabilities.get(Sentiment.NEGATIVE));

            List<String> tokens = Tokenizer.tokenize(path);
            for (String token : tokens) {
                if (tokenLogProbs.containsKey(token)) {
                    positive = positive + tokenLogProbs.get(token).get(Sentiment.POSITIVE);
                    negative = negative + tokenLogProbs.get(token).get(Sentiment.NEGATIVE);
                } else {
                    //DONE T0D0
                }
            }

            if (positive > negative) {
                sentiment = Sentiment.POSITIVE;
            }

            result.put(path, sentiment);
        }

        return result;
    }
}
