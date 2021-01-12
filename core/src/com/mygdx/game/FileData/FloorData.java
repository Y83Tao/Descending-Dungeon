package com.mygdx.game.FileData;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.github.czyzby.noise4j.map.Grid;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.Inventory;
import com.mygdx.game.Item;
import com.mygdx.game.WorldObjects;

import java.util.ArrayList;

public class FloorData {

    public Vector3[] getFloor1() {
        return floor1;
    }

    public Vector3[] floor1, floor2, floor3, floor4, floor5, floor6, floor7, floor8, floor9, floor10;

    public Vector3[] itemFloor1, itemFloor2, itemFloor3, itemFloor4, itemFloor5, itemFloor6, itemFloor7, itemFloor8, itemFloor9, itemFloor10;

    public Vector2[] containerFloor1, containerFloor2, containerFloor3, containerFloor4, containerFloor5, containerFloor6, containerFloor7, containerFloor8, containerFloor9, containerFloor10;

    public Inventory.Items[] itemTypeFloor1, itemTypeFloor2, itemTypeFloor3, itemTypeFloor4, itemTypeFloor5, itemTypeFloor6, itemTypeFloor7, itemTypeFloor8, itemTypeFloor9, itemTypeFloor10, itemTypeFloor11;

    public Item.itemContainer.ChestType[] chestTypeFloor1, chestTypeFloor2, chestTypeFloor3, chestTypeFloor4, chestTypeFloor5, chestTypeFloor6, chestTypeFloor7, chestTypeFloor8, chestTypeFloor9, chestTypeFloor10, chestTypeFloor11;

    public Inventory.Items[][] containerItemTypeFloor1, containerItemTypeFloor2, containerItemTypeFloor3, containerItemTypeFloor4, containerItemTypeFloor5, containerItemTypeFloor6, containerItemTypeFloor7, containerItemTypeFloor8, containerItemTypeFloor9, containerItemTypeFloor10, containerItemTypeFloor11;

    public int[][] containerItemNumberFloor1, containerItemNumberFloor2, containerItemNumberFloor3, containerItemNumberFloor4, containerItemNumberFloor5, containerItemNumberFloor6, containerItemNumberFloor7, containerItemNumberFloor8, containerItemNumberFloor9, containerItemNumberFloor10, containerItemNumberFloor11;

    public String[] floor1Name, floor2Name, floor3Name, floor4Name, floor5Name, floor6Name, floor7Name, floor8Name, floor9Name, floor10Name;

    public Vector3[] worldObject1, worldObject2, worldObject3, worldObject4, worldObject5, worldObject6, worldObject7, worldObject8, worldObject9, worldObject10;
    public WorldObjects.InteractableObjectType[] worldObjectType1, worldObjectType2, worldObjectType3, worldObjectType4, worldObjectType5, worldObjectType6, worldObjectType7, worldObjectType8, worldObjectType9, worldObjectType10;

    public Item.interactableObject.InteractableType[]  interactableType1, interactableType2, interactableType3, interactableType4, interactableType5, interactableType6,interactableType7, interactableType8, interactableType9, interactableType10;
    public Inventory.Items[] interactableItem1, interactableItem2, interactableItem3, interactableItem4, interactableItem5, interactableItem6, interactableItem7, interactableItem8, interactableItem9, interactableItem10;
    public Vector3[] interactableXYZ1, interactableXYZ2, interactableXYZ3, interactableXYZ4, interactableXYZ5, interactableXYZ6, interactableXYZ7, interactableXYZ8, interactableXYZ9, interactableXYZ10;

    public Vector3[] makeArrayPos(ArrayList<EnemyTypesSpawn> array){
        Vector3[] arrayMade = new Vector3[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = new Vector3(array.get(i).posX, array.get(i).posY, array.get(i).currentHP);
        }
        return arrayMade;
    }

    public Vector3[] makeArrayPosItems(ArrayList<Item> array){
        Vector3[] arrayMade = new Vector3[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = new Vector3(array.get(i).x   , array.get(i).y, array.get(i).itemNumber);
        }
        return arrayMade;
    }

