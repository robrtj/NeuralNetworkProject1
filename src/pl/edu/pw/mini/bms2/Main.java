package pl.edu.pw.mini.bms2;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;

public class Main {

    public static void main(String[] args) {
        NeuralNetwork nn = new MultiLayerPerceptron(5, 10, 1);
    }
}
