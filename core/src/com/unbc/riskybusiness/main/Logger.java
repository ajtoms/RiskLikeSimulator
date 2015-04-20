package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.agents.Agent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A Class that prints to System.out while simultaneously appending to a log File.
 * 
 * //TODO: Add concurrent support.
 * 
 * @author Andrew J Toms II
 */
public class Logger implements Runnable{
        
    private int myId;
    private int gamesRun;
    private int gamesToRun;
    private String filename;
    private Agent[] theAgents;
    private Queue<String> gameProceedings;

    public Logger(int myId, int gamesToRun, Agent[] agents){

        this.myId = myId;
        this.gamesToRun = gamesToRun;
        this.gameProceedings = new LinkedList<String>();
        this.theAgents = agents;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd h_mm_ss");
        this.filename = String.format("%s Simulation %d.log", sdf.format(date), myId);
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while(gamesRun < gamesToRun){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.err.println("Logging thread interrupted.");
                return;
            }
        }
        time = System.currentTimeMillis() - time;
        time /= 1000;

        int[] wins = new int[theAgents.length];

        Queue<String> resultsCopy = new LinkedList<String>();
        while(!gameProceedings.isEmpty()){
            String logline = gameProceedings.poll();
            if(logline.endsWith("wins")){
                for(int i = 0; i < theAgents.length; i++){
                    if(logline.startsWith(theAgents[i].toString())){
                        wins[i]++;
                        break;
                    }
                }
            }
            resultsCopy.add(logline);
        }
        gameProceedings = resultsCopy;

        //Selection sort the agents and their scores simultaneously
        for(int i = 0; i < wins.length; i++){
            int highestInd = i;
            for(int j = i; j < wins.length; j++){
                if(wins[j] > wins[highestInd])
                    highestInd = j;
            }
            int temp = wins[i];
            wins[i] = wins[highestInd];
            wins[highestInd] = temp;

            Agent tempAg = theAgents[i];
            theAgents[i] = theAgents[highestInd];
            theAgents[highestInd] = tempAg;
        }

        //Create a String ranking the winners.
        String results = "";
        for(int i = 0; i < theAgents.length; i++){
            results += String.format("(%d)\t%s (%.2f%%)\n", (i+1), theAgents[i], 
                    ((double)wins[i] / (double)gamesToRun * 100.0));
        }
        try {
            printFile(results, (int)time);
        } catch (IOException ex) {
            System.err.printf("Couldn't print to file. Here are the results: \n%s\n", results);
        }
    }

    public synchronized void logAction(String text){
        if(text.endsWith("wins")){
            gamesRun++;
        }
        gameProceedings.add(text);
    }

    /**
     * A utility method that prints all our logged info to a file.
     * 
     * @param resultList A formatted String naming the winners of the simulation.
     * @param runningTimeSeconds The number of seconds it took to run the entire simulation.
     * @throws IOException 
     */
    private void printFile(String resultList, int runningTimeSeconds) throws IOException{
        //Make sure the file exists
        File f = new File(filename);
        if(!f.exists())
            f.createNewFile();

        //Print general information about the simulation.
        PrintWriter pw = new PrintWriter(new FileOutputStream(f), true);
        pw.println(filename.substring(0, filename.length()-4));
        pw.println();
        pw.printf("Games Run:\t%d\n", this.gamesToRun);
        pw.printf("Running Time:\t%d seconds\n", runningTimeSeconds);
        pw.println();
        pw.println(resultList);
        pw.println();

        //Log the game proceedings lines.
        while(!gameProceedings.isEmpty()){
            String nextLine = gameProceedings.poll();
            pw.println(nextLine);

            if(nextLine.endsWith("wins")){
                pw.println();
                pw.println("---");
                pw.println();
            }
        }

        //Close the PrintWriter.
        pw.close();
    }

}
