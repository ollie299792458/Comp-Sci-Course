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

        T endState = getEndState(model, delta, psi, observedSequence.size(), finalProbs, statesT);

        List<T> result = grandFinale(model, observedSequence.size(), psi, endState);

        assert result.size() == observedSequence.size();

        return result;
    }

    private <T, U> List<T> grandFinale(HiddenMarkovModel<U, T> model, int length, List<Map<T, T>> psi, T endState) {
        List<T> result = new LinkedList<T>();

        result.add(endState);
        while (result.size() <= length) {
            T currentState = result.get(result.size()-1);
            T bestPrevState = psi.get(length-result.size()).get(currentState);
            result.add(bestPrevState);
        }
        Collections.reverse(result);
        result.remove(0);
        return result;
    }

    private <U, T> T getEndState(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, List<Map<T, T>> psi, int length, Map<T, Double> finalProbs, T[] statesT) {
        T endState = null;
        double biggestProb = 0;
        for (T potentialEndState : statesT) {
            double deltaV = delta.get(delta.size()-1).get(potentialEndState);
            assert deltaV < 0;
            double aV = Math.log(finalProbs.get(potentialEndState));
            assert aV < 0;
            double newProb = aV+deltaV;
            assert newProb < 0;
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
            Map<T, T> psiInnerMap = new HashMap<>(statesT.length);
            for (T currentState : statesT) {
                T mpLastState = null;
                double mpProp = 0;

                for (T lastState : statesT) {
                    double prob = 0;
                    double deltaV = delta.get(i-1).get(lastState);
                    assert deltaV < 0;
                    double aV= Math.log(model.getTransitionMatrix().get(lastState).get(currentState));
                    assert aV < 0;
                    double bV = Math.log(model.getEmissionMatrix().get(lastState).get(observedSequence.get(i)));
                    assert bV < 0;
                    prob = deltaV+aV+bV;
                    if (prob < mpProp) {
                        mpLastState = lastState;
                        mpProp = prob;
                    }
                }

                assert mpLastState != null && mpProp != 0;
                psiInnerMap.put(currentState, mpLastState);
                deltaInnerMap.put(currentState, mpProp);
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