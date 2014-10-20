package pl.edu.pw.mini.bms2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Pawel on 2014-10-20.
 */
public class NeutralNetworksManager {

    public void run(){

        Scanner inScaner = new Scanner(System.in);
        boolean repeat = true;

        while(repeat){

            Properties networkProperties = new Properties();
            InputStream propertyFilePath = null;
            String answer = "";
            while(!answer.equals("Y") && !answer.equals("N")) {
                System.out.println("Enter neutral network property file path:");
                try {

                    String filePath = inScaner.next();
                    filePath = filePath.endsWith(".properties") ? filePath : filePath + ".properties";
                    filePath = filePath.startsWith("tests/") ? filePath : "tests" + filePath;
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

                System.out.println(networkProperties.getProperty("bias"));

                System.out.println("Repeat creating neutral network?");
                answer = inScaner.next().toUpperCase();
            }
        }

    }
}
