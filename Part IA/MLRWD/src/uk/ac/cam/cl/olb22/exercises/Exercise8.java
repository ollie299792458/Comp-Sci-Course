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
        Map<T, Double> result = new HashMap<>(values.length);

        for (HMMDataStore<U, T> store : dataStores) {
            T type = store.hiddenSequence.get(store.hiddenSequence.size() - 1);
            result.put(type, result.getOrDefault(type, 0.0) + 1.0/dataStores.size());
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

        addEndState(model, delta, psi, observedSequence, finalProbs, statesT);

        assert psi.size() == observedSequence.size();

        List<T> result = grandFinale(model, observedSequence.size(), delta, psi, statesT);

        assert result.size() == observedSequence.size();

        return result;
    }

    private <T, U> List<T> grandFinale(HiddenMarkovModel<U, T> model, int length, List<Map<T, Double>> delta, List<Map<T, T>> psi, T[] values) {
        List<T> result = new LinkedList<T>();

        Map.Entry<T, Double> endState = Collections.max(delta.get(delta.size()-1).entrySet(), Map.Entry.comparingByValue());

        T currentState = endState.getKey();
        result.add(currentState);
        for (int i = psi.size()-1; i>=0; i--){
            currentState = psi.get(i).get(currentState);
            result.add(currentState);
        }
        result.remove(0);
        Collections.reverse(result);
        return result;
    }

    private <U, T> void addEndState(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, List<Map<T, T>> psi, List<U> observedSequence, Map<T, Double> finalProbs, T[] statesT) {
        Map<T, Double> deltaInnerMap = new HashMap<>(statesT.length);
        Map<T, T> psiInnerMap = new HashMap<>(statesT.length);
        for (T currentState : statesT) {
            Map<T, Double> possibleProbs = new HashMap<>(statesT.length);

            for (T lastState : statesT) {
                double prob = 0;
                double deltaV = delta.get(delta.size()-1).get(lastState);
                assert deltaV < 0;
                double aV= Math.log(finalProbs.get(lastState));
                assert aV < 0;
                prob = deltaV+aV;
                possibleProbs.put(lastState, prob);
            }

            Map.Entry<T, Double> best = Collections.max(possibleProbs.entrySet(), Map.Entry.comparingByValue());
            psiInnerMap.put(currentState, best.getKey());
            deltaInnerMap.put(currentState, best.getValue());
        }
        delta.add(deltaInnerMap);
        psi.add(psiInnerMap);
    }

    private <U, T> void mainStep(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, List<Map<T, T>> psi, List<U> observedSequence, T[] statesT) {
        for (int t = 1; t < observedSequence.size(); t++) {
            Map<T, Double> deltaInnerMap = new HashMap<>(statesT.length);
            Map<T, T> psiInnerMap = new HashMap<>(statesT.length);
            for (T currentState : statesT) {
                Map<T, Double> possibleProbs = new HashMap<>(statesT.length);
                double bV = Math.log(model.getEmissionMatrix().get(currentState).get(observedSequence.get(t)));
                assert bV < 0;

                for (T lastState : statesT) {
                    double prob = 0;
                    double deltaV = delta.get(t-1).get(lastState);
                    assert deltaV < 0;
                    double aV= Math.log(model.getTransitionMatrix().get(lastState).get(currentState));
                    assert aV < 0;
                    prob = deltaV+aV+bV;
                    possibleProbs.put(lastState, prob);
                }

                Map.Entry<T, Double> best = Collections.max(possibleProbs.entrySet(), Map.Entry.comparingByValue());
                psiInnerMap.put(currentState, best.getKey());
                deltaInnerMap.put(currentState, best.getValue());
            }
            delta.add(deltaInnerMap);
            psi.add(psiInnerMap);
        }
    }

    private <U, T> void initialise(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, U initialRoll, T[] statesT) {
        Map<T, Double> initialProbs = model.getInitialProbs();
        Map<T, Double> initialMap = new HashMap<>();

        for (T state : statesT) {
            double deltaf = Math.log(initialProbs.get(state));
            assert deltaf < 0;
            deltaf += Math.log(model.getEmissionMatrix().get(state).get(initialRoll));
            assert deltaf < 0;
            initialMap.put(state, deltaf);
        }
        delta.add(initialMap);
    }

    @Override
    public Map<List<DiceType>, List<DiceType>> predictAll(HiddenMarkovModel<DiceRoll, DiceType> model, Map<DiceType, Double> finalProbs, List<Path> testFiles) throws IOException {
        List<HMMDataStore<DiceRoll, DiceType>> dataStores = HMMDataStore.loadDiceFiles(testFiles);
        return predictAllGen(model, finalProbs, dataStores, DiceType.values());
    }

    private <T,U> Map<List<T>, List<T>> predictAllGen(HiddenMarkovModel<U, T> model, Map<T, Double> finalProbs, List<HMMDataStore<U, T>> dataStores, T[] statesT) throws IOException {
        Map<List<T>, List<U>> dataToTest = new HashMap<>();
        for (HMMDataStore<U, T> dataStore : dataStores) {
            dataToTest.put(dataStore.hiddenSequence, dataStore.observedSequence);
        }
        Map<List<T>, List<T>> result = new HashMap<>(dataToTest.size());
        for (Map.Entry<List<T>, List<U>> entry : dataToTest.entrySet()) {
            result.put(entry.getKey(), viterbiGen(model, finalProbs, entry.getValue(), statesT));
        }
        return result;
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
        return recallGen(true2PredictedMap, DiceType.WEIGHTED);
    }

    private <T> double recallGen(Map<List<T>, List<T>> true2PredictedMap, T interestingState) {
        int numberOfCorrectlyPredictedi = 0;
        int trueNumberOfi = 0;
        for (Map.Entry<List<T>, List<T>> entry : true2PredictedMap.entrySet()) {
            List<T> trueStates = entry.getKey();
            List<T> predictedStates = entry.getValue();
            for (int i = 0; i < trueStates.size() && i < predictedStates.size(); i++) {
                if (trueStates.get(i) == interestingState) {
                    trueNumberOfi++;
                    if (predictedStates.get(i)==interestingState) {
                        numberOfCorrectlyPredictedi++;
                    }
                }
            }
        }
        return (double) numberOfCorrectlyPredictedi / (double) trueNumberOfi;
    }

    @Override
    public double fOneMeasure(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return fOneMeasureGen(true2PredictedMap, DiceType.WEIGHTED);
    }

    private <T> double fOneMeasureGen(Map<List<T>, List<T>> true2PredictedMap, T interestingState) {
        double pre = precisionGen(true2PredictedMap, interestingState);
        double rec = recallGen(true2PredictedMap, interestingState);
        return (2 * pre * rec)/(pre + rec);
    }
}