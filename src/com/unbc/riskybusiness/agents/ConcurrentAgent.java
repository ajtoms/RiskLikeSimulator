package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.Game;

/**
 * This Agent is one that can carry on multiple games at once. It's purely a theoretical idea for 
 * now but could be something interesting to implement in the future. Under the hood it would simply
 * have multiple agent objects hashed to the games they're playing, but still be treated as one 
 * single agent because the agents it has stored would be of one type (and thus theoretically 'of 
 * one mind').
 * 
 * @author Andrew J Toms II
 */
public class ConcurrentAgent implements Agent{

    /**
     * Creates a Concurrent Agent capable of playing multiple games at once.
     * 
     * @param agentType The Class object representing the type of Agent Implementation to use for
     * any given Game.
     * @throws An IllegalArgumentException if the programmer tries to initiate a Concurrent Agent
     * from an implementation that couldn't possibly play concurrent games. The HumanAgent is the 
     * prime example of this; implementing a ConcurrentAgent as a Human would defeat the purpose of 
     * having COncurrent Agents, which is being able to thread and run multiple games at once to 
     * speed up collection of data.
     */
    public ConcurrentAgent(Class<? extends Agent> agentType){
        if(agentType.equals(HumanAgent.class))
            throw new IllegalArgumentException("You cannot implement a Concurrent Human Agent.");
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setGame(Game g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reinforce(int numReinforcements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean wantsToAttack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void tacticalMove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
