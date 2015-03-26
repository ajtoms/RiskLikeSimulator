/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unbc.riskybusiness.models.Board;
import java.util.HashMap;

/**
 *
 * @author leefoster
 */
public class BoardView {
    int mapWidth, mapHeight, tileWidth,tileHeight;
    TiledMapTileLayer islandLayer, pointLayer, connectionLayer;
    Board boardModel;
    TiledMap tiledMap;
    HashMap<String, BoardNode> nodes = new HashMap<String, BoardNode>();
    
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
        if (!nodes.containsKey(nodeName))
            return null;
        
        return nodes.get(nodeName).center;
    }
    
    public void dispose() {
        tiledMap.dispose();
    }
    
    public class BoardNode {
        Vector2 center = new Vector2();
        Rectangle rect;
        String name;
        BoardNode left;
        BoardNode right;
        BoardNode top;
        BoardNode bottom;
        
        public BoardNode(RectangleMapObject mapObject) {
            center = mapObject.getRectangle().getCenter(center);
            rect = mapObject.getRectangle();
            name = mapObject.getName();
        }
    }
    
    private class BoardPath {
        int top,bottom,left,right;
    }
}
