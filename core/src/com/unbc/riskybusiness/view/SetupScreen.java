/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.unbc.riskybusiness.agents.AbstractAgent;
import com.unbc.riskybusiness.agents.HumanAgent;
import com.unbc.riskybusiness.agents.RandomAgent;
import com.unbc.riskybusiness.controllers.GameController;
import com.unbc.riskybusiness.main.Logger;

/**
 *
 * @author leefoster
 */
public class SetupScreen extends ScreenAdapter {
    RiskLikeGame game;
    SpriteBatch batch;
    Stage stage;
    Texture backgroundTexture = new Texture("Sprite/water.png");
    String[] agentNames = {"Human","Random"};
    
    final Window window = new Window("", RiskLikeGame.getUiSkin(),"grayDown");
    final Table contentTable = new Table();
    final ScrollPane scrollPane = new ScrollPane(contentTable);
    final Table agentSection = new Table();
    final Table plusMinusSection = new Table();
    final Table continentBonusSection = new Table();
    final Table guiEnabledSection = new Table();
    final Table simulationSettingsSections = new Table();
    
    final Image verticalBar1 = new Image(new Texture(Gdx.files.internal("UI/HighResUI/vertical-line.png")));
    final Image verticalBar2 = new Image(new Texture(Gdx.files.internal("UI/HighResUI/vertical-line.png")));
    final Image horizontalBar1 = new Image(new Texture(Gdx.files.internal("UI/HighResUI/horizontal-line.png")));
    final Image horizontalBar2 = new Image(new Texture(Gdx.files.internal("UI/HighResUI/horizontal-line.png")));
    
    final CheckBox redCheckBox = new CheckBox("", RiskLikeGame.getUiSkin(), "red");
    final CheckBox yellowCheckBox = new CheckBox("", RiskLikeGame.getUiSkin(), "yellow");
    final CheckBox greenCheckBox = new CheckBox("", RiskLikeGame.getUiSkin(), "green");
    final CheckBox blueCheckBox = new CheckBox("", RiskLikeGame.getUiSkin(), "blue");
    final CheckBox guiEnabledBox = new CheckBox("", RiskLikeGame.getUiSkin(), "gray");
    
    final SelectBox<String> redAgentSelect = new SelectBox(RiskLikeGame.getUiSkin(), "red");
    final SelectBox<String> yellowAgentSelect = new SelectBox(RiskLikeGame.getUiSkin(), "yellow");
    final SelectBox<String> greenAgentSelect = new SelectBox(RiskLikeGame.getUiSkin(), "green");
    final SelectBox<String> blueAgentSelect = new SelectBox(RiskLikeGame.getUiSkin(), "blue");
    
    final TextField percentToWinField = new TextField("100%",RiskLikeGame.getUiSkin(), "default");
    final TextField unitsPerTurnField = new TextField("5",RiskLikeGame.getUiSkin(),"default");
    final TextField startingUnitsField = new TextField("3", RiskLikeGame.getUiSkin(),"default");
    final TextField continentAField = new TextField("3", RiskLikeGame.getUiSkin(),"default");
    final TextField continentBField = new TextField("3", RiskLikeGame.getUiSkin(), "default");
    final TextField continentCField = new TextField("3", RiskLikeGame.getUiSkin(), "default");
    final TextField continentDField = new TextField("3", RiskLikeGame.getUiSkin(), "default");
    final TextField aiDelayField = new TextField("2", RiskLikeGame.getUiSkin(), "default");
    final TextField gamesToRunField = new TextField("4", RiskLikeGame.getUiSkin(), "default");        
    
