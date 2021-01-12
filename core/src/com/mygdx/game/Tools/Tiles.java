package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Player;
import com.mygdx.game.PlayingField;
import com.mygdx.game.WorldObjects;
import org.xguzm.pathfinding.gdxbridge.NavTmxMapLoader;
import org.xguzm.pathfinding.gdxbridge.NavigationTiledMapLayer;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.util.ArrayList;

public class Tiles {

    static TiledMap map;
    public static NavigationTiledMapLayer pathGrid;
    public static AStarGridFinder<GridCell> finder;
    public static GridFinderOptions aStarOptions;

    public static Vector3 tileLocation(Vector3 vector, boolean YesForIntNoForFloat){

        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) PlayingField.tiledMap.getLayers().get(0)).getCell((int) vector.x, (int) vector.y);
        if (cell != null) {
            TiledMapTile tile = cell.getTile();
            if (tile != null) {
                if (YesForIntNoForFloat) {
                    vector.x = (int) vector.x;
                    vector.y = (int) vector.y;
                }
                return vector;
            }
        }
        return null;

        //System.out.println(Tiles.getTileId(worldCoordinates.x, worldCoordinates.y, 0));
    }
    public static int getTileId(float x, float y, int layer) {
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) PlayingField.tiledMap.getLayers().get(layer)).getCell((int) x,(int) y);
        if (cell != null) {
            TiledMapTile tile = cell.getTile();
            if (tile != null) {
                return tile.getId();
            }
        }
        return -1;
    }


    public static boolean collisionChecker(float x, float y){
        if (getTileId(x + 1, y, 0) == -1){
            return false;
        }

        for (int i = 0; i < ToolClass.unwalkableTileIds.size(); i++){
            if (getTileId(x + 1, y, 3) == ToolClass.unwalkableTileIds.get(i)) return false;
        }
        return true;
    }
    public static boolean doorCollisionChecker(float x, float y, Rectangle collisionBox){
        for (int i = 0; i < PlayingField.doors.size(); i++){
            if (!PlayingField.doors.get(i).open) {
                if (Intersector.overlaps(PlayingField.doors.get(i).collisionBox, new Rectangle (x + 0.5f, y, collisionBox.width, collisionBox.height / 8))) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean miscDoorCollisionChecker(float x, float y, Rectangle collisionBox){
        for (int i = 0; i < PlayingField.doors.size(); i++){
            if (!PlayingField.doors.get(i).open) {
                if (Intersector.overlaps(PlayingField.doors.get(i).collisionBox, new Rectangle (x, y, collisionBox.width, collisionBox.height / 8))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean playerObjectCollisionChecker(Rectangle collisionBox){
        for (int i = 0; i < PlayingField.worldObjects.size(); i++){
            if (!PlayingField.worldObjects.get(i).destroyed) {
                if (Intersector.overlaps(PlayingField.worldObjects.get(i).collisionBox, new Rectangle(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height / 2))) {
                    PlayingField.worldObjects.get(i).collisionMovement(Player.posX + 1f, Player.posY + 0.5f);
                    Player.objectPushSpeed = PlayingField.worldObjects.get(i).speed;
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean miscObjectCollisionChecker(Rectangle collisionBox){
        for (int i = 0; i < PlayingField.worldObjects.size(); i++){
            if (!PlayingField.worldObjects.get(i).destroyed) {
                if (Intersector.overlaps(PlayingField.worldObjects.get(i).collisionBox, new Rectangle(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height / 2))) {
                    PlayingField.worldObjects.get(i).collisionMovement(collisionBox.x, collisionBox.y);
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean projectileCollisionChecker(float x, float y){
        if (getTileId(x, y - 1, 0) != -1) return true;
  //      if (getTileId(x, y + 1, 0 ) != -1) return true;
        if (getTileId(x, y, 0) == -1 ){
            return false;
        }
        return true;
    }

    public static void setUpPathing(){
        map = new NavTmxMapLoader().load(FloorManager.filePath);
        pathGrid = (NavigationTiledMapLayer) map.getLayers().get("navigation");

        aStarOptions = new GridFinderOptions();
        aStarOptions.allowDiagonal = true;
        aStarOptions.isYDown = true;

        finder = new AStarGridFinder<>(GridCell.class, aStarOptions);
    }

}
