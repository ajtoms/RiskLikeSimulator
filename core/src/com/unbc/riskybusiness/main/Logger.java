package com.unbc.riskybusiness.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * A Class that prints to System.out while simultaneously appending to a log File.
 * 
 * //TODO: Add concurrent support.
 * 
 * @author Andrew J Toms II
 */
public class Logger {

    private static String logName;
    
    public static void startLog(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("(yy-MM-dd @ h_mm_ss)");
        String myTag = randomTag();
        logName = String.format("Risk Game %s %s.log", myTag, sdf.format(date));
        File f = new File(logName);
        
        System.out.printf("Running Simulation %s:\n", myTag);
        
        try {
            if(!f.exists())
                f.createNewFile();
        } catch (IOException ex) {
            System.out.println("Couldn't create Log File!");
        }
    }
    
    public static void log(String text){
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
}
