package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.main.Logger;

/**
 * This Agent performs the typical strategy used by most players of the Board Game Risk. It is 
 * aggressive in doing so, 
 * 
 * @author Andrew J Toms II
 */
public class TypicalAgent extends Agent{

    @Override
    public synchronized void startReinforcing(int reinforcements){
        super.startReinforcing(reinforcements);
    }
    
    @Override
    public synchronized void startAttacking(){
        super.startAttacking();
    }
    
    @Override
    public synchronized void startMoving(){
        super.startMoving();
    }
    
}
