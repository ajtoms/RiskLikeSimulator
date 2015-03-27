package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Game;
import com.unbc.riskybusiness.agents.Agent;
import com.unbc.riskybusiness.agents.BaseAgent;
import com.unbc.riskybusiness.agents.HumanAgent;
import com.unbc.riskybusiness.models.Territory;
import java.util.ArrayList;
import java.util.Random;

/** This is the class that starts running the GUI.  Currently it just shows
 * the game screen, but this is where we would put different screens, like
 * a start screen, config screen, etc.
 * @author leefoster
 */
public class GameGUI extends Game{
    
    @Override
    public void create() {
        
        ArrayList<Territory> territories = new ArrayList<Territory>();
            ArrayList<Agent> agents = new ArrayList<Agent>();
            Random r = new Random();
            
            HumanAgent h1 = new HumanAgent();
            HumanAgent h2 = new HumanAgent();
            BaseAgent a1 = new BaseAgent();
            agents.add(h1);
            agents.add(h2);
            agents.add(a1);
            
            for (BoardLocation b : BoardLocation.values()) {
                territories.add(new Territory(b));
            }
            
            for (Territory t : territories) {
                t.initialAssignment(agents.get(r.nextInt(2)),
                        r.nextInt(10) + 1);
            }
        
            setScreen(new GameScreen(agents, territories));
    }
    
}
