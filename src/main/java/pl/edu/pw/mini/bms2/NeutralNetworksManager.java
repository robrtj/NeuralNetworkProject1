package pl.edu.pw.mini.bms2;

import java.util.Scanner;

/**
 * Created by Pawel on 2014-10-20.
 */
public class NeutralNetworksManager {

    public void run(){

        Scanner inScaner = new Scanner(System.in);
        boolean repeat = true;

        while(repeat){

            String answer = "";
            while(!answer.equals("Y") && !answer.equals("N")) {

                

                System.out.println("Repeat creating neutral network?");
                answer = inScaner.next().toUpperCase();
            }
        }

    }
}
