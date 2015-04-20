package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.models.Board;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This Agent performs the typical strategy used by most players of the Board Game Risk. It is 
 * aggressive in doing so, 
 * 
 * @author Andrew J Toms II
 */
public class TypicalAgent extends Agent{

    private static int TYPICAL_ID = 0;
    private int myID;
    private List<Territory> potentialAttackers;
    
    public TypicalAgent(){
        myID = TYPICAL_ID++;
        potentialAttackers = new ArrayList<Territory>();
    }
    
    @Override
    public void startReinforcing(int reinforcements){
        super.startReinforcing(reinforcements);
        
        Board b = gameController.getBoard();
        List<Territory> myLands = b.getAgentsTerritories(this);
        
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
            myLands.get(0).reinforce(reinforcements);
        
        //After loooking for all threatened territories, the one with the highest will get my troops
        //Netbeans says this is dereferencing a possible null pointer; it isn't though, assuming we
        //have territories in myLands, which we always should have
        threatened.reinforce(reinforcements);
        
        setDoneReinforcing();
    }
    
    @Override
    public void startAttacking(){
        super.startAttacking();
        
        while(wantsToAttack())
            attack();
        
        setDoneAttacking();
    }
    
    @Override
    public void startMoving(){
        super.startMoving();
        
        Board b = gameController.getBoard();
        List<Territory> myLands = b.getAgentsTerritories(this);
        int threatPairDiff = Integer.MIN_VALUE;
        Territory threatPair1 = null;
        Territory threatPair2 = null;
        for(Territory t : myLands){
            
            int myThreat = 0;
            List<Territory> potentialPairings = new ArrayList<Territory>();
            
            //Determine the threat on this territory and all potential territories we may be able to
            //match to this one.
            List<Territory> adjs = b.getAdjacentTerritories(t);
            for(Territory adj : adjs){
                if(!adj.getOwner().equals(this)){   //This is an enemy
                    myThreat += adj.getNumTroops();
                } else {
                    potentialPairings.add(adj);
                }
            }
            
            //Now Find the threat on all potential pairings, and use a hash so we can store the
            //association
            HashMap<Territory, Integer> potsToThreats = new HashMap<Territory, Integer>();
            for(Territory threatened : potentialPairings){
                
                int tempThreat = 0;
                
                //Repurpose adjacents list here to be the potential pairing's adjacents
                adjs = b.getAdjacentTerritories(t);
                for(Territory adj : adjs){
                    if(!adj.getOwner().equals(this)){   //This is an enemy
                        tempThreat += adj.getNumTroops();
                    }
                }
                
                potsToThreats.put(threatened, tempThreat);
            }
            
            //Now, if there are adjacent threatened territories to t, potsToThreats won't be empty
            //so we'll pick the pot(ential) with the highest threat difference to t;
            if(!potsToThreats.isEmpty()){
                int highestDiff = Integer.MIN_VALUE;
                Territory highestPot = null;
                for(Territory pot : potsToThreats.keySet()){
                    int threatDiff = Math.abs(myThreat - potsToThreats.get(pot));
                    if(threatDiff > highestDiff){
                        highestDiff = threatDiff;
                        highestPot = pot;
                    }
                }
                
                //We've found the best candidate pairing attached to this territory (t). But we need
                //to contrast this territory and it's candidate pairings with all other territories
                //and their candidate pairings. 
                if(threatPairDiff < highestDiff){
                    
                    //Since this is the best candidate pairing, remember it! ThreatPair1 goes to the
                    //least threatened ally, and ThreatPair2 goes to the most threatened ally.
                    highestDiff = threatPairDiff;
                    if(myThreat > potsToThreats.get(highestPot)){
                        threatPair1 = highestPot;
                        threatPair2 = t;
                    } else {
                        threatPair2 = highestPot;
                        threatPair1 = t;
                    }
                }
            }
        }
        
        //After all that checking was done to optimize our tactical move, we may not have one. For
        //instance, if none of our territories are adjacent (Damn the luck!) then there is no 
        //tactical move to make. We also don't make a tactical move if doing so would require more
        //resources from the giver than we could spare.
        if(threatPairDiff > 0 && (threatPair1.getNumTroops() - (threatPairDiff / 2)) > 0){
            //We want to even the threat levels across all nodes, so move half the difference from
            //one to the other.
            int moveNum = threatPairDiff / 2;   
            threatPair1.setTroops(threatPair1.getNumTroops() - moveNum);
            threatPair2.setTroops(threatPair2.getNumTroops() + moveNum);
        }
            
        setDoneMoving();
    }
    
    /**
     * A Base Agent's attack strategy is attack whenever he physically can.
     * 
     * @return True if the Agent has a territory that is adjacent to enemy territory with enough 
     * troops (>1) to attack it.
     */
    public boolean wantsToAttack() {
        boolean ret = false;
        
        potentialAttackers.clear(); //Previous potentials might no longer be adjacent to an enemy
        
        //Look at all my territories. If it has enough troops to smack someone, it does.
        for(Territory t : gameController.getBoard().getAgentsTerritories(this)){
            if(t.getNumTroops() > 1){
                
                //Check adjacent territories to see if we can do anything about it.
                List<Territory> adjs = gameController.getBoard().getAdjacentTerritories(t);
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
    public void attack() {
        //Grab an attacker.
        Territory attacker = potentialAttackers.remove(0);
        
        //Find the weakest adjacent territory
        int victimStrength = Integer.MAX_VALUE;
        Territory victim = null;
        List<Territory> adjs = gameController.getBoard().getAdjacentTerritories(attacker);
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
    public String toString(){
        return String.format("Typical Agent %d", myID);
    }
}