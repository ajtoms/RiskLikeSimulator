package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;

/**
 * An implementation of an Agent Class giving a Human agency to play the game against one of the AI
 * agents.
 * 
 * @author Andrew J Toms II
 */
public class HumanAgent extends BaseAgent{

    public synchronized void setReinforcements(int reinforcements) {
        this.reinforcementsToPlace = reinforcements;
    }
    
    @Override
    public String toString() {
        return "Human"+myId;
    }
    
}
