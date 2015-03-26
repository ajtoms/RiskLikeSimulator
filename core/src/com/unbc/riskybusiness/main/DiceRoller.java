package com.unbc.riskybusiness.main;

import java.util.Random;

/**
 * An RNG utility class to make dice rolls through.
 * 
 * @author Andrew J Toms II
 */
public class DiceRoller {

    private static Random rng;  //Handles the random number generation.
    static {
        rng = new Random();
    }
    
    /**
     * This method simulates rolling n six-sided dice and returns the resultant sum of the roll.
     * 
     * @param n The number of six-sided dice to roll.
     * @return The sum of rolling nD6
     */
    public static int rollD6(int n){
        int ret = 0;
        for(int i = 0; i < n; i++){
            ret += rng.nextInt(6) + 1;
        }
        return ret;
    }
    
}
