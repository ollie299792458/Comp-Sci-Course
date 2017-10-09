package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise6;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.NuancedSentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 06/02/17.
 */
public class Exercise6 implements IExercise6 {
    @Override
    public Map<NuancedSentiment, Double> calculateClassProbabilities(Map<Path, NuancedSentiment> trainingSet) throws IOException {
        Map<NuancedSentiment, Integer> totalReviews = new HashMap<>(2);
        totalReviews.put(NuancedSentiment.NEGATIVE, 0);
        totalReviews.put(NuancedSentiment.POSITIVE, 0);
        totalReviews.put(NuancedSentiment.NEUTRAL, 0);

        for (Map.Entry<Path, NuancedSentiment> entry : trainingSet.entrySet()) {
            totalReviews.put(entry.getValue(), totalReviews.get(entry.getValue())+1);
        }

        Map<NuancedSentiment, Double> classProbabilities= new HashMap<>(totalReviews.size());
        classProbabilities.put(NuancedSentiment.NEGATIVE, ((double) totalReviews.get(NuancedSentiment.NEGATIVE))/((double) trainingSet.size()));
        classProbabilities.put(NuancedSentiment.POSITIVE, ((double) totalReviews.get(NuancedSentiment.POSITIVE))/((double) trainingSet.size()));
        classProbabilities.put(NuancedSentiment.NEUTRAL, ((double) totalReviews.get(NuancedSentiment.NEUTRAL)/(double) trainingSet.size()));
        return classProbabilities;
    }

    @Override
    public Map<String, Map<NuancedSentiment, Double>> calculateNuancedLogProbs(Map<Path, NuancedSentiment> trainingSet) throws IOException {
        Map<String, Map<NuancedSentiment, Integer>> countingMap = new HashMap<>();

        Map<NuancedSentiment, Integer> totalWords = new HashMap<>(2);
        totalWords.put(NuancedSentiment.POSITIVE, 0);
        totalWords.put(NuancedSentiment.NEGATIVE, 0);
        totalWords.put(NuancedSentiment.NEUTRAL, 0);

        for (Map.Entry<Path, NuancedSentiment> entry : trainingSet.entrySet()) {

            List<String> tokens = Tokenizer.tokenize(entry.getKey());
            for (String token : tokens) {

                if (!countingMap.containsKey(token)) {
                    Map<NuancedSentiment, Integer> sentimentMap = new HashMap<>(2);
                    sentimentMap.put(NuancedSentiment.POSITIVE, 1);
                    sentimentMap.put(NuancedSentiment.NEGATIVE, 1);
                    sentimentMap.put(NuancedSentiment.NEUTRAL, 1);
                    totalWords.put(NuancedSentiment.POSITIVE, totalWords.get(NuancedSentiment.POSITIVE) + 1);
                    totalWords.put(NuancedSentiment.NEGATIVE, totalWords.get(NuancedSentiment.NEGATIVE) + 1);
                    totalWords.put(NuancedSentiment.NEUTRAL, totalWords.get(NuancedSentiment.NEUTRAL) + 1);
                    countingMap.put(token, sentimentMap);
                }

                totalWords.put(entry.getValue(), totalWords.get(entry.getValue())+1);
                Map<NuancedSentiment, Integer> sentimentMap = countingMap.get(token);
                sentimentMap.put(entry.getValue(), sentimentMap.get(entry.getValue())+1);
                countingMap.put(token, sentimentMap);
            }
        }

        Map<String, Map<NuancedSentiment, Double>> result = new HashMap<>(countingMap.size());

        for (Map.Entry<String, Map<NuancedSentiment, Integer>> entry : countingMap.entrySet()) {
            Map<NuancedSentiment, Double> sentimentDoubleMap = new HashMap<>(entry.getValue().size());
            double positiveProbability = ((double) entry.getValue().get(NuancedSentiment.POSITIVE))/((double) totalWords.get(NuancedSentiment.POSITIVE));
            double negativeProbability = ((double) entry.getValue().get(NuancedSentiment.NEGATIVE))/((double) totalWords.get(NuancedSentiment.NEGATIVE));
            double neutralProbability = ((double) entry.getValue().get(NuancedSentiment.NEUTRAL)/(double) totalWords.get(NuancedSentiment.NEUTRAL));
            positiveProbability = Math.log(positiveProbability);
            negativeProbability = Math.log(negativeProbability);
            neutralProbability = Math.log(neutralProbability);
            sentimentDoubleMap.put(NuancedSentiment.POSITIVE, positiveProbability);
            sentimentDoubleMap.put(NuancedSentiment.NEGATIVE, negativeProbability);
            sentimentDoubleMap.put(NuancedSentiment.NEUTRAL, neutralProbability);
            result.put(entry.getKey(), sentimentDoubleMap);
        }

        return result;
    }

