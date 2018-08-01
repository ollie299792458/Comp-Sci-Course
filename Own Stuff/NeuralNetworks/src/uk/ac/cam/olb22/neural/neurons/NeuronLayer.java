package uk.ac.cam.olb22.neural.neurons;

public class NeuronLayer {
    private final int id;

    private final Neuron[] neurons;

    public NeuronLayer(int id, Neuron[] neurons) {
        this.id = id;
        this.neurons = neurons;
    }

    public int getId() {
        return id;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public int size() {
        return neurons.length;
    }

    public double[] calculateOutput(double[] inputActivations) {
        double[] outputs = new double[size()];
        for (int i = 0; i < size(); i++) {
            outputs[i] = neurons[i].calculateOutput(inputActivations);
        }
        return outputs;
    }
}