    final Label agentsLabel = new Label("Agents:", RiskLikeGame.getUiSkin(), "default");
    final Label perentToWinLabel = new Label("Percent To Win:", RiskLikeGame.getUiSkin(), "default");
    final Label unitsPerTurnLabel = new Label("Units Per Turn:", RiskLikeGame.getUiSkin(), "default");
    final Label startingUnitsLabel = new Label("Starting Units:", RiskLikeGame.getUiSkin(), "default");
    final Label continentBonusLabel = new Label("Continent Bonuses:", RiskLikeGame.getUiSkin(), "default");
    final Label continentALabel = new Label("A", RiskLikeGame.getUiSkin(), "default");
    final Label continentBLabel = new Label("B", RiskLikeGame.getUiSkin(), "default");
    final Label continentCLabel = new Label("C", RiskLikeGame.getUiSkin(), "default");
    final Label continentDLabel = new Label("D", RiskLikeGame.getUiSkin(),"default");
    final Label aiDelayLabel = new Label("AI Delay (seconds):", RiskLikeGame.getUiSkin(), "default");
    final Label gamesToRunLabel = new Label("Games to Run: ", RiskLikeGame.getUiSkin(), "default");
    final Label guiEnabledLabel = new Label("GUI Enabled", RiskLikeGame.getUiSkin(), "default");
    
    final Button percentToWinPlus = new Button(RiskLikeGame.getUiSkin(),"plus");
    final Button percentToWinMinus = new Button(RiskLikeGame.getUiSkin(), "minus");
    final Button unitsPerTurnPlus = new Button(RiskLikeGame.getUiSkin(),"plus");
    final Button unitsPerTurnMinus = new Button(RiskLikeGame.getUiSkin(), "minus");
    final Button startingUnitsPlus = new Button(RiskLikeGame.getUiSkin(),"plus");
    final Button startingUnitsMinus = new Button(RiskLikeGame.getUiSkin(), "minus");
    final Button aiDelayPlus = new Button(RiskLikeGame.getUiSkin(), "plus");
    final Button aiDelayMinus = new Button(RiskLikeGame.getUiSkin(), "minus");
    final Button gamesToRunPlus = new Button(RiskLikeGame.getUiSkin(), "plus");
    final Button gamesToRunMinus = new Button(RiskLikeGame.getUiSkin(), "minus");
    final TextButton startButton = new TextButton("Start",RiskLikeGame.getUiSkin(),"gray");
    String[] items = {"Android1", "Windows1 long text in item", "Linux1", "OSX1", "Android2", "Windows2", "Linux2", "OSX2", "Android3",
			"Windows3", "Linux3", "OSX3", "Android4", "Windows4", "Linux4", "OSX4", "Android5", "Windows5", "Linux5", "OSX5",
			"Android6", "Windows6", "Linux6", "OSX6", "Android7", "Windows7", "Linux7", "OSX7"};
    
    public SetupScreen(RiskLikeGame game) {
        super();
        this.game = game;
    }
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        
        setInputListeners();
        
        redCheckBox.setChecked(true);
        yellowCheckBox.setChecked(true);
        greenCheckBox.setChecked(true);
        blueCheckBox.setChecked(true);
        guiEnabledBox.setChecked(true);
        
        redAgentSelect.setItems(agentNames);
        yellowAgentSelect.setItems(agentNames);
        greenAgentSelect.setItems(agentNames);
        blueAgentSelect.setItems(agentNames);
        
        contentTable.add(agentsLabel).left().padLeft(15);
        contentTable.row();
        agentSection.row();
        agentSection.add(blueCheckBox).width(40f).height(40f).padRight(10);
        agentSection.add(blueAgentSelect).width(300).left().padTop(5).padBottom(5);
        agentSection.row();
        agentSection.add(redCheckBox).width(40f).height(40f).padRight(10);
        agentSection.add(redAgentSelect).width(300).left().padTop(5).padBottom(5);
        agentSection.row();
        agentSection.add(yellowCheckBox).width(40f).height(40f).padRight(10);
        agentSection.add(yellowAgentSelect).width(300).left().padTop(5).padBottom(5);
        agentSection.row();
        agentSection.add(greenCheckBox).width(40f).height(40f).padRight(10);
        agentSection.add(greenAgentSelect).width(300).left().padTop(5).padBottom(5);
        contentTable.add(agentSection).left().padLeft(15).padBottom(15);
        contentTable.row();
        
