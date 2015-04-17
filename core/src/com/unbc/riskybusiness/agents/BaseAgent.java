package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.models.Board;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of an Agent class, programmed with a trivial knowledge of minimal strategy
 * required to make decisions. Intended to be used as a control Agent when testing other Agent 
 * classes out in experiments.
 * 
 * @author Andrew J Toms II
 */
public class BaseAgent implements Agent{

    private int stub = 0;
    
    private static int ID = 0;
    private int myId;
    private GameController gameInstance;
    
    private ArrayList<Territory> potentialAttackers;
    
    public BaseAgent(){
        myId = ID++;
        potentialAttackers = new ArrayList<Territory>();
    }
    
    @Override
    public void setGame(GameController g) {
        this.gameInstance = g;
    }
    
    /**
     * The Base Agent Reinforcement Strategy is this: Look at all of my territories. Calculate the 
     * difference between all adjacent enemy territories to each of my territories. The one with the
     * highest difference is the territory where I will place all my troops.
     * 
     * @param numReinforcements The number of reinforcements I get.
     */
    @Override
    public void reinforce(int numReinforcements) {
        Board b = gameInstance.getBoard();
        List<Territory> myLands = null; //b.getAgentsTerritories(this);
        
        //For each of my territories, calculate the threat and find the one with the highest.
        int highestThreat = Integer.MIN_VALUE;
        Territory threatened = null;
        for(Territory t : myLands){
            //Threat is sum of adjacent enemy troops
            int threat = 0;
            List<Territory> adjs = b.getAdjacentTerritories(t);
            for(Territory adj : adjs){
                if(!adj.getOwner().equals(this)){   //This is an enemy
                    threat += adj.getNumTroops();
                }
            }
            
            //Are we the most threatened territory?
            if(threat > highestThreat){
                highestThreat = threat;
                threatened = t;
            }
        }
        
        //If none of my territories are surrounded by enemy territories, then I have no preference
        //for reinforcement
        if(threatened == null)
            myLands.get(0).reinforce(numReinforcements);
        
        //After loooking for all threatened territories, the one with the highest will get my troops
        //Netbeans says this is dereferencing a possible null pointer; it isn't though, assuming we
        //have territories in myLands, which we always should have
        threatened.reinforce(numReinforcements);
    }

    /**
     * A Base Agent's attack strategy is attack whenever he physically can.
     * 
     * @return True if the Agent has a territory that is adjacent to enemy territory with enough 
     * troops (>1) to attack it.
     */
    @Override
    public boolean wantsToAttack() {
        boolean ret = false;
        
        potentialAttackers.clear(); //Previous potentials might no longer be adjacent to an enemy
        
        //Look at all my territories. If it has enough troops to smack someone, it does.
        for(Territory t : gameInstance.getBoard().getAgentsTerritories(null)){
            if(t.getNumTroops() > 1){
                
                //Check adjacent territories to see if we can do anything about it.
                List<Territory> adjs = gameInstance.getBoard().getAdjacentTerritories(t);
                for(Territory adj : adjs){
                    //Record a boolean so we don't need to call it twice;
                    boolean yes = !adj.getOwner().equals(this);
                    
                    //If we can attack with this territory, yes will be true, throw this Territory
                    //in the list of potential attackers if it isn't already in there.
                    if(yes){
                        if(!potentialAttackers.contains(t)){
                            potentialAttackers.add(t);
                        }
                    }
                    ret |= yes; //Ret will be set to true and can't be unset if even 1 t can attack
                }
            }
        }
        
        return ret;
    }

    /**
     * Takes a potential attacker, finds the weakest adjacent enemy and smash it!
     */
    @Override
    public void attack() {
        //Grab an attacker.
        Territory attacker = potentialAttackers.remove(0);
        
        //Find the weakest adjacent territory
        int victimStrength = Integer.MAX_VALUE;
        Territory victim = null;
        List<Territory> adjs = gameInstance.getBoard().getAdjacentTerritories(attacker);
        for(Territory adj : adjs){
            if(adj.getNumTroops() < victimStrength && !adj.getOwner().equals(this)){
                victim = adj;
                victimStrength = adj.getNumTroops();
            }
        }
        
        //Resolve the Attack by building forces and settle the winner in Victim's land
        Force atkForce = attacker.buildForce(attacker.getNumTroops() - 1);
        Force defForce = victim.buildForce(victimStrength);  //Guaranteed not null
        Force winner = atkForce.attack(defForce);
        victim.settleForce(winner);
    }

    @Override
    public void tacticalMove() {
        //TODO: Give it a slightly less dumb tactical strat
    }
    
    @Override
    public String toString(){
        return String.format("Base Agent %d", myId);
    }
    
}
