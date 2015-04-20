package com.unbc.riskybusiness.controllers;

import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.main.Logger;

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

        Logger l =  new Logger(0, numGames, new Agent[]{a,b,c,d});
        Thread t = new Thread(l);
        t.start();
        for(int i = 0; i < numGames; i++){
            
            GameController g = new GameController(a,b,c,d, l);
            a.setGameController(g);
            b.setGameController(g);
            c.setGameController(g);
            d.setGameController(g);
            
            Agent winner = g.play();
        }
    }
    
}