        contentTable.add(continentBonusLabel).left().padLeft(15).padBottom(15);
        contentTable.row();
        continentBonusSection.add(continentALabel).padRight(5);
        continentBonusSection.add(continentAField).width(50);
        continentBonusSection.add(horizontalBar1);
        continentBonusSection.add(continentBField).width(50);
        continentBonusSection.add(continentBLabel).padLeft(5);
        continentBonusSection.row();
        continentBonusSection.add();
        continentBonusSection.add(verticalBar1);
        continentBonusSection.add();
        continentBonusSection.add(verticalBar2);
        continentBonusSection.row();
        continentBonusSection.add(continentCLabel);
        continentBonusSection.add(continentCField).width(50);
        continentBonusSection.add(horizontalBar2);
        continentBonusSection.add(continentDField).width(50);
        continentBonusSection.add(continentDLabel).padLeft(5);
        contentTable.add(continentBonusSection).padBottom(15);
        contentTable.row();
        
        contentTable.row();
        plusMinusSection.add(perentToWinLabel).left();
        plusMinusSection.add(percentToWinMinus).width(35).height(35).padLeft(20).padRight(5);
        plusMinusSection.add(percentToWinField).width(85).padBottom(5).padTop(5);
        plusMinusSection.add(percentToWinPlus).width(35).height(35).padLeft(5);
        plusMinusSection.row();
        plusMinusSection.add(unitsPerTurnLabel).left();
        plusMinusSection.add(unitsPerTurnMinus).width(35).height(35).padLeft(20).padRight(5);;
        plusMinusSection.add(unitsPerTurnField).width(85).padBottom(5).padTop(5);
        plusMinusSection.add(unitsPerTurnPlus).width(35).height(35).padLeft(5);
        plusMinusSection.row();
        plusMinusSection.add(startingUnitsLabel).left();
        plusMinusSection.add(startingUnitsMinus).width(35).height(35).padLeft(20).padRight(5);;
        plusMinusSection.add(startingUnitsField).width(85).padBottom(5).padTop(5);
        plusMinusSection.add(startingUnitsPlus).width(35).height(35).padLeft(5);
        plusMinusSection.row();
        plusMinusSection.add(aiDelayLabel).left();
        plusMinusSection.add(aiDelayMinus).width(35).height(35).padLeft(20).padRight(5);;
        plusMinusSection.add(aiDelayField).width(85).padBottom(5).padTop(5);
        plusMinusSection.add(aiDelayPlus).width(35).height(35).padLeft(5);
        contentTable.row();
        contentTable.add(plusMinusSection).left().padLeft(15);
        contentTable.row();
        
        guiEnabledSection.add(guiEnabledBox).width(35f).height(35f).padRight(15);
        guiEnabledSection.add(guiEnabledLabel);
        contentTable.add(guiEnabledSection).left().padLeft(15);
        contentTable.row();
       
        simulationSettingsSections.add(gamesToRunLabel).left();
        simulationSettingsSections.add(gamesToRunMinus).width(35).height(35).padLeft(20).padRight(5);
        simulationSettingsSections.add(gamesToRunField).width(85);
        simulationSettingsSections.add(gamesToRunPlus).width(35).height(35).padLeft(5);
        simulationSettingsSections.setVisible(false);
        
        contentTable.add(simulationSettingsSections).padBottom(10);
        contentTable.row();
        
        contentTable.add(startButton).center().width(150);
        
