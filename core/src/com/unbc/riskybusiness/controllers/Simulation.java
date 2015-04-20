package com.unbc.riskybusiness.controllers;

import com.unbc.riskybusiness.agents.Agent;
import java.util.HashMap;

/**
 * The main focus of this project is a Simulation session. It will initialize agents that we want to
 * test and it will also create one or more games for them to play. When a full simulation has been
 * run, all of the data from the session is reported.
 * 
 * Special parameters for how games are run are set here after reading them from a config.ini file.
 * 
 * @author Andrew J Toms II
 */
public class Simulation {

    public Simulation(Agent a, Agent b, Agent c, Agent d, int numGames){
        
        //Use a HashMap as a scoreboard
        HashMap<Agent, Integer> results = new HashMap<Agent, Integer>();
        results.put(a, 0);
        results.put(b, 0);
        results.put(c, 0);
        results.put(d, 0);
        
//        long allTimes = 0;
//        for(int i = 0; i < numGames; i++){
//            
////            Game g = new Game(a,b,c,d);
////            a.setGame(g);
////            b.setGame(g);
////            c.setGame(g);
////            d.setGame(g);
////            
//            long time = System.nanoTime();
//            Agent winner = g.play();
//            time = System.nanoTime() - time;
//            allTimes += time;
//            
//            results.put(winner, results.get(winner) + 1);
//        }
//        
//        
//        allTimes /= numGames;
//        double avgTimeSeconds = allTimes / 1000000;
//        Logger.startLog(false);
//        Logger.log("Simulation Results");
//        Logger.log(String.format("Average Runtime: %.4fms", avgTimeSeconds));
//        for(Agent ag : results.keySet()){
//            int resultsAg = results.get(ag);
//            double percentAg = (double) resultsAg / (double) numGames;
//            String s = String.format("%s won %.2f%% of games (%d)", ag, percentAg, resultsAg);
//            Logger.log(s);
//        }
    }
    
}
