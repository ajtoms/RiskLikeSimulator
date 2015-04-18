/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;

/**
 *
 * @author leefoster
 */
public abstract class AbstractAgent {
    
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
    
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    
    public synchronized boolean isTakingTurn() {
        return takingTurn;
    }
    
    public synchronized boolean isReinforcing() {
        return isReinforcing;
    }
    
    public synchronized void setDoneReinforcing() {
        pendingForce = null;
        selectedTerritory = null;
        isReinforcing = false;
    }
    
    public synchronized boolean isAttacking() {
        return isAttacking;
    }
    
    public synchronized void setDoneAttacking() {
        pendingForce = null;
        selectedTerritory = null;
        isAttacking = false;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public synchronized void setDoneMoving() {
        pendingForce = null;
        selectedTerritory = null;
        isMoving = false;
    }
    
    public synchronized boolean isDoneTakingTurn() {
        pendingForce = null;
        selectedTerritory = null;
        return finishedTurn;
    }
    
    public synchronized void setDead() {
        isDead = true;
    }
    
    public synchronized boolean isDead() {
        return isDead;
    }
    
    public synchronized void setTakingTurn() {
        takingTurn = true;
        finishedTurn = false;
        isReinforcing = false;
        isAttacking = false;
        isMoving = false;
    }
    
    public synchronized void doneTurn() {
        takingTurn = false;
        finishedTurn = false;
        isReinforcing = false;
        isAttacking = false;
        isMoving = false;
    }
    
    public int getReinforcements() {
        return reinforcementsToPlace;
    }
            
    public Territory getSelectedTerritory() {
        return selectedTerritory;
    }
    
    public void setSelectedTerritory(Territory selectedTerritory) {
        this.selectedTerritory = selectedTerritory;
    }
    
    public Force getPendingForce() {
        return pendingForce;
    }
    
    public void setPendingForce(Force f) {
        pendingForce = f;
    }
    
    public void sleep() {
        try {
            Thread.sleep(gameController.aiDelaySeconds * 1000);
        }
        catch (Exception e) {

        }
    }
    
    public abstract void startReinforcing(int reinforcments);

    public abstract void startAttacking();

    public abstract void startMoving();


}
