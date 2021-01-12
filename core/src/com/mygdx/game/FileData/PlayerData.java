package com.mygdx.game.FileData;

import com.mygdx.game.Inventory;
import com.mygdx.game.ItemAttributes;
import com.mygdx.game.Tools.Effects;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;

public class PlayerData {

    public int health;
    public int maxHealth;
    public int maxStamina;
    public boolean gotStarterItems;
    public int gold;
    public int stamina;
    public int criticalChance;
    public int level;
    public int skillPoints;
    public int leveUpThreshHold;
    public int experience;
    public int Strength;
    public int Endurance;
    public int Sorcery;
    public int Luck;
    public Inventory.Items[] hotKeyItems = new Inventory.Items[6];
    public  Inventory.Items[] InventoryItems;
    public int[] stackSize, itemNumber;

    public void setInventoryItems(ArrayList<Inventory.slot> slots){
        InventoryItems = new Inventory.Items[slots.size()];
        stackSize = new int[slots.size()];
        itemNumber = new int[slots.size()];
        for (Inventory.slot a: slots){
            InventoryItems[a.index] = a.item;
            stackSize[a.index] = a.stackNumber;
            itemNumber[a.index] = a.itemNumber;
        }
    }



    public void deployItems(){
        for (int i = 0; i < 19; i++){
            Inventory.slots.get(i).item = InventoryItems[i];
            Inventory.slots.get(i).stackNumber = stackSize[i];
            Inventory.slots.get(i).itemNumber = itemNumber[i];
        }
    }

    public void resetInventory(){
    }

    public void setHotKey(Inventory.Items item, int index){
        hotKeyItems[index] = item;
    }

    //// item Attributes
    public Effects.effectType[] effectType;
    public int[] assignedNumber, damagePoints, upgradeNumber;
    public float[] criticalStrikeMultiplier;
    public void setItemAttributes(){
        effectType = new Effects.effectType[Inventory.itemAttributes.size()];
        assignedNumber = new int[Inventory.itemAttributes.size()];
        damagePoints = new int[Inventory.itemAttributes.size()];
        criticalStrikeMultiplier = new float[Inventory.itemAttributes.size()];
        upgradeNumber = new int[Inventory.itemAttributes.size()];
        for (int i = 0; i < Inventory.itemAttributes.size(); i++){
            effectType[i] = Inventory.itemAttributes.get(i).effectType;
            assignedNumber[i] = Inventory.itemAttributes.get(i).assignedNumber;
            damagePoints[i] = Inventory.itemAttributes.get(i).damagePoints;
            criticalStrikeMultiplier[i] = Inventory.itemAttributes.get(i).criticalStrikeMultiplier;
            upgradeNumber[i] = Inventory.itemAttributes.get(i).upgradeNumber;
        }
    }
    public void deployItemAttributes(){
        for (int i = 0; i < assignedNumber.length; i++ ){
            Inventory.itemAttributes.add(new ItemAttributes(assignedNumber[i]));
            Inventory.itemAttributes.get(i).effectType = effectType[i];
            Inventory.itemAttributes.get(i).damagePoints = damagePoints[i];
            Inventory.itemAttributes.get(i).criticalStrikeMultiplier = criticalStrikeMultiplier[i];
            Inventory.itemAttributes.get(i).upgradeNumber = upgradeNumber[i];
        }
    }

    public int bonusHealth;
    public int bonusStamina;
    public int bonusStrength;
    public int bonusSorcery;
    public int bonusEndurance;
    public int bonusLuck;

    public int healthPotionProficiency;
    public int invisibilityPotionProficiency;
    public int steelPotionProficiency;
    public int stinkyPotionProficiency;
    public int cleaningPotionProficiency;
    public int staminaPotionProficiency;

    public int scrollOfSwiftness;
    public int scrollOfTheDeadKing;
    public int scrollOfRoots;
    public int scrollOfGhastlySteel;
    public int scrollOfTheIronHeart;
    public int scrollOfTheGhostPirate;
    public int scrollOfTheForgottenShield;
    public int scrollOfTheFrozenMalice;
    public int scrollOfFire;
    public int scrollOfRain;
    public int scrollOfBloodRain;
    public int scrollOfVampirism;
    public int scrollOfTheShadows;

    boolean knowledgeOfGuardiansOfTheOverworld;
    boolean knowledgeOfExiledMarksmen;
    boolean knowledgeOfTheFallenVanguard;
    boolean knowledgeOfPaladinsOfTheKeep;


}
