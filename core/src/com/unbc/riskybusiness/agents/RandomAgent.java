/*
 * Agent with Aggresive Random behaviour.
 * Reinforcments: Randomly picks spots to reinforce
 * Attacking: Will attack always if there is one.  If multiple attacks are possible it will randomly choose one.
 * Moving: Picks a spot it controls and moves a random number to a random adjacent spot.
 */
package com.unbc.riskybusiness.agents;

import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author leefoster
 */
public class RandomAgent extends BaseAgent {

    @Override
    public void startReinforcing(int reinforcments) {
        super.startReinforcing(reinforcments);
        
        List<Territory> myTerritories = gameController.getBoard().getAgentsTerritories(this);
        Random r = new Random();
        for (int i = 0; i < reinforcments; i++) {
            Territory terr = myTerritories.get(r.nextInt(myTerritories.size()));
            selectedTerritory = terr;
            terr.setTroops(terr.getNumTroops() + 1);
            this.reinforcementsToPlace--;
            sleep();
        }

        setDoneReinforcing();
    }

    @Override
    public void startAttacking() {
        super.startAttacking();
        
        List<Territory> myTerritories = gameController.getBoard().getAgentsTerritories(this);
        Random r = new Random();
        
        for (Territory myTerritory : myTerritories) {
            List<Territory> enemyTerritories = getAdjacentEnemyTerritories(myTerritory);
            if (enemyTerritories.size() > 0) {
                Territory enemyTerritory = enemyTerritories.get(r.nextInt(enemyTerritories.size()));
                int attackingForceTroops = myTerritory.getNumTroops()-1;
                // Make sure we don't try to attack 
                if (attackingForceTroops > 0) {
                    selectedTerritory = myTerritory;
                    pendingForce = new Force(this, attackingForceTroops);
                    // Sleep here to show that where we are attacking and with how much before the attack
                    sleep();
                    Force enemyForce = new Force(enemyTerritory.getOwner(), enemyTerritory.getNumTroops());
                    Force resultingForce = pendingForce.attack(enemyForce);
                    myTerritory.setTroops(myTerritory.getNumTroops() - attackingForceTroops);
                    // I won
                    if (resultingForce.getOwner() == this) {
                        enemyTerritory.changeOwner(resultingForce);
                    } // They won
                    else {
                        enemyTerritory.setTroops(enemyForce.getTroops());
                    }
                }
            }
        }

        setDoneAttacking();
    }

    @Override
    public void startMoving() {
        super.startMoving();
        
        Random r = new Random();
        List<Territory> myTerrirotires = gameController.getBoard().getAgentsTerritories(this);
        Territory moveFrom = myTerrirotires.get(r.nextInt(myTerrirotires.size()));
        List<Territory> myAdjacent = getAdjacentOwnedTerritories(moveFrom);
        if (myAdjacent.size() > 0) {
            Territory moveTo = myAdjacent.get(r.nextInt(myAdjacent.size()));
            int numberToMove = moveFrom.getNumTroops()-1;
            if (numberToMove > 0) {
            selectedTerritory = moveFrom;
            pendingForce = moveFrom.buildForce(numberToMove);
            sleep();
            moveTo.settleForce(pendingForce);
            }
        }
        setDoneMoving();
    }

    private synchronized List<Territory> getAdjacentEnemyTerritories(Territory terriroty) {
        List<Territory> adjacent = gameController.getBoard().getAdjacentTerritories(terriroty);
        List<Territory> enemyTerritories = new ArrayList<Territory>();
        for (int i = 0; i < adjacent.size(); i++) {
            if (adjacent.get(i).getOwner() != this) {
                enemyTerritories.add(adjacent.get(i));
            }
        }
        return enemyTerritories;
    }
    
        private synchronized List<Territory> getAdjacentOwnedTerritories(Territory terriroty) {
        List<Territory> adjacent = gameController.getBoard().getAdjacentTerritories(terriroty);
        List<Territory> ownedByMe = new ArrayList<Territory>();
        for (int i = 0; i < adjacent.size(); i++) {
            if (adjacent.get(i).getOwner() == this) {
                ownedByMe.add(adjacent.get(i));
            }
        }
        return ownedByMe;
    }
        
    @Override
    public String toString() {
        return "Random"+myId;
    }
}
