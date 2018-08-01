package uk.ac.cam.olb22.neural.neurons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Neuron {

    private final int id;

    private double bias = 0;

    private double[] weights;

    private final Neuron[] incomingNeurons;

    private final Function<Double, Double> activationFunction;

    private boolean outputFresh = false;

    private double output;

    public Neuron(int id, Neuron[] incomingNeurons, Function<Double, Double> activationFunction) {
        this.incomingNeurons = incomingNeurons;
        this.weights = new double[incomingNeurons.length];
        this.activationFunction = activationFunction;
        this.id = id;
    }

    public double calculateOutput() {
        if (!outputFresh) {
            double sum = 0;
            for (int i = 0; i < weights.length; i++) {
                sum += weights[i] * (incomingNeurons[i].calculateOutput());
            }
            output = activationFunction.apply(sum);
            outputFresh = true;
        }
        return output;
    }

    public void reset() {
        outputFresh = false;
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
