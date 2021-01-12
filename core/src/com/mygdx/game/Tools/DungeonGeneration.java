package com.mygdx.game.Tools;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.room.AbstractRoomGenerator;
import com.github.czyzby.noise4j.map.generator.room.RoomType;
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator;

import java.util.ArrayList;
import java.util.Random;

public class DungeonGeneration {
    String encodedMap, encodedNavigationMap, encodedWallMap, encodedWallMap2;
    boolean wallDesign = false, skullNextToBlood;
    public static int generating;
    boolean spawnMade = false, spawnMade2 = false,  exitMade = false, exitMade2 = false;

    public void create(String filePath, int floorType) {
        //chance variables must have sum of 100
       final int chanceForExcellentLoot = 10;
       final int chanceForDecentLoot = 25;
       final int chanceForCommonLoot = 45;
       final int chanceForDecentChest = 15;
       final int chanceForExcellentChest = 5;

       final  int chanceForPotionLoot = 50;

        spawnMade = false; spawnMade2 = false;  exitMade = false; exitMade2 = false;

       // final Pixmap map = new Pixmap(75, 75, Pixmap.Format.RGBA8888);
        final Grid grid = new Grid(25); // This algorithm likes odd-sized maps, although it works either way.
        final Grid gridConversion = new Grid(75);
        final Random random = new Random();
        final int[] countz = {0};
        final int[] specialRoom = {0};
        countz[0] = 0;
        final ArrayList<Vector2> usedTiles = new ArrayList<>();
        final DungeonGenerator dungeonGenerator = new DungeonGenerator();
        dungeonGenerator.setRoomGenerationAttempts(500);
        dungeonGenerator.setRandomConnectorChance(0.25f);
        dungeonGenerator.setMaxRoomSize(5);
        dungeonGenerator.addRoomType(new RoomType() { //0.1 is spawn   0.2 is exit    0.5 is enemy spawn,    0.4 is empty    0.7 is special room         Loot is 0.01-0.09
                                         @Override
                                         public void carve(AbstractRoomGenerator.Room room, Grid grid, float value) {
                                             System.out.println("ROOM");
                                             System.out.println("HEIGHT = " + room.getHeight());
                                             System.out.println("WIDTH = " + room.getHeight());
                                           if (countz[0] == 1) {
                                               room.fill(grid, 0.4f);
                                               grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.1f);
                                           }
                                           else if (countz[0] == 10){
                                               room.fill(grid, 0.4f);
                                               grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.2f);
                                           }
                                           else {
                                               if ((random.nextInt(100) < 40) && (specialRoom[0] < 3)) { //numbers 0.7-0.79 are special room variants

                                                   int roll = random.nextInt(100) + 1; ///RITUAL ROOM

                                                   if (roll > 0 && roll < 20){
                                                       room.fill(grid, 0.71f);
                                                       grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.711f);
                                                       usedTiles.add(new Vector2(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2)));
                                                       for (int i = 0; i <= random.nextInt(room.getHeight()) + 1 + random.nextInt(room.getWidth()); i++){
                                                           int x = room.getX() + 1 + (random.nextInt(room.getWidth()) - 1);
                                                           int y = room.getY() + 1 + (random.nextInt(room.getHeight()) - 1);
                                                           if (x == (room.getX() + (room.getWidth() / 2))&& y == (room.getY() + (room.getHeight() / 2)) ){
                                                                i --;
                                                           }
                                                           else
                                                           grid.set(x, y, 0.7111f);
                                                           usedTiles.add(new Vector2(x, y));
                                                       }
                                                       for (int i = 0; i <= room.getHeight(); i++) {
                                                           int x = room.getX() +(random.nextInt(room.getWidth() ));
                                                           int y = room.getY()  +(random.nextInt(room.getHeight()));
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                   alreadyUsed= true;
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               if (random.nextInt(100) < chanceForPotionLoot)  grid.set(x, y, 0.71111f);
                                                               else grid.set(x, y, 0.71f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }

                                                       }
                                                   }
                                                   else if (roll > 20 && roll < 40){ //////ANVIL ROOM
                                                       final int halfSize = (room.getWidth() + room.getHeight()) / 2;
                                                       final int maxDistanceFromCenter = halfSize * 9 / 10;
                                                       for (int x = 0, width = room.getWidth(); x < width; x++) {
                                                           for (int y = 0, height = room.getHeight(); y < height; y++) {
                                                               final int distanceFromCenter = Math.abs(x - width / 2) + Math.abs(y - height / 2);
                                                               if (distanceFromCenter < maxDistanceFromCenter) {
                                                                   grid.set(x + room.getX(), y + room.getY(), 0.72f);
                                                               }
                                                           }
                                                       }
                                                       grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.722f);
                                                   }
                                                   else if (roll > 40 && roll < 60){
                                                       room.fill(grid, 0.73f);
                                                       grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.733f);
                                                   }
                                                   else if (roll > 60 && roll < 80){ //GrassROOM

                                                       room.fill(grid, 0.74f);
                                                       int retry = 0;
                                                       for (int i = 0; i <= random.nextInt(4); i++) {
                                                           int x = room.getX() + 1 +(random.nextInt(room.getWidth() - 1));
                                                           int y = room.getY() + 1 +(random.nextInt(room.getHeight() - 1));
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                   alreadyUsed = true; if (retry < 10){i--; retry++;}
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               grid.set(x, y, 0.7444f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }

                                                       }
                                                       for (int i = 0; i <= random.nextInt(4); i++) {
                                                           int x = room.getX() + 1 +(random.nextInt(room.getWidth() - 1));
                                                           int y = room.getY() + 1 +(random.nextInt(room.getHeight() - 1));
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                   alreadyUsed = true; if (retry < 10){i--; retry++;}
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               grid.set(x, y, 0.74444f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }

                                                       }

                                                       grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.744f);



                                                   }
                                                   else if (roll > 80 && roll <= 90){ //Coffin Room
                                                       room.fill(grid, 0.75f);
                                                       for (int i = 0; i <= room.getHeight() + room.getWidth(); i++) {
                                                           int x = room.getX() +(random.nextInt(room.getWidth() ));
                                                           int y = room.getY()  +(random.nextInt(room.getHeight()));
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                   alreadyUsed= true;
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               grid.set(x, y, 0.755f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }

                                                       }
                                                   }
                                                   else if (roll > 90 && roll <= 100){ //Grass room w potion loot
                                                       final int size = Math.min(room.getWidth(), room.getHeight());
                                                       final int tSize = Math.max((size - 1) / 4, 2);
                                                       final int offset = Math.max(tSize / 4, tSize == 2 ? 1 : 2);
                                                       for (int x = offset, width = room.getWidth() - offset; x < width; x++) {
                                                           for (int y = offset, height = room.getHeight() - offset; y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.76f);
                                                           }
                                                       }for (int x = 0, width = tSize; x < width; x++) {
                                                           for (int y = 0, height = tSize; y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.76f);
                                                           }
                                                       }
                                                       for (int x = room.getWidth() - tSize, width = room.getWidth(); x < width; x++) {
                                                           for (int y = 0, height = tSize; y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.76f);
                                                           }
                                                       }
                                                       for (int x = 0, width = tSize; x < width; x++) {
                                                           for (int y = room.getHeight() - tSize, height = room.getHeight(); y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.76f);
                                                           }
                                                       }
                                                       for (int x = room.getWidth() - tSize, width = room.getWidth(); x < width; x++) {
                                                           for (int y = room.getHeight() - tSize, height = room.getHeight(); y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.76f);
                                                           }
                                                       }
                                                   }


                                                   //  else if (roll > 80 && roll < 100){room.fill(grid, 0.75f);}
                                                   else room.fill(grid, 0.7f);
                                                   specialRoom[0]++;

                                               } else {
                                                   switch(random.nextInt(2)) {
                                                       case 0:
                                                       final int towerSize = 3;
                                                       final int offset = Math.max(towerSize / 4, 2);
                                                       // Main room:
                                                       for (int x = offset, width = room.getWidth(); x < width; x++) {
                                                           for (int y = offset, height = room.getHeight(); y < height; y++) {
                                                               grid.set(x + room.getX(), y + room.getY(), 0.4f);
                                                           }
                                                       }
                                                       for (int x = 0, width = towerSize; x < width; x++) {
                                                           for (int y = 0, height = towerSize; y < height; y++) {
                                                               grid.set(x + room.getX() , y + room.getY() , 0.4f);
                                                           }
                                                       }
                                                       for (int x = room.getWidth() - towerSize, width = room.getWidth(); x < width; x++) {
                                                           for (int y = 0, height = towerSize; y < height; y++) {
                                                               grid.set(x + room.getX() , y + room.getY(), 0.4f);
                                                           }
                                                       }
                                                       for (int x = 0, width = towerSize; x < width; x++) {
                                                           for (int y = room.getHeight() - towerSize, height = room.getHeight(); y < height; y++) {
                                                               grid.set(x + room.getX() , y + room.getY() , 0.4f);
                                                           }
                                                       }
                                                       for (int x = room.getWidth() - towerSize, width = room.getWidth(); x < width; x++) {
                                                           for (int y = room.getHeight() - towerSize, height = room.getHeight(); y < height; y++) {
                                                               grid.set(x + room.getX() , y + room.getY() , 0.4f);
                                                           }
                                                       } break;
                                                       case 1:
                                                           room.fill(grid, 0.4f); break;
                                                   }


                                                   //room.fill(grid, 0.4f);
                                                   int amount = 0;
                                                   if (random.nextInt(100) < 20) {  grid.set(room.getX() + (room.getWidth() / 2), room.getY() + (room.getHeight() / 2), 0.5f); amount++; }
                                                   //ENEMY SPAWNS
                                                        int retry = 0;
                                                       for (int i = 0; i <= random.nextInt(4); i++) {
                                                           int x = room.getX() + 1 +(random.nextInt(room.getWidth() - 1));
                                                           int y = room.getY() + 1 +(random.nextInt(room.getHeight() - 1));
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                    alreadyUsed = true; if (retry < 10){i--; retry++;}
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               grid.set(x, y, 0.5f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }

                                                   } retry = 0;
                                                       for (int i = 0; i <= random.nextInt(3) + 1; i++) {
                                                           int x = room.getX() + 1 + (random.nextInt(room.getWidth() - 1) );
                                                           int y = room.getY() + 1 + (random.nextInt(room.getHeight() - 1) );
                                                           boolean alreadyUsed = false;
                                                           for (int a = 0; a < usedTiles.size(); a++) {
                                                               if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                   alreadyUsed = true; if (retry < 10){i--; retry++;}
                                                               }
                                                           }
                                                           if (!alreadyUsed) {
                                                               grid.set(x, y, 0.41f);
                                                               usedTiles.add(new Vector2(x, y));
                                                           }
                                                       }
                                                       retry = 0;

                                                   for (int i = 0; i <= 4; i++) {
                                                       int x = room.getX() +(random.nextInt(room.getWidth() ));
                                                       int y = room.getY()  +(random.nextInt(room.getHeight()));
                                                       boolean alreadyUsed = false;
                                                       for (int a = 0; a < usedTiles.size(); a++) {
                                                           if (x == usedTiles.get(a).x && y == usedTiles.get(a).y) {
                                                                alreadyUsed= true;
                                                           }
                                                       }
                                                       if (!alreadyUsed) {
                                                           if (random.nextInt(100) < chanceForExcellentLoot)  grid.set(x, y, 0.03f);
                                                           else if (random.nextInt(100) < chanceForDecentLoot) grid.set(x, y, 0.02f);
                                                           else if (random.nextInt(100) < chanceForCommonLoot) grid.set(x, y, 0.01f);
                                                           else if (random.nextInt(100) < chanceForExcellentChest) grid.set(x, y, 0.04f);
                                                           else if (random.nextInt(100) < chanceForDecentChest) grid.set(x, y, 0.05f);
                                                           else grid.set(x, y, 0.4f);
                                                           usedTiles.add(new Vector2(x, y));
                                                       }

                                                   }


                                               }
                                           }
                                         }

                                         @Override
                                         public boolean isValid(AbstractRoomGenerator.Room room) {
                                             //System.out.println(countz[0] == 0);
                                             countz[0] ++;
                                             //System.out.println(countz[0]);
                                             return true;
                                         }
                                     });
        dungeonGenerator.setTolerance(5); // Max difference between width and height.
        dungeonGenerator.setDeadEndRemovalIterations(5);
        dungeonGenerator.setWindingChance(0.25f);
        dungeonGenerator.setMinRoomSize(3);
        dungeonGenerator.setMaxRoomsAmount(10);
        dungeonGenerator.generate(grid);
        System.out.println("COMPLETED MAP GENERATION... BEGINNING ASSIGNING PROCESS");


        encodedMap = ""; encodedNavigationMap = ""; encodedWallMap = ""; encodedWallMap2 = "";
        int row = 1;
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                    if (grid.get(x, y) == 0.5 || grid.get(x, y) == 0.41f || grid.get(x,y) == 0.755) {
                        if (grid.get(x, y - 1) == 0f || grid.get(x, y + 1) == 0f || grid.get(x - 1, y) == 0f || grid.get(x + 1, y) == 0f
                                || grid.get(x, y - 1) == 0f || grid.get(x, y + 1) == 0f || grid.get(x - 1, y) == 0f || grid.get(x + 1, y) == 0f) {
                            grid.set(x, y, 0.4f);
                        }
                    }


                        float value = 0.5f;
                    if (grid.get(x, y) == 1.0) {value = 0f;}
                    else if (grid.get(x, y) == 0.1f){ value = 0.1f; }
                    else if (grid.get(x, y) == 0.2f){ value = 0.2f;}
                    else  if (grid.get(x, y) == 0f) {value = 1.0f;}
                    else if (grid.get(x, y) == 0.7f) {value = 0.7f;}
                    else if (grid.get(x, y) == 0.4f){value = 0.4f;}
                    else if (grid.get(x, y) == 0.71f) {value = 0.71f;}
                    else if (grid.get(x, y) == 0.72f) {value = 0.72f;}
                    else if (grid.get(x, y) == 0.73f) {value = 0.73f;}
                    else {value = grid.get(x, y);}
                    float[] values = new float[9];

                    boolean randomize = false;
                    float mainValue = 0, randomizedValue = 0;
                    if (value == 0.711f) {
                        randomize = true; mainValue = 0.71f; randomizedValue = 0.711f;
                    } else if (value == 0.722f){
                        randomize = true; mainValue = 0.72f; randomizedValue = 0.722f;
                    }else if (value == 0.733f){
                        randomize = true; mainValue = 0.73f; randomizedValue = 0.733f;
                    }else if (value == 0.744f){
                        randomize = true; mainValue = 0.74f; randomizedValue = 0.744f;
                    }else if (value == 0.7444f){
                        randomize = true; mainValue = 0.74f; randomizedValue = 0.7444f;
                    }else if (value == 0.74444f){
                        randomize = true; mainValue = 0.74f; randomizedValue = 0.74444f;
                    }
                    else if (value == 0.7111f){
                        randomize = true; mainValue = 0.71f; randomizedValue = 0.7111f;
                    } else if (value == 0.71111f){
                        randomize = true; mainValue = 0.71f; randomizedValue = 0.71111f;
                    }else if (value == 0.41f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.41f;
                        ///\/ loot
                    } else if (value == 0.5f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.5f;
                    }else if (value == 0.01f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.01f;
                    } else if (value == 0.02f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.02f;
                    } else if (value == 0.03f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.03f;
                    } else if (value == 0.04f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.04f;
                    } else if (value == 0.05f){
                        randomize = true; mainValue = 0.4f; randomizedValue = 0.05f;
                    }
                    else if (value == 0.755f){
                        for (int i = 0; i < values.length; i++){values[i] = 0.75f;}
                        values[1] = 0.755f;
                        values[0] = 0.755f;
                    }
                    else {
                        for (int i = 0; i < values.length; i++){values[i] = value;}
                    }

                    if (randomize){
                        for (int i = 0; i < values.length; i++){values[i] = mainValue;}
                        int roll = random.nextInt(9);
                        values[roll] = randomizedValue;
                    }


                    gridConversion.set((x * 3) + 1, (y * 3), values[0]);
                    gridConversion.set((x * 3) + 2, (y * 3), values[1]);
                    gridConversion.set((x * 3), (y * 3), values[2]);

                    gridConversion.set((x * 3) + 1, (y * 3) +1, values[3]);
                    gridConversion.set((x * 3) + 2, (y * 3) +1, values[4]);
                    gridConversion.set((x * 3), (y * 3) + 1, values[5]);

                    gridConversion.set((x * 3) + 1, (y * 3) + 2, values[6]);
                    gridConversion.set((x * 3) + 2, (y * 3) + 2, values[7]);
                    gridConversion.set((x * 3), (y * 3) + 2, values[8]);



              //  if (grid.get(x, y) != 1.0) {gridConversion.set((x * 2) + 1, (y * 2), 0.5f);}
              //  if (grid.get(x, y) != 1.0) {gridConversion.set((x * 2 )+ 1, (y * 2)- 1, 0.5f);}
              //  if (grid.get(x, y) != 1.0) {gridConversion.set((x * 2), (y * 2)- 1, 0.5f);}
              //  if (grid.get(x, y) != 1.0) {gridConversion.set((x * 2), (y * 2), 0.5f);}
            }
        }
        System.out.println("COMPLETED GRID RESIZE AND PLACEMENTS");

       // System.out.println(grid);
        /*
        final Color color = new Color();
        for (int x = 0; x < gridConversion.getWidth(); x++) {
            for (int y = 0; y < gridConversion.getHeight(); y++) {
                final float cell = 1f - gridConversion.get(x, y);
                color.set(cell, cell, cell, 1f);
                map.drawPixel(x, y, Color.rgba8888(color));
            }
        }
         ////////////////////////////MAIN MAP//////////////////////////////////////////
         */

        row = 1;
        for (int x = 0; x < gridConversion.getWidth(); x++) {
            for (int y = 0; y < gridConversion.getHeight(); y++) {
                generating ++;

                ////////////////////////////MAIN MAP//////////////////////////////////////////

                if (gridConversion.get(x, y) == 0f){encodedMap += "0,";}
                else if (gridConversion.get(x, y) != 0f) {

                    boolean up = false, down = false, right = false, left = false;

                    if (x + 1 != 75) {if (gridConversion.get(x + 1, y) != 0f){down = true;}}
                    if (x - 1 > -1) {if (gridConversion.get(x - 1, y) != 0f){up = true;}}
                    if (y + 1 != 75) {if (gridConversion.get(x , y + 1) != 0f){right = true;}}
                    if (y - 1 > -1) {if (gridConversion.get(x , y - 1) != 0f){left = true;}}

                    if (up && down && right && left) {
                        encodedMap += "212,";
                    } else if (down && right && up && !left) { encodedMap += "195,"; }
                    else if (down && right && !up && !left) { encodedMap += "179,"; }
                    else if (down && left && up && !right) { encodedMap += "198,"; }
                    else if (down && left && !right && !up) { encodedMap += "182,"; }
                    else if (up && left && !down && !right) { encodedMap += "230,"; }
                    else if (up && right && !down && !left) { encodedMap += "227,"; }
                    else if (down && left && right && !up) { encodedMap += "181,"; }
                    else if (up && left && right && !down) { encodedMap += "228,"; }
                    else { encodedMap += "195,"; } }
                int count = 0;
                for(int i = 0; i < encodedMap.length(); i++) {
                    if(encodedMap.charAt(i) == ',')
                        count++; }
                if (count >= row * 75 ) {row ++; encodedMap += "\n";}





                //// WALL MAP 1 ======================================================================================================================================
                //// WALL MAP 1 ======================================================================================================================================
                //// WALL MAP 1 ======================================================================================================================================

                generateWallMap1(gridConversion, x, y, row);


                //////////// AI NAVIGATION MAP ====================================================================================================
                //////////// AI NAVIGATION MAP ====================================================================================================
                //////////// AI NAVIGATION MAP ====================================================================================================



                generateAiNavigationGrid(gridConversion, x, y, row);



                /// WALL MAP 2 ====================================================================================================================
                /// WALL MAP 2 ====================================================================================================================
                /// WALL MAP 2 ====================================================================================================================


                generateWallMap2Grid(gridConversion, x, y, row);

            }
        }

        encodedMap += ","; encodedNavigationMap += ","; encodedWallMap += ","; encodedWallMap2 += ",";
        encodedMap = encodedMap.replace(",\n,", " ");
        encodedMap = encodedMap.replace(",,", " ");
        encodedNavigationMap = encodedNavigationMap.replace(",\n,", " ");
        encodedNavigationMap = encodedNavigationMap.replace(",,", " ");

        encodedWallMap = encodedWallMap.replace(",\n,", " ");
        encodedWallMap = encodedWallMap.replace(",,", " ");

        encodedWallMap2 = encodedWallMap2.replace(",\n,", " ");
        encodedWallMap2 = encodedWallMap2.replace(",,", " ");


        //texture = new Texture(map);
        //batch = new SpriteBatch();
        //System.out.println("START = " + encodedNavigationMap);
        FileHandle file = Gdx.files.local(filePath);
        file.writeString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<map version=\"1.2\" tiledversion=\"1.2.4\" orientation=\"orthogonal\" renderorder=\"right-down\" width=\"75\" height=\"75\" tilewidth=\"16\" tileheight=\"16\" infinite=\"0\" nextlayerid=\"2\" nextobjectid=\"1\">\n" +
                " <tileset firstgid=\"1\" source=\"Dungeon1-Tileset v3.tsx\"/>\n" +
                " <layer id=\"1\" name=\"Layer1\" width=\"75\" height=\"75\">\n" +
                "  <data encoding=\"csv\">\n" + encodedMap + "\n</data>\n" +
                " </layer>\n" +
                " <layer id=\"2\" name=\"navigation\" width=\"75\" height=\"75\">\n" +
                "  <data encoding=\"csv\">\n" + encodedNavigationMap + "\n</data>" +
                " \n </layer>\n" +
                " <layer id=\"3\" name=\"wall\" width=\"75\" height=\"75\">\n" +
                "  <data encoding=\"csv\">\n" + encodedWallMap + "\n</data>" +
                " \n </layer>\n" +
                " <layer id=\"4\" name=\"wall2\" width=\"75\" height=\"75\">\n" +
                "  <data encoding=\"csv\">\n" + encodedWallMap2
                + "\n</data>\n" +
                " </layer>\n" +
                "</map>", false);

    }

    /*
    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        batch.dispose();
    }
    */

    public void generateAiNavigationGrid(Grid gridConversion, int x, int y, int row){

        boolean navMade = false;
        if (y + 1 != 75) {
            for (int i = 0; i < ToolClass.unwalkableTileGridId.size(); i++){
                if (gridConversion.get(x, y + 1) == ToolClass.unwalkableTileGridId.get(i)) {encodedNavigationMap += "0,"; navMade =true; }
                if (navMade) break;
            }

            if (!navMade){//DOORS//////////////////////////////////
                if (x + 1 < 75 && x - 1 > 0 && y + 1 < 75 && y - 1 > 0){
                    if (gridConversion.get(x, y) == 1.0f){
                        if (gridConversion.get(x, y - 1) == 0.4f || gridConversion.get(x, y + 1) == 0.4f || gridConversion.get(x - 1, y) == 0.4f || gridConversion.get(x + 1, y)  == 0.4f
                                || gridConversion.get(x, y - 1) == 0.5f || gridConversion.get(x, y + 1) == 0.5f || gridConversion.get(x - 1, y) == 0.5f || gridConversion.get(x + 1, y)  == 0.5f ){

                            if ((gridConversion.get(x + 1, y) == 0.4f || gridConversion.get(x - 1, y) == 0.4f) || (gridConversion.get(x + 1, y) == 0.5f && gridConversion.get(x - 1, y) == 0.5f)  ){
                                if (gridConversion.get(x, y + 1) == 1.0f && gridConversion.get(x, y - 1) == 1.0f && gridConversion.get(x, y + 2) == 1.0f && gridConversion.get(x, y - 2) == 1.0f){
                                    encodedNavigationMap  += "0,";
                                    navMade = true;
                                } else if (gridConversion.get(x, y + 1) == 1.0f && gridConversion.get(x, y - 1) == 1.0f) {
                                    encodedNavigationMap  += "0,";
                                    navMade = true;
                                } else{
                                    encodedNavigationMap  += "212,";
                                    navMade = true;
                                }
                            }
                            else if ((gridConversion.get(x, y + 1) == 0.4f || gridConversion.get(x, y - 1) == 0.4f) || (gridConversion.get(x, y + 1) == 0.5f && gridConversion.get(x, y - 1) == 0.5f) ){
                                if (gridConversion.get(x + 1, y) == 1.0f && gridConversion.get(x - 1, y) == 1.0f && gridConversion.get(x + 2, y) == 1.0f && gridConversion.get(x - 2, y) == 1.0f){
                                    encodedNavigationMap += "212,";
                                    navMade = true;
                                } else if (gridConversion.get(x + 1, y) == 1.0f && gridConversion.get(x - 1, y) == 1.0f) {
                                    encodedNavigationMap  += "212,";
                                    navMade = true;
                                } else{
                                    encodedNavigationMap  += "0,";
                                    navMade = true;
                                }

                            }
                        }
                    }

                }

            }



            if (gridConversion.get(x, y+1) == 0f && !navMade) {navMade = true; encodedNavigationMap += "0,";}
            if (gridConversion.get(x, y + 1) != 0f && !navMade) {
                encodedNavigationMap += "212,"; }
        } else { encodedNavigationMap += "0,"; }
        int count = 0;
        for(int i = 0; i < encodedNavigationMap.length(); i++) {
            if(encodedNavigationMap.charAt(i) == ',')
                count++; }
        if (count >= row * 75 ) {row ++; encodedNavigationMap += "\n";}
    }


    public void generateWallMap1(Grid gridConversion, int x, int y, int row){


        boolean made = false;
        if (!made &&   x + 1 < 75 && x - 1 > 0 && y + 1 < 75 && y - 1 > 0) {

            boolean up = gridConversion.get(x - 1, y) != 0;
            boolean down = gridConversion.get(x + 1, y) != 0;
            boolean right = gridConversion.get(x, y + 1) != 0;
            boolean left = gridConversion.get(x, y - 1) != 0;
            if (down && !left && right && !up) {
                if (gridConversion.get(x, y) == 0) {
                    encodedWallMap += "162,";
                    made = true;
                }
            }
            if (down && left && !right && !up && !made) {
                if (gridConversion.get(x, y) == 0) {
                    encodedWallMap += "161,";
                    made = true;
                }
            }
        }
        Random random = new Random();
        if (!made) {
            if (x + 1 != 75) {
                if (gridConversion.get(x + 1, y) != 0) {
                    if (gridConversion.get(x, y) == 0) {
                        int wallType = random.nextInt(15);
                        if (!wallDesign) {
                            if (wallType == 1) {
                                encodedWallMap += "138,";
                                made = true;
                            } else if (wallType == 2 || wallType == 3) {
                                encodedWallMap += "135,";
                                made = true;
                            } else {
                                encodedWallMap += "164,";
                                made = true;
                            }
                            wallDesign =  true;
                        } else {
                            wallDesign = false;
                            encodedWallMap += "164,";
                            made = true;
                        }
                    } } }
        }

        if ( !made) {
            if (y + 1 < 75 && y - 1 > 0 && (x + 3 < 75)) {
                boolean up = gridConversion.get(x - 1, y) != 0;
                boolean down = gridConversion.get(x + 1, y) != 0;
                boolean right = gridConversion.get(x, y + 1) != 0;
                boolean left = gridConversion.get(x, y - 1) != 0;
                boolean down2 = gridConversion.get(x + 2, y) != 0;
                if (gridConversion.get(x + 2, y - 1) != 0 && !right && !up && !down && !left && !down2) {
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap += "117,";
                        made = true;
                    }
                }
                if (gridConversion.get(x + 2, y + 1) != 0 && !right && !up && !down && !left && !down2 && !made) {
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap += "115,";
                        made = true;
                    }
                }
            }
        }
        if (!made) {
            if (x + 2 < 75) {
                if (gridConversion.get(x + 2, y) != 0  && gridConversion.get(x + 1, y) == 0) {
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap += "148,"; made = true;
                    } }
            }
        }

        if (!made){
            if (y + 1 < 75 && y - 1 > -1) {
                if (gridConversion.get(x, y + 1) != 0) {
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap += "131,"; made = true;
                    } }
                if (gridConversion.get(x, y - 1) != 0 && !made) {
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap += "133,"; made = true;
                    } }
            }
        }
        if (!made){  ///SPECIAL ROOMS /////////////////////////////////////////////////////////////////////////////////////////
            if ( x + 1 < 75 && x - 1 > 0 && y + 1 < 75 && y - 1 > 0 && (gridConversion.get(x, y) == 0.71f || gridConversion.get(x, y) == 0.711f || gridConversion.get(x, y) == 0.7111f || gridConversion.get(x, y) == 0.71111f)){//RITUAL ROOM
                boolean up = gridConversion.get(x - 1, y) != 0  ;
                boolean down = gridConversion.get(x + 1, y) != 0;
                boolean right = gridConversion.get(x, y + 1) != 0;
                boolean left = gridConversion.get(x, y - 1) != 0;
                if (up && down && left && right) encodedWallMap += "232,";
                else if (!up && down && left && right) encodedWallMap += "216,";
                else if (!up && down && !left && right) encodedWallMap += "215,";
                else if (!up && down && left && !right) encodedWallMap += "217,";
                else if (up && !down && left && right) encodedWallMap += "248,";
                else if (up && !down && !left && right) encodedWallMap += "247,";
                else if (up && !down && left && !right) encodedWallMap += "249,";
                else if (up && down && !left && right) encodedWallMap += "231,";
                else if (up && down && left && !right) encodedWallMap += "233,";

                made = true;
            }
            if (!made && (gridConversion.get(x, y) == 0.72f || gridConversion.get(x, y) == 0.722f)){//ANVIL ROOM
                encodedWallMap += "183,";
                made = true;
            }
            if (!made && (gridConversion.get(x, y) == 0.74f) || (gridConversion.get(x, y) == 0.744f) || (gridConversion.get(x, y) == 0.7444f)  || (gridConversion.get(x, y) == 0.74444f)){
                encodedWallMap += "239,";
                made = true;
            }

            if ( gridConversion.get(x, y) == 0.7f && !made){
                encodedWallMap += "100,"; made = true; }
        }

        if (!made){ encodedWallMap += "0,";}



        int count = 0;
        for(int i = 0; i < encodedWallMap.length(); i++) {
            if(encodedWallMap.charAt(i) == ',')
                count++; }
        if (count >= row * 75 ) {row ++; encodedWallMap += "\n";}
    }


    public void generateWallMap2Grid(Grid gridConversion, int x, int y, int row){
        // NUMBERS FROM 1-14 ARE PROP PLACEHOLDERS////////////////////////////////////////
        boolean made = false;
        Random random = new Random();

        if (!made){
            if (x + 1 < 75 && x - 1 > 0 && y + 1 < 75 && y - 1 > 0 && x + 2 < 75){
                boolean up = gridConversion.get(x - 1, y) != 0;
                boolean down = gridConversion.get(x + 1, y) != 0;
                boolean right = gridConversion.get(x, y + 1) != 0;
                boolean left = gridConversion.get(x, y - 1) != 0;
                boolean up2 = gridConversion.get(x -2, y) != 0;
                boolean down2 = gridConversion.get(x + 2, y) != 0;
                boolean upRight =  gridConversion.get(x - 1, y + 1) != 0;
                boolean upLeft =  gridConversion.get(x - 1, y - 1) != 0;
                boolean downRight =  gridConversion.get(x + 1, y + 1) != 0;
                boolean downLeft =  gridConversion.get(x + 1, y - 1) != 0;
                if (up && !down && right && !left){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "130,"; made = true; } }
                else if (up && !down && left && !right){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "129,"; made = true; } }
                else if (down2 && !down &&left && !right && !up){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "145,"; made = true; } }
                else if (down2 && !down &&!left && right && !up){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "146,"; made = true; } }
                else if (upRight && !right && !up && !down && !left){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "114,"; made = true; } }
                else if (upLeft && !right && !up && !down && !left){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "113,"; made = true; } }

                else if (downRight && !right && !up && !down && !left){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "131,"; made = true; } }
                else if (downLeft && !right && !up && !down && !left){
                    if (gridConversion.get(x, y) == 0) {
                        encodedWallMap2 += "133,"; made = true; } }
            }
        }
        ///////////////////////////////////////////////////SPECIAL ROOM OBJECTS
        if (!made){  ///ENCHANTMENTCIRCLE
            if (gridConversion.get(x, y) == 0.711f){
                encodedWallMap2 += "103,"; made = true; }
            ///ANVIL
            if (gridConversion.get(x, y) == 0.722f){
                encodedWallMap2 += "81,"; made = true;
            }
            // BIG POTION
            if (gridConversion.get(x, y) == 0.744f){
                encodedWallMap2 += "67,"; made = true;
            }
            // MISC LOOT
            if (gridConversion.get(x, y) == 0.7444f){
                encodedWallMap2 += "93,"; made = true;
            }// SPIRIT SPAWN
            if (gridConversion.get(x, y) == 0.74444f){
                encodedWallMap2 += "68,"; made = true;
            }

            //SPECTRAL CHEST
            if (gridConversion.get(x, y) == 0.733f){
                encodedWallMap2 += "83,"; made = true;
            }

            //BOOK
            if (gridConversion.get(x, y) == 0.7111f){
                encodedWallMap2 += "199,"; made = true;
            }

            //Coffin
            if (gridConversion.get(x, y) == 0.755f){
                encodedWallMap2 += "84,"; made = true;
            }
        }


        /////////////////////////////////////////////////MISC OBJECTS
        if (!made){   //BOOK
            if (gridConversion.get(x, y) == 0.41f){
                encodedWallMap2 += "253,"; made = true;
            }
        }
        if (!made && x + 2 < 75 && y + 2 < 75 && y - 2 > 0 && x - 2 > 0 && gridConversion.get(x, y) == 1.0f ){
            boolean up = gridConversion.get(x - 1, y) != 0;
            boolean down = gridConversion.get(x + 1, y) != 0;
            boolean right = gridConversion.get(x, y + 1) != 0;
            boolean left = gridConversion.get(x, y - 1) != 0;
            boolean up2 = gridConversion.get(x - 2, y) != 0;
            boolean down2 = gridConversion.get(x + 2, y) != 0;
            boolean right2 = gridConversion.get(x, y + 2) != 0;
            boolean left2 = gridConversion.get(x, y - 2) != 0;
            int openarea = 0;
            if (!up2) openarea ++; if (!down2) openarea ++; if (!right2) openarea ++; if (!left2) openarea++;
            if (up && down && left && right && (openarea == 3)){
                encodedWallMap2 += "82,"; made = true;
            }
        }
        if (!made){
            if (x - 1 > -1){
                if ( gridConversion.get(x - 1, y) != 0){
                    if (gridConversion.get(x, y) == 0){
                        encodedWallMap2 += "243,"; made = true; }
                }
            }
        }
        if (!made){
            if ( gridConversion.get(x, y) == 0.1f && !spawnMade){
                if (spawnMade2){
                    spawnMade = true;
                    encodedWallMap2 += "236,"; made = true;
                } else {
                    encodedWallMap2 += "235,"; made = true;
                }
                spawnMade2 = true;
            }
        }
        if (!made){
            if ( gridConversion.get(x, y) == 0.2f && !exitMade){
                encodedWallMap2 += "219,"; made = true; exitMade = true;}
            else if (gridConversion.get(x, y) == 0.2f && !exitMade2){
                encodedWallMap2 += "220,"; made = true; exitMade2 = true;
            }
        }

        if (!made){
            if ( gridConversion.get(x, y) == 1.0f && random.nextInt(100) < 2){
                encodedWallMap2 += "102,"; made = true;
            }
        }

        if (!made){
            if ( gridConversion.get(x, y) == 0.5f){
                encodedWallMap2 += "101,"; made = true;
            }
        }

        if (!made){ //DOORS//////////////////////////////////
            if (x + 1 < 75 && x - 1 > 0 && y + 1 < 75 && y - 1 > 0){
                if (gridConversion.get(x, y) == 0f){
                    if (gridConversion.get(x + 1, y) == 1.0f && (gridConversion.get(x + 1, y + 1) == 0.4f || gridConversion.get(x + 1, y - 1) == 0.4f) ){
                        encodedWallMap2 += "186,";
                        made = true;
                    }
                }

                if (gridConversion.get(x, y) == 1.0f){
                    if (gridConversion.get(x, y - 1) == 0.4f || gridConversion.get(x, y + 1) == 0.4f || gridConversion.get(x - 1, y) == 0.4f || gridConversion.get(x + 1, y)  == 0.4f
                            || gridConversion.get(x, y - 1) == 0.5f || gridConversion.get(x, y + 1) == 0.5f || gridConversion.get(x - 1, y) == 0.5f || gridConversion.get(x + 1, y)  == 0.5f ){

                        if ((gridConversion.get(x + 1, y) == 0.4f || gridConversion.get(x - 1, y) == 0.4f) || (gridConversion.get(x + 1, y) == 0.5f && gridConversion.get(x - 1, y) == 0.5f)  ){
                            if (gridConversion.get(x, y + 1) == 1.0f && gridConversion.get(x, y - 1) == 1.0f && gridConversion.get(x, y + 2) == 1.0f && gridConversion.get(x, y - 2) == 1.0f){
                                encodedWallMap2 += "104,";
                                made = true;
                            } else if (gridConversion.get(x, y + 1) == 1.0f && gridConversion.get(x, y - 1) == 1.0f) {
                                encodedWallMap2 += "104,";
                                made = true;
                            } else{
                                encodedWallMap2 += "164,";
                                made = true;
                            }
                        }


                        else if ((gridConversion.get(x, y + 1) == 0.4f || gridConversion.get(x, y - 1) == 0.4f) || (gridConversion.get(x, y + 1) == 0.5f && gridConversion.get(x, y - 1) == 0.5f) ){
                            if (gridConversion.get(x + 1, y) == 1.0f && gridConversion.get(x - 1, y) == 1.0f && gridConversion.get(x + 2, y) == 1.0f && gridConversion.get(x - 2, y) == 1.0f){
                                encodedWallMap2 += "104,";
                                made = true;
                            } else if (gridConversion.get(x + 1, y) == 1.0f && gridConversion.get(x - 1, y) == 1.0f) {
                                encodedWallMap2 += "104,";
                                made = true;
                            } else{
                                encodedWallMap2 += "202,";
                                made = true;
                            }

                        }
                    }
                }

            }

        }


        /////////////////////////////////////////////////////////////////////////////////////////////LOOOOOOOOOOOOOOOOOT/////////////////////////////

        if (!made){
            if (gridConversion.get(x, y) == 0.01f){
                encodedWallMap2 += "90,"; made = true;
            } else if (gridConversion.get(x,y) == 0.02f){
                encodedWallMap2 += "91,"; made = true;
            } else if (gridConversion.get(x,y) == 0.03f){
                encodedWallMap2 += "92,"; made = true;
            } else if (gridConversion.get(x,y) == 0.04f){
                encodedWallMap2 += "89,"; made = true;
            } else if (gridConversion.get(x,y) == 0.05f){
                encodedWallMap2 += "88,"; made = true;
            }
            else if (gridConversion.get(x,y) == 0.71111f){
                encodedWallMap2 += "93,"; made = true;
            }
        }

        if (!made && x + 2 < 75 && y + 2 < 75 && y - 2 > 0 && x - 2 > 0){
            boolean up = gridConversion.get(x - 1, y) == 0;
            boolean down = gridConversion.get(x + 1, y) == 0;
            boolean right = gridConversion.get(x, y + 1) == 0;
            boolean left = gridConversion.get(x, y - 1) == 0;
            boolean up2 = gridConversion.get(x - 2, y) == 0;
            boolean down2 = gridConversion.get(x + 2, y) == 0;
            boolean upRight = gridConversion.get(x - 1, y + 1) == 0;
            boolean upLeft = gridConversion.get(x - 1, y - 1) == 0;
            boolean downRight = gridConversion.get(x + 1, y + 1) == 0;
            boolean downLeft = gridConversion.get(x + 1, y - 1) == 0;

            if ( gridConversion.get(x, y) == 1.0f ) {if (up || down || right || left) {encodedWallMap2 += "105,"; made = true;}}
            else if ( gridConversion.get(x, y) == 0.4f)  {encodedWallMap2 += "106,"; made = true;}

        }



        if (!made){
            if ( gridConversion.get(x, y) != 0f){
                if (random.nextInt(100) < 1){
                    encodedWallMap2 += "15,"; made = true;
                    skullNextToBlood = true;
                } else{
                            /*
                            if (skullNextToBlood){
                                if (random.nextInt(100) < 10){
                                    encodedWallMap2 += "189,"; made = true;
                                    skullNextToBlood = false;
                            } else {
                                    if (random.nextInt(100) < 5) {
                                        encodedWallMap2 += "188,";
                                        made = true;
                                    }


                             */

                }}
        }



        if (!made){ encodedWallMap2 += "0,";}

        int count = 0;
        for(int i = 0; i < encodedWallMap2.length(); i++) {
            if(encodedWallMap2.charAt(i) == ',')
                count++; }
        if (count >= row * 75 ) {row ++; encodedWallMap2 += "\n";}
    }
}
