package com.unbc.riskybusiness.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.unbc.riskybusiness.view.RiskLikeGame;

public class DesktopLauncher {

    public static void main(String[] args) {
      //  packTextures();
        showGUI();
    }

    private static void showGUI() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.samples = 4;
        new LwjglApplication(new RiskLikeGame(), config);
    }
    
    private static void packTextures() {
        TexturePacker.process("UI", "UI", "UI");
    }
}
