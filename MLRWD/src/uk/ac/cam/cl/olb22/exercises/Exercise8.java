package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.markov_models.DiceRoll;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.DiceType;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrwd.exercises.markov_models.IExercise8;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by oliver on 16/02/17.
 */
public class Exercise8 implements IExercise8 {
    @Override
    public List<DiceType> viterbi(HiddenMarkovModel<DiceRoll, DiceType> model, List<DiceRoll> observedSequence) {
        return return viterbiGen(model, observedSequence);
    }

    private <T, U> List<T> viterbiGen(HiddenMarkovModel<U, T> model, List<U> observedSequence, T[] statesT, U[] statesU) {
        List<Map<T, T>> psi = new LinkedList<>();
        List<Map<T, Double>> delta = new LinkedList<>();

        initialise(model, delta, observedSequence.get(0), statesT);



        return null;
    }

    private <U, T> void initialise(HiddenMarkovModel<U, T> model, List<Map<T, Double>> delta, U initialRoll, T[] statesT) {
        Map<T, Double> initialProbs = model.getInitialProbs();
        Map<T, Double> initialMap = new HashMap<>();

        for (T state : statesT) {
            double deltaf = initialProbs.get(state) * model.getEmissionMatrix().get(state).get(initialRoll);
            deltaf = Math.log(deltaf);
            initialMap.put(state, deltaf);
        }
    }

    @Override
    public Map<List<DiceType>, List<DiceType>> predictAll(HiddenMarkovModel<DiceRoll, DiceType> model, List<Path> testFiles) throws IOException {
        return null;
    }

    @Override
    public double precision(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        return 0;
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
