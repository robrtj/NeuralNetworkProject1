package pl.edu.pw.mini.bms2;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static Scanner in;

    public static void main(String[] args) {
//        int hiddenLayers, inputNeurons, outputNeurons;
//        List<Integer> neuronsInLayers = new ArrayList<>();
//        in = new Scanner(System.in);
//
//        //problem type setting
//        System.out.println("Choose problem type:\n" +
//                        "0:\tRegression\n" +
//                        "1:\tClustering"
//        );
//        int problemType = in.nextInt();
//
////        add input layer
////        System.out.println("Number of input neurons");
////        inputNeurons = in.nextInt();
//        neuronsInLayers.add(problemType + 1);
//
//        System.out.println("Number of hidden layers");
//        hiddenLayers = in.nextInt();
//
//        for (int i = 1; i <= hiddenLayers; i++) {
//            System.out.println("Number of neurons in hidden layer " + i);
//            int tmp = in.nextInt();
//            neuronsInLayers.add(tmp);
//        }
//
////        add output layer
////        System.out.println("Number of output neurons");
////        outputNeurons = in.nextInt();
//        neuronsInLayers.add(1);
//
//        //bias setting
//        System.out.println("Use bias? (Y/N)");
//        String biasString;
//        boolean bias;
//
//        while(true) {
//            biasString = in.next();
//            if (biasString.equals("Y") || biasString.equals("y")) {
//                bias = true;
//                break;
//            } else if (biasString.equals("N") || biasString.equals("n"))  {
//                bias = false;
//                break;
//            } else {
//                System.out.println("Try again. (Y/N)");
//            }
//        }
//
//        //activation function setting
//        System.out.println("Choose Activation function:\n" +
//                            "0:\tLINEAR\n" +
//                            "1:\tRAMP\n" +
//                            "2:\tSTEP\n" +
//                            "3:\tSIGMOID\n" +
//                            "4:\tTANH\n" +
//                            "5:\tGAUSSIAN\n" +
//                            "6:\tTRAPEZOID\n" +
//                            "7:\tSGN\n" +
//                            "8:\tSIN\n" +
//                            "9:\tLOG\n"
//            );
//        String chosen = in.next().toUpperCase();
//        NeuronProperties np = new NeuronProperties(TransferFunctionType.valueOf(chosen), bias);
//
//        //Setting learning ratio, momentum ratio, max iterations
//        System.out.println("Set learning ratio:");
//        Double learningRatio = in.nextDouble();
//
//        System.out.println("Set momentum ratio:");
//        Double momentumRatio = in.nextDouble();
//
//        System.out.println("Set max iterations:");
//        int maxIterations = in.nextInt();
//
//        System.out.println("Set max error:");
//        double maxError = in.nextDouble();
//
//        MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();
//        momentumBackpropagation.setLearningRate(learningRatio);
//        momentumBackpropagation.setMomentum(momentumRatio);
//        momentumBackpropagation.setMaxIterations(maxIterations);
//        momentumBackpropagation.setMaxError(maxError);
//
//        DataSet trainingSet = LoadTrainingSet(problemType);
//
//        //creating network
//        NeuralNetwork nn = new MultiLayerPerceptron(neuronsInLayers, np);
//        nn.setLearningRule(momentumBackpropagation);
//
//        //learn
//        nn.learn(trainingSet);
//
//        //after learning test network
//        TestNetwork(nn);

        List<Integer> neuronsInLayers = new ArrayList<>();
        neuronsInLayers.add(1);
        neuronsInLayers.add(10);
        neuronsInLayers.add(5);
        neuronsInLayers.add(1);
        NeuronProperties np = new NeuronProperties(TransferFunctionType.TANH, true);
        NeuralNetwork myNeutralNetwork = new MultiLayerPerceptron(neuronsInLayers, np);

        MomentumBackpropagation myMomentumBackpropagation = new MomentumBackpropagation();
        myMomentumBackpropagation.setLearningRate(0.0005d);
        myMomentumBackpropagation.setMomentum(0.9d);
        myMomentumBackpropagation.setMaxIterations(1000);
        myMomentumBackpropagation.setMaxError(0.001d);
        myNeutralNetwork.setLearningRule(myMomentumBackpropagation);

        DataSet trainingSet = LoadTrainingSet(0);

        myNeutralNetwork.learn(trainingSet);
        TestNetwork(myNeutralNetwork);

//        //NeuralNetwork test = new Perceptron(2, 1);
//        List<Integer> layers = new ArrayList<>();
//        //layers.add(1)
//        NeuralNetwork test = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
//        test.learn(trainingSet);
//        TestNetwork(test);
//        test = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
//        DataSet dataSet = new DataSet(2, 1);
//        dataSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
//        dataSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
//        dataSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
//        dataSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
//        test.learn(dataSet);
//        test.setInput(0, 0);
//        test.calculate();
//        System.out.println("test:" + test.getOutput()[0]);
//        test.setInput(0, 1);
//        test.calculate();
//        System.out.println("test:" + test.getOutput()[0]);
//        test.setInput(1, 0);
//        test.calculate();
//        System.out.println("test:" + test.getOutput()[0]);
//        test.setInput(1, 1);
//        test.calculate();
//        System.out.println("test:" + test.getOutput()[0]);
    }

    private static DataSet LoadTrainingSet(int problemType) {
        boolean noFileError = true;
        DataSet trainingSet = null;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of training set file:");
//            String trainingSetFilePath = "./y=x.train.csv";//in.next();
            String trainingSetFilePath = "./data.xsq.train.csv";//in.next();
            trainingSet = new DataSet(problemType + 1, 1);

            CSVReader reader = null;
            try {
                reader = new CSVReader(new FileReader(trainingSetFilePath));

                String[] nextLine;

                //read headers
                nextLine = reader.readNext();
                System.out.println(nextLine);
                while ((nextLine = reader.readNext()) != null) {
                    int length = nextLine.length;
                    double[] input = new double[length - 1];

                    for (int i = 0; i < length - 1; i++) {
                        input[i] = Double.parseDouble(nextLine[i]);
                    }

                    double[] output = new double[]{Double.parseDouble(nextLine[length - 1])};

                    trainingSet.addRow(input, output);
                }
            } catch (Exception e) {
                e.printStackTrace();
                noFileError = true;
            }
        }

        return trainingSet;
    }

    private static void TestNetwork(NeuralNetwork nnet) {
        boolean noFileError = true;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of test set file:\n");
//            String testSetFilePath = "y=x.test.csv";//in.next();
            String testSetFilePath = "data.xsq.test.csv";//in.next();
            CSVReader reader = null;
            CSVWriter writer = null;
            try {
                reader = new CSVReader(new FileReader(testSetFilePath));
                writer = new CSVWriter(new FileWriter("y=x.ans.csv"), '\t', '\'');

                FileWriter fr = new FileWriter("abc.csv");

                String[] nextLine;

                //read headers
                nextLine = reader.readNext();
                System.out.println(nextLine);
                while ((nextLine = reader.readNext()) != null) {
                    int length = nextLine.length;
                    double[] input = new double[length];

                    for (int i = 0; i < length; i++) {
                        input[i] = Double.parseDouble(nextLine[i]);
                    }
                    nnet.setInput(input);
                    nnet.calculate();
                    double[] networkOutput = nnet.getOutput();
                    System.out.print("Input: " + Arrays.toString(input));

//                    writer.writeNext(Arrays.toString(input) + "," + Arrays.toString(networkOutput));
                    for (int i = 0; i < length; i++) {
                        if(i>0) {
                            fr.write(",");
                        }
                        fr.write(String.valueOf(input[i]));
                    }
                    fr.write(",");
                    fr.write(String.valueOf(networkOutput[0]) + "\n");
                    System.out.println(" Output: " + Arrays.toString(networkOutput));
                }
                writer.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
                noFileError = true;
            }
        }
    }
}
