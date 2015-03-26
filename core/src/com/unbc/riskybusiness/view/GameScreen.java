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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.unbc.riskybusiness.view.BoardView.BoardNode;
import static com.unbc.riskybusiness.view.GameState.*;
/**
 *
 * @author leefoster
 */
public class GameScreen extends ScreenAdapter implements GestureListener, InputProcessor{

    private boolean firstRun = true;
    private final float scale = 1.0f;
    private float zoomLevel = 0.75f;
    
    private BoardView board = new BoardView("RiskMap.tmx");
    private Texture backgroundTexture = new Texture("water.png");
    private OrthographicCamera camera = new OrthographicCamera();
    
    private Player bluePlayer = new Player(PlayerColor.BLUE, scale);
    private Player redPlayer = new Player(PlayerColor.RED, scale);
    private Player yellowPlayer = new Player(PlayerColor.YELLOW, scale);
    private Player greenPlayer = new Player(PlayerColor.GREEN, scale);
    private GameState currentState = GameState.START;
    
    private OrthogonalTiledMapRenderer renderer;
    
    @Override
    public void show() {
        renderer = new OrthogonalTiledMapRenderer(board.tiledMap, scale);
        
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.viewportWidth = (int)(Gdx.graphics.getWidth() / zoomLevel);
        camera.viewportHeight = (int)(Gdx.graphics.getHeight() / zoomLevel);
        camera.update();
        renderer.setView(camera);

        updateState();
        bluePlayer.setPosition(board.getNodeLocation("AA"));
        // Draw the tiled BG
        drawBackground(renderer.getBatch());
        
        // Draw the map
        renderer.render();
        
        // Draw the pieces and UI
        renderer.getBatch().begin();
        bluePlayer.draw(renderer.getBatch(), camera);
        redPlayer.draw(renderer.getBatch(), camera);
        renderer.getBatch().end();
    }
    
    
    private void updateState() {
        switch(currentState) {
            case START: 
                if (firstRun) {
                    camera.position.set(new Vector3(camera.viewportWidth/4,
                            camera.viewportHeight/2, 0));
                    firstRun = false;
                }
                currentState = BLUETURN;
                break;
            case BLUETURN:
                bluePlayer.startTurn();
                break;
            case GOTBLUEMOVE: 
                bluePlayer.endTurn();
                currentState = REDTURN;
                break;
            case REDTURN: 
                redPlayer.startTurn();
                break;
            case GOTREDTURN: 
                redPlayer.endTurn();
                currentState = BLUETURN;
                break;
        }
    }
    
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / zoomLevel;
        camera.viewportHeight = height / zoomLevel;
    }

    @Override
    public void dispose() {
        board.dispose();
        renderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 tmp3 = new Vector3(x,y,0);
        camera.unproject(tmp3);
        Vector2 tmp2 = new Vector2(tmp3.x, tmp3.y);
        for (BoardNode b : board.nodes.values()) {
            //System.out.println(b.rect);
            if (b.rect.contains(tmp2)) {
                System.out.println(b.name);
                break;
            }
        }
        if (currentState == BLUETURN)
            currentState = GOTBLUEMOVE;
        if (currentState == REDTURN)
            currentState = GOTREDTURN;
        return true;
    }

        @Override
    public boolean scrolled(int i) {
        zoomLevel += 0.25 * -i;
        if (zoomLevel < 0.5f)
            zoomLevel = 0.5f;
        if (zoomLevel > 10)
            zoomLevel = 10;
        return true;
    }

    private void drawBackground(Batch batch) {
        batch.begin();
        int xStart = (int) (camera.position.x - camera.viewportWidth/2);
        int xEnd = (int) (camera.position.x + camera.viewportWidth/2);
        int yStart = (int) (camera.position.y - camera.viewportHeight/2);
        int yEnd = (int) (camera.position.y + camera.viewportHeight/2);
        
        for (int i = xStart; i < xEnd; i+=backgroundTexture.getWidth()) {
            for (int j = yStart; j < yEnd; j+=backgroundTexture.getHeight()) {
                renderer.getBatch().draw(backgroundTexture, i, j);
            }
        }
        batch.end();
    }
    
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.position.set(camera.position.x + -deltaX,
                camera.position.y + deltaY,
                camera.position.z);
        
        return true;
    }
    
    @Override
    public boolean tap(float f, float f1, int i, int i1) {
        return false;
    }

    @Override
    public boolean longPress(float f, float f1) {
        return false;
    }

    @Override
    public boolean fling(float f, float f1, int i) {
        return false;
    }

    @Override
    public boolean panStop(float f, float f1, int i, int i1) {
        return false;
    }

    @Override
    public boolean zoom(float f, float f1) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vctr, Vector2 vctr1, Vector2 vctr2, Vector2 vctr3) {
        return false;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

}
