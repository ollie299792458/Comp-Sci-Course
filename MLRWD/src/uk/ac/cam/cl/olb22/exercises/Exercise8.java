package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 16/02/17.
 */
public class Exercise8 implements IExercise8 {
    @Override
    public Map<DiceType, Double> getFinalProbs(List<Path> trainingFiles) throws IOException {
        List<HMMDataStore<DiceRoll, DiceType>> dataStores = HMMDataStore.loadDiceFiles(trainingFiles);
        return getFinalProbsGen(dataStores, DiceType.values());
    }

    private <T,U> Map<T,Double> getFinalProbsGen(List<HMMDataStore<U, T>> dataStores, T[] values) {
        Map<T, Double> result = new HashMap<T, Double>(values.length);
        Map<T, Integer> resultCount = new HashMap<T, Integer>(values.length);
        int total = dataStores.size();

        for (T value : values) {
            resultCount.put(value, 0);
        }

        for (HMMDataStore<U, T> dataStore : dataStores) {
            T last = dataStore.hiddenSequence.get(dataStore.hiddenSequence.size()-1);
            resultCount.put(last, resultCount.get(last)+1);
        }

        for (T value : values) {
            result.put(value, (double) resultCount.get(value)/(double) total);
        }
        return result;
    }

    @Override
    public List<DiceType> viterbi(HiddenMarkovModel<DiceRoll, DiceType> model, Map<DiceType, Double> finalProbs, List<DiceRoll> observedSequence) {
        return viterbiGen(model, finalProbs, observedSequence, DiceType.values());
    }

    private <T, U> List<T> viterbiGen(HiddenMarkovModel<U, T> model, Map<T, Double> finalProbs, List<U> observedSequence, T[] statesT) {
        List<Map<T, T>> psi = new LinkedList<>();
        List<Map<T, Double>> delta = new LinkedList<>();

        initialise(model, delta, observedSequence.get(0), statesT);

        mainStep(model, delta, psi, observedSequence, statesT);

        T endState = finalProbs(model, delta, psi, observedSequence.size(), finalProbs, statesT);

        List<T> result = grandFinale(model, observedSequence.size(), psi, endState);

        assert result.size() == observedSequence.size();

        return result;
    }

    private <T, U> List<T> grandFinale(HiddenMarkovModel<U, T> model, int length, List<Map<T, T>> psi, T endState) {
        List<T> result = new LinkedList<T>();

        result.add(endState);
        for (int i = length-1; i >= 0; i--) {
            T stateToRight = result.get(length-i-1);
            T bestStateToLeft = psi.get(i).get(stateToRight);
            result.add(bestStateToLeft);
        }
        Collections.reverse(result);
        result.remove(0);
        return result;
    }

    private <U, T> T finalProbs(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, List<Map<T, T>> psi, int length, Map<T, Double> finalProbs, T[] statesT) {
        T endState = null;
        double biggestProb = 0;
        for (T potentialEndState : statesT) {
            double newProb = finalProbs.get(potentialEndState);
            if (newProb < biggestProb) {
                endState = potentialEndState;
                biggestProb = newProb;
            }
        }
        Map<T, Double> innerDeltaMap = new HashMap<>();
        innerDeltaMap.put(endState, biggestProb);
        Map<T, T> innerPsiMap = new HashMap<>();
        for (T state : statesT) {
            innerPsiMap.put(endState, state);
        }
        delta.add(innerDeltaMap);
        psi.add(innerPsiMap);
        return endState;
    }

    private <U, T> void mainStep(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, List<Map<T, T>> psi, List<U> observedSequence, T[] statesT) {
        for (int i = 1; i < observedSequence.size(); i++) {
            Map<T, Double> deltaInnerMap = new HashMap<>(statesT.length);
            Map<T, T> psiInnerMap = new HashMap<>(statesT.length*statesT.length);
            for (T state : statesT) {
                double probability = 0;
                T bestState = null;
                for (T oldstate : statesT) {
                    double newProbability = delta.get(i-1).get(oldstate);
                    newProbability += Math.log(model.getTransitionMatrix().get(oldstate).get(state));
                    newProbability += Math.log(model.getEmissionMatrix().get(state).get(observedSequence.get(i)));
                    if (newProbability < probability) {
                        probability = newProbability;
                        bestState = oldstate;
                    }
                }
                assert bestState != null;
                deltaInnerMap.put(state, probability);
                psiInnerMap.put(state, bestState);
            }
            delta.add(deltaInnerMap);
            psi.add(psiInnerMap);
        }
    }

    private <U, T> void initialise(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, U initialRoll, T[] statesT) {
        Map<T, Double> initialProbs = model.getInitialProbs();
        Map<T, Double> initialMap = new HashMap<>();

        for (T state : statesT) {
            double deltaf = initialProbs.get(state) * model.getEmissionMatrix().get(state).get(initialRoll);
            deltaf = Math.log(deltaf);
            initialMap.put(state, deltaf);
        }
        delta.add(initialMap);
    }

    @Override
    public Map<List<DiceType>, List<DiceType>> predictAll(HiddenMarkovModel<DiceRoll, DiceType> model, Map<DiceType, Double> finalProbs, List<Path> testFiles) throws IOException {
        List<HMMDataStore<DiceRoll, DiceType>> dataStores = HMMDataStore.loadDiceFiles(testFiles);
        return predictAllGen(model, finalProbs, dataStores);
    }

    private <T,U> Map<List<T>, List<T>> predictAllGen(HiddenMarkovModel<U, T> model, Map<T, Double> finalProbs, List<HMMDataStore<U, T>> dataStores) throws IOException {
    }

    @Override
    public double precision(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return precisionGen(true2PredictedMap, DiceType.WEIGHTED);
    }

    private <T> double precisionGen(Map<List<T>, List<T>> true2PredictedMap, T interestingState) {
        int numberOfCorrectlyPredictedi = 0;
        int numberOfPredictedi = 0;
        for (Map.Entry<List<T>, List<T>> entry : true2PredictedMap.entrySet()) {
            List<T> trueStates = entry.getKey();
            List<T> predictedStates = entry.getValue();
            for (int i = 0; i < trueStates.size() && i < predictedStates.size(); i++) {
                if (predictedStates.get(i) == interestingState) {
                    numberOfPredictedi++;
                    if (trueStates.get(i) == interestingState) {
                        numberOfCorrectlyPredictedi++;
                    }
                }
            }
        }
        return (double) numberOfCorrectlyPredictedi/(double) numberOfPredictedi;
    }

    @Override
    public double recall(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return 0;
    }

    @Override
    public double fOneMeasure(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return 0;
    }
}