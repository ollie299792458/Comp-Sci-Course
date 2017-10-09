package uk.ac.cam.cl.mlrwd.exercises.sentiment_detection;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface IExercise4 {

	/**
	 * Modify the simple classifier from Exercise1 to include the information
	 * about the magnitude/strength of a sentiment.
	 * 
	 * @param testSet
	 *            {@link Set}<{@link Path}> Paths to reviews to classify
	 * @param lexiconFile
	 *            {@link Path} Path to the lexicon file
	 * @return {@link Map}<{@link Path}, {@link Sentiment}> Map of calculated
	 *         sentiment for each review
	 * @throws IOException
	 */
	public Map<Path, Sentiment> magnitudeClassifier(Set<Path> testSet, Path lexiconFile) throws IOException;

	/**
	 * Implement the two-sided sign test algorithm to determine if one
	 * classifier is significantly better or worse than another.
	 * 
	 * @param actualSentiments
	 *            {@link Map}<{@link Path}, {@link Sentiment}> True sentiment
	 *            mapping.
	 * @param classificationA
	 *            {@link Map}<{@link Path}, {@link Sentiment}> Predictions of
	 *            one classifier.
	 * @param classificationB
	 *            {@link Map}<{@link Path}, {@link Sentiment}> Predictions of
	 *            another classifier.
	 * @return <code>double</code>
	 */
	public double signTest(Map<Path, Sentiment> actualSentiments, Map<Path, Sentiment> classificationA,
			Map<Path, Sentiment> classificationB);
}