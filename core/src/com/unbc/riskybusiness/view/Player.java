/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.unbc.riskybusiness.agents.Agent;

/**
 *
 * @author leefoster
 */
public class Player {
    BitmapFont font = null;
    Texture pieceTexture = null;
    PlayerUI ui = null;
    Agent agent = null;
    PlayerColor color;
    float scale = 0.0f;
    
    public Player(PlayerColor color, float scale, Agent agent,BitmapFont font) {
        this.color = color;
        switch (color) {
            case BLUE: pieceTexture = new Texture("pieceBlue.png"); break;
            case RED: pieceTexture = new Texture("pieceRed.png"); break;
            case YELLOW: pieceTexture = new Texture("pieceYellow.png"); break;
            case GREEN: pieceTexture = new Texture("pieceGreen.png"); break;
        }
        this.agent = agent;
        this.scale = scale;
        this.font = font;
        
        ui = new PlayerUI(color, font);
    }
    
    /* batch.begin() is called in GameScreen draw() so it doesn't need to be 
       called here.
    */
    public void draw(Batch batch, OrthographicCamera camera) {
        ui.update(camera);
        ui.draw(batch);
    }
    
    public void startTurn() {
        ui.setTexture(ui.enabled);
    }
    
    public void endTurn() {
        ui.setTexture(ui.disabled);
    }
    
}