    @Override
    public Map<Path, NuancedSentiment> nuancedClassifier(Set<Path> testSet, Map<String, Map<NuancedSentiment, Double>> tokenLogProbs, Map<NuancedSentiment, Double> classProbabilities) throws IOException {
        Map<Path, NuancedSentiment> result = new HashMap<>(testSet.size());

        for (Path path : testSet) {
            NuancedSentiment sentiment = NuancedSentiment.NEUTRAL;
            double positive = Math.log(classProbabilities.get(NuancedSentiment.NEGATIVE));
            double negative = Math.log(classProbabilities.get(NuancedSentiment.POSITIVE));
            double neutral = Math.log(classProbabilities.get(NuancedSentiment.NEUTRAL));

            List<String> tokens = Tokenizer.tokenize(path);
            for (String token : tokens) {
                if (tokenLogProbs.containsKey(token)) {
                    positive += tokenLogProbs.get(token).get(NuancedSentiment.POSITIVE);
                    negative += tokenLogProbs.get(token).get(NuancedSentiment.NEGATIVE);
                    neutral+= tokenLogProbs.get(token).get(NuancedSentiment.NEUTRAL);
                } else {
                    //DONE T0D0
                }
            }

            if ((positive > neutral) || (negative > neutral)) {
                if (positive > negative) {
                    sentiment = NuancedSentiment.POSITIVE;
                } else {
                    sentiment = NuancedSentiment.NEGATIVE;
                }
            }

            result.put(path, sentiment);
        }

        return result;
    }

    @Override
    public double nuancedAccuracy(Map<Path, NuancedSentiment> trueSentiments, Map<Path, NuancedSentiment> predictedSentiments) {
        int correct = 0;
        if (predictedSentiments == null) {
            return 0;
        }
        int total = predictedSentiments.size();

        for (Map.Entry<Path, NuancedSentiment> predictedSentiment : predictedSentiments.entrySet()) {
            NuancedSentiment trueSentiment;
            if ((trueSentiment = trueSentiments.get(predictedSentiment.getKey())) != null) {
                if (trueSentiment == predictedSentiment.getValue()){
                    correct++;
                }
            }
        }
        return ((double)correct)/((double)total);
    }

    @Override
    public Map<Integer, Map<Sentiment, Integer>> agreementTable(Collection<Map<Integer, Sentiment>> predictedSentiments) {
        Map<Integer, Map<Sentiment, Integer>> result = new HashMap<>();

        for (Map<Integer, Sentiment> prediction : predictedSentiments) {

            for (Map.Entry<Integer, Sentiment> entry : prediction.entrySet()) {

                Map<Sentiment, Integer> internalMap = result.getOrDefault(entry.getKey(), new HashMap<>(2));

                if (!   internalMap.containsKey(entry.getValue())) {
                    internalMap.put(entry.getValue(), 0);
                }

                internalMap.put(entry.getValue(), internalMap.get(entry.getValue()) + 1);

                result.put(entry.getKey(), internalMap);
            }
        }
        return result;
    }

    @Override
    public double kappa(Map<Integer, Map<Sentiment, Integer>> agreementTable) {
        final int bigN = agreementTable.size();

        Map<Integer, Integer> totalIs = new HashMap<>(agreementTable.size());

        for (Map.Entry<Integer, Map<Sentiment, Integer>> iEntry : agreementTable.entrySet()) {
            Map<Sentiment, Integer> iEntryMap = iEntry.getValue();
            int totalI = 0;
            for (Sentiment sentiment: Sentiment.values()) {
                totalI += iEntryMap.getOrDefault(sentiment, 0);
            }
            totalIs.put(iEntry.getKey(), totalI);
        }

        double pe = 0;
        for (Sentiment j: Sentiment.values()) {
            double peinside = 0;

            for (Map.Entry<Integer, Map<Sentiment, Integer>> innerEntry : agreementTable.entrySet()) {
                Map<Sentiment, Integer> innerMap = innerEntry.getValue();

                if (innerMap.containsKey(j) && totalIs.containsKey(innerEntry.getKey())) {
                    double nij = innerMap.get(j);
                    double ni = totalIs.get(innerEntry.getKey());
                    peinside += nij / ni;
                }
            }
            pe += Math.pow(peinside/(double) bigN, 2);
        }

        double pa = 0;
        for (Map.Entry<Integer, Map<Sentiment, Integer>> innerEntry : agreementTable.entrySet()) {
            double painside = 0;

            for (Sentiment j : Sentiment.values()) {
                Map<Sentiment, Integer> innerMap = innerEntry.getValue();

                if (innerMap.containsKey(j)) {
                    double nij = innerMap.get(j);
                    painside += nij * (nij - 1);
                }
            }

            if (totalIs.containsKey(innerEntry.getKey())) {
                double ni = totalIs.get(innerEntry.getKey());
                pa += painside / (ni * (ni - 1));
            }
        }
        pa = (pa/(double) bigN);

        final double kappa = (pa - pe)/(1.0 - pe);
        return kappa;
    }
}
