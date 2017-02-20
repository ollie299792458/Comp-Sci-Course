package uk.ac.cam.cl.mlrwd.testing;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.*;

//DONE Replace with your package.
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.olb22.exercises.Exercise7;
import uk.ac.cam.cl.olb22.exercises.Exercise8;

public class Exercise8Tester {

	static final Path dataDirectory = Paths.get("data/topic2/task7/dice_dataset");

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		List<Path> sequenceFiles = new ArrayList<>();
		try (DirectoryStream<Path> files = Files.newDirectoryStream(dataDirectory)) {
			for (Path item : files) {
				sequenceFiles.add(item);
			}
		} catch (IOException e) {
			throw new IOException("Cant access the dataset.", e);
		}
		
		// Use for testing the code
		Collections.shuffle(sequenceFiles, new Random(0));
		int testSize = sequenceFiles.size()/10;
		List<Path> devSet = sequenceFiles.subList(0, testSize);
		List<Path> testSet = sequenceFiles.subList(testSize, 2*testSize);
		List<Path> trainingSet = sequenceFiles.subList(testSize*2, sequenceFiles.size());
		
		IExercise7 implementation7 = (IExercise7) new Exercise7();
		HiddenMarkovModel<DiceRoll, DiceType> model = implementation7.estimateHMM(trainingSet);

		IExercise8 implementation = (IExercise8) new Exercise8();
		Map<DiceType, Double> finalProbs = implementation.getFinalProbs(trainingSet);
		System.out.println("Final probs");
		System.out.println(finalProbs);
		System.out.println();
			
		HMMDataStore<DiceRoll, DiceType> data = HMMDataStore.loadDiceFile(devSet.get(0));
		List<DiceType> predicted = implementation.viterbi(model, finalProbs, data.observedSequence);
		System.out.println("True hidden sequence:");
		System.out.println(data.hiddenSequence);
		System.out.println();
		
		System.out.println("Predicted hidden sequence:");
		System.out.println(predicted);
		System.out.println();
	

		Map<List<DiceType>, List<DiceType>> true2PredictedMap = implementation.predictAll(model, finalProbs, devSet);
		double precision = implementation.precision(true2PredictedMap);
		System.out.println("Prediction precision:");
		System.out.println(precision);
		System.out.println();

		double recall = implementation.recall(true2PredictedMap);
		System.out.println("Prediction recall:");
		System.out.println(recall);
		System.out.println();

		double fOneMeasure = implementation.fOneMeasure(true2PredictedMap);
		System.out.println("Prediction fOneMeasure:");
		System.out.println(fOneMeasure);
		System.out.println();


        // But:
        // DONE: Replace with cross-validation for the tick.
        Collections.shuffle(sequenceFiles, new Random(0));
        testSize = sequenceFiles.size()/10;
        List<List<Path>> cvSets = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            List<Path> cvSet = sequenceFiles.subList(i*testSize, (i+1)*testSize);
            cvSets.add(cvSet);
        }

        List<Double> cvPrecisions = new LinkedList<>();
        List<Double> cvRecalls = new LinkedList<>();
        List<Double> cvF1Scores = new LinkedList<>();

        for (List<Path> cvTestSet : cvSets) {
            List<Path> cvTrainingSet = new ArrayList<>();
            for (List<Path> cvtrainingSubSet : cvSets) {
                if (cvtrainingSubSet != cvTestSet) {
                    cvTrainingSet.addAll(cvtrainingSubSet);
                }
            }

            IExercise7 cvimplementation7 = (IExercise7) new Exercise7();
            HiddenMarkovModel<DiceRoll, DiceType> cvmodel = cvimplementation7.estimateHMM(cvTrainingSet);

            IExercise8 cvimplementation = (IExercise8) new Exercise8();
            Map<DiceType, Double> cvfinalProbs = cvimplementation.getFinalProbs(cvTrainingSet);

            Map<List<DiceType>, List<DiceType>> cvtrue2PredictedMap = cvimplementation.predictAll(cvmodel, cvfinalProbs, cvTestSet);
            cvPrecisions.add(cvimplementation.precision(cvtrue2PredictedMap));
            cvRecalls.add(cvimplementation.recall(cvtrue2PredictedMap));
            cvF1Scores.add(cvimplementation.fOneMeasure(cvtrue2PredictedMap));
        }
        double meanF1 = 0, meanRecall = 0, meanPrecision = 0;
        for (int i = 0; i<cvF1Scores.size(); i++) {
            meanF1 += cvF1Scores.get(i)/cvF1Scores.size();
            meanRecall += cvRecalls.get(i)/cvRecalls.size();
            meanPrecision += cvPrecisions.get(i)/cvPrecisions.size();
        }

        System.out.println("Cross Validation");
        System.out.println("Precision: "+meanPrecision+", Recall: "+meanRecall+", F1: "+meanF1);
        System.out.println();
	}
}
