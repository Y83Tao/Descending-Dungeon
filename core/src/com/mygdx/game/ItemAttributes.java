package com.mygdx.game;

import com.mygdx.game.Tools.Effects;

public class ItemAttributes {

    public Effects.effectType effectType;
    public Inventory.Items upgradeMaterial;
    public int assignedNumber;
    public int upgradeNumber;
    public int damagePoints;
    public float criticalStrikeMultiplier;
    public float cooldownTimer;


    public ItemAttributes(int assignedNumber){
        this.assignedNumber = assignedNumber;
    }



    public void upgradeItemRequirements(){

    }



}
