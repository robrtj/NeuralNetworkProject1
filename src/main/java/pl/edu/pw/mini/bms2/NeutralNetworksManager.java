package pl.edu.pw.mini.bms2;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Pawel on 2014-10-20.
 */
public class NeutralNetworksManager {

    private double lowLimit;
    private int problemType;
    private int outputNeurons;
    private int iterations;
    private Properties networkProperties;

    public NeutralNetworksManager() {
        lowLimit = 0.0d;
        problemType = 0;
        outputNeurons = 0;
        iterations = 1000;
        networkProperties = new Properties();
    }

    public NeutralNetworksManager(Properties prop) {
        this();
        networkProperties = prop;
    }

    public NeuralNetwork run(){

        Scanner inScaner = new Scanner(System.in);
        boolean repeat = true;

        NeuralNetwork neuralNetwork = null;

        while(repeat){

            InputStream propertyFilePath = null;

            System.out.println("Enter neutral network property file path:");
            try {

                String filePath = inScaner.next();
                filePath = filePath.endsWith(".properties") ? filePath : filePath + ".properties";
                filePath = filePath.startsWith("tests/") ? filePath : "tests/" + filePath;
                propertyFilePath = new FileInputStream(filePath);
                networkProperties.load(propertyFilePath);

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (propertyFilePath != null) {
                    try {
                        propertyFilePath.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            neuralNetwork = createMLPBasedOnProperties();

            String answer = "";
            while(!answer.equals("Y") && !answer.equals("N")) {
                System.out.println("Repeat creating neutral network?");
                answer = inScaner.next().toUpperCase();
            }
            repeat = answer.equals("Y") ? true : false;
        }

        return neuralNetwork;
    }

    public NeuralNetwork createMLPBasedOnProperties() {
        MultiLayerPerceptron mlp = null;

        MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();

        System.out.println("Preparing MLP network with properties:");
        NeuronProperties np = new NeuronProperties();

        problemType = Integer.parseInt(networkProperties.getProperty("problemType"));
        System.out.println("problemType: " + networkProperties.getProperty("problemType"));

        np.setProperty("useBias", Boolean.valueOf(networkProperties.getProperty("bias")));
        System.out.println("bias: " + networkProperties.getProperty("bias"));

        String transferFunction = networkProperties.getProperty("transferFunction");
        TransferFunctionType transferFunctionType = TransferFunctionType.valueOf(transferFunction);
        np.setProperty("transferFunction", transferFunctionType);
        System.out.println("transferFunction: " + transferFunction);

        if(transferFunction == "TANH"
                || transferFunction == "SGN"
                ){
            lowLimit = -1.0d;
        }else if(transferFunction == "SIGMOID"
                || transferFunction == "STEP"
                || transferFunction == "RAMP"
                ){
            lowLimit = 0.0d;
        }

        List<Integer> neuronsInLayers = new ArrayList<>();
        neuronsInLayers.add(Integer.parseInt(
                networkProperties.getProperty("inputNeurons")));
        System.out.println("inputNeurons: " + networkProperties.getProperty("inputNeurons"));

        String[] items = networkProperties.getProperty("hiddenLayers")
                            .replaceAll("\\[", "").replaceAll("\\]", "").split(",");

        for (int i = 0; i < items.length; i++) {
            try {
                int hiddenLayer = Integer.parseInt(items[i]);
                neuronsInLayers.add(hiddenLayer);
            } catch (NumberFormatException nfe) {
                System.err.println("Error in hidden layer vector!");
            };
        }
        System.out.println("hiddenLayers: " + networkProperties.getProperty("hiddenLayers"));

        outputNeurons = Integer.parseInt(networkProperties.getProperty("outputNeurons"));
        neuronsInLayers.add(outputNeurons);
        System.out.println("outputNeurons: " + outputNeurons);

        momentumBackpropagation.setLearningRate(
                Double.valueOf(networkProperties.getProperty("learningRate"))
        );
        System.out.println("learningRate: " + networkProperties.getProperty("learningRate"));

        momentumBackpropagation.setMomentum(
                Double.parseDouble(networkProperties.getProperty("momentum"))
        );
        System.out.println("momentum: " + networkProperties.getProperty("momentum"));

        iterations = Integer.parseInt(networkProperties.getProperty("maxIterations"));
        momentumBackpropagation.setMaxIterations(iterations);
        System.out.println("maxIterations: " + networkProperties.getProperty("maxIterations"));

        momentumBackpropagation.setMaxError(
                Double.parseDouble(networkProperties.getProperty("maxError"))
        );
        System.out.println("maxError: " + networkProperties.getProperty("maxError"));


        mlp = new MultiLayerPerceptron(neuronsInLayers, np);
        mlp.setLearningRule(momentumBackpropagation);

        return mlp;
    }

    public double getLowLimit() {
        return lowLimit;
    }

    public int getProblemType() {
        return problemType;
    }

    public int outputNeurons() {
        return outputNeurons;
    }

    public int getIterations() { return iterations; }

    public Properties getNetworkProperties() { return networkProperties; }
}
