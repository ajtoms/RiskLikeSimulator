package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.Game;

/**
 * A basic implementation of an Agent class, programmed with a trivial knowledge of minimal strategy
 * required to make decisions. Intended to be used as a control Agent when testing other Agent 
 * classes out in experiments.
 * 
 * @author Andrew J Toms II
 */
public class BaseAgent implements Agent{

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
