package com.mygdx.game.Enemies;

import com.mygdx.game.Tools.ToolClass;

public class EnemyTypesSpawn extends Enemies {

    int health;

    public EnemyTypesSpawn(float posX, float posY, String enemy, int health){
        super(posX, posY);
        this.health = health;
        switch (enemy){
            case "skeletonBrawler":
                super.name = enemy;
                super.animations = ToolClass.skeleton;
                super.animationSize = ToolClass.sizeBase;
                super.heldExperience = 25;
                super.attackType = "Melee";
                super.currentHP = health;
                super.maxHealth = 100;
                super.staggerDamageIntervals = 25;
                super.actionIntervals = 1f;
                initialSpeed = 0.025f;
                    healthBarYOffset = ToolClass.skeletonsHBYoffset;
                    damageDealing = 15;
                    hitBox.width = 0.8f;
                    hitBox.height = 1.5f;
                break;
            case "skeletonSharpShooter":
                super.name = enemy;
                super.animations = ToolClass.skeletonShooter;
                super.animationSize = ToolClass.sizeBase;
                super.heldExperience = 35;
                super.attackType = "Ranged";
                super.currentHP = health;
                super.maxHealth = 75;
                super.staggerDamageIntervals = 1;
                super.actionIntervals = 1;
                initialSpeed = 0.025f;
                    healthBarYOffset = ToolClass.skeletonsHBYoffset;
                    damageDealing = 15;
                    hitBox.width = 0.8f;
                    hitBox.height = 1.5f;
                break;
            case "skeletonKnight":
                super.name = enemy;
                super.animations = ToolClass.skeletonKnight;
                super.animationSize = ToolClass.sizeBase;
                super.heldExperience = 55;
                super.attackType = "Melee";
                super.currentHP = health;
                super.maxHealth = 150;
                super.staggerDamageIntervals = 50;
                super.actionIntervals = 2;
                initialSpeed = 0.025f;
                    healthBarYOffset = ToolClass.skeletonsHBYoffset;
                    damageDealing = 45;
                    hitBox.width = 0.8f;
                    hitBox.height = 1.5f;
                break;
            case "dungeonMauler":
                super.name = enemy;
                super.animations = ToolClass.dungeonMauler;
                super.animationSize = ToolClass.sizeBase;
                super.heldExperience = 55;
                super.attackType = "Melee";
                super.currentHP = health;
                super.maxHealth = 300;
                super.staggerDamageIntervals = 150;
                super.actionIntervals = 2;
                 speed = 0.05f;
                 initialSpeed = 0.05f;
                 healthBarYOffset = ToolClass.dungeonMaulerHBYOffset;
                 damageDealing = 45;
                    hitBox.width = 1f;
                    hitBox.height = 1.7f;
                break;
            case "angelicaMinion":
                super.name = enemy;
                super.animations = ToolClass.angelicaMinion;
                super.animationSize = ToolClass.sizeBase;
                super.heldExperience = 55;
                super.attackType = "Ranged";
                super.currentHP = health;
                super.maxHealth = 100;
                super.staggerDamageIntervals = 35;
                super.actionIntervals = 1f;
                speed = 0.04f;
                initialSpeed = 0.04f;
                healthBarYOffset = ToolClass.angelicaHBYOffset;
                damageDealing = 10;
                hitBox.width = 0.8f;
                hitBox.height = 1.5f;
                break;
            case "DarkGuardian":
                super.name = enemy;
                super.animations = ToolClass.darkGuardian;
                super.animationSize = ToolClass.boss1Size;
                super.heldExperience = 300;
                super.attackType = "Melee";
                super.currentHP = health;
                super.maxHealth = 1000;
                super.staggerDamageIntervals = 375;
                super.actionIntervals = 1f;
                speed = 0.025f;
                initialSpeed = 0.04f;
                healthBarYOffset = ToolClass.angelicaHBYOffset;
                damageDealing = 40;
                hitBox.width = 0.8f;
                hitBox.height = 1.5f;
                break;
            case "Spirit":
                super.name = enemy;
                super.animations = ToolClass.spirit;
                super.animationSize = ToolClass.spiritSize;
                super.heldExperience = 10;
                super.attackType = "Ranged";
                super.currentHP = health;
                super.maxHealth = 50;
                super.staggerDamageIntervals = 375;
                super.actionIntervals = 2f;
                speed = 0.04f;
                initialSpeed = 0.04f;
                healthBarYOffset = ToolClass.angelicaHBYOffset;
                damageDealing = 40;
                hitBox.width = 0.8f;
                hitBox.height = 2.15f;
                break;
        }
    }












}
