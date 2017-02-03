package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by oliver on 03/02/17.
 */
public class Exercise5 implements IExercise5 {
    @Override
    public List<Map<Path, Sentiment>> splitCVRandom(Map<Path, Sentiment> dataSet, int seed) {
        int noOfFolds = 10;
        List<Map<Path, Sentiment>> result = new LinkedList<>();
        for (int i = 0; i < noOfFolds; i++) {
            result.add(new HashMap<>());
        }
        Random ran = new Random(seed);
        for (Map.Entry<Path, Sentiment> entry : dataSet.entrySet()) {
            int ranint = ran.nextInt(noOfFolds);
            while (result.get(ranint).size() >= dataSet.size() / noOfFolds) {
                ranint = ran.nextInt(noOfFolds);
            }
            result.get(ranint).put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public List<Map<Path, Sentiment>> splitCVStratifiedRandom(Map<Path, Sentiment> dataSet, int seed) {
        Map<Path, Sentiment> positives = new HashMap<>();
        Map<Path, Sentiment> negatives = new HashMap<>();
        for (Map.Entry<Path, Sentiment> entry : dataSet.entrySet()) {
            if (entry.getValue() == Sentiment.POSITIVE) {
                positives.put(entry.getKey(), entry.getValue());
            } else {
                negatives.put(entry.getKey(), entry.getValue());
            }
        }
        List<Map<Path, Sentiment>> positiveList = splitCVRandom(positives, seed);
        List<Map<Path, Sentiment>> negativeList = splitCVRandom(negatives, seed);
        for (Map<Path, Sentiment> positiveListEntry : positiveList) {
            positiveListEntry.putAll(negativeList.get(positiveList.indexOf(positiveListEntry)));
        }
        return positiveList;
    }

    @Override
    public double[] crossValidate(List<Map<Path, Sentiment>> folds) throws IOException {
        double[] result = new double[folds.size()];
        for (Map<Path, Sentiment> testingSet : folds) {
            Map<Path, Sentiment> trainingSet = new HashMap<>(testingSet.size()*(folds.size()-1));
            for (Map<Path, Sentiment> tempMap : folds) {
                if (tempMap != testingSet) {
                    trainingSet.putAll(tempMap);
                }
            }
            Exercise2 exercise2 = new Exercise2();
            Map<Sentiment, Double> classProbabilities = exercise2.calculateClassProbabilities(trainingSet);
            Map<String, Map<Sentiment, Double>> logProbabilities = exercise2.calculateSmoothedLogProbs(trainingSet);
            Map<Path, Sentiment> naiveBayesResult = exercise2.naiveBayes(testingSet.keySet(), logProbabilities, classProbabilities);
            Exercise1 exercise1 = new Exercise1();
            double accuracy = exercise1.calculateAccuracy(testingSet, naiveBayesResult);
            result[folds.indexOf(testingSet)] = accuracy;
        }
        return result;
    }

    @Override
    public double cvAccuracy(double[] scores) {
        int n = scores.length;
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += scores[i]/(double) n;
        }
        return result;
    }

    @Override
    public double cvVariance(double[] scores) {
        int n = scores.length;
        double result = 0;
        double mew = cvAccuracy(scores);
        for (int i = 0; i < n; i++) {
            result += Math.pow(scores[i] - mew, 2);
        }
        double oneOverN = 1/(double) n;
        result = oneOverN*result;
        return result;
    }

    static final Path dataDirectory = Paths.get("data/topic1/sentiment_dataset");
    static final Path testDirectory = Paths.get("data/topic1/task5/sentiment_test_set");
    static final Path newDataDirectory = Paths.get("data/topic1/task5/year_2016_dataset");
    static final int seed = 0;

    public static void main(String[] args) throws IOException {
        // Read in the answer key.
        Path sentimentFile = dataDirectory.resolve("review_sentiment");
        // Get the data set.
        Map<Path, Sentiment> dataSet = DataPreparation1.loadSentimentDataset(dataDirectory.resolve("reviews"),
                sentimentFile);

        IExercise5 implementation = (IExercise5) new Exercise5();

        List<Map<Path, Sentiment>> randomFolds = implementation.splitCVRandom(dataSet, seed);
        double[] randomScores = implementation.crossValidate(randomFolds);
        double randomScore = implementation.cvAccuracy(randomScores);
        double randomVar= implementation.cvVariance(randomScores);
        System.out.println("CV score for folds split randomly: ");
        System.out.println("Average:" + randomScore);
        System.out.println("Variance:" + randomVar);

        List<Map<Path, Sentiment>> randomStratFolds = implementation.splitCVStratifiedRandom(dataSet, seed);
        double[] stratScores = implementation.crossValidate(randomStratFolds);
        double stratScore = implementation.cvAccuracy(stratScores);
        double stratVar= implementation.cvVariance(stratScores);
        System.out.println("CV score for stratified random folds: ");
        System.out.println("Average:" + stratScore);
        System.out.println("Variance:" + stratVar);

        Path oldSentFile = testDirectory.resolve("test_sentiment");
        Map<Path, Sentiment> testSet = DataPreparation1.loadSentimentDataset(testDirectory.resolve("reviews"),
                oldSentFile);
        Path newSentFile = newDataDirectory.resolve("review_sentiment");
        Map<Path, Sentiment> newTestSet = DataPreparation1.loadSentimentDataset(newDataDirectory.resolve("reviews"),
                newSentFile);

        Exercise1 impl1 = new Exercise1();
        Path lexiconFile = Paths.get("data/topic1/task1/sentiment_lexicon");
        Map<Path, Sentiment> testPredictions = impl1.simpleClassifier(testSet.keySet(), lexiconFile);
        Map<Path, Sentiment> newPredictions = impl1.simpleClassifier(newTestSet.keySet(), lexiconFile);

        double oldAccuracy = impl1.calculateAccuracy(testSet, testPredictions);
        double newAccuracy = impl1.calculateAccuracy(newTestSet, newPredictions);

        System.out.println("Accuracy on the original test set:");
        System.out.println(oldAccuracy);
        System.out.println();

        System.out.println("Accuracy on the 2016 test set:");
        System.out.println(newAccuracy);
        System.out.println();

        Exercise4 signTestResult = new Exercise4();

        Exercise2 implementation2 = new Exercise2();
        Map<String, Map<Sentiment, Double>> probs = implementation2.calculateSmoothedLogProbs(dataSet);
        Map<Sentiment, Double> classProbs = implementation2.calculateClassProbabilities(dataSet);
        Map<Path, Sentiment> newNaiveBayesPredictions = implementation2.naiveBayes(newTestSet.keySet(), probs, classProbs);

        double signResultNBMag = signTestResult.signTest(newTestSet, newPredictions, newNaiveBayesPredictions);
        System.out.println("Sign test results:");
        System.out.println("2016 NB vs 2016 Simple");
        System.out.println(signResultNBMag);
    }

}
