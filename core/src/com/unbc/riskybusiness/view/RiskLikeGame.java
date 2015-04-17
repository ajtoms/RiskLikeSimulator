package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.unbc.riskybusiness.models.Territory;
import java.util.HashMap;

/** This is the class that starts running the GUI.  Currently it just shows
 * the game screen, but this is where we would put different screens, like
 * a start screen, config screen, etc.
 * @author leefoster
 */
public class RiskLikeGame extends Game{
    
    private static float scale = 1.0f;
    private static float zoomLevel = 0.75f;
    private static float minZoomLevel = 0.5f;
    private static float maxZoomLevel = 10f;
    
    private static int tileWidth;
    private static int tileHeight;
    
    private static Skin uiSkin;
    private static BitmapFont font;
    private static BitmapFont fontBig;
    private static Texture bluePieceTexture;
    private static Texture greenPieceTexture;
    private static Texture yellowPieceTexture;
    private static Texture redPieceTexture;
    private static Texture waterBGTexture;
    
    private static WindowStyle blueUpWindowStyle;
    private static WindowStyle blueDownWindowStyle;
    
    private static WindowStyle greenUpWindowStyle;
    private static WindowStyle greenDownWindowStyle;
    
    private static WindowStyle yellowUpWindowStyle;
    private static WindowStyle yellowDownWindowStyle;
    
    private static WindowStyle redUpWindowStyle;
    private static WindowStyle redDownWindowStyle;
    
    private static WindowStyle grayUpWindowStyle;
    private static WindowStyle grayDownWindowStyle;
    
    private static TiledMap map;
    private static TiledMapTileLayer pointLayer;
    private static HashMap<String, Rectangle> tileLocations;

    @Override
    public void create() {
          loadAssets();
          setScreen(new SetupScreen(this));
    }

    private void loadAssets() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UI/Font/kenvector_future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter); 
        parameter.size = 22;
        fontBig = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        
        uiSkin = new Skin();
        uiSkin.add("default-font", font);
        uiSkin.add("default-font-big", fontBig);
        uiSkin.addRegions(new TextureAtlas(Gdx.files.internal("UI/UI.atlas")));
        uiSkin.load(Gdx.files.internal("UI/UI.json"));
        
        bluePieceTexture = new Texture(Gdx.files.internal("Sprite/pieceBlue.png"));
        greenPieceTexture = new Texture(Gdx.files.internal("Sprite/pieceGreen.png"));
        yellowPieceTexture = new Texture(Gdx.files.internal("Sprite/pieceYellow.png"));
        redPieceTexture = new Texture(Gdx.files.internal("Sprite/pieceRed.png"));
        waterBGTexture = new Texture(Gdx.files.internal("Sprite/water.png"));
        
        blueUpWindowStyle = uiSkin.get("blueUp", WindowStyle.class);
        blueDownWindowStyle = uiSkin.get("blueDown", WindowStyle.class);
        redUpWindowStyle = uiSkin.get("redUp", WindowStyle.class);
        redDownWindowStyle = uiSkin.get("redDown", WindowStyle.class);
        yellowUpWindowStyle = uiSkin.get("yellowUp", WindowStyle.class);
        yellowDownWindowStyle = uiSkin.get("yellowDown", WindowStyle.class);
        greenUpWindowStyle = uiSkin.get("greenUp", WindowStyle.class);
        greenDownWindowStyle = uiSkin.get("greenDown", WindowStyle.class);
        grayUpWindowStyle = uiSkin.get("grayUp", WindowStyle.class);
        grayDownWindowStyle = uiSkin.get("grayDown", WindowStyle.class);
        
        map = new TmxMapLoader().load("Map/RiskMap.tmx");
        pointLayer = (TiledMapTileLayer) map.getLayers().get("Points");
        tileWidth = ((TiledMapTileLayer) map.getLayers().get("Islands")).getWidth();
        tileHeight = ((TiledMapTileLayer) map.getLayers().get("Islands")).getHeight();
        tileLocations = new HashMap<String, Rectangle>();
        for (MapObject mapObject : map.getLayers().get("PointObjects").getObjects()) {
            Vector2 center = new Vector2();
            tileLocations.put(mapObject.getName(), ((RectangleMapObject) mapObject).getRectangle());
        }
    }
    
    public static BitmapFont getFont() {
        return font;
    }

    /**
     * @return the waterBGTexture
     */
    public static Texture getWaterBGTexture() {
        return waterBGTexture;
    }
    
    public static float getScale() {
        return scale;
    }
    
    public static void setZoomLevel(int amount) {
        zoomLevel += 0.25 * -amount;
        if (getZoomLevel() < getMinZoomLevel())
            zoomLevel = getMinZoomLevel();
        if (getZoomLevel() > getMaxZoomLevel())
            zoomLevel = getMaxZoomLevel();
    }
    
    public static float getZoomLevel() {
        return zoomLevel;
    }
    
    /**
     * @return the uiSkin
     */
    public static Skin getUiSkin() {
        return uiSkin;
    }
    
    /**
     * @return the minZoomLevel
     */
    public static float getMinZoomLevel() {
        return minZoomLevel;
    }

    /**
     * @return the maxZoomLevel
     */
    public static float getMaxZoomLevel() {
        return maxZoomLevel;
    }
    
    public static BitmapFont getFontBig() {
        return fontBig;
    }
    
    public static WindowStyle getActiveWindowStyle(PlayerView.COLOR color) {
        if (color == null)
            return grayUpWindowStyle;
        switch (color) {
            case BLUE: return blueUpWindowStyle;
            case RED: return redUpWindowStyle;
            case YELLOW: return yellowUpWindowStyle;
            case GREEN: return greenUpWindowStyle;
        }
        return grayUpWindowStyle;
    }
    
    public static WindowStyle getDeactiveWindowStyle(PlayerView.COLOR color) {
        if (color == null)
            return grayDownWindowStyle;
        
        switch (color) {
            case BLUE: return blueDownWindowStyle;
            case RED: return redDownWindowStyle;
            case YELLOW: return yellowDownWindowStyle;
            case GREEN: return greenDownWindowStyle;
        }
        return grayDownWindowStyle;
    }
    
    public static Texture getPieceTexture(PlayerView.COLOR color) {
        switch (color) {
            case BLUE: return bluePieceTexture;
            case RED: return redPieceTexture;
            case GREEN: return greenPieceTexture;
            case YELLOW: return yellowPieceTexture;
        }
        
        return null;
    }
    
    public static TiledMap getMap() {
        return map;
    }
    
    public static TiledMapTileLayer getTileLayer(String name) {
        return (TiledMapTileLayer) map.getLayers().get(name);
    }
    
    public static Rectangle getTileLocation(String tileName) {
        return tileLocations.get(tileName);
    }
    
    public static String getTileNameAtLocation(Vector2 location) {
        for (String key : tileLocations.keySet()) {
            Rectangle r = tileLocations.get(key);
            if (r.contains(location))
                return key;
        }
        
        return null;
    }
    
    public static int getTileWidth() {
        return tileWidth;
    }
    
    public static int getTileHeight() {
        return tileHeight;
    }
    
    public static TiledMapTileLayer getPointLayer() {
        return pointLayer;
    }
}
