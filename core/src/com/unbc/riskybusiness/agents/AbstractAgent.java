/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.agents;

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
    
    public synchronized boolean isTakingTurn() {
        return takingTurn;
    }
    
    public synchronized boolean isReinforcing() {
        return isReinforcing;
    }
    
    public synchronized void setDoneReinforcing() {
        isReinforcing = false;
    }
    
    public synchronized boolean isAttacking() {
        return isAttacking;
    }
    
    public synchronized void setDoneAttacking() {
        isAttacking = false;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public synchronized void setDoneMoving() {
        isMoving = false;
    }
    
    public synchronized boolean isDoneTakingTurn() {
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
    
    public abstract void startReinforcing(int reinforcments);

    public abstract void startAttacking();

    public abstract void startMoving();


}
