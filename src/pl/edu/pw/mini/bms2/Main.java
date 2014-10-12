package pl.edu.pw.mini.bms2;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

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

        System.out.println("Use bias? (Y/N)");
        String biasString;
        boolean bias;

        while(true) {
            biasString = in.next();
            if (biasString.equals("Y") || biasString.equals("y")) {
                bias = true;
                break;
            } else if (biasString.equals("N") || biasString.equals("n"))  {
                bias = false;
                break;
            } else {
                System.out.println("Try again. (Y/N)");
            }
        }

        NeuronProperties np = new NeuronProperties(TransferFunctionType.LINEAR, bias);
        NeuralNetwork nn = new MultiLayerPerceptron(neuronsInLayers, np);
    }
}
