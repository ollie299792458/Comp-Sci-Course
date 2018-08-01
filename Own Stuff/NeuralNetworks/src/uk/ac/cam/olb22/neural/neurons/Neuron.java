package uk.ac.cam.olb22.neural.neurons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Neuron {

    private final int id;

    private double bias = 0;

    private double[] weights;

    private final int inputs;

    private final Function<Double, Double> activationFunction;

    public Neuron(int id, int inputs, Function<Double, Double> activationFunction) {
        this.inputs = inputs;
        this.weights = new double[inputs];
        this.activationFunction = activationFunction;
        this.id = id;
    }

    public double calculateOutput(double[] inputActivations) {
        if(inputActivations.length != inputs) {
            throw new NeuralNetException("Incorrect number of input neurons");
        }

        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputActivations[i];
        }
        return activationFunction.apply(sum);
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void setWeight(int id, double weight) {
        weights[id] = weight;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getWeight(int id) {
        return weights[id];
    }

    public double getBias() {
        return bias;
    }

    public int getId() {
        return id;
    }
}