        window.add(scrollPane).height(Gdx.graphics.getHeight()-20);
        window.pack();
        window.setPosition(Gdx.graphics.getWidth()/2-window.getWidth()/2, 
                Gdx.graphics.getHeight()/2-window.getHeight()/2);
        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) { 
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground(batch);
        batch.begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        batch.end();
    }

        private void drawBackground(Batch batch) {
        batch.begin();
        int xStart = (int) (stage.getCamera().position.x - stage.getCamera().viewportWidth/2);
        int xEnd = (int) (stage.getCamera().position.x + stage.getCamera().viewportWidth/2);
        int yStart = (int) (stage.getCamera().position.y - stage.getCamera().viewportHeight/2);
        int yEnd = (int) (stage.getCamera().position.y + stage.getCamera().viewportHeight/2);
        
        for (int i = xStart; i < xEnd; i+=backgroundTexture.getWidth()) {
            for (int j = yStart; j < yEnd; j+=backgroundTexture.getHeight()) {
                batch.draw(backgroundTexture, i, j);
            }
        }
        batch.end();
    }
    
    private void setInputListeners() {
        
        redCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                if (redCheckBox.isChecked())
//                    redAgentSelect.setDisabled(false);
//                else
//                    redAgentSelect.setDisabled(true);
            }
        });
        greenCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (greenCheckBox.isChecked())
                    greenAgentSelect.setDisabled(false);
                else
                    greenAgentSelect.setDisabled(true);
            }
        });
        yellowCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (yellowCheckBox.isChecked())
                    yellowAgentSelect.setDisabled(false);
                else
                    yellowAgentSelect.setDisabled(true);
            }
        });
        blueCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                if (blueCheckBox.isChecked())
