package pl.edu.pw.mini.bms2;

import au.com.bytecode.opencsv.CSVReader;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.RangeNormalizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static Scanner in;
    private static double[] maxIn, maxOut; // contains max values for in and out columns
    private static double[] minIn, minOut; // contains min values for in and out columns
    private static final double highLimit = 1.0;
    private static double lowLimit = 0.0;

    public static void main(String[] args) {

        NeutralNetworksManager neutralNetworksManager = new NeutralNetworksManager();
        NeuralNetwork myNeutralNetwork = neutralNetworksManager.run();

        lowLimit = neutralNetworksManager.getLowLimit();

        int problemType = neutralNetworksManager.getProblemType();
        if(problemType == 0){
            DataSet trainingSet = LoadTrainingSet(1, 1);
            trainingSet.shuffle();  //Randomly permutes set

            findMaxAndMinVectors(trainingSet);
            RangeNormalizer normalizer = new RangeNormalizer(lowLimit, 1);
            normalizer.normalize(trainingSet);

            myNeutralNetwork.learn(trainingSet);
            TestNetwork(myNeutralNetwork, problemType);

        }else if(problemType == 1){

        }

    }

    private static DataSet LoadTrainingSet(int inputSize, int outputSize) {
        boolean noFileError = true;
        DataSet trainingSet = null;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of training set file:");
            String trainingSetFilePath = "./tests/data.xsq.train.csv"; //in.next();
            trainingSet = new DataSet(inputSize, outputSize);

            CSVReader reader = null;
            try {
                reader = new CSVReader(new FileReader(trainingSetFilePath));

                String[] nextLine;

                //read headers
                nextLine = reader.readNext();
                System.out.println(nextLine);
                while ((nextLine = reader.readNext()) != null) {
                    int length = nextLine.length;
                    double[] input = new double[inputSize];

                    for (int i = 0; i < inputSize; i++) {
                        input[i] = Double.parseDouble(nextLine[i]);
                    }

                    double[] output = new double[outputSize];

                    for (int i = inputSize-1; i < inputSize+outputSize-1; i++) {
                        output[i] = Double.parseDouble(nextLine[i+1]);
                    }

                    trainingSet.addRow(input, output);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                noFileError = false;
            } catch (IOException e) {
                e.printStackTrace();
                noFileError = true;
            }
        }

        return trainingSet;
    }

    private static void TestNetwork(NeuralNetwork nnet, int problemType) {
        boolean noFileError = true;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of test set file:");
            String testSetFilePath = "tests/data.xsq.test.csv";
            CSVReader reader = null;

            try {
                reader = new CSVReader(new FileReader(testSetFilePath));

                FileWriter fr = new FileWriter("tests/abc.ans.csv");

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
                    double[] normalizedInput = normalizeToRange(input, minIn, maxIn);
                    nnet.setInput(normalizedInput);
                    nnet.calculate();
                    double[] networkOutput = nnet.getOutput();

                    System.out.print("Input: " + Arrays.toString(normalizedInput));
                    System.out.println("out: " + Arrays.toString(networkOutput));

                    networkOutput = denormalizeToRange(networkOutput, minOut, maxOut);

                    System.out.print("Input: " + Arrays.toString(input));

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

                fr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                noFileError = false;
            } catch (IOException e) {
                e.printStackTrace();
                noFileError = true;
            }
        }
    }

    private static void findMaxAndMinVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();

        maxIn = new double[inputSize];
        minIn = new double[inputSize];

        for(int i=0; i<inputSize; i++) {
            maxIn[i] = Double.MIN_VALUE;
            minIn[i] = Double.MAX_VALUE;
        }

        maxOut = new double[outputSize];
        minOut = new double[outputSize];

        for(int i=0; i<outputSize; i++) {
            maxOut[i] = Double.MIN_VALUE;
            minOut[i] = Double.MAX_VALUE;
        }

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i];
                }
                if (input[i] < minIn[i]) {
                    minIn[i] = input[i];
                }
            }

            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < outputSize; i++) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i];
                }
                if (output[i] < minOut[i]) {
                    minOut[i] = output[i];
                }
            }

        }

        System.out.println("min max");
        for (int i = 0; i < minIn.length; i++) {
            System.out.print(minIn[i] + ",");
        }
        System.out.println();
        for (int i = 0; i < maxIn.length; i++) {
            System.out.print(maxIn[i] + ",");
        }
        System.out.println();
        for (int i = 0; i < minOut.length; i++) {
            System.out.print(minOut[i] + ",");
        }
        System.out.println();
        for (int i = 0; i < maxOut.length; i++) {
            System.out.print(maxOut[i] + ",");
        }
        System.out.println();
    }

    private static void denormalize(DataSet dataSet) {
        for (DataSetRow row : dataSet.getRows()) {
            double[] denormalizedInput = denormalizeToRange(row.getInput(), minIn, maxIn);
            row.setInput(denormalizedInput);

            if (dataSet.isSupervised()) {
                double[] denormalizedOutput = denormalizeToRange(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(denormalizedOutput);
            }

        }

    }

    private static double[] denormalizeToRange(double[] vector, double[] min, double[] max) {
        double[] denormalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            denormalizedVector[i] = ((vector[i] - lowLimit) * (max[i] - min[i]))/(highLimit - lowLimit) + min[i];
        }

        return denormalizedVector;
    }

    private static double[] normalizeToRange(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = ((vector[i] - min[i]) / (max[i] - min[i])) * (highLimit - lowLimit) + lowLimit ;
        }

        return normalizedVector;
    }
}
