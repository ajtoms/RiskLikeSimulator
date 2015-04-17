package com.unbc.riskybusiness.models;

import com.unbc.riskybusiness.agents.AbstractAgent;
import com.unbc.riskybusiness.agents.Agent;
import oldview.BoardLocation;

/**
 * A Territory is one of the capturable spaces on the Board. It has a specific owner at all times,
 * and an integer number of troops that are stationed there. 
 * 
 * @author Andrew J Toms II
 */
public class Territory {

    private static int TERR_ID = 0;
    private int myId;
    private int troops;
    private AbstractAgent owner;
    String myLoc;
    
    /**
     * Initializes a Territory with the needed information. Initialized Territories will have no 
     * reference to any Agents or troops, so the only required fields are the location information.
     * 
     * @param x The X coordinate of this territory.
     * @param y The Y coordinate of this territory.
     */
    public Territory(int x, int y){
        myId = TERR_ID++;
        this.troops = 0;
        owner = null;
        
        //Figure out my BoardLocation value by converting the x,y coord into a Board Location
        char cont, terr;
        if(x < 2){  //A or C
            if(y < 2){
                cont = 'A';
                terr = (char)(65 + y*2 + x);        //We kinda convert to binary and add to 65 (ish)
            } else {
                cont = 'C';
                terr = (char)(65 + (y-2)*2 + x);
            }
        } else {    //B or D
            if(y < 2){
                cont = 'B';
                terr = (char)(65 + y*2 + x-2);
            } else {
                cont = 'D';
                terr = (char)(65 + (y-2)*2 + x-2);
            }
        }
        
        myLoc = cont + "" + terr;
    }
    
    /**
     * Assigns a Player Agent to this territory and give the inital number of troops they are 
     * putting there. This method needs to be called before a game can be played, otherwise methods
     * of this class involved in playing of the game will throw IllegalStateExceptions due to the 
     * class invariant.
     * 
     * @param owner The Agent to assign to the territory.
     * @param forces The number of troops he starts with on the territory.
     */
    public void initialAssignment(AbstractAgent owner, int forces){
        this.owner = owner;
        this.troops = forces;
        inGameInvariant();      //A game has begun. Start enforcing the invariant.
    }
    
    /**
     * A helper method to check the class invariant for a territory model in play (while a game 
     * is going on). If this is ever false while a game is in session something is borked.
     * 
     * @return True if the territory has a master and troops on it; which should be always as long 
     * as a game has been started. 
     */
    private boolean inGameInvariant(){
        if (owner != null && troops >= 1)
            return true;
        else
            throw new IllegalStateException("A Territory is left without a Master or Troops");
    }
    
    /**
     * Reinforces this Territory with new troops. This method enforces the Class Invariant as a 
     * precondition and a post condition.
     * 
     * @param numTroops The number of troops to add.
     */
    public void reinforce(int numTroops){
        inGameInvariant();
        this.troops += numTroops;
        inGameInvariant();
    }
    
    /**
     * Builds a Force object with troops of this Territory. It enforces the invariant (at least one 
     * troop belonging to a specific agent is on this Territory) as a precondition, but since a 
     * Force can be a defending Force made up of all troops on the territory we don't enforce it as
     * a post condition. Additionally, we enforce that there are enough troops to build a given 
     * Force on this territory as a precondition.
     * 
     * @param numTroops The Number of troops to take from this Territory as a Force.
     * @throws IllegalArgumentException in the event that we don't have enough troops to make the 
     * Force requested of us.
     * @return The Force of troops from this Territory.
     */
    public Force buildForce(int numTroops){
        //Error checking
        inGameInvariant();
        if(numTroops > troops)
            throw new IllegalArgumentException("Cannot build a Force with troops that don't exist");
        
        //In theory, if this is a defending force we break the invariant as a post condition; but we
        //trust that we will have new troops at the end of this so ignore it here... Damn it. Maybe
        //I should get rid of the invariant checking but it feels like a really good idea in lieu of
        //Unit Testing.
        this.troops -= numTroops;
        Force f = new Force(this.owner, numTroops);
        return f;
    }
    
    /**
     * Takes a Force Object and settles it back in the Territory, changing ownership if need be and
     * adding the troops to this Territory's pool of troops. After that's said and done we enforce 
     * the Game Invariant to make sure the Territory conforms to the norms. We cannot enforce it as
     * a precondition because if this Territory was previously being defended by a Force then there
     * would be 0 troops on it at the moment. We do enforce that if this territory is being taken 
     * over by a new owner that there were none of the old owner's troops on the territory still.
     * 
     * @param f The Force to settle into this Territory.
     */
    public void settleForce(Force f){
        if(!f.getOwner().equals(this.owner)){
            if(this.troops != 0)
                throw new IllegalArgumentException("Can't settle a Force in an occupied Territory");
            this.owner = f.getOwner();
        }
        this.troops += f.getTroops();
        inGameInvariant();
    }
    
    /**
     * Getter for the Territory's Owner Agent.
     * 
     * @return The Owner Agent of this Territory.
     */
    public AbstractAgent getOwner(){
        return owner;
    }
    
    /**
     * Getter for the Territory's Number of Troops.
     * 
     * @return The number of troops on this Territory.
     */
    public int getNumTroops(){
        return this.troops;
    }
    
    /**
     * Returns the enumerated type value of the Board Location for simplified GUI rendering purposes
     * 
     * @return As above.
     */
    public String getLocationName(){
        return myLoc;
    }
    
    /**
     * Lazy Hashing. There should be zero collisions and the number of ids should be small enough 
     * that we're fine.
     * 
     * @return The hash code for this Territory, which is a static ID value assigned to it on 
     * construction.
     */
    @Override
    public int hashCode(){
        return this.myId;
    }
    
    /**
     * Compares this Territory object with another object. If the other object is a Territory, this
     * returns true iff the two Territories have the same static identifier assigned to them, which
     * is only possible in the event that they are the same Territory.
     * 
     * @param o The Object to compare.
     * @return True iff the object o is the same as this Territory.
     */
    @Override
    public boolean equals(Object o){
        return this.myId == ((Territory)o).myId;
    }
    
    @Override
    public String toString(){
        return myLoc.toString();
    }
    
    public void setTroops(int numTroops) {
        this.troops = numTroops;
    }
    
    public void changeOwner(Force f) {
        this.owner = f.getOwner();
        this.troops = f.getTroops();
    }
}
