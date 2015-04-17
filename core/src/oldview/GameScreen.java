/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldview;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.models.Territory;
import oldview.BoardView;
import com.unbc.riskybusiness.view.GameState;
import com.unbc.riskybusiness.view.GameState;
import com.unbc.riskybusiness.view.RiskLikeGame;
import com.unbc.riskybusiness.view.RiskLikeGame;
import oldview.PlayerView;
import java.util.List;
/**
 *
 * @author leefoster
 */
public class GameScreen extends ScreenAdapter implements GestureListener, InputProcessor {

    private boolean firstRun = true;
    private final float scale = 1.0f;
    private float zoomLevel = 0.75f;
    
    private BoardView boardView = new BoardView("RiskMap.tmx");
    private Texture backgroundTexture = new Texture("water.png");
    private OrthographicCamera camera = new OrthographicCamera();
    
    private PlayerView bluePlayerView;
    private PlayerView redPlayerView;
    private PlayerView yellowPlayerView;
    private PlayerView greenPlayerView;
    
    private GameState currentState;
    
    private OrthogonalTiledMapRenderer renderer;
    
    private GameController gameController;
    private RiskLikeGame game;
    private Stage uiStage;
    
    public GameScreen(GameController g, RiskLikeGame game) {
       this.gameController = g;
       this.game = game;
       this.boardView.setFont(game.getFont()); 
       this.boardView.setScale(scale);
       this.uiStage = new Stage();
       
       if (g.getPlayers().length >= 1) {
           //bluePlayerView = new PlayerView(PlayerView.COLOR.BLUE, g.getPlayers()[0], game.getFont());
           bluePlayerView.setUIWindow(uiStage);
       }
       if (g.getPlayers().length >= 2) {
        //redPlayerView = new PlayerView(PlayerView.COLOR.RED, g.getPlayers()[1], game.getFont());
        redPlayerView.setUIWindow(uiStage);
       }
       if (g.getPlayers().length >= 3) {
         //yellowPlayerView = new PlayerView(PlayerView.COLOR.YELLOW, g.getPlayers()[2], game.getFont());
         yellowPlayerView.setUIWindow(uiStage);
       }
       if (g.getPlayers().length >= 4) {
         //  greenPlayerView = new PlayerView(PlayerView.COLOR.GREEN, g.getPlayers()[3], game.getFont());
           greenPlayerView.setUIWindow(uiStage);
       }
       
       g.play();
    }
    
    @Override
    public void show() {
        renderer = new OrthogonalTiledMapRenderer(boardView.getTileMap(), scale);
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

        // Update the boar view, the ui and the game state
        updateBoardView();
        updateUI();
        updateGameState();
               
        // Draw the tiled BG
        drawBackground(renderer.getBatch());
        
        // Draw the tiled map
        renderer.render();
        // Draw the UI
        uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        uiStage.draw();
        
        // Draw the territories
        renderer.getBatch().begin();
        boardView.drawTerritories(renderer.getBatch(), camera);
        renderer.getBatch().end();
    }
    
    
    private void updateGameState() {
//        switch(currentState) {
//            case START: 
//                if (firstRun) {
//                    camera.position.set(new Vector3(camera.viewportWidth/4,
//                            camera.viewportHeight/2, 0));
//                    firstRun = false;
//                }
//                currentState = BLUETURN;
//                break;
//            case BLUETURN:
//                currentPlayer = bluePlayer;
//                bluePlayer.startTurn();
//                break;
//            case GOTBLUEMOVE: 
//                bluePlayer.endTurn();
//                currentState = REDTURN;
//                break;
//            case REDTURN: 
//                currentPlayer = redPlayer;
//                redPlayer.startTurn();
//                break;
//            case GOTREDTURN: 
//                redPlayer.endTurn();
//                currentState = BLUETURN;
//                break;
//        }
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
        boardView.dispose();
        renderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // Set up the various coordinates
        Vector3 windowCoords = new Vector3(x,y,0);
        camera.unproject(windowCoords);
        Vector2 cameraCoords = new Vector2(windowCoords.x, windowCoords.y);
        
//        for (BoardNode b : boardView.getNodes().values()) {
//            if (b.rect.contains(cameraCoords)) {
//                // Initial selection has to be on one of these players tiles
//                if (board.pieces.get(b.name).owner == currentPlayer) {
//                    if (board.selectedNode == null)
//                        board.setSelected(b);
//                    else if (board.selectedNode == b)
//                        board.deselect();
//                }
//                else if (board.pieces.get(b.name).owner != currentPlayer &&
//                    b.adjacentTo(board.selectedNode)) {
//                    board.deselect();
//                    if (currentState == BLUETURN)
//                        currentState = GOTBLUEMOVE;
//                    if (currentState == REDTURN)
//                        currentState = GOTREDTURN;
//                }
//                break;
//            }
//        }
        
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

    private void updateBoardView() {
//        if (bluePlayerView != null) {
//            List<Territory> bluePlayerTerritories = gameController.getBoard().getAgentsTerritories(bluePlayerView.getAgent());
//            for (Territory t : bluePlayerTerritories)
//                boardView.updateTerritories(t, bluePlayerView);
//        }
//        if (greenPlayerView != null) {
//            List<Territory> greenPlayerTerritories = gameController.getBoard().getAgentsTerritories(greenPlayerView.getAgent());
//            for (Territory t : greenPlayerTerritories)
//                boardView.updateTerritories(t, greenPlayerView);
//        }
//        if (yellowPlayerView != null) {
//            List<Territory> yellowPlayerTerritories = gameController.getBoard().getAgentsTerritories(yellowPlayerView.getAgent());
//            for (Territory t : yellowPlayerTerritories)
//                boardView.updateTerritories(t, yellowPlayerView);
//        }
//        if (redPlayerView != null) {
//            List<Territory> redPlayerTerritories = gameController.getBoard().getAgentsTerritories(redPlayerView.getAgent());
//            for (Territory t : redPlayerTerritories)
//                boardView.updateTerritories(t, redPlayerView);
//        }
    }

    // Makes player gray if they have no territory left
    // Update percent controlled
    // Make current player ui appear raised
    // Make non current player appear lower
    private void updateUI() {
        
    }

}
