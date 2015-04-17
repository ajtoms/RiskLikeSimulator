/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.HumanAgent;
import com.unbc.riskybusiness.agents.RandomAgent;
import com.unbc.riskybusiness.view.RiskLikeGame;
import com.unbc.riskybusiness.view.RiskLikeGame;

/**
 *
 * @author leefoster
 */
public class PlayerView {

    public enum COLOR { GREEN,BLUE,RED,YELLOW }
    
    private BitmapFont font;
    private COLOR color;
    private Agent agent;
    private Texture pieceTexture;
    private Window window;
    private Table content;
    private TextButton button;
    
    public PlayerView(COLOR color, Agent agent, BitmapFont font) {
        this.color = color;
        this.agent = agent;
        this.font = font;
    }
    
    public Window setUIWindow(Stage stage) {
        this.window = new Window("", RiskLikeGame.getUiSkin(),"ui");
        window.setSize(stage.getCamera().viewportWidth * 0.2f, stage.getCamera().viewportHeight * 0.1f);
        this.content = new Table();
        float x,y;
        
        // Set window position, piece texture, button style
        switch (this.color) {
            case BLUE: 
                this.button = new TextButton("", RiskLikeGame.getUiSkin(), "blueUI");
                this.pieceTexture = new Texture("Sprite/pieceBlue.png");
                x = stage.getCamera().position.x - stage.getCamera().viewportWidth/2 
                        + (float)(stage.getCamera().viewportWidth * .01);
                y = stage.getCamera().position.y + stage.getCamera().viewportHeight/2 
                        - window.getHeight() - (float)(stage.getCamera().viewportWidth * .02);
                window.setPosition(x, y);
                break;
            case GREEN: 
                this.button = new TextButton("", RiskLikeGame.getUiSkin(), "greenUI");
                this.pieceTexture = new Texture("Sprite/pieceGreen.png"); 
                x = stage.getCamera().position.x + stage.getCamera().viewportWidth/2 
                        - this.window.getWidth() 
                        - (float)(stage.getCamera().viewportWidth * .01);
                y = stage.getCamera().position.y + stage.getCamera().viewportHeight/2 
                        - this.window.getHeight() 
                        - (float)(stage.getCamera().viewportWidth * .02);
                window.setPosition(x,y);
                break;
            case RED:
                this.button = new TextButton("", RiskLikeGame.getUiSkin(), "redUI");
                this.pieceTexture = new Texture("Sprite/pieceRed.png"); 
                x = stage.getCamera().position.x - stage.getCamera().viewportWidth/2 
                        + (float)(stage.getCamera().viewportWidth * .01);
                y = stage.getCamera().position.y - stage.getCamera().viewportHeight/2 
                        + (float)(stage.getCamera().viewportWidth * .02);
                window.setPosition(x, y);
                break;
            case YELLOW:
                this.button = new TextButton("", RiskLikeGame.getUiSkin(), "yellowUI");
                this.pieceTexture = new Texture("Sprite/pieceYellow.png");
                x = stage.getCamera().position.x + stage.getCamera().viewportWidth/2 
                        - window.getWidth() - (float)(stage.getCamera().viewportWidth * .01);
                y = stage.getCamera().position.y - stage.getCamera().viewportHeight/2 
                        + (float)(stage.getCamera().viewportWidth * .02);
                window.setPosition(x,y);
                break;     
        }
        if (this.agent.getClass().isInstance(HumanAgent.class))
            this.button.setText("Human");
        if (this.agent.getClass().isInstance(RandomAgent.class))
            this.button.setText("Random");
        
        this.content.add(this.button);
        this.window.add(this.content);
        this.window.pack();
        
        return this.window;
    }
    
    public Agent getAgent() {
        return this.agent;
    }
    
    public Texture getPieceTexture() {
        return this.pieceTexture;
    }
    
    public void setTakingTurnView() {
        
    }
    
    public void setDoneTurnView() {
        
    }
    
    public void setOutOfGameView() {
        
    }
}
