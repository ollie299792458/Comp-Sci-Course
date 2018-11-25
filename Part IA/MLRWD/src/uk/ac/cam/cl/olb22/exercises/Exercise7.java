package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 13/02/17.
 */
public class Exercise7 implements IExercise7 {
    @Override
    public HiddenMarkovModel<DiceRoll, DiceType> estimateHMM(Collection<Path> sequenceFiles) throws IOException {
        List<HMMDataStore<DiceRoll, DiceType>> dataStores = HMMDataStore.loadDiceFiles(sequenceFiles);
        return estimateHMMGeneric(dataStores, DiceRoll.values(), DiceType.values());
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
