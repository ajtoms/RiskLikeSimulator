package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.BaseAgent;
import com.unbc.riskybusiness.controllers.Game;
import com.unbc.riskybusiness.models.Territory;

/**
 * Just your friendly neighborhood Main class, it launches the project by initializing and running a
 * Simulation Object.
 * 
 * @author Andrew J Toms II
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Agent a = new BaseAgent();
        Agent b = new BaseAgent();
        Agent c = new BaseAgent();
        Agent d = new BaseAgent();
        Game g = new Game(a,b,c,d);
        
        System.out.println(g.getBoard());
        
        //Give the Players references to the Game.
        a.setGame(g);
        b.setGame(g);
        c.setGame(g);
        d.setGame(g);
        
        g.play();
    }

}