//                    blueAgentSelect.setDisabled(false);
//                else
//                    blueAgentSelect.setDisabled(true);
            }
        });
        continentAField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!continentAField.getText().matches("\\d{1,2}"))
                    continentAField.setText("3");
              }
          }
        });
        continentBField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!continentBField.getText().matches("\\d{1,2}"))
                    continentBField.setText("3");
              }
          }
        });
        continentCField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!continentCField.getText().matches("\\d{1,2}"))
                    continentCField.setText("3");
              }
          }
        });
        continentDField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!continentDField.getText().matches("\\d{1,2}"))
                    continentDField.setText("3");
              }
          }
        });
        
       percentToWinMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int percent = Integer.parseInt(percentToWinField.getText().substring(0, percentToWinField.getText().length()-1));
                if (percent != 0)
                    percent--;
                percentToWinField.setText(percent + "%");
            }
        });
       percentToWinField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (percentToWinField.getText().matches("\\d{1,3}%")) {
                     String text = percentToWinField.getText().replace("%", "");
                     int val = Integer.parseInt(text);
                     if (val < 0)
                         val = 0;
                     if (val > 100)
                         val = 100;
                     percentToWinField.setText(val+"%");
                  }
                  else if (percentToWinField.getText().matches("\\d{1,3}.")) {
                      String text = percentToWinField.getText();
                      String[] split = text.split("[^\\d{1,3}]");
                      percentToWinField.setText(split[0] + "%");
                  }
                  else {
                      percentToWinField.setText("0%");
                  }
              }
          }
       });
       percentToWinPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int percent = Integer.parseInt(percentToWinField.getText().substring(0, percentToWinField.getText().length()-1));
                if (percent != 100)
                    percent++;
                percentToWinField.setText(percent + "%");
            }
        });

        unitsPerTurnMinus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(unitsPerTurnField.getText());
                if (unitsVal != 0)
                    unitsVal--;
                unitsPerTurnField.setText(unitsVal+"");
            };
        });
        unitsPerTurnField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!unitsPerTurnField.getText().matches("\\d{1,3}")) {
                     unitsPerTurnField.setText("1");
                  }
              }
          }
       });
        unitsPerTurnPlus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(unitsPerTurnField.getText());
                if (unitsVal != 100)
                    unitsVal++;
                unitsPerTurnField.setText(unitsVal+"");
            }
        });
        
        startingUnitsMinus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(startingUnitsField.getText());
                if (unitsVal != 1)
                    unitsVal--;
                startingUnitsField.setText(unitsVal+"");
            };
        });
        startingUnitsField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!startingUnitsField.getText().matches("\\d{1,3}")) {
                     startingUnitsField.setText("1");
                  }
                  else if (Integer.parseInt(startingUnitsField.getText()) <= 0){
                      startingUnitsField.setText("1");
                  }
              }
          }
       });
        startingUnitsPlus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(startingUnitsField.getText());
                if (unitsVal != 100)
                    unitsVal++;
                startingUnitsField.setText(unitsVal+"");
            }
        });
        
        aiDelayMinus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(aiDelayField.getText());
                if (unitsVal != 0)
                    unitsVal--;
                aiDelayField.setText(unitsVal+"");
            };
        });
        aiDelayField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!aiDelayField.getText().matches("\\d{1,2}")) {
                     aiDelayField.setText("1");
                  }
              }
          }
       });
        aiDelayPlus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(aiDelayField.getText());
                if (unitsVal != 99)
                    unitsVal++;
                aiDelayField.setText(unitsVal+"");
            }
        });
        guiEnabledBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (guiEnabledBox.isChecked()) {
                    simulationSettingsSections.setVisible(false);
                }
                else {
                    simulationSettingsSections.setVisible(true);
                }
            }
        });
        
        gamesToRunMinus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(gamesToRunField.getText());
                if (unitsVal != 1)
                    unitsVal--;
                gamesToRunField.setText(unitsVal+"");
            };
        });
        gamesToRunField.addListener(new FocusListener() {
          @Override
          public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
              if (!focused) {
                  if (!gamesToRunField.getText().matches("\\d{1,2}"))
                     gamesToRunField.setText("1");
                  else if (Integer.parseInt(gamesToRunField.getText()) <= 0)
                      gamesToRunField.setText("1");
              }
          }
       });
        gamesToRunPlus.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int unitsVal = Integer.parseInt(gamesToRunField.getText());
                if (unitsVal != 99)
                    unitsVal++;
                gamesToRunField.setText(unitsVal+"");
            }
        });
        startButton.addListener( new ClickListener() {              
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TextButtonStyle buttonStyle = RiskLikeGame.getUiSkin().get("blue", TextButtonStyle.class);
                //startButton.setStyle(buttonStyle);
                startSimulation();
            }
        });
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenHeight(height);
        stage.getViewport().setScreenWidth(width);
    }
    
    private void startSimulation() {
        Logger.startLog();
        AbstractAgent blueAgent = getAgent(blueAgentSelect.getSelected());
        AbstractAgent redAgent = getAgent(redAgentSelect.getSelected());
        AbstractAgent greenAgent = (greenCheckBox.isChecked()) ? getAgent(greenAgentSelect.getSelected()):null;
        AbstractAgent yellowAgent = (yellowCheckBox.isChecked()) ? getAgent(yellowAgentSelect.getSelected()):null;
        
        GameController g = new GameController(blueAgent, redAgent, greenAgent, yellowAgent);
        g.setContinentBonuses(Integer.parseInt(continentAField.getText()), 
                Integer.parseInt(continentBField.getText()), 
                Integer.parseInt(continentCField.getText()), 
                Integer.parseInt(continentDField.getText()));
        String percentToWinText = percentToWinField.getText().replace("%", "");
        g.percentToWin = Integer.parseInt(percentToWinText);
        g.unitsPerTurn = Integer.parseInt(unitsPerTurnField.getText());
        g.startingUnits = Integer.parseInt(startingUnitsField.getText());
        g.aiDelaySeconds = Integer.parseInt(aiDelayField.getText());
        
        if (guiEnabledBox.isChecked()) {
            game.setScreen(new PlayScreen(g, game));
        }
        else {
            g.play();
        }
    }

    private AbstractAgent getAgent(String agentName) {
        if (agentName.equals("Human"))
            return new HumanAgent();
        if (agentName.equals("Random"))
            return new RandomAgent();
        
        return null;
    }

}