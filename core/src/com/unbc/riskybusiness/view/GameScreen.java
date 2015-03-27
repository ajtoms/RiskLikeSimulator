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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.models.Territory;
import com.unbc.riskybusiness.view.BoardView.BoardNode;
import static com.unbc.riskybusiness.view.GameState.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author leefoster
 */
public class GameScreen extends ScreenAdapter implements GestureListener, InputProcessor {

    private boolean firstRun = true;
    private final float scale = 1.0f;
    private float zoomLevel = 0.75f;
    
    private BoardView board = new BoardView("RiskMap.tmx");
    private Texture backgroundTexture = new Texture("water.png");
    private OrthographicCamera camera = new OrthographicCamera();
    
    private Player currentPlayer = null;
    
    private Player bluePlayer = null;
    private Player redPlayer = null;
    private Player yellowPlayer = null;
    private Player greenPlayer = null;
    
    private GameState currentState = GameState.START;
    
    private OrthogonalTiledMapRenderer renderer;
    private BitmapFont font;
    
    
    public GameScreen(ArrayList<Agent> agents, List<Territory> territories) {
       FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("opensans-bold.ttf"));
       FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
       font = generator.generateFont(parameter);
       font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
       generator.dispose(); 
       board.font = font; 
       board.scale = scale;
       
       if (agents.size() >= 1)
        this.bluePlayer = new Player(PlayerColor.BLUE,scale, agents.get(0),font);
       if (agents.size() >= 2)
        this.redPlayer = new Player(PlayerColor.RED,scale, agents.get(1),font);
       if (agents.size() >= 3)
        this.yellowPlayer = new Player(PlayerColor.YELLOW,scale,agents.get(2),font);
       if (agents.size() >= 4)
        this.greenPlayer = new Player(PlayerColor.GREEN,scale,agents.get(3),font);
       
       updateBoardView(territories);

    }
    
    @Override
    public void show() {
        renderer = new OrthogonalTiledMapRenderer(board.tiledMap, scale);
        
        board.font = font;
        
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
        
        // bluePlayer.setPosition(board.getNodeLocation("AA"));
       
        // Draw the tiled BG
        drawBackground(renderer.getBatch());
        
        // Draw the map
        renderer.render();
        
        // Draw the pieces and UI
        renderer.getBatch().begin();
        board.draw(renderer.getBatch(), camera);
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
                currentPlayer = bluePlayer;
                bluePlayer.startTurn();
                break;
            case GOTBLUEMOVE: 
                bluePlayer.endTurn();
                currentState = REDTURN;
                break;
            case REDTURN: 
                currentPlayer = redPlayer;
                redPlayer.startTurn();
                break;
            case GOTREDTURN: 
                redPlayer.endTurn();
                currentState = BLUETURN;
                break;
        }
    }
    
    private void updateBoardView(List<Territory> territories) {
       for (Territory t : territories) {
            if (bluePlayer != null && t.getOwner() == bluePlayer.agent) {
               board.updatePieces(t, bluePlayer);
            }
            else if (redPlayer != null && t.getOwner() == redPlayer.agent) {
               board.updatePieces(t, redPlayer);
            }
            else if (greenPlayer != null && t.getOwner() == greenPlayer.agent) {
               board.updatePieces(t, greenPlayer);
            }
            else if (yellowPlayer != null && t.getOwner() == yellowPlayer.agent) {
               board.updatePieces(t, yellowPlayer);
            }
        }
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
        // Set up the various coordinates
        Vector3 windowCoords = new Vector3(x,y,0);
        camera.unproject(windowCoords);
        Vector2 cameraCoords = new Vector2(windowCoords.x, windowCoords.y);
        
        for (BoardNode b : board.nodes.values()) {
            if (b.rect.contains(cameraCoords)) {
                // Initial selection has to be on one of these players tiles
                if (board.pieces.get(b.name).owner == currentPlayer) {
                    if (board.selectedNode == null)
                        board.setSelected(b);
                    else if (board.selectedNode == b)
                        board.deselect();
                }
                else if (board.pieces.get(b.name).owner != currentPlayer &&
                    b.adjacentTo(board.selectedNode)) {
                    board.deselect();
                    if (currentState == BLUETURN)
                        currentState = GOTBLUEMOVE;
                    if (currentState == REDTURN)
                        currentState = GOTREDTURN;
                }
                break;
            }
        }
        
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