    public Inventory.Items[] makeArrayPosItemsNames(ArrayList<Item> array){
        Inventory.Items[] arrayMade = new Inventory.Items[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).itemType;
        }
        return arrayMade;
    }


    public String[] makeArrayName(ArrayList<EnemyTypesSpawn> array) {
        String[] arrayMade = new String[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).name;
        }
        return arrayMade;
    }

    public Vector2[] makeArrayContainer(ArrayList<Item.itemContainer> array){
        Vector2[] arrayMade = new Vector2[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = new Vector2(array.get(i).x   , array.get(i).y);
        }
        return arrayMade;
    }
    public Item.itemContainer.ChestType[] makeArrayContainerChestType(ArrayList<Item.itemContainer> array){
        Item.itemContainer.ChestType[] arrayMade = new Item.itemContainer.ChestType[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).chestType;
        }
        return arrayMade;
    }

    public Inventory.Items[][] makeArrayContainerItems(ArrayList<Item.itemContainer> array){
        Inventory.Items[][] arrayMade = new Inventory.Items[array.size()][4];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i][0] = array.get(i).container[0].item;
            arrayMade[i][1] = array.get(i).container[1].item;
            arrayMade[i][2] = array.get(i).container[2].item;
            arrayMade[i][3] = array.get(i).container[3].item;
        }
        return arrayMade;
    }
    public int[][] makeArrayContainerItemNumbers(ArrayList<Item.itemContainer> array){
        int[][] arrayMade = new int[array.size()][4];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i][0] = array.get(i).container[0].itemNumber;
            arrayMade[i][1] = array.get(i).container[1].itemNumber;
            arrayMade[i][2] = array.get(i).container[2].itemNumber;
            arrayMade[i][3] = array.get(i).container[3].itemNumber;
        }
        return arrayMade;
    }
    public Vector3[] makeArrayWorldObjectValues(ArrayList<WorldObjects> array){
        Vector3[] arrayMade = new Vector3[array.size()];
        for (int i = 0; i < array.size(); i++){
            int z;
            if (array.get(i).destroyed) z = 1;
            else z = 0;
            arrayMade[i] = new Vector3(array.get(i).posX, array.get(i).posY, z);
        }
        return arrayMade;
    }
    public WorldObjects.InteractableObjectType[] makeArrayWorldObjectType(ArrayList<WorldObjects> array){
        WorldObjects.InteractableObjectType[] arrayMade = new WorldObjects.InteractableObjectType[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).interactableObjectType;
        }
        return arrayMade;
    }


    public Item.interactableObject.InteractableType[] makeArrayForInteractableObjects(ArrayList<Item.interactableObject> array){
        Item.interactableObject.InteractableType[] arrayMade = new Item.interactableObject.InteractableType[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).interactableType;
        }
        return arrayMade;
    }
    public Inventory.Items[] makeArrayForInteractableItemTrader(ArrayList<Item.interactableObject> array){
        Inventory.Items[] arrayMade = new Inventory.Items[array.size()];
        for (int i = 0; i < array.size(); i++){
            arrayMade[i] = array.get(i).ifTraderItem;
        }
        return arrayMade;
    }
    public Vector3[]  makeArrayForInteractableXYBOOL(ArrayList<Item.interactableObject> array){
        Vector3[] arrayMade = new Vector3[array.size()];
        for (int i = 0; i < array.size(); i++){
            int z = 0;
            if (array.get(i).isUsed) { z =0;}
            else z = 1;
            arrayMade[i] = new Vector3(array.get(i).x,array.get(i).y ,z);
        }
        return arrayMade;
    }

    public void setFloor(ArrayList<EnemyTypesSpawn> enemies, ArrayList<Item> items, ArrayList<Item.itemContainer> container, ArrayList<WorldObjects> worldObjects, ArrayList<Item.interactableObject> interactableObjects, int floor) {

        switch (floor) {
            case 1:
                floor1 = null;
                floor1 = makeArrayPos(enemies);
                floor1Name = makeArrayName(enemies);
                itemFloor1 = makeArrayPosItems(items);
                itemTypeFloor1 = makeArrayPosItemsNames(items);
                containerFloor1 = makeArrayContainer(container);
                containerItemTypeFloor1 = makeArrayContainerItems(container);
                chestTypeFloor1 = makeArrayContainerChestType(container);
                containerItemNumberFloor1 = makeArrayContainerItemNumbers(container);
                worldObject1 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType1 = makeArrayWorldObjectType(worldObjects);
                interactableType1 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem1 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ1 = makeArrayForInteractableXYBOOL(interactableObjects);

                break;
            case 2:
                floor2 = null;
                floor2 = makeArrayPos(enemies);
                floor2Name = makeArrayName(enemies);
                itemFloor2 = makeArrayPosItems(items);
                itemTypeFloor2 = makeArrayPosItemsNames(items);
                containerFloor2 = makeArrayContainer(container);
                containerItemTypeFloor2 = makeArrayContainerItems(container);
                chestTypeFloor2 = makeArrayContainerChestType(container);
                containerItemNumberFloor2 = makeArrayContainerItemNumbers(container);
                worldObject2 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType2 = makeArrayWorldObjectType(worldObjects);
                interactableType2 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem2 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ2 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 3:
                floor3 = null;
                floor3 = makeArrayPos(enemies);
                floor3Name = makeArrayName(enemies);
                itemFloor3 = makeArrayPosItems(items);
                itemTypeFloor3 = makeArrayPosItemsNames(items);
                containerFloor3 = makeArrayContainer(container);
                containerItemTypeFloor3 = makeArrayContainerItems(container);
                chestTypeFloor3 = makeArrayContainerChestType(container);
                containerItemNumberFloor3 = makeArrayContainerItemNumbers(container);
                worldObject3 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType3 = makeArrayWorldObjectType(worldObjects);
                interactableType3 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem3 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ3 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 4:
                floor4 = null;
                floor4 = makeArrayPos(enemies);
                floor4Name = makeArrayName(enemies);
                itemFloor4 = makeArrayPosItems(items);
                itemTypeFloor4 = makeArrayPosItemsNames(items);
                containerFloor4 = makeArrayContainer(container);
                containerItemTypeFloor4 = makeArrayContainerItems(container);
                chestTypeFloor4 = makeArrayContainerChestType(container);
                containerItemNumberFloor4 = makeArrayContainerItemNumbers(container);
                worldObject4 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType4 = makeArrayWorldObjectType(worldObjects);

                interactableType4 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem4 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ4 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 5:
                floor5 = null;
                floor5 = makeArrayPos(enemies);
                floor5Name = makeArrayName(enemies);
                itemFloor5 = makeArrayPosItems(items);
                itemTypeFloor5 = makeArrayPosItemsNames(items);
                containerFloor5 = makeArrayContainer(container);
                containerItemTypeFloor5 = makeArrayContainerItems(container);
                chestTypeFloor5 = makeArrayContainerChestType(container);
                containerItemNumberFloor5 = makeArrayContainerItemNumbers(container);
                worldObject5 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType5 = makeArrayWorldObjectType(worldObjects);

                interactableType5 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem5 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ5 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 6:
                floor6 = null;
                floor6 = makeArrayPos(enemies);
                floor6Name = makeArrayName(enemies);
                itemFloor6 = makeArrayPosItems(items);
                itemTypeFloor6 = makeArrayPosItemsNames(items);
                containerFloor6 = makeArrayContainer(container);
                containerItemTypeFloor6 = makeArrayContainerItems(container);
                chestTypeFloor6 = makeArrayContainerChestType(container);
                containerItemNumberFloor6 = makeArrayContainerItemNumbers(container);
                worldObject6 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType6 = makeArrayWorldObjectType(worldObjects);

                interactableType6 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem6 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ6 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 7:
                floor7 = null;
                floor7 = makeArrayPos(enemies);
                floor7Name = makeArrayName(enemies);
                itemFloor7 = makeArrayPosItems(items);
                itemTypeFloor7 = makeArrayPosItemsNames(items);
                containerFloor7 = makeArrayContainer(container);
                containerItemTypeFloor7 = makeArrayContainerItems(container);
                chestTypeFloor7 = makeArrayContainerChestType(container);
                containerItemNumberFloor7 = makeArrayContainerItemNumbers(container);
                worldObject7 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType7 = makeArrayWorldObjectType(worldObjects);

                interactableType7 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem7 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ7 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 8:
                floor8 = null;
                floor8 = makeArrayPos(enemies);
                floor8Name = makeArrayName(enemies);
                itemFloor8 = makeArrayPosItems(items);
                itemTypeFloor8 = makeArrayPosItemsNames(items);
                containerFloor8 = makeArrayContainer(container);
                containerItemTypeFloor8 = makeArrayContainerItems(container);
                chestTypeFloor8 = makeArrayContainerChestType(container);
                containerItemNumberFloor8 = makeArrayContainerItemNumbers(container);
                worldObject8 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType8 = makeArrayWorldObjectType(worldObjects);

                interactableType8 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem8 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ8 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 9:
                floor9 = null;
                floor9 = makeArrayPos(enemies);
                floor9Name = makeArrayName(enemies);
                itemFloor9 = makeArrayPosItems(items);
                itemTypeFloor9 = makeArrayPosItemsNames(items);
                containerFloor9 = makeArrayContainer(container);
                containerItemTypeFloor9 = makeArrayContainerItems(container);
                chestTypeFloor9 = makeArrayContainerChestType(container);
                containerItemNumberFloor9 = makeArrayContainerItemNumbers(container);
                worldObject9 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType9 = makeArrayWorldObjectType(worldObjects);

                interactableType9 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem9 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ9 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;

            case 10:
                floor10 = null;
                floor10 = makeArrayPos(enemies);
                floor10Name = makeArrayName(enemies);
                itemFloor10 = makeArrayPosItems(items);
                itemTypeFloor10 = makeArrayPosItemsNames(items);
                containerFloor10 = makeArrayContainer(container);
                containerItemTypeFloor10 = makeArrayContainerItems(container);
                chestTypeFloor10 = makeArrayContainerChestType(container);
                containerItemNumberFloor10 = makeArrayContainerItemNumbers(container);
                worldObject10 = makeArrayWorldObjectValues(worldObjects);
                worldObjectType10 = makeArrayWorldObjectType(worldObjects);

                interactableType10 = makeArrayForInteractableObjects(interactableObjects);
                interactableItem10 = makeArrayForInteractableItemTrader(interactableObjects);
                interactableXYZ10 = makeArrayForInteractableXYBOOL(interactableObjects);
                break;


        }
    }

    public Vector3[] getFloor(int floor){

        switch(floor){
            case 1:
                return getFloor1();
            case 2:
                return getFloor2();
            case 3:
                return getFloor3();
            case 4:
                return getFloor4();
            case 5:
                return getFloor5();
            case 6:
                return getFloor6();
            case 7:
                return getFloor7();
            case 8:
                return getFloor8();
            case 9:
                return getFloor9();
            case 10:
                return getFloor10();

        }
        return null;
    }

    public String[] getFloorName(int floor){
        switch(floor){
            case 1:
                return getFloor1Name();
            case 2:
                return getFloor2Name();
            case 3:
                return  getFloor3Name();
            case 4:
                return  getFloor4Name();
            case 5:
                return getFloor5Name();
            case 6:
                return  getFloor6Name();
            case 7:
                return  getFloor7Name();
            case 8:
                return  getFloor8Name();
            case 9:
                return getFloor9Name();
            case 10:
                return getFloor10Name();
        }
        return null;
    }

    public Vector3[] getFloorItemArray(int floor){
        switch(floor){
            case 1:
                return getItemFloor1();
            case 2:
                System.out.println("called");
                return getItemFloor2();
            case 3:
                return  getItemFloor3();
            case 4:
                return  getItemFloor4();
            case 5:
                return getItemFloor5();
            case 6:
                return  getItemFloor6();
            case 7:
                return  getItemFloor7();
            case 8:
                return  getItemFloor8();
            case 9:
                return getItemFloor9();
            case 10:
                return getItemFloor10();
        }
        return null;
    }
    public Inventory.Items[] getFloorItemArrayName(int floor){
        switch(floor){
            case 1:
                return getItemTypeFloor1();
            case 2:
                return getItemTypeFloor2();
            case 3:
                return  getItemTypeFloor3();
            case 4:
                return getItemTypeFloor4();
            case 5:
                return getItemTypeFloor5();
            case 6:
                return  getItemTypeFloor6();
            case 7:
                return  getItemTypeFloor7();
            case 8:
                return  getItemTypeFloor8();
            case 9:
                return getItemTypeFloor9();
            case 10:
                return getItemTypeFloor10();
        }
        return null;
    }

    public Inventory.Items[][] getFloorContainerItemArrayName(int floor){
        switch(floor){
            case 1:
                return containerItemTypeFloor1;
            case 2:
                return containerItemTypeFloor2;
            case 3:
                return  containerItemTypeFloor3;
            case 4:
                return containerItemTypeFloor4;
            case 5:
                return containerItemTypeFloor5;
            case 6:
                return  containerItemTypeFloor6;
            case 7:
                return  containerItemTypeFloor7;
            case 8:
                return containerItemTypeFloor8;
            case 9:
                return containerItemTypeFloor9;
            case 10:
                return containerItemTypeFloor10;
        }
        return null;
    }
    public int[][] getFloorContainerItemNumberArray(int floor){
        switch(floor){
            case 1:
                return containerItemNumberFloor1;
            case 2:
                return containerItemNumberFloor2;
            case 3:
                return  containerItemNumberFloor3;
            case 4:
                return containerItemNumberFloor4;
            case 5:
                return containerItemNumberFloor5;
            case 6:
                return  containerItemNumberFloor6;
            case 7:
                return  containerItemNumberFloor7;
            case 8:
                return containerItemNumberFloor8;
            case 9:
                return containerItemNumberFloor9;
            case 10:
                return containerItemNumberFloor10;
        }
        return null;
    }

    public Item.itemContainer.ChestType[] getFloorChestTypeArray(int floor){
        switch(floor){
            case 1:
                return chestTypeFloor1;
            case 2:
                return chestTypeFloor2;
            case 3:
                return  chestTypeFloor3;
            case 4:
                return chestTypeFloor4;
            case 5:
                return chestTypeFloor5;
            case 6:
                return  chestTypeFloor6;
            case 7:
                return  chestTypeFloor7;
            case 8:
                return chestTypeFloor8;
            case 9:
                return chestTypeFloor9;
            case 10:
                return chestTypeFloor10;
        }
        return null;
    }

    public Vector2[] getFloorContainerArray(int floor){
        switch(floor){
            case 1:
                return containerFloor1;
            case 2:
                return  containerFloor2;
            case 3:
                return   containerFloor3;
            case 4:
                return  containerFloor4;
            case 5:
                return  containerFloor5;
            case 6:
                return   containerFloor6;
            case 7:
                return   containerFloor7;
            case 8:
                return  containerFloor8;
            case 9:
                return  containerFloor9;
            case 10:
                return  containerFloor10;
        }
        return null;
    }

    public WorldObjects.InteractableObjectType[] getWorldObjectType(int floor){
        switch(floor){
            case 1:
                return worldObjectType1;
            case 2:
                return  worldObjectType2;
            case 3:
                return   worldObjectType3;
            case 4:
                return  worldObjectType4;
            case 5:
                return  worldObjectType5;
            case 6:
                return   worldObjectType6;
            case 7:
                return  worldObjectType7;
            case 8:
                return  worldObjectType8;
            case 9:
                return  worldObjectType9;
            case 10:
                return  worldObjectType10;
        }
        return null;
    }

    public Vector3[] getWorldObjectValues(int floor){
        switch(floor){
            case 1:
                return worldObject1;
            case 2:
                return  worldObject2;
            case 3:
                return   worldObject3;
            case 4:
                return  worldObject4;
            case 5:
                return  worldObject5;
            case 6:
                return   worldObject6;
            case 7:
                return  worldObject7;
            case 8:
                return  worldObject8;
            case 9:
                return  worldObject9;
            case 10:
                return  worldObject10;
        }
        return null;
    }
    public Item.interactableObject.InteractableType[] getInteractableType(int floor){
        switch(floor){
            case 1:
                return interactableType1;
            case 2:
                return  interactableType2;
            case 3:
                return   interactableType3;
            case 4:
                return  interactableType4;
            case 5:
                return  interactableType5;
            case 6:
                return   interactableType6;
            case 7:
                return  interactableType7;
            case 8:
                return  interactableType8;
            case 9:
                return  interactableType9;
            case 10:
                return  interactableType10;
        }
        return null;
    }
    public Inventory.Items[] getInteractableItem(int floor){
        switch(floor){
            case 1:
                return interactableItem1;
            case 2:
                return  interactableItem2;
            case 3:
                return   interactableItem3;
            case 4:
                return  interactableItem4;
            case 5:
                return  interactableItem5;
            case 6:
                return   interactableItem6;
            case 7:
                return  interactableItem7;
            case 8:
                return  interactableItem8;
            case 9:
                return  interactableItem9;
            case 10:
                return  interactableItem10;
        }
        return null;
    }
    public Vector3[] getInteractableXYZ(int floor){
        switch(floor){
            case 1:
                return interactableXYZ1;
            case 2:
                return  interactableXYZ2;
            case 3:
                return   interactableXYZ3;
            case 4:
                return  interactableXYZ4;
            case 5:
                return  interactableXYZ5;
            case 6:
                return   interactableXYZ6;
            case 7:
                return  interactableXYZ7;
            case 8:
                return  interactableXYZ8;
            case 9:
                return  interactableXYZ9;
            case 10:
                return  interactableXYZ10;
        }
        return null;
    }


    public Vector3[] getItemFloor1() {
        return itemFloor1;
    }


    public Vector3[] getItemFloor2() {
        return itemFloor2;
    }


    public Vector3[] getItemFloor3() {
        return itemFloor3;
    }


    public Vector3[] getItemFloor4() {
        return itemFloor4;
    }


    public Vector3[] getItemFloor5() {
        return itemFloor5;
    }

    public Vector3[] getItemFloor6() {
        return itemFloor6;
    }


    public Vector3[] getItemFloor7() {
        return itemFloor7;
    }


    public Vector3[] getItemFloor8() {
        return itemFloor8;
    }


    public Vector3[] getItemFloor9() {
        return itemFloor9;
    }


    public Vector3[] getItemFloor10() {
        return itemFloor10;
    }


    public Inventory.Items[] getItemTypeFloor1() {
        return itemTypeFloor1;
    }


    public Inventory.Items[] getItemTypeFloor2() {
        return itemTypeFloor2;
    }


    public Inventory.Items[] getItemTypeFloor3() {
        return itemTypeFloor3;
    }


    public Inventory.Items[] getItemTypeFloor4() {
        return itemTypeFloor4;
    }


    public Inventory.Items[] getItemTypeFloor5() {
        return itemTypeFloor5;
    }


    public Inventory.Items[] getItemTypeFloor6() {
        return itemTypeFloor6;
    }


    public Inventory.Items[] getItemTypeFloor7() {
        return itemTypeFloor7;
    }


    public Inventory.Items[] getItemTypeFloor8() {
        return itemTypeFloor8;
    }


    public Inventory.Items[] getItemTypeFloor9() {
        return itemTypeFloor9;
    }


    public Inventory.Items[] getItemTypeFloor10() {
        return itemTypeFloor10;
    }

    public Inventory.Items[] getItemTypeFloor11() {
        return itemTypeFloor11;
    }


    public String[] getFloor1Name() {
        return floor1Name;
    }


    public String[] getFloor2Name() {
        return floor2Name;
    }


    public String[] getFloor3Name() {
        return floor3Name;
    }


    public String[] getFloor4Name() {
        return floor4Name;
    }


    public String[] getFloor5Name() {
        return floor5Name;
    }


    public String[] getFloor6Name() {
        return floor6Name;
    }


    public String[] getFloor7Name() {
        return floor7Name;
    }


    public String[] getFloor8Name() {
        return floor8Name;
    }



    public String[] getFloor9Name() {
        return floor9Name;
    }


    public String[] getFloor10Name() {
        return floor10Name;
    }



    public Vector3[] getFloor2() {
        return floor2;
    }

    public Vector3[] getFloor3() {
        return floor3;
    }


    public Vector3[] getFloor4() {
        return floor4;
    }


    public Vector3[] getFloor5() {
        return floor5;
    }


    public Vector3[] getFloor6() {
        return floor6;
    }


    public Vector3[] getFloor7() {
        return floor7;
    }


    public Vector3[] getFloor8() {
        return floor8;
    }


    public Vector3[] getFloor9() {
        return floor9;
    }


    public Vector3[] getFloor10() {
        return floor10;
    }



}
