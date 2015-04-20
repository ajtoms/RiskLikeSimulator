package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;

/**
 *
 */
public abstract class Agent {
    
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
    
    public Agent() {
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
        takingTurn = true;
        finishedTurn = false;
        isReinforcing = false;
        isAttacking = false;
        isMoving = false;
    }
    
    public void setDead() {
        if (!isDead) {
            isDead = true;
        }
    }
    
    public void setDoneMoving() {
        pendingForce = null;
        selectedTerritory = null;
        isMoving = false;
    }
    
    public void setDoneAttacking() {
        pendingForce = null;
        selectedTerritory = null;
        isAttacking = false;
    }
    
    public void setDoneReinforcing() {
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
        isReinforcing = true;
        this.reinforcementsToPlace = reinforcments;
    }

    public void startAttacking() {
        isAttacking = true;
    }

    public void startMoving() {
        isMoving = true;
    }


}
