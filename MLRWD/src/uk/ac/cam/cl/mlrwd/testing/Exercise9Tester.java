package uk.ac.cam.cl.mlrwd.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.AminoAcid;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.Feature;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.HMMDataStore;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.IExercise9;
//DONE: Replace with your package.
import uk.ac.cam.cl.olb22.exercises.Exercise9;

public class Exercise9Tester {
	
	static final Path dataFile = Paths.get("data/topic2/task8/bio_dataset.txt");


    public static void main(String[] args) throws IOException {

        List<List<AminoAcid>> obsSeqs = new ArrayList<List<AminoAcid>>();
        List<List<Feature>> hiddenSeqs = new ArrayList<List<Feature>>();

        try (BufferedReader reader = Files.newBufferedReader(dataFile)) {
            reader.lines().forEach(new Consumer<String>() {
                @Override
                public void accept(String line) {
                    if (!line.isEmpty()) {
                        if (line.startsWith("#")) {
                            obsSeqs.add(line.substring(1).chars()
                                    .mapToObj(i -> AminoAcid.valueOf((char)i))
                                    .collect(Collectors.toList()));
                        } else {
                            hiddenSeqs.add(line.chars()
                                    .mapToObj(i -> Feature.valueOf((char)i))
                                    .collect(Collectors.toList()));
                        }
                    }
                }
            });
        } catch (IOException e) {
            throw new IOException("Can't access the file " + dataFile, e);
        }

        List<HMMDataStore<AminoAcid, Feature>> sequencePairs = IntStream.range(0, obsSeqs.size())
                .mapToObj(i -> new HMMDataStore<AminoAcid, Feature>(obsSeqs.get(i), hiddenSeqs.get(i)))
                .collect(Collectors.toList());

        // Use for testing the code
        Collections.shuffle(sequencePairs, new Random(0));
        int testSize = sequencePairs.size()/10;
        List<HMMDataStore<AminoAcid, Feature>> devSet = sequencePairs.subList(0, testSize);
        List<HMMDataStore<AminoAcid, Feature>> testSet = sequencePairs.subList(testSize, 2*testSize);
        List<HMMDataStore<AminoAcid, Feature>> trainingSet = sequencePairs.subList(testSize*2, sequencePairs.size());
        // But:
        // DONE: Replace with cross-validation for the tick.


        IExercise9 implementation = (IExercise9) new Exercise9();

        HiddenMarkovModel<AminoAcid, Feature> model = implementation
                .estimateHMM(trainingSet);
        System.out.println("Predicted transitions:");
        System.out.println(model.getTransitionMatrix());
        System.out.println();
        System.out.println("Predicted emissions:");
        System.out.println(model.getEmissionMatrix());
        System.out.println();
        System.out.println("Predicted start probabilities:");
        System.out.println(model.getInitialProbs());
        System.out.println();

        Map<Feature, Double> finalProbs = implementation.getFinalProbs(trainingSet);
        HMMDataStore<AminoAcid, Feature> data = devSet.get(0);
        List<Feature> predicted = implementation.viterbi(model, finalProbs, data.observedSequence);
        System.out.println("True hidden sequence:");
        System.out.println(data.hiddenSequence);
        System.out.println();

        System.out.println("Predicted hidden sequence:");
        System.out.println(predicted);
        System.out.println();

        Map<List<Feature>, List<Feature>> true2PredictedSequences = implementation.predictAll(model, finalProbs, devSet);
        double accuracy = implementation.precision(true2PredictedSequences);
        System.out.println("Prediction precision:");
        System.out.println(accuracy);
        System.out.println();

        double recall = implementation.recall(true2PredictedSequences);
        System.out.println("Prediction recall:");
        System.out.println(recall);
        System.out.println();

        double f1Score = implementation.fOneMeasure(true2PredictedSequences);
        System.out.println("Prediction F1 score:");
        System.out.println(f1Score);
        System.out.println();

        // But:
        // DONE: Replace with cross-validation for the tick.
        Collections.shuffle(sequencePairs, new Random(0));
        testSize = sequencePairs.size()/10;
        List<List<HMMDataStore<AminoAcid, Feature>>> cvSets = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            List<HMMDataStore<AminoAcid, Feature>> cvSet = sequencePairs.subList(i*testSize, (i+1)*testSize);
            cvSets.add(cvSet);
        }

        List<Double> cvPrecisions = new LinkedList<>();
        List<Double> cvRecalls = new LinkedList<>();
        List<Double> cvF1Scores = new LinkedList<>();

        for (List<HMMDataStore<AminoAcid, Feature>> cvTestSet : cvSets) {
            List<HMMDataStore<AminoAcid, Feature>> cvTrainingSet = new ArrayList<>();
            for (List<HMMDataStore<AminoAcid, Feature>> cvtrainingSubSet : cvSets) {
                if (cvtrainingSubSet != cvTestSet) {
                    cvTrainingSet.addAll(cvtrainingSubSet);
                }
            }

            IExercise9 cvimplementation = (IExercise9) new Exercise9();

            HiddenMarkovModel<AminoAcid, Feature> cvmodel = cvimplementation
                    .estimateHMM(cvTrainingSet);

            Map<Feature, Double> cvfinalProbs = cvimplementation.getFinalProbs(cvTrainingSet);

            Map<List<Feature>, List<Feature>> cvtrue2PredictedMap = cvimplementation.predictAll(cvmodel, cvfinalProbs, cvTestSet);
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
