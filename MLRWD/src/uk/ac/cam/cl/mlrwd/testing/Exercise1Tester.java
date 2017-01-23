package uk.ac.cam.cl.mlrwd.testing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.DataPreparation1;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise1;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;

import uk.ac.cam.cl.mlrwd.utils.DataSplit;
import uk.ac.cam.cl.olb22.exercises.Exercise1;

public class Exercise1Tester {
	static final Path dataDirectory = Paths.get("data/topic1/sentiment_dataset");

	public static void main(String[] args) throws IOException {
		Path lexiconFile = Paths.get("data/topic1/task1/sentiment_lexicon");

		// Loading the dataset.
		Path sentimentFile = dataDirectory.resolve("review_sentiment");
		Path reviewsDir = dataDirectory.resolve("reviews");
		Map<Path, Sentiment> dataSet = DataPreparation1.loadSentimentDataset(reviewsDir, sentimentFile);

		IExercise1 implementation = new Exercise1();

		Map<Path, Sentiment> predictedSentiments = implementation.simpleClassifier(dataSet.keySet(), lexiconFile);
		System.out.println("Classifier predictions:");
		System.out.println(predictedSentiments);
		System.out.println();

		double calculatedAccuracy = implementation.calculateAccuracy(dataSet, predictedSentiments);
		System.out.println("Classifier accuracy:");
		System.out.println(calculatedAccuracy);
		System.out.println();

		Map<Path, Sentiment> improvedPredictions = implementation.improvedClassifier(dataSet.keySet(), lexiconFile);
		System.out.println("Improved classifier predictions:");
		System.out.println(improvedPredictions);
		System.out.println();

		System.out.println("Improved classifier accuracy:");
		System.out.println(implementation.calculateAccuracy(dataSet, improvedPredictions));
		System.out.println();
	}
}
