/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unbc.riskybusiness.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.unbc.riskybusiness.models.Territory;

/**
 *
 * @author leefoster
 */
public class MapView {
    
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private Territory currentSelected; 
    
    public MapView(SpriteBatch batch) {
        tiledMap = RiskLikeGame.getMap();
        camera = new OrthographicCamera();
        renderer = new OrthogonalTiledMapRenderer(tiledMap, RiskLikeGame.getScale(), batch);
    }

    public void render() {
        camera.viewportWidth = (int)(Gdx.graphics.getWidth() / RiskLikeGame.getZoomLevel());
        camera.viewportHeight = (int)(Gdx.graphics.getHeight() / RiskLikeGame.getZoomLevel());
        camera.update();
        renderer.setView(camera);
        drawBackground(renderer.getBatch());
        
        // Draw the tiled map
        renderer.render();
    }
    
    private void drawBackground(Batch batch) {
        batch.begin();
        int xStart = (int) (camera.position.x - camera.viewportWidth/2);
        int xEnd = (int) (camera.position.x + camera.viewportWidth/2);
        int yStart = (int) (camera.position.y - camera.viewportHeight/2);
        int yEnd = (int) (camera.position.y + camera.viewportHeight/2);
        
        for (int i = xStart; i < xEnd; i+=RiskLikeGame.getWaterBGTexture().getWidth()) {
            for (int j = yStart; j < yEnd; j+=RiskLikeGame.getWaterBGTexture().getHeight()) {
                renderer.getBatch().draw(RiskLikeGame.getWaterBGTexture(), i, j);
            }
        }
        batch.end();
    }
    
    public void setCameraPosition (int x, int y) {
        camera.position.set(new Vector3(x,y,0));
    }
    
    public void moveCameraPosition (float deltaX, float deltaY) {
        camera.position.set(camera.position.x + -deltaX,
                camera.position.y + deltaY,
                camera.position.z);
    }
    
    public Camera getCamera() {
        return camera;
    }

    public void setSelectedTerritory(Territory selectedTerritory) {
        if (selectedTerritory == null) {
            return;
        }
        currentSelected = selectedTerritory;
        Rectangle tileRect = RiskLikeGame.getTileLocation(selectedTerritory.getLocationName());
        Vector2 rectCenter = new Vector2();
        tileRect.getCenter(rectCenter);
        Vector2 mapCoords = new Vector2((int) (rectCenter.x / RiskLikeGame.getTileWidth()),
                (int) (rectCenter.y / RiskLikeGame.getTileHeight()));
        // For some reason we have to make these adjustments to for each continent
        mapCoords.y--;
        if (selectedTerritory.getLocationName().startsWith("A")) {
            mapCoords.x--;
            mapCoords.y--;
        }
        if (selectedTerritory.getLocationName().startsWith("B")) {
            mapCoords.x = mapCoords.x - 2;
            mapCoords.y--;
        }
        if (selectedTerritory.getLocationName().startsWith("C"))
            mapCoords.x--;
        if (selectedTerritory.getLocationName().startsWith("D"))
            mapCoords.x = mapCoords.x - 2;
        TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Points").getTile(43);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(selectedTile);
        RiskLikeGame.getPointLayer().setCell((int) mapCoords.x, (int) mapCoords.y, cell);
    }
    
    public void deselectTerritory(Territory t) {
        if (t == null)
            return;
        currentSelected = null;
        Rectangle tileRect = RiskLikeGame.getTileLocation(t.getLocationName());
        Vector2 rectCenter = new Vector2();
        tileRect.getCenter(rectCenter);
        Vector2 mapCoords = new Vector2((int) (rectCenter.x / RiskLikeGame.getTileWidth()),
                (int) (rectCenter.y / RiskLikeGame.getTileHeight()));
        mapCoords.y--;
        if (t.getLocationName().startsWith("A")) {
            mapCoords.x--;
            mapCoords.y--;
        }
        if (t.getLocationName().startsWith("B")) {
            mapCoords.x = mapCoords.x - 2;
            mapCoords.y--;
        }
        if (t.getLocationName().startsWith("C"))
            mapCoords.x--;
        if (t.getLocationName().startsWith("D"))
            mapCoords.x = mapCoords.x - 2;
        TiledMapTile selectedTile = tiledMap.getTileSets().getTileSet("Points").getTile(42);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(selectedTile);
        RiskLikeGame.getPointLayer().setCell((int) mapCoords.x, (int) mapCoords.y, cell);
    }
    
    public Territory getSelectedTerritory() {
        return currentSelected;
    }
}
