package com.unbc.riskybusiness.controllers;

import com.unbc.riskybusiness.agents.BaseAgent;
import com.unbc.riskybusiness.main.Logger;
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
public class GameController {

    public enum STATE {SET_NEXT_PLAYER, REINFORCING, ATTACKING, MOVING, DONE_GAME};
    
    private int playerIndex;    //The integer index of the current player.
    private BaseAgent[] players;
    private BaseAgent currentPlayer;
    private int[] territoriesOwned;
    private Board board;
    private STATE currentState;
    /* Game Settings Default Values */
    public int percentToWin = 100;
    public int unitsPerTurn = 5;
    public int startingUnits = 3;
    public int aiDelaySeconds = 2;
    /**
     * Initializes a Game Object to track the control flow of a Risklike Game. It takes the Players
     * as arguments so that it has reference to them to request they do their required actions. It
     * randomly assigns the territories for each of the players in the game,
     * @param player1
     * @param player2
     * @param player3
     * @param player4
     */
    public GameController(BaseAgent player1, BaseAgent player2, BaseAgent player3, BaseAgent player4){
        ArrayList<BaseAgent> notNullAgents = new ArrayList<BaseAgent>();
        if (player1 != null)
            notNullAgents.add(player1);
        if (player2 != null)
            notNullAgents.add(player2);
        if (player3 != null)
            notNullAgents.add(player3);
        if (player4 != null)
            notNullAgents.add(player4);
        
        playerIndex = 0;
        players = (BaseAgent[]) notNullAgents.toArray(new BaseAgent[notNullAgents.size()]);
        board = new Board();
        
        if (players.length == 4)
            territoriesOwned = new int[] {4,4,4,4};
        else if (players.length == 3)
            territoriesOwned = new int[] {6,5,5};
        else
            territoriesOwned = new int[] {8,8};
            
        
        //Boards don't know what players are so we will assign territories from the board to the 
        //players now.
        int switcher = 0;           //To track which player to give territories to round robin style
        Random r = new Random();    //So that we can pick a territory at random from what's left
        ArrayList<Territory> available = (ArrayList<Territory>) board.getAllTerritories();
        while(!available.isEmpty()){
            //Pick the territory at random from the remaining ones
            Territory terr = available.get(r.nextInt(available.size()));
            
            //Now give it to the players round robin.
            terr.initialAssignment(players[switcher % this.players.length], startingUnits);
            available.remove(terr);
            switcher++;
        }
    }
    
    /**
     * This method runs the Game loop, and returns the Agent that won.
     * 
     * @return The Agent that won this game.
     */
    public BaseAgent play(){
        for (BaseAgent a : players)
            a.setGameController(this);
        currentState = STATE.SET_NEXT_PLAYER;
        while(currentState != STATE.DONE_GAME){
            setDeadAgents();
            switch (currentState) {
                case SET_NEXT_PLAYER:
                    if (isOver()) {
                        currentState = STATE.DONE_GAME;
                    }
                        
                    currentPlayer = players[playerIndex % this.players.length];
                    if (currentPlayer.isDead()) {
                        playerIndex++;
                        break;
                    }

                    currentPlayer.setTakingTurn();
                    currentState = STATE.REINFORCING;
                    currentPlayer.startReinforcing(unitsPerTurn + board.continentBonusFor(currentPlayer));
                    break;
                case REINFORCING:
                    if (!currentPlayer.isReinforcing()) {
                        currentState = STATE.ATTACKING;
                        currentPlayer.startAttacking();
                    }
                    break;
                case ATTACKING:
                    if (!currentPlayer.isAttacking()) {
                        currentState = STATE.MOVING;
                        currentPlayer.startMoving();
                    }
                    break;
                case MOVING:
                    if (!currentPlayer.isMoving()) {
                        currentState = STATE.SET_NEXT_PLAYER;
                        currentPlayer.doneTurn();
                        playerIndex++;
                    }
                    break;
            }
        }
        
        //The winner is the owner of all territories.
        BaseAgent victor = board.getTerritory(0, 0).getOwner();
        Logger.log(String.format("The winner is %s", victor));
        Logger.logVictory(victor);
        return victor;
    }
    
