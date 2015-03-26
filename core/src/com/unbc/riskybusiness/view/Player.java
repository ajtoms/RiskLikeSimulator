/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author leefoster
 */
public class Player extends Sprite {
    private final BitmapFont font;
    
    String unitCount = "10";
    float scale;
    PlayerUI ui;
    
    public Player(PlayerColor color, float scale) {
        super(new Sprite(new Texture("pieceBlue.png")));
        Texture t = null;
        
        switch (color) {
            case BLUE: t = new Texture("pieceBlue.png"); break;
            case RED: t = new Texture("pieceRed.png"); break;
            case YELLOW: t = new Texture("pieceYellow.png"); break;
            case GREEN: t = new Texture("pieceGreen.png"); break;
        }
        
        this.scale = scale;
        setTexture(t);
        setSize(this.getWidth() * scale * 1.2f, this.getHeight() * scale * 1.2f);
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("opensans-bold.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        font = generator.generateFont(parameter);
        generator.dispose();
        ui = new PlayerUI(color, font);
    }

    public void setPosition(Vector2 newPosition) {
        setPosition(newPosition.x * scale - this.getWidth()/2,
                newPosition.y* scale - 30);
    }
    
    public void draw(Batch batch, OrthographicCamera camera) {
        super.draw(batch);
        ui.update(camera);
        ui.draw(batch);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, 
                unitCount, 
                (getX()+getWidth()*0.5f) - font.getBounds(unitCount).width * 0.5f, 
                getY()+getHeight()*0.5f);
    }
    
    public void startTurn() {
        ui.setTexture(ui.enabled);
    }
    
    public void endTurn() {
        ui.setTexture(ui.disabled);
    }
}
