package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.DiceRoll;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.DiceType;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.IExercise8;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by oliver on 16/02/17.
 */
public class Exercise8 implements IExercise8 {
    @Override
    public List<DiceType> viterbi(HiddenMarkovModel<DiceRoll, DiceType> model, List<DiceRoll> observedSequence) {
        return viterbiGen(model, observedSequence, DiceType.values(), DiceRoll.values());
    }

    private <T, U> List<T> viterbiGen(HiddenMarkovModel<U, T> model, List<U> observedSequence, T[] statesT, U[] statesU) {
        List<Map<T, T>> psi = new LinkedList<>();
        List<Map<T, Double>> delta = new LinkedList<>();

        initialise(model, delta, observedSequence.get(0), statesT);

        mainStep(model, delta, psi, observedSequence, statesT);

        List<T> result = grandFinale(model, observedSequence.size(), delta, psi, statesT);

        assert result.size() == observedSequence.size();

        return result;
    }

    private <T, U> List<T> grandFinale(HiddenMarkovModel<U, T> model, int length, List<Map<T, Double>> delta, List<Map<T, T>> psi, T[] statesT) {
        List<T> result = new LinkedList<T>();

        T endState = null;
        T bestPreEndState = null;
        double biggestProb = 0;
        for (T potentialEndState : statesT) {
            for (T lastState : statesT) {
                double newProb = Math.log(model.getTransitionMatrix().get(lastState).get(potentialEndState));
                newProb += delta.get(length - 1).get(lastState);
                if (newProb < biggestProb) {
                    bestPreEndState = lastState;
                    endState = potentialEndState;
                    biggestProb = newProb;
                }
            }
        }
        Map<T, Double> innerDeltaMap = new HashMap<>();
        innerDeltaMap.put(endState, biggestProb);
        Map<T, T> innerPsiMap = new HashMap<>();
        innerPsiMap.put(endState, bestPreEndState);
        delta.add(innerDeltaMap);
        psi.add(innerPsiMap);

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
    public Map<List<DiceType>, List<DiceType>> predictAll(HiddenMarkovModel<DiceRoll, DiceType> model, List<Path> testFiles) throws IOException {
        return null;
    }

    @Override
    public double precision(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return precisionGen(true2PredictedMap, DiceType.values());
    }

    private <T> double precisionGen(Map<List<T>, List<T>> true2PredictedMap, T[] values) {
        for (Map.Entry<List<T>, List<T>> entry : true2PredictedMap.entrySet()) {
            precisionGenSub(entry.getKey(), entry.getValue());
        }
    }

    private <T> void precisionGenSub(List<T> key, List<T> value) {

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