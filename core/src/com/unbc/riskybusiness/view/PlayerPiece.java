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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author leefoster
 */
public class PlayerPiece extends Sprite {
    float scale;
    BitmapFont font;
    String unitCount;
    Player owner;
    
    public PlayerPiece(Texture t, float scale, BitmapFont font, String unitCount, Player owner) {
        super(new Sprite(t));
        setTexture(t);
        setSize(this.getWidth() * scale * 1.2f, this.getHeight() * scale * 1.2f);
        this.scale = scale;
        this.font = font;
        this.unitCount = unitCount;
        this.owner = owner;
    }
    
    
    public void setPosition(Vector2 newPosition) {
        setPosition(newPosition.x * scale - this.getWidth()/2,
                newPosition.y * scale - 30);
    }
    
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, 
                unitCount, 
                (getX()+getWidth()*0.5f) - font.getBounds(unitCount).width * 0.5f, 
                getY()+getHeight()*0.5f);
    }
}
