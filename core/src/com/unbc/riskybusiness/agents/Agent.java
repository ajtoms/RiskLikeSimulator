package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.Game;

/**
 * Base interface that all Agents will abide by to play the game. Allows for use of polymorphism to
 * develop many different types of agents quickly.
 * 
 * @author Andrew J Toms II
 */
public interface Agent {
    /**
     * Gives the Agent a reference to a Game object that he is now 'playing'. Any decision-making
     * methods called will reference this Game Object to get its up-to-date state information.
     * 
     * @param g The Game this Agent is now playing.
     */
    public void setGame(Game g);
    
    /**
     * The Agent decides how to distribute his reinforcements for phase one of his turn and then 
     * enacts that plan. This method requires that the agent have a reference to the current Board
     * object so that he read its state for planning and then inform the Game object of his 
     * decisions.
     * 
     * @param numReinforcements The number of reinforcements the Agent will have to distribute.
     */
    public void reinforce(int numReinforcements);
    
    /**
     * Returns true if the Agent still has plans for attacks this turn, and is capable of enacting
     * them. As a side effect, calling this method will have the Agent re-evaluate current plans and
     * potentially dynamically adapt to develop new ones.
     * 
     * @return True if the agent can still attack and has planned intentions to do so.
     */
    public boolean wantsToAttack();
    
    /**
     * Calls for an Agent to enact a plan he has developed for attacking. The Agent will relay his
     * plan through the Game object which will resolve it. Post-processing of the attack will happen
     * in the next call to <code>playerInstance.wantsToAttack()</code>
     */
    public void attack();
    
    /**
     * Calls for an Agent to make any tactical move decisions before relinquishing his turn to the
     * next player. He will make his tactical move through the Game Object.
     */
    public void tacticalMove();
    
}
