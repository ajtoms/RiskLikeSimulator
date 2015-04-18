/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.main.Logger;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;

/**
 *
 * @author leefoster
 */
public abstract class BaseAgent {
    
    protected static int ID = 0;
    protected int myId;
    
    protected boolean takingTurn;
    protected boolean finishedTurn;
    protected boolean isReinforcing;
    protected boolean isAttacking;
    protected boolean isMoving;
    protected boolean isDead;
    protected int reinforcementsToPlace;
    protected GameController gameController;
    
    // Used by GUI to show what territory this agent is dealing with
    protected Territory selectedTerritory;
    
    // Used by GUI to show how many forces the adgent is moving or attacking
    protected Force pendingForce;
    
    public BaseAgent() {
        this.myId = ID++;
    }
    
    
    public int getReinforcements() {
        return reinforcementsToPlace;
    }
            
    public Territory getSelectedTerritory() {
        return selectedTerritory;
    }
    
    public Force getPendingForce() {
        return pendingForce;
    }
    
    public boolean isTakingTurn() {
        return takingTurn;
    }
    
    public boolean isReinforcing() {
        return isReinforcing;
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public boolean isDoneTakingTurn() {
        pendingForce = null;
        selectedTerritory = null;
        return finishedTurn;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public void doneTurn() {
        gameController.logChanges(this);
        Logger.log(String.format("%s is finished their turn.", this));
        takingTurn = false;
        finishedTurn = false;
        isReinforcing = false;
        isAttacking = false;
        isMoving = false;
    }
    
    public void setSelectedTerritory(Territory selectedTerritory) {
        this.selectedTerritory = selectedTerritory;
    }
    
    public void setPendingForce(Force f) {
        pendingForce = f;
    }
    
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    
    public void setTakingTurn() {
        Logger.log(String.format("%s begins their turn.", this));
        takingTurn = true;
        finishedTurn = false;
        isReinforcing = false;
        isAttacking = false;
        isMoving = false;
    }
    
    public void setDead() {
        if (!isDead) {
            Logger.log(String.format("%s has died.", this));
            isDead = true;
        }
    }
    
    public void setDoneMoving() {
        Logger.log(String.format("%s is done tactical moving.", this));
        pendingForce = null;
        selectedTerritory = null;
        isMoving = false;
    }
    
    public void setDoneAttacking() {
        Logger.log(String.format("%s is done attacking.", this));
        pendingForce = null;
        selectedTerritory = null;
        isAttacking = false;
    }
    
    public void setDoneReinforcing() {
        Logger.log(String.format("%s is done reinforcing.", this));
        pendingForce = null;
        selectedTerritory = null;
        isReinforcing = false;
    }
    
    public void sleep() {
        try {
            Thread.sleep(gameController.aiDelaySeconds * 1000);
        }
        catch (Exception e) {

        }
    }
    
    public void startReinforcing(int reinforcments) {
        Logger.log(String.format("%s gets %d reinforcements.", this, reinforcments));
        isReinforcing = true;
        this.reinforcementsToPlace = reinforcments;
    }

    public void startAttacking() {
        Logger.log(String.format("%s will now attack.", this));
        isAttacking = true;
    }

    public void startMoving() {
        Logger.log(String.format("%s is done attacking and makes a tactical move.", this));
        isMoving = true;
    }


}
