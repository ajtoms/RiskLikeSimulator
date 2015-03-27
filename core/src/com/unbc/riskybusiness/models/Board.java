package com.unbc.riskybusiness.models;

import com.unbc.riskybusiness.agents.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A Model class that stores territories and keeps track of their connectivity to each other as well
 * as providing an interface for Agents to interact with the aforementioned Territory objects. Logic
 * that oversees the existence of 'continents' is also implemented here.
 * 
 * @author Andrew J Toms II
 */
public class Board {

    private Territory[][] territories;
    private HashMap<Territory, ArrayList<Territory>> adjacencies;
    
    public Board(){
        territories = new Territory[4][4];
        adjacencies = new HashMap<Territory, ArrayList<Territory>>();
        for(int x = 0; x < territories.length; x++){
            for (int y = 0; y < territories[x].length; y++){
                territories[x][y] = new Territory(x,y);
                
                //For now, we can't make the adjacency matrix because not all territories are 
                //guaranteed to be created.
                ArrayList<Territory> list = new ArrayList<Territory>();
                adjacencies.put(territories[x][y], list);
            }
        }
        
        //I could write the algorithm to build adjacencies nice and expandable, or because we only 
        //intend to use sixteen territories, I could hard code it. Yeah it's gross, but it'll be 
        //just as quick for me to figure it out this way.
        
        //CONTINENT A
        adjacencies.get(territories[0][0]).add(territories[1][0]);
        adjacencies.get(territories[0][0]).add(territories[0][1]);
        adjacencies.get(territories[1][0]).add(territories[0][0]);
        adjacencies.get(territories[1][0]).add(territories[1][1]);
        adjacencies.get(territories[1][0]).add(territories[2][0]);
        adjacencies.get(territories[0][1]).add(territories[0][0]);
        adjacencies.get(territories[0][1]).add(territories[1][1]);
        adjacencies.get(territories[0][1]).add(territories[0][2]);
        adjacencies.get(territories[1][1]).add(territories[0][1]);
        adjacencies.get(territories[1][1]).add(territories[1][0]);
        
        //CONTINENT B
        adjacencies.get(territories[3][0]).add(territories[2][0]);
        adjacencies.get(territories[3][0]).add(territories[3][1]);
        adjacencies.get(territories[2][0]).add(territories[3][0]);
        adjacencies.get(territories[2][0]).add(territories[2][1]);
        adjacencies.get(territories[2][0]).add(territories[1][0]);
        adjacencies.get(territories[3][1]).add(territories[3][0]);
        adjacencies.get(territories[3][1]).add(territories[2][1]);
        adjacencies.get(territories[3][1]).add(territories[3][2]);
        adjacencies.get(territories[2][1]).add(territories[3][1]);
        adjacencies.get(territories[2][1]).add(territories[2][0]);
        
        //CONTINENT C
        adjacencies.get(territories[0][2]).add(territories[0][1]);
        adjacencies.get(territories[0][2]).add(territories[0][3]);
        adjacencies.get(territories[0][2]).add(territories[1][2]);
        adjacencies.get(territories[0][3]).add(territories[0][2]);
        adjacencies.get(territories[0][3]).add(territories[1][3]);
        adjacencies.get(territories[1][2]).add(territories[0][2]);
        adjacencies.get(territories[1][2]).add(territories[1][3]);
        adjacencies.get(territories[1][3]).add(territories[0][3]);
        adjacencies.get(territories[1][3]).add(territories[1][2]);
        adjacencies.get(territories[1][3]).add(territories[2][3]);
        
        //CONTINENT D
        adjacencies.get(territories[2][2]).add(territories[2][3]);
        adjacencies.get(territories[2][2]).add(territories[3][2]);
        adjacencies.get(territories[3][3]).add(territories[2][3]);
        adjacencies.get(territories[3][3]).add(territories[3][2]);
        adjacencies.get(territories[2][3]).add(territories[3][3]);
        adjacencies.get(territories[2][3]).add(territories[2][2]);
        adjacencies.get(territories[2][3]).add(territories[1][3]);
        adjacencies.get(territories[3][2]).add(territories[3][3]);
        adjacencies.get(territories[3][2]).add(territories[2][2]);
        adjacencies.get(territories[3][2]).add(territories[3][1]);
        
        //Dear Lee: I'm sorry.
    }
    
    /**
     * This method returns a given Territory object from the Board at the posted x, y coordinates.
     * 
     * @param x The x-Axis Location of the Territory
     * @param y The y-Axis Location of the Territory
     * @return The Territory at the coordinates given.
     */
    public Territory getTerritory(int x, int y){
        return territories[x][y];
    }
    
    /**
     * This method returns all the Territories on the Board adjacent to the others. 
     * 
     * @param terr The Territory you want Territories adjacent to.
     * @return All Territories adjacent to the one passed.
     */
    public List<Territory> getAdjacentTerritories(Territory terr){
        return adjacencies.get(terr);
    }
    
    /**
     * A Method to return all Territories on the Board owned by a given Agent.
     * 
     * @param a The Agent to get the list of Territories from.
     * @return The List of Territories belonging to a given Agent.
     */
    public List<Territory> getAgentsTerritories(Agent a){
        List<Territory> ret = new ArrayList<Territory>();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(territories[x][y].getOwner().equals(a))
                    ret.add(territories[x][y]);
            }
        }
        return ret;
    }
    
    /**
     * This method returns all the Territories on the Board. 
     * 
     * @return A list of all Territories
     */
    public List<Territory> getAllTerritories(){
        List<Territory> all = new ArrayList<Territory>();
        for(int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++) {
                all.add(territories[x][y]);
            }
        }
        return all;
    }
    
}