    // Checks to see if any agents are dead and sets their isDead flag
    private void setDeadAgents() {
        for (BaseAgent player : players) {
            if (board.getAgentsTerritories(player).isEmpty()) {
                player.setDead();
            }
        }
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
     * The game is over when one agent controls the percent of the board specified
     * by the percentToWin variable.
     * 
     * @return True if the game is over, else false.
     */
    public boolean isOver(){
        for (BaseAgent a: players) {
            if (getPercentControlled(a) >= percentToWin)
                return true;
        }
        return false;

    }
    
    public void logChanges(BaseAgent active){
        //Precondition, we know an attack just happened. Find out what went down.
        if(board.getAgentsTerritories(active).size() > territoriesOwned[playerIndex % players.length]){
            
            //Active player has one more territory.
            territoriesOwned[playerIndex % players.length] += 1;
            
            //In this block we are guaranteed that a territory exchanged hands
            BaseAgent loser = null;
            List<Territory> loserLands = null;
            for(int i = 0; i < players.length && loser == null; i++){
                //By virtue of the fact that this player is the winner we know it isn't the loser.
                if(i == playerIndex)
                    continue;
                
                //The loser will have one less territory than we remembered.
                BaseAgent a = players[i];
                List<Territory> aLands = board.getAgentsTerritories(a);
                if(aLands.size() < territoriesOwned[i]){
                    loser = a;
                    loserLands = aLands;
                    territoriesOwned[i] -= 1;
                }
            }
            
            //Log when players get eliminated.
            if(loserLands.isEmpty()){
                String report = String.format("%s lost its last territory to %s. %s is out.", 
                        loser, active, loser);
                Logger.log(report);
            } else {
                String report = String.format("%s lost a territory to %s.", loser, active);
                Logger.log(report);
            }
        } else {    //In the else clause, the active player lost a skirmish
            String r = String.format("%s failed to capture a territory with its attack.", active);
                Logger.log(r);
        }
    }
    
    public void setContinentBonuses(int a, int b, int c, int d) {
        board.setContinentABonus(a);
        board.setContinentBBonus(b);
        board.setContinentCBonus(c);
        board.setContinentDBonus(d);
    }
    
    public int getPercentControlled (BaseAgent a) {
        float percentControlled = 0;
        List<Territory> controlledTerritories = board.getAgentsTerritories(a);
        List<Territory> allTerritories = board.getAllTerritories();
        percentControlled =  ((float) controlledTerritories.size() / (float) allTerritories.size()) * 100;
        return (int) percentControlled;
    }
    
    public BaseAgent[] getPlayers() {
        return players;
    }
    
    public boolean hasPlayer1() {
        try {
            return players[0] != null;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean hasPlayer2() {
        try {
            return players[1] != null;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean hasPlayer3() {
        try {
            return players[2] != null;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean hasPlayer4() {
        try {
            return players[3] != null;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public BaseAgent getPlayer1() {
        return hasPlayer1() ? players[0] : null;
    }
    
    public BaseAgent getPlayer2() {
        return hasPlayer2() ? players[1] : null;
    }
    
    public BaseAgent getPlayer3() {
        return hasPlayer3() ? players[2] : null;
    }
    
    public BaseAgent getPlayer4() {
        return hasPlayer4() ? players[3] : null;
    }
    
    public BaseAgent getAgentTakingTurn() {
        return currentPlayer;
    }
    
    public STATE getCurrentState() {
        return currentState;
    }
    
    public BaseAgent getWinner() {
        if (isOver()) {
            for (BaseAgent a: players) {
                if (getPercentControlled(a) >= percentToWin)
                    return a;
            }
        }
        return null;
    }
}
