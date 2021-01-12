package com.mygdx.game.FileData;

public class SaveData {

    int floorsCompleted;

    int floorPlayer;

    boolean spawnAtEntrance;

    public boolean isSpawnAtEntrance() {
        return spawnAtEntrance;
    }

    public void setSpawnAtEntrance(boolean spawnAtEntrance) {
        this.spawnAtEntrance = spawnAtEntrance;
    }


    public int getFloorPlayer() {
        return floorPlayer;
    }

    public void setFloorPlayer(int floorPlayer) {
        this.floorPlayer = floorPlayer;
    }

    public int getFloorsCompleted() {
        return floorsCompleted;
    }

    public void setFloorsCompleted(int floorsCompleted) {
        this.floorsCompleted = floorsCompleted;
    }



}
