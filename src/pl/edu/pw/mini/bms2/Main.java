package pl.edu.pw.mini.bms2;

import au.com.bytecode.opencsv.CSVReader;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
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

        //bias setting
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

        //activation function setting
        System.out.println("Choose Activation function:\n" +
                            "0:\tLINEAR\n" +
                            "1:\tRAMP\n" +
                            "2:\tSTEP\n" +
                            "3:\tSIGMOID\n" +
                            "4:\tTANH\n" +
                            "5:\tGAUSSIAN\n" +
                            "6:\tTRAPEZOID\n" +
                            "7:\tSGN\n" +
                            "8:\tSIN\n" +
                            "9:\tLOG\n"
            );
        String chosen = in.next().toUpperCase();
        NeuronProperties np = new NeuronProperties(TransferFunctionType.valueOf(chosen), bias);

        //Setting learning ratio, momentum ratio, max iterations
        System.out.println("Set learning ratio:");
        Double learningRatio = in.nextDouble();

        System.out.println("Set momentum ratio:");
        Double momentumRatio = in.nextDouble();

        System.out.println("Set max iterations:");
        int maxIterations = in.nextInt();

        MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();
        momentumBackpropagation.setLearningRate(learningRatio);
        momentumBackpropagation.setMomentum(momentumRatio);
        momentumBackpropagation.setMaxIterations(maxIterations);

        //problem type setting
        System.out.println("Choose problem type:\n" +
                        "0:\tRegression\n" +
                        "1:\tClustering"
        );
        int problemType = in.nextInt();

        System.out.println("Type location of training set file:\n");
        String trainingSetFilePath = in.next();
        DataSet traingSet = CreateTrainingSet(trainingSetFilePath, problemType);

        //creating network
        NeuralNetwork nn = new MultiLayerPerceptron(neuronsInLayers, np);
        nn.setLearningRule(momentumBackpropagation);
    }

    private static DataSet CreateTrainingSet(String trainingSetFilePath, int problemType) {
        DataSet trainingSet =  new DataSet(problemType + 1, 1);

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(trainingSetFilePath));
            String [] nextLine;

            //read headers
            System.out.println(reader.readNext().toString());
            while ((nextLine = reader.readNext()) != null) {
                int length = nextLine.length;
                double[] input = new double[length-1];

                for (int i = 0; i < length-1; i++) {
                    input[i] = Double.parseDouble(nextLine[i]);
                }

                double[] output = new double[]{Double.parseDouble(nextLine[length - 1])};

                trainingSet.addRow(input, output);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trainingSet;
    }
}
