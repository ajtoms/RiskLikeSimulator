/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldview;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unbc.riskybusiness.models.Territory;
import java.util.HashMap;

/**
 *
 * @author leefoster
 */
public class BoardView {

    private float scale = 1.0f;
    private BoardNode selectedNode = null;

    private int mapWidth, mapHeight, tileWidth, tileHeight;
    private TiledMapTileLayer islandLayer, pointLayer, connectionLayer;
    private TiledMap tiledMap;
    private BitmapFont font;

    private HashMap<String, BoardNode> nodes = new HashMap<String, BoardNode>();
    private HashMap<String, TerritoryView> territoryViews = new HashMap<String, TerritoryView>();

    public BoardView(String mapName) {
        tiledMap = new TmxMapLoader().load(mapName);
        islandLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Islands");
        pointLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Points");
        connectionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Connection");

        mapWidth = islandLayer.getWidth();
        mapHeight = islandLayer.getHeight();
        tileWidth = (int) islandLayer.getTileWidth();
        tileHeight = (int) islandLayer.getTileHeight();

        for (MapObject mapObject : tiledMap.getLayers().get("PointObjects").getObjects()) {
            nodes.put(mapObject.getName(), new BoardNode((RectangleMapObject) mapObject));
        }
    }

    public Vector2 getNodeLocation(String nodeName) {
        if (!nodes.containsKey(nodeName)) {
            return null;
        }

        return nodes.get(nodeName).center;
    }

    public void dispose() {
        tiledMap.dispose();
    }

    public void drawTerritories(Batch batch, OrthographicCamera camera) {
        for (TerritoryView piece : territoryViews.values()) {
            piece.draw(batch);
        }
    }
    
    public void updateTerritories(Territory territory, PlayerView playerView) {
//        if (territoryViews.containsKey(territory.getLocation().toString())) {
//            territoryViews.remove(territory.getLocation().toString());
//        }
//        TerritoryView territoryView = new TerritoryView(playerView.getPieceTexture(), scale, font,
//                territory.getNumTroops() + "", playerView);
//        territoryView.setPosition(nodes.get(territory.getLocation().toString()).center);
//        //piece.setPosition(piecePosition);
//        territoryViews.put(territory.getLocation().toString(), territoryView);
    }

    public void setSelected(BoardNode b) {
        Vector2 mapCoords = new Vector2((int) (b.center.x / tileWidth),
                (int) (b.center.y / tileHeight));
        mapCoords.y--;
        if (pointLayer.getCell((int) mapCoords.x - 1, (int) mapCoords.y) != null) {
            mapCoords.x = mapCoords.x - 1;
        }
        TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Points").getTile(43);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(selectedTile);
        pointLayer.setCell((int) mapCoords.x, (int) mapCoords.y, cell);
        selectedNode = b;
    }

    void deselect() {
        if (selectedNode != null) {
            Vector2 mapCoords = new Vector2((int) (selectedNode.center.x / tileWidth),
                    (int) (selectedNode.center.y / tileHeight));
            mapCoords.y--;
            if (pointLayer.getCell((int) mapCoords.x - 1, (int) mapCoords.y) != null) {
                mapCoords.x = mapCoords.x - 1;
            }
            TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Points").getTile(42);
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(selectedTile);
            pointLayer.setCell((int) mapCoords.x, (int) mapCoords.y, cell);
        }
        selectedNode = null;
    }



    public class BoardNode {

        Vector2 center = new Vector2();
        Rectangle rect;
        String name;

        public BoardNode(RectangleMapObject mapObject) {
            center = mapObject.getRectangle().getCenter(center);
            rect = mapObject.getRectangle();
            name = mapObject.getName();
        }

        // SO MUCH HARD CODING
        boolean adjacentTo(BoardNode selectedNode) {
            if (selectedNode == null)
                return false;
            // THEM A'S
            if (selectedNode.name.equals("AA"))
                return name.equals("AC") || name.equals("AB");
            if (selectedNode.name.equals("AB"))
                return name.equals("AA") || name.equals("AD") || name.equals("BA");
            if (selectedNode.name.equals("AC"))
                return name.equals("AA") || name.equals("AD") || name.equals("CA");
            if (selectedNode.name.equals("AD"))
                return name.equals("AB") || name.equals("AC");
        
            // DEM B'S
            if (selectedNode.name.equals("BA"))
                return name.equals("BB") || name.equals("BC") || name.equals("AB");
            if (selectedNode.name.equals("BB"))
                return name.equals("BC") || name.equals("BD");
            if (selectedNode.name.equals("BC"))
                return name.equals("BA") || name.equals("BD");
            if (selectedNode.name.equals("BD"))
                return name.equals("BB") || name.equals("BC") || name.equals("DB");
            
            // DEM C'S
            if (selectedNode.name.equals("CA"))
                return name.equals("CB") || name.equals("CC") || name.equals("AC");
            if (selectedNode.name.equals("CB"))
                return name.equals("CA") || name.equals("CD");
            if (selectedNode.name.equals("CC"))
                return name.equals("CA") || name.equals("CD");
            if (selectedNode.name.equals("CD"))
                return name.equals("CB") || name.equals("CC") || name.equals("DC");
            
            // DEM D'S
            if (selectedNode.name.equals("DA"))
                return name.equals("DB") || name.equals("DC");
            if (selectedNode.name.equals("DB"))
                return name.equals("DA") || name.equals("DD") || name.equals("BD");
            if (selectedNode.name.equals("DC"))
                return name.equals("DA") || name.equals("DD") || name.equals("DC");
            if (selectedNode.name.equals("DD"))
                return name.equals("DB") || name.equals("DC");
            
            return false;
        }
    }
    
    public void setFont(BitmapFont font) {
        this.font = font;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public TiledMap getTileMap() {
        return this.tiledMap;
    }
    
    public HashMap<String, BoardNode> getNodes() {
        return this.nodes;
    }
}