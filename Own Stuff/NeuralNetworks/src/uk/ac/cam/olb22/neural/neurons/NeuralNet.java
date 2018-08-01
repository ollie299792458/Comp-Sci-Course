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


    private double[] calculateOutput() {
        return outputLayer.calculateOutput();
    }

    private void reset() {
        outputLayer.reset();
        inputLayer.reset();
        for (NeuronLayer layer:hiddenLayers) {
            layer.reset();
        }
    }
}
