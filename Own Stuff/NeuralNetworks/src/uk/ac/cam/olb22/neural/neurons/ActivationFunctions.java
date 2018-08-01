package uk.ac.cam.olb22.neural.neurons;

import java.util.function.Function;

public class ActivationFunctions {
    public static final Function<Double, Double> reLu = d -> Math.max(0, d);
    //public static final Function<Double, Double> softmax;
}

