package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;

/**
 * An implementation of an Agent Class giving a Human agency to play the game against one of the AI
 * agents.
 * 
 * @author Andrew J Toms II
 */
public class HumanAgent extends AbstractAgent{

    private int reinforcments;
    
    @Override
    public synchronized void startReinforcing(int reinforcments) {
        isReinforcing = true;
        this.reinforcments = reinforcments;
    }

    @Override
    public synchronized void startAttacking() {
        isAttacking = true;
        // The isAttacking flag will be set to false via the UI
    }

    @Override
    public synchronized void startMoving() {
        isMoving = true;
        // The isMoving flag will be set to false via the UI.
    }
    
    public synchronized int getReinforcements() {
        return reinforcments;
    }
    
    public synchronized void setReinforcements(int reinforcements) {
        this.reinforcments = reinforcements;
    }
    
    @Override
    public String toString() {
        return "Human";
    }
    
}
