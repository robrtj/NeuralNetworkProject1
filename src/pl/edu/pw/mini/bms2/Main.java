package pl.edu.pw.mini.bms2;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int hiddenLayers, inputNeurons;
        List<Integer> neuronsInLayers = new ArrayList<Integer>();
        Scanner in = new Scanner(System.in);

        System.out.println("Number of input neurons");
        inputNeurons = in.nextInt();
        neuronsInLayers.add(inputNeurons);
        System.out.println("Number of hidden layers");
        hiddenLayers = in.nextInt();

        for (int i = 1; i <= hiddenLayers; i++) {
            System.out.println("Number of neurons in hidden layer " + i);
            int tmp = in.nextInt();
            neuronsInLayers.add(tmp);
        }

        //add output layer
        neuronsInLayers.add(1);

        NeuralNetwork nn = new MultiLayerPerceptron(neuronsInLayers);
    }
}
