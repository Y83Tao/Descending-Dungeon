package com.mygdx.game.FileData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.*;
import com.mygdx.game.Tools.DungeonGeneration;
import com.mygdx.game.UserInterface.Minimap;

public class FloorManager {

    int floor;
    public static String floorName;
    public static String filePath;
    public static boolean generatedFloor, enterGeneratedFloor, deployItems;
    static DungeonGeneration generation;
    public static SaveData data;
    static Json json = new Json();
    static FileHandle fileHandle = Gdx.files.local("Saves/GameData.json"), enemyFileHandle = Gdx.files.local("Saves/FloorData.json"), playerFileHandle = Gdx.files.local("Saves/PlayerData.json");
    public static FloorData floorData;
    public static PlayerData playerData;

    public FloorManager(boolean newOrLoad){
        if (newOrLoad){
            data = new SaveData();
            playerData = new PlayerData();
            floorData = new FloorData();
            data.setFloorPlayer(1);
            data.setFloorsCompleted(1);
            data.setSpawnAtEntrance(true);
            playerData.leveUpThreshHold = 100;
            playerData.health = 100;
            playerData.maxHealth = 100;
            playerData.maxStamina = 100;
            playerData.stamina = 100;
            floorName = "Type1";
            saveData();
            generateFloor("Map/Floor1.tmx");
        } else{
            loadData();
            enterFloor(data.getFloorPlayer());
            System.out.println("LOADED DATA");
        }
    }
    public static  void saveData(){
        fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(data)), false);

    }
    public static void saveObjectData(){
        System.out.println("SAVED OBJECT DATA FOR FLOOR " + data.getFloorPlayer());
        playerData.setInventoryItems(Inventory.slots);
        playerData.setItemAttributes();
        playerFileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(playerData)), false);
        floorData.setFloor(PlayingField.enemies, PlayingField.items, PlayingField.containers, PlayingField.worldObjects, PlayingField.interactableObjects,  data.getFloorPlayer());
        enemyFileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(floorData)), false);
    }

    public static void loadData(){
        data = json.fromJson(SaveData.class, Base64Coder.decodeString(fileHandle.readString()));
        playerData = json.fromJson(PlayerData.class, Base64Coder.decodeString(playerFileHandle.readString()));
    }

    public static void loadObjectData(){
        floorData = json.fromJson(FloorData.class, Base64Coder.decodeString(enemyFileHandle.readString()));
    }

    public static void enterFloor(int floor){
        FloorManager.filePath = floorIntToPath(floor);
        PlayingField.floorChange();
        saveData();
    }

    public static String floorIntToPath(int floorNumber){
        return "Map/Floor" + floorNumber + ".tmx";
    }

    public static boolean doneGeneratingLevel;

    public static void generateFloor(String filePath){
            if (data.getFloorPlayer() == 5) {
                FileHandle internalFile = Gdx.files.internal("Map/Boss1floor.tmx");
                internalFile.copyTo(Gdx.files.local("Map/Floor5.tmx"));
                FloorManager.filePath = filePath;
            }
            else {

               doneGeneratingLevel = false;
               generation = new DungeonGeneration();
               generation.create(filePath, 0);

               FloorManager.filePath = filePath;
               doneGeneratingLevel = true;


                System.out.println("GENERATED NEW FLOOR " + filePath);
            }
        generatedFloor = true;

    }
    public static void spawnAndExit(){

    }

    public static void descendFloor(){
        data.setSpawnAtEntrance(true);
        FloorManager.saveObjectData();
        Minimap.uploadMiniMap(FloorManager.data.getFloorPlayer());
        data.setFloorPlayer(data.getFloorPlayer() + 1);

        if (data.getFloorsCompleted() < data.getFloorPlayer()){
            data.setFloorsCompleted(data.getFloorsCompleted() + 1);
            generateFloor("Map/Floor" + data.getFloorPlayer() + ".tmx");
            PixmapIO.writePNG(new FileHandle("Map/Minimap_Textures/Floormap" + data.getFloorPlayer() + ".png"), new Pixmap(75, 75, Pixmap.Format.RGBA8888));
        } else {
            enterGeneratedFloor = true;
            enterFloor(data.getFloorPlayer());
        }
        saveData();
        PlayingField.floorChange();

}
    public static void goUpFloor(){
        FloorManager.saveObjectData();
        Minimap.uploadMiniMap(FloorManager.data.getFloorPlayer());
        data.setSpawnAtEntrance(false);
        enterGeneratedFloor = true;
        data.setFloorPlayer(data.floorPlayer - 1);
        enterFloor(data.getFloorPlayer());
    }
}
