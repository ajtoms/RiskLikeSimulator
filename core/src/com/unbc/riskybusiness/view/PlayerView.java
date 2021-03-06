/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.HumanAgent;
import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.models.Force;
import com.unbc.riskybusiness.models.Territory;

/**
 *
 * @author leefoster
 */
public class PlayerView implements InputProcessor{

    public enum COLOR { BLUE, YELLOW, RED, GREEN };

    private COLOR color;
    private Agent playerAgent;
    private HumanAgent humanAgent;
    private GameController g;
    private MapView mapView;
    private Window uiWindow;
    private Label agentNameLabel;
    private Label percentControlledLabel;
    private Label stateLabel;
    private Label unitsLabel;
    private Label doneLabel;
    private Button doneStateButton;
    
    public PlayerView(COLOR color, Agent playerAgent, Stage stage, MapView mapView, GameController g) {
        this.color = color;
        this.playerAgent = playerAgent;
        if (playerAgent instanceof HumanAgent )
            humanAgent = (HumanAgent) playerAgent;
        this.g = g;
        this.mapView = mapView;
        uiWindow = new Window("", RiskLikeGame.getDeactiveWindowStyle(color));
        uiWindow.setWidth((float) (Gdx.graphics.getWidth() * .18));
        uiWindow.setHeight((float) (Gdx.graphics.getHeight() * .13));
        uiWindow.left();
        if (playerAgent != null) {
            
            agentNameLabel = new Label(playerAgent.toString() + ":", RiskLikeGame.getUiSkin(), "white");
            percentControlledLabel = new Label("0%", RiskLikeGame.getUiSkin(), "white");
            stateLabel = new Label("Start", RiskLikeGame.getUiSkin(), "white-small");
            unitsLabel = new Label("3 Units", RiskLikeGame.getUiSkin(), "white-small");
            doneLabel = new Label("Done Phase?", RiskLikeGame.getUiSkin(), "white-small");
            doneStateButton = new Button(RiskLikeGame.getUiSkin(), "grayCheck");
            doneStateButton.addListener( new ClickListener() {              
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setNextPlayerState();
                }
        });
            uiWindow.add(agentNameLabel).padLeft(2);
            uiWindow.add(percentControlledLabel).padLeft(15);
            uiWindow.row();
            uiWindow.add(stateLabel)
                    .right()
                    .width(140);
            uiWindow.add(unitsLabel);
            uiWindow.row();
            uiWindow.add(doneLabel);
            uiWindow.add(doneStateButton)
                    .width(30)
                    .height(30)
                    .left()
                    .padLeft(10);
            uiWindow.setPosition(getUIPosition(color, stage).x,
                getUIPosition(color, stage).y);
            stage.addActor(uiWindow);
        } else {
            //uiWindow.setStyle(RiskLikeGame.getDeactiveWindowStyle(null));
        }
    }

    public void update(Batch batch) {
        if (playerAgent == null)
            return;
        
        batch.begin();
        /* Update where the pieces are and draw them */
        for (Territory t : g.getBoard().getAgentsTerritories(playerAgent)) {
            PieceView p = new PieceView(t.getNumTroops(), RiskLikeGame.getPieceTexture(color));
            Vector2 center = new Vector2();
            RiskLikeGame.getTileLocation(t.getLocationName().toString()).getCenter(center);
            p.setCenter(center.x, center.y + 10);
            p.draw(batch);
        }
        batch.end();
        
        batch.begin();
        int percentControlled = g.getPercentControlled(playerAgent); 
        percentControlledLabel.setText(percentControlled + "%");
        
        // Dead
        if (playerAgent.isDead()) {
            uiWindow.setStyle(RiskLikeGame.getDeactiveWindowStyle(null));
            agentNameLabel.setStyle(RiskLikeGame.getUiSkin().get("default-big", LabelStyle.class));
            percentControlledLabel.setStyle(RiskLikeGame.getUiSkin().get("default", LabelStyle.class));
        }
        // My turn
        else if (g.getAgentTakingTurn() == playerAgent) {
            uiWindow.setStyle(RiskLikeGame.getActiveWindowStyle(color));
            doneStateButton.setVisible(true);
            doneStateButton.setDisabled(false);
            stateLabel.setVisible(true);
            doneLabel.setVisible(true);
            unitsLabel.setVisible(true);
            
            // Selecting tiles for humans are handled by the click listener
            if (humanAgent == null) {
                doneStateButton.setVisible(false);
                doneLabel.setVisible(false);
                if (playerAgent.getSelectedTerritory() != mapView.getSelectedTerritory())
                    mapView.deselectTerritory(mapView.getSelectedTerritory());
                mapView.setSelectedTerritory(playerAgent.getSelectedTerritory());
            }
            
            Pixmap p = new Pixmap(Gdx.files.internal("Sprite/defaultCursor.png"));
            switch (g.getCurrentState()) {
                case REINFORCING:
                    if (humanAgent != null)
                        mapView.deselectTerritory(mapView.getSelectedTerritory());
                    p = new Pixmap(Gdx.files.internal("Sprite/plusCursor.png"));
                    stateLabel.setText("Reinforcing:");
                    unitsLabel.setText(playerAgent.getReinforcements() + " units");
                    break;
                case ATTACKING:
                    p = new Pixmap(Gdx.files.internal("Sprite/swordCursor.png"));
                    stateLabel.setText("Attacking:");
                    if (playerAgent.getPendingForce() == null)
                        unitsLabel.setText("0 units");
                    else
                        unitsLabel.setText(playerAgent.getPendingForce().getTroops() + " units");
                    break;
                case MOVING:
                    p = new Pixmap(Gdx.files.internal("Sprite/gauntletCursor.png"));
                    stateLabel.setText("Moving:");
                    if (playerAgent.getPendingForce() == null)
                        unitsLabel.setText("0 units");
                    else
                        unitsLabel.setText(playerAgent.getPendingForce().getTroops() + " units");
                    break;
            }
            Gdx.input.setCursorImage(p, 0, 0);
        }
        // Not my turn
        else {
            uiWindow.setStyle(RiskLikeGame.getDeactiveWindowStyle(color));
            doneStateButton.setVisible(false);
            stateLabel.setVisible(false);
            doneLabel.setVisible(false);
            unitsLabel.setVisible(false);
        }
        batch.end();
    }

    private Vector2 getUIPosition(COLOR color, Stage stage) {
        float x = 0;
        float y = 0;
        switch (color) {
            case BLUE:
                x = stage.getCamera().position.x - stage.getCamera().viewportWidth / 2;
                y = stage.getCamera().position.y + stage.getCamera().viewportHeight / 2;
                break;
            case RED:
                x = stage.getCamera().position.x + stage.getCamera().viewportWidth / 2;
                y = stage.getCamera().position.y + stage.getCamera().viewportHeight / 2;
                break;
            case GREEN:
                x = stage.getCamera().position.x + stage.getCamera().viewportWidth / 2;
                y = stage.getCamera().position.y - stage.getCamera().viewportHeight / 2;
                break;
            case YELLOW:
                x = stage.getCamera().position.x - stage.getCamera().viewportWidth / 2;
                y = stage.getCamera().position.y - stage.getCamera().viewportHeight / 2;
                break;

        }
        return new Vector2(x, y);
    }
    
        
    // Only enabled on this players turn and if it is human.  Called when
    // the check box is clicked.
    public void setNextPlayerState() {
        mapView.deselectTerritory(playerAgent.getSelectedTerritory());
        playerAgent.setSelectedTerritory(null);
        playerAgent.setPendingForce(null);
        switch (g.getCurrentState()) {
            case REINFORCING:
                playerAgent.setDoneReinforcing();
                break;
            case ATTACKING: 
                playerAgent.setDoneAttacking();
                break;
            case MOVING:
                playerAgent.setDoneMoving();
                break;
        }
    }
    
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (g.getAgentTakingTurn() == playerAgent && humanAgent != null) {
            Vector3 screenCords = new Vector3(screenX, screenY, 0);
            mapView.getCamera().unproject(screenCords);
            Vector2 cameraCoords = new Vector2(screenCords.x, screenCords.y);
            String tileName = RiskLikeGame.getTileNameAtLocation(cameraCoords);
            Territory t = g.getBoard().getTerritoryNamed(tileName);
            // Only handle touch if its my turn and if there is infact a tile where I clicked
            if(tileName != null) {
                switch (g.getCurrentState()) {
                    case REINFORCING:
                        if (t.getOwner() == playerAgent) {
                            int availableReinforcements = humanAgent.getReinforcements();
                            t.reinforce(1);
                            availableReinforcements--;
                            humanAgent.setReinforcements(availableReinforcements);
                            if (availableReinforcements == 0)
                                    humanAgent.setDoneReinforcing();
                        }
                        break;
                    case ATTACKING:
                        // First time selecting one of my territories
                        if (t.getOwner() == playerAgent 
                                && playerAgent.getSelectedTerritory() == null
                                && t.getNumTroops() > 1) {
                            playerAgent.setSelectedTerritory(t);
                            mapView.setSelectedTerritory(t);
                            playerAgent.setPendingForce(new Force(playerAgent, 0));
                            if (t.getNumTroops() > 1)
                                playerAgent.getPendingForce().incrementTroops();
                        }
                        // Selecting my territory to choose more troops
                        else if (t == playerAgent.getSelectedTerritory()) {
                            if (playerAgent.getSelectedTerritory().getNumTroops() > 1
                                    && playerAgent.getPendingForce().getTroops() != playerAgent.getSelectedTerritory().getNumTroops()-1)
                                playerAgent.getPendingForce().incrementTroops();
                        }
                        // Selecting nonadjacent territory
                        else if (g.getBoard().isAdjacentTo(playerAgent.getSelectedTerritory(), t) == false) {
                            mapView.deselectTerritory(playerAgent.getSelectedTerritory());
                            playerAgent.setSelectedTerritory(null);
                            playerAgent.setPendingForce(null);
                        }
                        // Selecting adjacent enemy territory to attack
                        else if (g.getBoard().isAdjacentTo(playerAgent.getSelectedTerritory(), t)
                                && t.getOwner() != playerAgent
                                && playerAgent.getPendingForce() != null
                                && playerAgent.getPendingForce().getTroops() >= 1) {
                            int attackingForceTroops = playerAgent.getPendingForce().getTroops();
                            Force enemyForce = new Force(t.getOwner(), t.getNumTroops());
                            Force resultingForce = playerAgent.getPendingForce().attack(enemyForce);
                            playerAgent.getSelectedTerritory().setTroops(playerAgent.getSelectedTerritory().getNumTroops() - attackingForceTroops);
                            // I won
                            if (resultingForce.getOwner() == playerAgent) {
                                t.changeOwner(resultingForce);
                            }
                            // They won
                            else {
                                t.setTroops(enemyForce.getTroops());
                            }
                            playerAgent.setPendingForce(null);
                            mapView.deselectTerritory(playerAgent.getSelectedTerritory());
                            playerAgent.setSelectedTerritory(null);
                        }
                        break;
                    case MOVING:
                        // First time selecting one of my territories
                        if (t.getOwner() == playerAgent 
                                && playerAgent.getSelectedTerritory() == null
                                && t.getNumTroops() > 1) {
                            playerAgent.setSelectedTerritory(t);
                            mapView.setSelectedTerritory(playerAgent.getSelectedTerritory());
                            playerAgent.setPendingForce(new Force(playerAgent, 0));
                            if (t.getNumTroops() > 1)
                                playerAgent.getPendingForce().incrementTroops();
                        }
                        // Selecting my territory to choose more troops
                        else if (t == playerAgent.getSelectedTerritory()) {
                            if (playerAgent.getSelectedTerritory().getNumTroops() > 1
                                    && playerAgent.getPendingForce().getTroops() != playerAgent.getSelectedTerritory().getNumTroops()-1)
                                playerAgent.getPendingForce().incrementTroops();
                        }
                        // Selecting nonadjacent territory
                        else if (g.getBoard().isAdjacentTo(playerAgent.getSelectedTerritory(), t) == false) {
                            mapView.deselectTerritory(playerAgent.getSelectedTerritory());
                            playerAgent.setSelectedTerritory(null);
                            playerAgent.setPendingForce(null);
                        }
                        // Selecting adjacent territory to move to
                        else if (g.getBoard().isAdjacentTo(playerAgent.getSelectedTerritory(), t)
                                && t.getOwner() == playerAgent
                                && playerAgent.getPendingForce() != null
                                && playerAgent.getPendingForce().getTroops() >= 1) {
                            t.reinforce(playerAgent.getPendingForce().getTroops());
                            playerAgent.getSelectedTerritory().setTroops(playerAgent.getSelectedTerritory().getNumTroops() 
                                    - playerAgent.getPendingForce().getTroops());
                            playerAgent.setPendingForce(null);
                            mapView.deselectTerritory(playerAgent.getSelectedTerritory());
                            playerAgent.setSelectedTerritory(null);
                            setNextPlayerState();
                        }
                        break;
                }
            }
            else if (tileName == null || g.getBoard().isAdjacentTo(playerAgent.getSelectedTerritory(), t) == false) {
                mapView.deselectTerritory(playerAgent.getSelectedTerritory());
                playerAgent.setSelectedTerritory(null);
                playerAgent.setPendingForce(null);
            }
            return true;
        }
        
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    private class PieceView extends Sprite {
        private String units;
        
        public PieceView (int units, Texture t) {
            super(new Sprite(t));
            setTexture(t);
            setSize(this.getWidth() * RiskLikeGame.getScale() * 1.2f, 
                    this.getHeight() * RiskLikeGame.getScale() * 1.2f);
            this.units = units+"";
        }
        
        @Override
        public void draw(Batch batch) {
            super.draw(batch);
            RiskLikeGame.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
            float x = (getX()+getWidth()*0.5f) - RiskLikeGame.getFont().getBounds(units).width * 0.5f;
            float y = getY()+getHeight()*0.5f;
            RiskLikeGame.getFont().draw(batch, units, x , y);
        }
    }
    
    public Agent getAgent() {
        return playerAgent;
    }
}
