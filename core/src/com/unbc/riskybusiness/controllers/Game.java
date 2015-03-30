package com.unbc.riskybusiness.controllers;

import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.models.Board;
import com.unbc.riskybusiness.models.Territory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Game object is used to control the Game play loop, enforce player turns being taken, check for 
 * winners and provide models for the Agents so that they can see the state of the Board and through
 * it the Territories and their Forces.
 * 
 * @author Andrew J Toms II
 */
public class Game {

    private static final int BASE_REINFORCEMENTS = 3;
    private int playerIndex;    //The integer index of the current player.
    private Agent[] players;
    private Board board;
    
    /**
     * Initializes a Game Object to track the control flow of a Risklike Game. It takes the Players
     * as arguments so that it has reference to them to request they do their required actions. It
     * randomly assigns the territories for each of the players in the game,
     * @param player1
     * @param player2 
     */
    public Game(Agent player1, Agent player2, Agent player3, Agent player4){
        this.playerIndex = 0;
        this.players = new Agent[]{player1, player2, player3, player4};
        this.board = new Board();
        
        //Boards don't know what players are so we will assign territories from the board to the 
        //players now.
        int switcher = 0;           //To track which player to give territories to round robin style
        Random r = new Random();    //So that we can pick a territory at random from what's left
        ArrayList<Territory> available = (ArrayList<Territory>) board.getAllTerritories();
        while(!available.isEmpty()){
            //Pick the territory at random from the remaining ones
            Territory terr = available.get(r.nextInt(available.size()));
            
            //Now give it to the players round robin.
            terr.initialAssignment(players[switcher % 4], 1);
            available.remove(terr);
            switcher++;
        }
    }
    
    /**
     * This method runs the Game loop, and returns the Agent that won.
     * 
     * @return The Agent that won this game.
     */
    public Agent play(){
        while(!isOver()){
            //Grab our next player.
            Agent a = players[playerIndex % 4];
            
            //If an Agent is currently dead, then skip their turn.
            if(board.getAgentsTerritories(a).isEmpty()){
                playerIndex++;
                continue;
            }
            
            //Player turn logic.
            a.reinforce(BASE_REINFORCEMENTS + board.continentBonusFor(a));                     //TODO: Replace the static 3 reinforcements.
            while(a.wantsToAttack()){
                a.attack();
            }
            a.tacticalMove();
            
            //Signal to next player to do a thing.
            playerIndex++;
        }
        
        //The winner is the owner of all territories.
        return board.getTerritory(0, 0).getOwner();
    }
    
    /**
     * This method gives access to the Board object of a given game session.
     * 
     * @return The Board instance for this Game.
     */
    public Board getBoard(){
        return board;
    }
    
    /**
     * Returns true if the game is over. The Game is over when all territories are occupied by a 
     * single Agent. 
     * 
     * @return True if the game is over, else false.
     */
    public boolean isOver(){
        Agent lastAgent = null;
        boolean isOver = true;      //Using a True until proven false loop (TUPF)
        ArrayList<Territory> all = (ArrayList<Territory>) board.getAllTerritories();
        
        //TUPF: Assume the value is true thus far. Then, grab a territory. If the 'last agent' is 
        //null assume we're in the first iteration and carry on, otherwise, is Over is true iff the 
        //last agent is the same as the current agent. Make the current agent the last agent. If we
        //end up with isOver being true, then we've proved via induction that it is.
        for(int i = 0; isOver && i < all.size(); i++){
            Territory t = board.getAllTerritories().get(i);
            isOver &= (lastAgent == null || t.getOwner().equals(lastAgent));
            lastAgent = t.getOwner();
        }
        
        //If all the territories' owners are null for whatever reason, then we have not finished!
        return lastAgent != null && isOver;
    }
    
}
