package com.unbc.riskybusiness.models;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
