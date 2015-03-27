package com.unbc.riskybusiness.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unbc.riskybusiness.view.GameGUI;

public class DesktopLauncher {
    
        public static void main(String[] args) {
           showGUI();
        }
        
        private static void showGUI() {
             LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
                config.height = 720;
                new LwjglApplication(new GameGUI(), config);
        }

}
