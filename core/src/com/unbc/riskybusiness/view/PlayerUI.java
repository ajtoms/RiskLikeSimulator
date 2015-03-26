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

/**
 *
 * @author leefoster
 */
public class PlayerUI extends Sprite {
    private final BitmapFont font;
    PlayerColor color;
    Texture enabled;
    Texture disabled;
    
    public PlayerUI(PlayerColor color, BitmapFont font) {
        super(new Sprite(new Texture("blueEnabled.png")));
        this.font = font;
        switch (color) {
            case BLUE: 
                enabled = new Texture("blueEnabled.png");
                disabled = new Texture("blueDisabled.png");
                break;
            case RED: 
                enabled = new Texture("redEnabled.png");
                disabled = new Texture("redDisabled.png");
                break;
            case YELLOW: 
                enabled = new Texture("yellowEnabled.png");
                disabled = new Texture("yellowDisabled.png");
                break;
            case GREEN: 
                enabled = new Texture("greenEnabled.png");
                disabled = new Texture("greenDisabled.png");
                break;
        }
        
        setTexture(disabled);
        this.color = color;
    }
    
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void update(OrthographicCamera camera) {
        setSize(camera.viewportWidth * 0.2f, camera.viewportHeight * 0.1f);
        switch (color) {
            case BLUE: 
                setLeftTop(camera);
                break;
            case RED: 
                setRightTop(camera);
                break;
            case YELLOW: 
                setBottomLeft(camera);
                break;
            case GREEN: 
                setBottomRight(camera);
                break;
        }
    }

    private void setLeftTop(OrthographicCamera camera) {
        setPosition(camera.position.x - camera.viewportWidth/2 + (float)(camera.viewportWidth * .01), 
                    camera.position.y + camera.viewportHeight/2 - getHeight() - (float)(camera.viewportWidth * .02));

    }

    private void setRightTop(OrthographicCamera camera) {
        setPosition(camera.position.x + camera.viewportWidth/2 - getWidth() - (float)(camera.viewportWidth * .01) , 
                    camera.position.y + camera.viewportHeight/2 - getHeight() - (float)(camera.viewportWidth * .02));

    }

    private void setBottomLeft(OrthographicCamera camera) {
        setPosition(camera.position.x - camera.viewportWidth/2 + (float)(camera.viewportWidth * .01), 
                    camera.position.y - camera.viewportHeight/2 + (float)(camera.viewportWidth * .02));
    }

    private void setBottomRight(OrthographicCamera camera) {
       setPosition(camera.position.x + camera.viewportWidth/2 - getWidth() - (float)(camera.viewportWidth * .01), 
                    camera.position.y - camera.viewportHeight/2 + (float)(camera.viewportWidth * .02));
}

}
