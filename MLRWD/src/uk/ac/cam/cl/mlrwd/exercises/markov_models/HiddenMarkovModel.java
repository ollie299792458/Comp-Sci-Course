package uk.ac.cam.cl.mlrwd.exercises.markov_models;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class HiddenMarkovModel<T, U> {

	protected Map<U, Map<U, Double>> transitions;
	protected Map<U, Map<T, Double>> emissions;
	protected Map<U, Double> stateProbs;

	/**
	 * Constructs a HiddenMarkovModel object from a transition matrix, emission
	 * matrix, and initial probability list. Not all transitions or emissions
	 * have to be defined, but there must be at least one transition and
	 * emission from each hidden states, and probabilities must be positive and
	 * total more than zero and less than {@link Double#MAX_VALUE}. There should
	 * be no null values anywhere in the parameters.
	 * 
	 * @param transitionMatrix
	 *            {@link Map}<{@link U}, {@link Map}<{@link U}, {@link Double}>>
	 *            The transition matrix
	 * @param emissionMatrix
	 *            {@link Map}<{@link U}, {@link Map}<{@link T}, {@link Double}>>
	 *            The emission matrix
	 * @param initialProbs
	 *            {@link Map}<{@link U}, {@link Double}> The initial
	 *            probabilities
	 */
	public HiddenMarkovModel(Map<U, Map<U, Double>> transitionMatrix, Map<U, Map<T, Double>> emissionMatrix,
			Map<U, Double> initialProbs) {

		Objects.requireNonNull(transitionMatrix, "transition matrix cannot be null");
		Objects.requireNonNull(emissionMatrix, "emission matrix cannot be null");
		Objects.requireNonNull(initialProbs, "initial probabilities must be defined");
		if (transitionMatrix.containsValue(null)) {
			throw new IllegalArgumentException("transition matrix must not contain null values");
		}
		if (emissionMatrix.containsValue(null)) {
			throw new IllegalArgumentException("emission matrix must not contain null values");
		}
		if (initialProbs.containsValue(null)) {
			throw new IllegalArgumentException("initial probabilities must not contain null values");
		}
		if (transitionMatrix.containsKey(null)) {
			throw new IllegalArgumentException("transition matrix must not contain null keys");
		}
		if (emissionMatrix.containsKey(null)) {
			throw new IllegalArgumentException("emission matrix must not contain null keys");
		}
		if (initialProbs.containsKey(null)) {
			throw new IllegalArgumentException("initial probabilities must not contain null keys");
		}

		transitions = new HashMap<U, Map<U, Double>>();
		emissions = new HashMap<U, Map<T, Double>>();
		stateProbs = new HashMap<U, Double>();

		Set<U> states = new HashSet<U>();
		states.addAll(transitionMatrix.keySet());
		states.addAll(emissionMatrix.keySet());
		states.addAll(initialProbs.keySet());

		if (states.isEmpty()) {
			throw new IllegalArgumentException("some internal state must exist");
		}
		if (states.size() != transitionMatrix.size()) {
			throw new IllegalArgumentException("transitions must be defined for all hidden states");
		}
		if (states.size() != emissionMatrix.size()) {
			throw new IllegalArgumentException("emissions must be defined for all hidden states");
		}

		double totalInitialProb = 0.0;
		for (Double prob : initialProbs.values()) {
			if (prob < 0.0) {
				throw new IllegalArgumentException("probabilities cannot be negative");
			}
			totalInitialProb += prob;
		}
		if (totalInitialProb == 0.0) {
			throw new IllegalArgumentException("probabilities cannot sum to 0");
		}
		if (totalInitialProb >= Double.MAX_VALUE) {
			throw new IllegalArgumentException("sum of probabilities must not overflow");
		}
		for (Entry<U, Double> prob : initialProbs.entrySet()) {
			if (prob.getValue() > 0.0) {
				stateProbs.put(prob.getKey(), prob.getValue() / totalInitialProb);
			}
		}
		for (U state : states) {
			Map<U, Double> stateTransitions = transitionMatrix.get(state);
			if (stateTransitions.containsValue(null)) {
				throw new IllegalArgumentException("transition matrix submatrices must not contain null values");
			}
			if (stateTransitions.containsKey(null)) {
				throw new IllegalArgumentException("transition matrix submatrices must not contain null keys");
			}
			double totalProb = 0.0;
			for (Double prob : stateTransitions.values()) {
				if (prob < 0.0) {
					throw new IllegalArgumentException("probabilities cannot be negative");
				}
				totalProb += prob;
			}
			if (totalProb == 0.0) {
				throw new IllegalArgumentException("probabilities cannot sum to 0");
			}
			if (totalProb >= Double.MAX_VALUE) {
				throw new IllegalArgumentException("sum of probabilities must not overflow");
			}
			Map<U, Double> transitionProbs = new HashMap<U, Double>();
			for (Entry<U, Double> transitionProb : stateTransitions.entrySet()) {
				double prob = transitionProb.getValue() / totalProb;
				if (prob > 0.0) {
					transitionProbs.put(transitionProb.getKey(), prob);
				}
			}
			transitions.put(state, transitionProbs);

			Map<T, Double> stateEmissions = emissionMatrix.get(state);
			if (stateEmissions.containsValue(null)) {
				throw new IllegalArgumentException("emission matrix submatrices must not contain null values");
			}
			if (stateEmissions.containsKey(null)) {
				throw new IllegalArgumentException("emission matrix submatrices must not contain null keys");
			}
			totalProb = 0.0;
			for (Double prob : stateEmissions.values()) {
				if (prob < 0.0) {
					throw new IllegalArgumentException("probabilities cannot be negative");
				}
				totalProb += prob;
			}
			if (totalProb == 0.0) {
				throw new IllegalArgumentException("probabilities cannot sum to 0");
			}
			if (totalProb >= Double.MAX_VALUE) {
				throw new IllegalArgumentException("sum of probabilities must not overflow");
			}
			Map<T, Double> emissionProbs = new HashMap<T, Double>();
			for (Entry<T, Double> emissionProb : stateEmissions.entrySet()) {
				double prob = emissionProb.getValue() / totalProb;
				if (prob > 0.0) {
					emissionProbs.put(emissionProb.getKey(), prob);
				}
			}
			emissions.put(state, emissionProbs);
		}
	}

	/**
	 * Constructs a HiddenMarkovModel object from a transition matrix, emission
	 * matrix, initial probability list, list of hidden states, and list of
	 * visible states. All possible transitions and emissions must be defined,
	 * and probabilities must be positive and total more than zero and less than
	 * {@link Double#MAX_VALUE}. There should be no null values anywhere in the
	 * parameters.
	 * 
	 * @param transitionMatrix
	 *            <code>double[][]</code> The transition matrix
	 * @param emissionMatrix
	 *            <code>double[][]</code> The emission matrix
	 * @param output
	 *            <code>double[]</code> The visible states corresponding to each
	 *            column in the emission matrix
	 * @param hiddenType
	 *            <code>double[]</code> The hidden states corresponding to each
	 *            row in the emission and transition matrices
	 * @param initialProbs
	 *            <code>double[]</code> The initial probabilities
	 */
	public HiddenMarkovModel(double[][] transitionMatrix, double[][] emissionMatrix, T[] output, U[] hiddenType,
			double[] initialProbs) {
		Objects.requireNonNull(transitionMatrix);
		Objects.requireNonNull(emissionMatrix);
		Objects.requireNonNull(output);
		Objects.requireNonNull(hiddenType);
		Objects.requireNonNull(initialProbs);

		int length = hiddenType.length;

		if (length != transitionMatrix.length) {
			throw new IllegalArgumentException("transition matrix must contain transitions from all states");
		}
		if (length != emissionMatrix.length) {
			throw new IllegalArgumentException("emission matrix must contain emissions from all states");
		}
		if (length != initialProbs.length) {
			throw new IllegalArgumentException("initial probabilities must be defined for all states");
		}

		transitions = new HashMap<U, Map<U, Double>>();
		emissions = new HashMap<U, Map<T, Double>>();
		stateProbs = new HashMap<U, Double>();

		int i = length;
		while (--i >= 0) {
			double[] stateTransitions = transitionMatrix[i];
			double[] stateEmissions = emissionMatrix[i];
			Objects.requireNonNull(stateTransitions, "transitions must be defined for all states");
			Objects.requireNonNull(stateEmissions, "emissions must be defined for all states");
			if (length != stateTransitions.length) {
				throw new IllegalArgumentException("transitions must be defined to all other states");
			}
			if (output.length != stateEmissions.length) {
				throw new IllegalArgumentException("emissions must be defined to all output states");
			}
			Map<U, Double> transitionRow = new HashMap<U, Double>();
			double total = 0.0;
			int j = length;
			while (--j >= 0) {
				if (stateTransitions[j] < 0) {
					throw new IllegalArgumentException("probabilities cannot be negative");
				}
				total += stateTransitions[j];
			}
			if (total == 0.0) {
				throw new IllegalArgumentException("probabilities cannot sum to 0");
			}
			if (total >= Double.MAX_VALUE) {
				throw new IllegalArgumentException("sum of probabilities must not overflow");
			}
			j = length;
			while (--j >= 0) {
				double prob = stateTransitions[j] / total;
				if (prob > 0.0) {
					transitionRow.put(hiddenType[j], prob);
				}
			}
			transitions.put(hiddenType[i], transitionRow);

			Map<T, Double> emissionRow = new HashMap<T, Double>();
			total = 0.0;
			j = output.length;
			while (--j >= 0) {
				if (stateEmissions[j] < 0) {
					throw new IllegalArgumentException("probabilities cannot be negative");
				}
				total += stateEmissions[j];
			}
			if (total == 0.0) {
				throw new IllegalArgumentException("probabilities cannot sum to 0");
			}
			if (total >= Double.MAX_VALUE) {
				throw new IllegalArgumentException("sum of probabilities must not overflow");
			}
			j = output.length;
			while (--j >= 0) {
				double prob = stateEmissions[j] / total;
				if (prob > 0.0) {
					emissionRow.put(output[j], prob);
				}
			}
			emissions.put(hiddenType[i], emissionRow);
			stateProbs.put(hiddenType[i], initialProbs[i]);
		}
	}

	/**
	 * Randomly generates a hidden and observed sequence with the given length.
	 * Stores the sequence in the buffers.
	 * 
	 * @param observedBuffer
	 *            {@link List}<{@link T}> An empty buffer to take observed
	 *            values, which can be null, but must not already contain items
	 * @param hiddenBuffer
	 *            {@link List}<{@link T}> An empty buffer to take hidden values,
	 *            which can be null, but must not already contain items
	 * @param runLength
	 *            <code>int</code> The number of iterations to perform
	 */
	public void randomSequence(List<T> observedBuffer, List<U> hiddenBuffer, int runLength) {
		if (hiddenBuffer != null && !hiddenBuffer.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (observedBuffer != null && !observedBuffer.isEmpty()) {
			throw new IllegalArgumentException();
		}
		U hiddenState = null;
		int i = -1;
		while (++i < runLength) {
			Map<U, Double> row = hiddenState == null ? stateProbs : transitions.get(hiddenState);
			double randomValue = Math.random();
			for (Entry<U, Double> potentialState : row.entrySet()) {
				hiddenState = potentialState.getKey();
				if ((randomValue -= potentialState.getValue()) <= 0) {
					break;
				}
			}
			if (hiddenBuffer != null) {
				hiddenBuffer.add(hiddenState);
			}
			if (observedBuffer != null) {
				T observedState = null;
				Map<T, Double> emissionProbs = emissions.get(hiddenState);
				randomValue = Math.random();
				for (Entry<T, Double> emissionProb : emissionProbs.entrySet()) {
					if ((randomValue -= emissionProb.getValue()) <= 0) {
						observedState = emissionProb.getKey();
						break;
					}
				}
				observedBuffer.add(observedState);
			}
		}
	}

	/**
	 * Get a set of all possible hidden states.
	 * 
	 * @return {@link Set}<{@link U}> The hidden states
	 */
	public Set<U> getHiddenStates() {
		return Collections.unmodifiableSet(transitions.keySet());
	}

	/**
	 * Get a set of all possible transitions from a given state and their
	 * probabilities.
	 * 
	 * @param fromState
	 *            {@link U} The state to get transitions from
	 * @return {@link Map}<{@link U}, {@link Double}> Possible transitions and
	 *         probabilities
	 */
	public Map<U, Double> getPossibleTransitions(U fromState) {
		Objects.requireNonNull(fromState, "state should not be null");
		return Collections.unmodifiableMap(transitions.get(fromState));
	}

	/**
	 * Get the transition matrix (non-modifiable).
	 * 
	 * @return {@link Map}<{@link U}, {@link Map}<{@link U}, {@link Double}>>
	 *         Transition matrix
	 */
	public Map<U, Map<U, Double>> getTransitionMatrix() {
		Map<U, Map<U, Double>> transitionMap = new HashMap<U, Map<U, Double>>();
		for (U state : transitions.keySet()) {
			transitionMap.put(state, getPossibleTransitions(state));
		}
		return Collections.unmodifiableMap(transitionMap);
	}

	/**
	 * Get a set of all possible emissions from a given state and their
	 * probabilities.
	 * 
	 * @param fromState
	 *            {@link U} The state to get emissions from
	 * @return {@link Map}<{@link T}, {@link Double}> Possible emissions and
	 *         probabilities
	 */
	public Map<T, Double> getPossibleEmissions(U fromState) {
		Objects.requireNonNull(fromState, "state should not be null");
		return Collections.unmodifiableMap(emissions.get(fromState));
	}

	/**
	 * Get the emission matrix (non-modifiable).
	 * 
	 * @return {@link Map}<{@link U}, {@link Map}<{@link T}, {@link Double}>>
	 *         Emission matrix
	 */
	public Map<U, Map<T, Double>> getEmissionMatrix() {
		Map<U, Map<T, Double>> emissionMap = new HashMap<U, Map<T, Double>>();
		for (U state : transitions.keySet()) {
			emissionMap.put(state, getPossibleEmissions(state));
		}
		return Collections.unmodifiableMap(emissionMap);
	}

	/**
	 * Get a set of all initial state probabilities.
	 * 
	 * @return {@link Map}<{@link U}, {@link Double}> Possible start states with
	 *         probabilities
	 */
	public Map<U, Double> getInitialProbs() {
		return Collections.unmodifiableMap(stateProbs);
	}
}
