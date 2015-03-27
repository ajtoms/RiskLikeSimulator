package com.unbc.riskybusiness.main;

import com.unbc.riskybusiness.models.Board;
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
        Board b = new Board();
        for(Territory t : b.getAllTerritories()){
            System.out.println(t.getLocation());
        }
    }

}
