/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.unbc.riskybusiness.controllers.GameController;

/**
 *
 * @author leefoster
 */
public class PlayScreen extends ScreenAdapter implements GestureListener, InputProcessor  {
    private GameController gameController;
    private RiskLikeGame game;
    private PlayerView bluePlayer;
    private PlayerView redPlayer;
    private PlayerView greenPlayer;
    private PlayerView yellowPlayer;
    
    private SpriteBatch spriteBatch;
    private MapView mapView;
    private Stage uiStage;
    
    private GameOverDialog gameOverDialog;
    
    public PlayScreen(GameController gameController, RiskLikeGame game) {
        this.gameController = gameController;
        this.game = game;
        
        spriteBatch = new SpriteBatch();
        mapView = new MapView(spriteBatch);
        mapView.setCameraPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        uiStage = new Stage();
        
        bluePlayer = new PlayerView(PlayerView.COLOR.BLUE, gameController.getPlayer1(), uiStage, mapView, gameController);
        redPlayer = new PlayerView(PlayerView.COLOR.RED, gameController.getPlayer2(), uiStage, mapView, gameController);
        greenPlayer = new PlayerView(PlayerView.COLOR.GREEN, gameController.getPlayer3(), uiStage, mapView, gameController);
        yellowPlayer = new PlayerView(PlayerView.COLOR.YELLOW, gameController.getPlayer4(), uiStage, mapView, gameController);
    }
    
    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().setScreenSize(width, height);
    }
    
    @Override
    public void show() {
        GestureDetector gd = new GestureDetector(this);
        
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(uiStage);
        inputMultiplexer.addProcessor(bluePlayer);
        inputMultiplexer.addProcessor(redPlayer);
        inputMultiplexer.addProcessor(yellowPlayer);
        inputMultiplexer.addProcessor(greenPlayer);
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCursorPosition(0, 0);
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                gameController.play();
            }
        });  
        t1.start();
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        mapView.render();
        
        bluePlayer.update(spriteBatch);
        redPlayer.update(spriteBatch);
        greenPlayer.update(spriteBatch);
        yellowPlayer.update(spriteBatch);
        
        spriteBatch.begin();
        uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        if (gameController.isOver() && gameOverDialog == null) {
            gameOverDialog = new GameOverDialog("", RiskLikeGame.getUiSkin(), "grayDown");
            if (gameController.getWinner() == bluePlayer.getAgent())
                gameOverDialog.setLabelText("Blue Player Wins!");
            if (gameController.getWinner() == redPlayer.getAgent())
                gameOverDialog.setLabelText("Red Player Wins!");
            if (gameController.getWinner() == greenPlayer.getAgent())
                gameOverDialog.setLabelText("Green Player Wins!");
            if (gameController.getWinner() == yellowPlayer.getAgent())
                gameOverDialog.setLabelText("Yellow Player Wins!");
            gameOverDialog.setButton("Back", this);
            gameOverDialog.show(uiStage);
            Gdx.input.setCursorImage(null, 0, 0);
        }
        
        uiStage.draw();
        spriteBatch.end();
        
    }
    
    @Override
    public void dispose() {
        gameController = null;
        spriteBatch.dispose();
        bluePlayer = null;
        greenPlayer = null;
        yellowPlayer = null;
        redPlayer = null;
        uiStage.dispose();
        gameOverDialog = null;
    }
    
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        mapView.moveCameraPosition(deltaX, deltaY);
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        RiskLikeGame.setZoomLevel( (int) distance);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        float deltaX = pointer2.x - pointer1.x;
        RiskLikeGame.setZoomLevel((int) deltaX);
        return true;
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
        RiskLikeGame.setZoomLevel(amount);
        return true;
    }
    
    private class GameOverDialog extends Dialog {
        
        public GameOverDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }
        
        protected void setButton(String buttonText, PlayScreen screen) {
            button(buttonText, screen, RiskLikeGame.getUiSkin().get("gray", TextButtonStyle.class));
        }
        
        protected void setLabelText(String labelText) {
            text(labelText).center().padLeft(10).padRight(10);
        }
        
        @Override
        protected void result(Object object) {
            PlayScreen screen = (PlayScreen) object;
            screen.game.setScreen(new SetupScreen(game));
        }
        
    }

}
