package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.HumanAgent;
import com.unbc.riskybusiness.agents.RandomAgent;
import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.controllers.GameController.STATE;
import com.unbc.riskybusiness.models.Board;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * A Class that prints to System.out while simultaneously appending to a log File.
 * 
 * //TODO: Add concurrent support.
 * 
 * @author Andrew J Toms II
 */
public class LoggerOne {

    private static final boolean SUPRESS_TERMINAL = true;
    private static String logName;
    
    public static void startLog(){
        startLog(true);
    }
    
    public static void startLog(boolean game){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("(yy-MM-dd @ h_mm_ss)");
        String myTag = randomTag();
        logName = game ? String.format("Risk Game %s %s.log", myTag, sdf.format(date)) :
                String.format("Simulation Results %s.log", sdf.format(date));
        File f = new File(logName);
        
        if(!SUPRESS_TERMINAL)
            System.out.printf("Running Simulation %s:\n", myTag);
        
        try {
            if(!f.exists())
                f.createNewFile();
        } catch (IOException ex) {
            System.out.println("Couldn't create Log File!");
        }
    }
    
    public static void log(String text){
        if(!SUPRESS_TERMINAL)
            System.out.println(text);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logName, true)));
            out.println(text);
            out.close();
        } catch (IOException e) {
            System.out.println("^ Could not Log statement to file, record from terminal to keep ^");
        }
    }
    
    /**
     * Generates a random 6-Letter Tag to add to game logs to help ensure they won't conflict with
     * other game logs from the same simulation (theoretically more than one sim can finish in a 
     * second, current logic would append them all and render each log useless).
     * 
     * @return A random 6-character String.
     */
    private static String randomTag(){
        char[] chars = new char[6];
        Random r = new Random();
        for(int i = 0; i < 6; i++){
            chars[i] = (char) (65 + r.nextInt(26));
        }
        return String.copyValueOf(chars);
    }

    public static void logVictory(Agent agentClass) {
        String className = "";
        if (agentClass instanceof HumanAgent)
            className = "HumanAgent";
        if (agentClass instanceof RandomAgent)
            className = "RandomAgent";
        File f = new File(className+"Wins.log");
        try {
            if(!f.exists()) {
                f.createNewFile();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                out.println(1);
                out.close();
            }
            else {
                Scanner s = new Scanner(f);
                int numWins = s.nextInt();
                numWins++;
                s.close();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
                out.println(numWins);
                out.close();
            }
        } catch (IOException ex) {
            System.out.println("Couldn't create Log File!");
        }
    }
}
