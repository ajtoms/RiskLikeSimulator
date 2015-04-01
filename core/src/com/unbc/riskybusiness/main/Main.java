package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.BaseAgent;
import com.unbc.riskybusiness.controllers.Game;
import com.unbc.riskybusiness.controllers.Simulation;
import java.util.HashMap;

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
        Simulation s = new Simulation(a,b,c,d,1000);
    }

}
