package uk.ac.cam.olb22.neural.neurons;

public class NeuralNet {
    //private final int id;

    private final NeuronLayer inputLayer;

    private final NeuronLayer[] hiddenLayers;

    private final NeuronLayer outputLayer;

    public NeuralNet(NeuronLayer inputLayer, NeuronLayer[] hiddenLayers, NeuronLayer outputLayer) {
        this.inputLayer = inputLayer;
        this.hiddenLayers = hiddenLayers;
        this.outputLayer = outputLayer;
    }


    private double[] calculateOutput(double[] inputs) {
        double[] outputs = inputLayer.calculateOutput(inputs);
        inputs = outputs;
        for (int i = 0; i < hiddenLayers.length; i++) {
            outputs = hiddenLayers[i].calculateOutput(inputs);
            inputs = outputs;
        }
        return outputLayer.calculateOutput(inputs);
    }
}
