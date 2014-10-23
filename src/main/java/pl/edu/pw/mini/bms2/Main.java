package pl.edu.pw.mini.bms2;

import au.com.bytecode.opencsv.CSVReader;
import javafx.print.Collation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
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
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    private static Scanner in;
    private static double[] maxIn, maxOut; // contains max values for in and out columns
    private static double[] minIn, minOut; // contains min values for in and out columns
    private static final double highLimit = 1.0;
    private static double lowLimit = 0.0;

    public static void main(String[] args) throws IOException {

        NeutralNetworksManager neutralNetworksManager = new NeutralNetworksManager();
        NeuralNetwork myNeutralNetwork = neutralNetworksManager.run();

        lowLimit = neutralNetworksManager.getLowLimit();

        int problemType = neutralNetworksManager.getProblemType();
        if(problemType == 0){
            DataSet trainingSet = Regression_LoadTrainingSet(1, 1);
            trainingSet.shuffle();  //Randomly permutes set

            findMaxAndMinVectors(trainingSet);
            RangeNormalizer normalizer = new RangeNormalizer(lowLimit, 1);
            normalizer.normalize(trainingSet);

            myNeutralNetwork.learn(trainingSet);
            Regression_TestNetwork(myNeutralNetwork, 1);

        }else if(problemType == 1){
            DataSet trainingSet = Classification_LoadTrainingSet(2, neutralNetworksManager.outputNeurons());
            trainingSet.shuffle();  //Randomly permutes set

            findMaxAndMinVectors(trainingSet);
            RangeNormalizer normalizer = new RangeNormalizer(lowLimit, 1);
            normalizer.normalize(trainingSet);

            myNeutralNetwork.learn(trainingSet);
            Classification_TestNetwork(myNeutralNetwork, neutralNetworksManager.outputNeurons());
        } else if(problemType == 2) {
            DataSet trainingSet = Regression_LoadTrainingSet(1, 1);
            trainingSet.shuffle();  //Randomly permutes set
            DataSet testSet = new DataSet(trainingSet.getInputSize(), trainingSet.getOutputSize());
            findMaxAndMinVectors(trainingSet);
            RangeNormalizer normalizer = new RangeNormalizer(lowLimit, 1);
            normalizer.normalize(trainingSet);

            int trainingSetSize = trainingSet.getRows().size();

            for (int i = trainingSetSize - 1; i > trainingSetSize / 2 ; i--) {
                testSet.addRow(trainingSet.getRowAt(i));
            }
            for (int i = trainingSetSize - 1; i > trainingSetSize / 2 ; i--) {
                trainingSet.removeRowAt(i);
            }

            FileWriter frTest = null;
            FileWriter frTrain = null;
            try {
                frTest = new FileWriter("tests/err_test_ans.csv");
                frTrain = new FileWriter("tests/err_train_ans.csv");

                int iterations = neutralNetworksManager.getIterations();

                Properties networkProperties = neutralNetworksManager.getNetworkProperties();
                networkProperties.setProperty("maxIterations", String.valueOf(10));
                neutralNetworksManager = new NeutralNetworksManager(networkProperties);
                myNeutralNetwork = neutralNetworksManager.createMLPBasedOnProperties();
                for (int i = 10; i <= iterations; i+=10) {
                    System.out.println(i);

                    myNeutralNetwork.learn(trainingSet);
                    Error_TestNetwork(myNeutralNetwork, testSet, frTest, i);
                    Error_TestNetwork(myNeutralNetwork, trainingSet, frTrain, i);
                }
            } catch (IOException e) {

            } finally {
                frTest.close();
                frTrain.close();
            }
        }

    }

    private static DataSet Regression_LoadTrainingSet(int inputSize, int outputSize) {
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

    private static DataSet Classification_LoadTrainingSet(int inputSize, int outputSize) {
        boolean noFileError = true;
        DataSet trainingSet = null;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of training set file:");
            String trainingSetFilePath = "./tests/data.train.csv"; //in.next();
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
                    for(int i=0; i<outputSize; ++i){
                        output[i] = 0;
                    }
                    int tmpOutput = Integer.parseInt(nextLine[inputSize]);
                    output[tmpOutput-1] = 1;

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

    private static void Regression_TestNetwork(NeuralNetwork nnet, int outputSize) {
        boolean noFileError = true;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of test set file:");
            String testSetFilePath = "tests/data.xsq.test.csv";
            CSVReader reader = null;

            try {
                reader = new CSVReader(new FileReader(testSetFilePath));

                FileWriter fr = new FileWriter("tests/reg_ans.csv");

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

    private static void Classification_TestNetwork(NeuralNetwork nnet, int outputSize) {
        boolean noFileError = true;

        while(noFileError) {
            noFileError = false;
            System.out.println("Type location of test set file:");
            String testSetFilePath = "tests/data.test.csv";
            CSVReader reader = null;

            try {
                reader = new CSVReader(new FileReader(testSetFilePath));

                FileWriter fr = new FileWriter("tests/cl_ans.csv");

                String[] nextLine;

                //read headers
                nextLine = reader.readNext();
                System.out.println(nextLine.toString());
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

                    double max = -Integer.MAX_VALUE;
                    int output = 0;
                    for(int i=0; i<networkOutput.length; ++i){
                        if(max < networkOutput[i]){
                            max = networkOutput[i];
                            output = i+1;
                        }
                    }

                    System.out.print("Input: " + Arrays.toString(normalizedInput));
                    System.out.println("out: " + Arrays.toString(networkOutput));

                    //networkOutput = denormalizeToRange(networkOutput, minOut, maxOut);

                    System.out.print("Input: " + Arrays.toString(input));

                    for (int i = 0; i < length; i++) {
                        if(i>0) {
                            fr.write(",");
                        }
                        fr.write(String.valueOf(input[i]));
                    }
                    fr.write(",");

                    fr.write(String.valueOf(output) + "\n");

                    System.out.println(" Output: " + output);
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

    private static void Error_TestNetwork(NeuralNetwork nnet, DataSet testSet, FileWriter fr, int iterations) throws IOException {

        Double diff = 0.0;

        for (DataSetRow row : testSet.getRows()) {

            double[] input = row.getInput();
            nnet.setInput(input);
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();

            //networkOutput = denormalizeToRange(networkOutput, minOut, maxOut);
            //double[] desireOutputDenormalized = denormalizeToRange(row.getDesiredOutput(), minOut, maxOut);

            diff += Math.abs(row.getDesiredOutput()[0] - networkOutput[0]);
            //System.out.println(desireOutputDenormalized[0] + " " + networkOutput[0]);
            //if(iterations == 99) {
                //fr.write(desireOutputDenormalized[0] + " " + networkOutput[0] + "\n");
            //}
        }

        diff /= testSet.getRows().size();
        fr.write(String.valueOf(iterations));
        fr.write(",");
        fr.write(diff.toString());
        fr.write("\n");
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
