package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by oliver on 20/02/17.
 */
public class Exercise9 implements IExercise9 {

    private static Feature interestingFeature = Feature.MEMBRANE;

    @Override
    public Map<Feature, Double> getFinalProbs(List<HMMDataStore<AminoAcid, Feature>> trainingPairs) throws IOException {
        return getFinalProbsGen(trainingPairs, Feature.values());
    }

    @Override
    public HiddenMarkovModel<AminoAcid, Feature> estimateHMM(List<HMMDataStore<AminoAcid, Feature>> sequencePairs) throws IOException {
        return estimateHMMGeneric(sequencePairs, AminoAcid.values(), Feature.values());
    }

    @Override
    public List<Feature> viterbi(HiddenMarkovModel<AminoAcid, Feature> model, Map<Feature, Double> finalProbs, List<AminoAcid> observedSequence) {
        return viterbiGen(model, finalProbs, observedSequence, Feature.values());
    }

    @Override
    public Map<List<Feature>, List<Feature>> predictAll(HiddenMarkovModel<AminoAcid, Feature> model, Map<Feature, Double> finalProbs, List<HMMDataStore<AminoAcid, Feature>> testSequencePairs) throws IOException {
        return predictAllGen(model, finalProbs, testSequencePairs, Feature.values());
    }

    @Override
    public double precision(Map<List<Feature>, List<Feature>> true2PredictedMap) {
        return precisionGen(true2PredictedMap, interestingFeature);
    }

    @Override
    public double recall(Map<List<Feature>, List<Feature>> true2PredictedMap) {
        return recallGen(true2PredictedMap, interestingFeature);
    }

    @Override
    public double fOneMeasure(Map<List<Feature>, List<Feature>> true2PredictedMap) {
        return fOneMeasureGen(true2PredictedMap, interestingFeature);
    }

    private <T> double fOneMeasureGen(Map<List<T>, List<T>> true2PredictedMap, T interestingState) {
        double pre = precisionGen(true2PredictedMap, interestingState);
        double rec = recallGen(true2PredictedMap, interestingState);
        return (2 * pre * rec)/(pre + rec);
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
                double aV= Math.log(finalProbs.getOrDefault(lastState,0.0));
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
                    double aV= Math.log(model.getTransitionMatrix().get(lastState).getOrDefault(currentState, 0.0));
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
            double deltaf = Math.log(initialProbs.getOrDefault(state,0.0));
            assert deltaf < 0;
            deltaf += Math.log(model.getEmissionMatrix().get(state).get(initialRoll));
            assert deltaf < 0;
            initialMap.put(state, deltaf);
        }
        delta.add(initialMap);
    }

    private <T,U> Map<T,Double> getFinalProbsGen(List<HMMDataStore<U, T>> dataStores, T[] values) {
        Map<T, Double> result = new HashMap<>(values.length);

        for (HMMDataStore<U, T> store : dataStores) {
            T type = store.hiddenSequence.get(store.hiddenSequence.size() - 1);
            result.put(type, result.getOrDefault(type, 0.0) + 1.0/dataStores.size());
        }

        return result;
    }

    private <T, U> HiddenMarkovModel<T, U> estimateHMMGeneric(List<HMMDataStore<T, U>> dataStores, T[] tValues, U[] uValues) throws IOException {
        Map<U, Map<U, Integer>> transitionMatrixCount = getCountingMatrix(uValues, uValues, 0);
        Map<U, Map<T, Integer>> emissionMatrixCount = getCountingMatrix(uValues, tValues, 0);
        Map<U, Integer> initialProbsCount = new HashMap<>();
        Map<U, Integer> transitionMatrixTotals = new HashMap<>(uValues.length);
        Map<U, Integer> emissionMatrixTotals = new HashMap<>(uValues.length);
        long initialProbsTotal = 0;

        for (HMMDataStore<T, U> dataStore : dataStores) {
            List<T> observedSequence = dataStore.observedSequence;
            List<U> hiddenSequence = dataStore.hiddenSequence;

            U lastType = null;
            for (U type: hiddenSequence) {
                if (lastType != null) {
                    incrementMap(transitionMatrixCount, lastType, type);
                    incrementMap(transitionMatrixTotals, type);
                }
                lastType = type;
            }

            for (int i = 0; (i < hiddenSequence.size()) && (i < observedSequence.size()); i++) {
                if (hiddenSequence.get(i) != null && observedSequence.get(i)!= null) {
                    incrementMap(emissionMatrixCount, hiddenSequence.get(i), observedSequence.get(i));
                    incrementMap(emissionMatrixTotals, hiddenSequence.get(i));
                }

            }

            initialProbsTotal++;
            for (U type : uValues) {
                if (hiddenSequence.get(0) == type) {
                    incrementMap(initialProbsCount, type);
                }
            }
        }

        Map<U, Map<T, Double>> emissionMatrix = getPropabilitiesFromCount2D(emissionMatrixCount, emissionMatrixTotals);
        Map<U, Map<U, Double>> transitionMatrix = getPropabilitiesFromCount2D(transitionMatrixCount, transitionMatrixTotals);
        Map<U, Double> initialProbs = getPropabilitiesFromCount(initialProbsCount, initialProbsTotal);

        HiddenMarkovModel<T, U> result = new HiddenMarkovModel<>(transitionMatrix, emissionMatrix, initialProbs);
        return result;
    }

    private static <U, T> void incrementMap(Map<U, Map<T, Integer>> map, U key1, T key2) {
        incrementMapBy(map, key1, key2, 1);
    }

    private static <U, T> void incrementMapBy(Map<U, Map<T, Integer>> map, U key1, T key2, int delta) {
        incrementMapBy(map.get(key1), key2, delta);
    }

    private static <T> void incrementMap(Map<T, Integer> map, T key) {
        incrementMapBy(map, key, 1);
    }

    private static <T> void incrementMapBy(Map<T, Integer> map, T key, int delta) {
        if (map != null) {
            if (map.containsKey(key)) {
                int value = map.get(key);
                map.put(key, value + delta);
            } else {
                map.put(key, delta);
            }
        }
    }

    private static <T, U> Map<T, Map<U, Integer>> getCountingMatrix(T[] values1, U[] values2, int defaultVal) {
        Map<T, Map<U, Integer>> matrix = new HashMap<>(values1.length);
        for (T typei : values1) {
            Map<U, Integer> innerMap = new HashMap<>(values2.length);
            for (U typej : values2) {
                innerMap.put(typej, defaultVal);
            }
            matrix.put(typei, innerMap);
        }
        return matrix;
    }



    private <U> Map<U, Double> getPropabilitiesFromCount(Map<U, Integer> counts, long total) {
        Map<U, Double> result = new HashMap<>(counts.size());

        for (Map.Entry<U, Integer> entry : counts.entrySet()) {
            result.put(entry.getKey(), (double) entry.getValue()/(double) total);
        }
        return result;
    }

    private static <T, U> Map<T, Map<U, Double>> getPropabilitiesFromCount2D(Map<T, Map<U, Integer>> counts, Map<T, Integer> totals) {
        Map<T, Map<U, Double>> result = new HashMap<>(counts.size());

        for (Map.Entry<T, Map<U, Integer>> innerMap : counts.entrySet()) {
            T type = innerMap.getKey();
            Map<U, Double> newInnerMap = new HashMap<>(innerMap.getValue().size());
            for (Map.Entry<U, Integer> entry : innerMap.getValue().entrySet()) {
                newInnerMap.put(entry.getKey(), (double) entry.getValue() / (double) totals.get(type));
            }
            result.put(innerMap.getKey(), newInnerMap);
        }

        return result;
    }
}