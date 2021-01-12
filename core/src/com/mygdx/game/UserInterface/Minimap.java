package com.mygdx.game.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.czyzby.noise4j.map.Grid;
import com.mygdx.game.Player;
import com.mygdx.game.PlayingField;
import com.mygdx.game.Tools.Tiles;

import java.util.ArrayList;

import static com.mygdx.game.PlayingField.camera;

public class Minimap {
    public static Pixmap miniMap;
    private Texture miniMapTexture;
    public static Grid visibleGrid;

    public Minimap(){
        miniMap = new Pixmap(75, 75, Pixmap.Format.RGBA8888);
        miniMap.setColor(Color.ROYAL);
        miniMapTexture = new Texture(miniMap, Pixmap.Format.RGB888, false);
        visibleGrid = new Grid(75, 75);
    }

    public void updateMiniMap(){
        for (int x = 0; x < visibleGrid.getWidth(); x++) {
            for (int y = 0; y < visibleGrid.getHeight(); y++) {
                float cellType;
                if (visibleGrid.get(x, y) == 1) {cellType = 0.25f;
                    miniMap.drawPixel(x, y, Color.rgba8888(cellType, cellType, cellType, 1f));
                } else if (visibleGrid.get(x, y) == 2) {cellType = 5;
                    miniMap.drawPixel(x, y, Color.rgba8888(cellType, cellType, cellType, 1f));
                } else {
                    if (visibleGrid.get(x, y) == 3) {
                        if (x + 1 < visibleGrid.getWidth() && x - 1 > 0 && y + 1 < visibleGrid.getHeight() && y - 1 > 0) {
                            if (visibleGrid.get(x + 1, y) == 1 || visibleGrid.get(x - 1, y) == 1 || visibleGrid.get(x, y + 1) == 1 || visibleGrid.get(x, y - 1) == 1) {
                                cellType = 0.5f;
                                miniMap.drawPixel(x, y, Color.rgba8888(cellType, cellType, cellType, 1f));
                            }
                        }
                    }
                }
            }
        }

        miniMapTexture.dispose();
        miniMapTexture = new Texture(miniMap, Pixmap.Format.RGB888, false);
    }

    public void updateVisibleGrid(TiledMapTileLayer layer){
        int posX = (int) Player.posX;
        int posY = (int) Player.posY;
        for (int x = -5; x < 5; x++){
            for (int y = -5; y < 5; y++){
                if (layer.getCell(posX + x, posY + y) != null){
                            visibleGrid.set(posX + x, posY + y, 1);
                } else {
                    if (posX + x < layer.getWidth() && posX + x > 0 && posY + y < layer.getHeight() && posY + y > 0)
                    visibleGrid.set(posX + x, posY + y, 3);
                }
            }
        }

        if (layer.getCell(posX, posY) != null) visibleGrid.set(posX, posY, 2);
        else if (layer.getCell(posX + 1, posY) != null) visibleGrid.set(posX + 1, posY, 2);
        else if (layer.getCell(posX - 1, posY) != null) visibleGrid.set(posX - 1, posY, 2);
        else if (layer.getCell(posX, posY + 1) != null) visibleGrid.set(posX, posY + 1, 2);
        else if (layer.getCell(posX, posY - 1) != null) visibleGrid.set(posX, posY - 1, 2);

    }


    public static void resetMiniMap(){
        visibleGrid  = new Grid(75, 75);
        miniMap = new Pixmap(75, 75, Pixmap.Format.RGBA8888);
    }
    public static void obtainMiniMap(int floor){
        visibleGrid  = new Grid(75, 75);
        Texture mapTexture = new Texture("Map/Minimap_Textures/Floormap" + floor + ".png");
        mapTexture.getTextureData().prepare();
        ///MIGHT NOT WORK FOR VERY SLOW SYSTEMS ^
            if (mapTexture.getTextureData().isPrepared()){
                miniMap = mapTexture.getTextureData().consumePixmap();
            }

    }

    public static void uploadMiniMap(int floor){
        PixmapIO.writePNG(new FileHandle("Map/Minimap_Textures/Floormap" + floor + ".png"), miniMap);
    }

    public void render(){
        if (Gdx.graphics.getWidth() > 1000 && Gdx.graphics.getHeight() > 1000) {
            Vector3 worldCoordinates = new Vector3(Gdx.graphics.getWidth() - 300, 150, 0);
            camera.unproject(worldCoordinates);

            PlayingField.HudStatic.setColor(1, 1, 1, 0.5f);
            PlayingField.HudStatic.draw(miniMapTexture, worldCoordinates.x, worldCoordinates.y, 4f, -4);
            PlayingField.HudStatic.setColor(1, 1, 1, 1f);
        } else {
            Vector3 worldCoordinates = new Vector3(Gdx.graphics.getWidth() - 250, 150, 0);
            camera.unproject(worldCoordinates);

            PlayingField.HudStatic.setColor(1, 1, 1, 0.5f);
            PlayingField.HudStatic.draw(miniMapTexture, worldCoordinates.x, worldCoordinates.y, 3f, -3);
            PlayingField.HudStatic.setColor(1, 1, 1, 1f);
        }
    }

}
