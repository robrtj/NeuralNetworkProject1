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

    private double lowLimit = 0.0d;
    private int problemType = 0;

    public NeuralNetwork run(){

        Scanner inScaner = new Scanner(System.in);
        boolean repeat = true;

        NeuralNetwork neuralNetwork = null;

        while(repeat){

            Properties networkProperties = new Properties();
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

            neuralNetwork = createMLPBasedOnProperties(networkProperties);

            String answer = "";
            while(!answer.equals("Y") && !answer.equals("N")) {
                System.out.println("Repeat creating neutral network?");
                answer = inScaner.next().toUpperCase();
            }
            repeat = answer.equals("Y") ? true : false;
        }

        return neuralNetwork;
    }

    private NeuralNetwork createMLPBasedOnProperties(Properties networkProperties) {
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

        neuronsInLayers.add(Integer.parseInt(
                            networkProperties.getProperty("outputNeurons")));
        System.out.println("outputNeurons: " + networkProperties.getProperty("outputNeurons"));

        momentumBackpropagation.setLearningRate(
                Double.valueOf(networkProperties.getProperty("learningRate"))
        );
        System.out.println("learningRate: " + networkProperties.getProperty("learningRate"));

        momentumBackpropagation.setMomentum(
                Double.parseDouble(networkProperties.getProperty("momentum"))
        );
        System.out.println("momentum: " + networkProperties.getProperty("momentum"));

        momentumBackpropagation.setMaxIterations(
                Integer.parseInt(networkProperties.getProperty("maxIterations"))
        );
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
}
