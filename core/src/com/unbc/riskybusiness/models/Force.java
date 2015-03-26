package com.unbc.riskybusiness.models;

import com.unbc.riskybusiness.main.DiceRoller;
import java.util.Arrays;
import java.util.Collections;

/**
 * A Force is a data structure used to move troops from territory to territory and resolve any 
 * battles for territory. Defending Territories will give all their troops to the Defending Force
 * object, Attacking troops will give as many troops as are being sent to attack (the default for 
 * this during this phase of the project is always #OfTroopsOnAttackingTerritory - 1) and the two
 * Forces will resolve a combat based on the <code>attack()</code> method.
 * 
 * @author Andrew J Toms II
 */
public class Force {

    private int myTroops;
    
    public Force(int troops){
        this.myTroops = troops;
    }
    
    /**
     * This method means that this Force object is attacking the defending Force Object in a battle
     * for its territory. The method will return the force who has won at the end, modified to match
     * its state at the end of the battle.
     * 
     * In future phases of the project, we may implement a way for Attacking Forces to retreat but
     * for now, committing to a battle means you've committed to potentially losing all troops sent 
     * in the Force.
     * 
     * @param defendingForce The Force that is defending their territory.
     * @return The Force who won the battle.
     */
    public Force attack(Force defendingForce){
        while(myTroops >= 1 && defendingForce.myTroops >= 1){
            
            //Attacker gets min of troops and 3 dice. Defender gets min of troops and 2 dice.
            int attackDice = Math.min(myTroops, 3);
            int defendDice = Math.min(defendingForce.myTroops, 2);
            
            //Roll each player's dice pool, 
            Integer[] attackPool = new Integer[attackDice];
            Integer[] defendPool = new Integer[defendDice];
            for(int i = 0; i < attackDice; i++){
                attackPool[i] = DiceRoller.rollD6(1);
            }
            for(int i = 0; i < defendDice; i++){
                defendPool[i] = DiceRoller.rollD6(1);
            }
            Arrays.sort(attackPool, Collections.reverseOrder());
            Arrays.sort(defendPool, Collections.reverseOrder());
            
            for(int i = 0; i < defendPool.length && i < attackPool.length; i++){
                if(defendPool[i] >= attackPool[i]){  //Defenders win ties.
                    this.myTroops--;
                } else {
                    defendingForce.myTroops--;
                }
            }
            
            
            
        }
        if(this.myTroops > defendingForce.myTroops)
            return this;
        else
            return defendingForce;
    }
    
}
