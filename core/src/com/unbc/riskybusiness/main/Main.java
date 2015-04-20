package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.agents.TypicalAgent;
import com.unbc.riskybusiness.controllers.Simulation;

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

        TypicalAgent ta = new TypicalAgent();
        TypicalAgent tb = new TypicalAgent();
        TypicalAgent tc = new TypicalAgent();
        TypicalAgent td = new TypicalAgent();
        Simulation s = new Simulation(ta,tb,tc,td,10000);
        
    }

}
