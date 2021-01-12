package com.mygdx.game;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.Tools.HitNumbers;
import com.mygdx.game.Tools.PlayerSpells;
import com.mygdx.game.Tools.Tiles;
import com.mygdx.game.Tools.ToolClass;

public class Projectiles {

    int damage;
    float posX, posY, velocity, distance, angle, animationTimer;
    float distanceTravelled;
    boolean disposed, isPlayerTheTarget;
    Animation projectileAnimation;
    TextureRegion projectileTextureRegion;
    projectileType type;
    PointLight light;

    public enum projectileType{
        ARROW, FIREBALL, PURPLEFLAMES;
    }

    public Projectiles(projectileType Type, float posX, float posY, float velocity, float distance, float angle, boolean isPlayerTheTarget, boolean isTargeting, float targettingRange, int damage){
        distanceTravelled = 0;
        posX += 1f;
        posY += 0.5f;
        this.type = Type;
        this.damage = damage;
        this.isPlayerTheTarget = isPlayerTheTarget;
        this.posX = posX;
        this.posY = posY;
        this.velocity = velocity;
        this.angle = angle;
        this.distance = distance;
        animationTimer = 0;
       if (Type == projectileType.ARROW)  projectileAnimation = ToolClass.fireball;
        if (Type == projectileType.FIREBALL) {
            projectileAnimation = ToolClass.fireball;
            light = new PointLight(PlayingField.rayHandler, 10, new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f ), 4f, 0 , 0);
            light.setActive(false);
            this.posX -= 0.5f;
        }
        if (Type == projectileType.PURPLEFLAMES) {
            projectileAnimation = ToolClass.staffAA;
            light = new PointLight(PlayingField.rayHandler, 10, new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 1f ), 4f, 0 , 0);
            light.setActive(false);
            this.posX -= 0.5f;
        }

    }

    public void remove(){
        if (type == projectileType.ARROW)
            PlayingField.items.add(new Item((int) posX - 1.5f, (int) posY, Inventory.Items.ARROW, -1, false));
        else if (type == projectileType.FIREBALL) {
            if (!disposed) {
                light.setActive(false);
                light.dispose();
                disposed = true;
            }
            Player.playerSpells.add(new PlayerSpells(PlayerSpells.Spells.explosionEffect, 5, posX -2f, posY - 2f));
        }
        else if (type == projectileType.PURPLEFLAMES) {
            if (!disposed) {
                light.setActive(false);
                light.dispose();
                disposed = true;
            }

            Player.playerSpells.add(new PlayerSpells(PlayerSpells.Spells.purpleExplosionEffect, 5, posX - 2f, posY - 2f));
        }
        PlayingField.projectiles.remove(this);
    }

    public void removeHit(){
        if (type == projectileType.FIREBALL) {
            if (!disposed) {
                light.setActive(false);
                light.dispose();
                disposed = true;
            }

            Player.playerSpells.add(new PlayerSpells(PlayerSpells.Spells.explosionEffect, 5, posX - 2f, posY - 2f));
        }
        if (type == projectileType.PURPLEFLAMES) {
            if (!disposed) {
                light.setActive(false);
                light.dispose();
                disposed = true;
            }

            Player.playerSpells.add(new PlayerSpells(PlayerSpells.Spells.purpleExplosionEffect, 5, posX - 2f, posY - 2f));
        }
        PlayingField.projectiles.remove(this);
    }

    public void firing() {
        int yoffset = 0;
        animationTimer += Gdx.graphics.getDeltaTime();

        if (type == projectileType.ARROW) {
            projectileTextureRegion = projectileAnimation.getKeyFrame(animationTimer, true);

            PlayingField.spriteBatch.setColor(1, 1, 1, 1f);
            PlayingField.spriteBatch.draw(projectileTextureRegion, posX, posY , 1, 1);
            PlayingField.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        if (type == projectileType.FIREBALL) {
            light.setActive(true);
            light.setPosition(posX, posY + 0.5f);
            PlayingField.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            projectileTextureRegion = projectileAnimation.getKeyFrame(animationTimer, true);

            PlayingField.spriteBatch.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 0.75f);
            PlayingField.spriteBatch.draw(projectileTextureRegion, posX, posY , 0.5f, 0.5f, 1, 1, 3, 3, (float) Math.toDegrees(angle));
        }
        if (type == projectileType.PURPLEFLAMES) {
            light.setActive(true);
            light.setPosition(posX, posY + 0.5f);
            PlayingField.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            projectileTextureRegion = projectileAnimation.getKeyFrame(animationTimer, true);

            PlayingField.spriteBatch.setColor(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 0.75f);
            PlayingField.spriteBatch.draw(projectileTextureRegion, posX, posY , 0.5f, 0.5f, 1, 1, 1, 1, (float) Math.toDegrees(angle));
        }


        PlayingField.spriteBatch.setColor(1, 1, 1, 1f);
        PlayingField.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        posX += velocity * Math.cos(angle);
        posY += velocity * Math.sin(angle);
        distanceTravelled += velocity;
        if (distanceTravelled > distance) {
           remove();
        }

        if (Tiles.getTileId((int) posX, posY - 1.5f, 0) != -1) yoffset = 1;

        if (!Tiles.projectileCollisionChecker((int) posX, posY)) {
            remove();
        }
        if (!isPlayerTheTarget) {
            for (EnemyTypesSpawn a : PlayingField.enemies) {
                if (Player.collisionWithHitbox(new Vector2(posX, posY), new Vector2(a.posX + 0.25f, a.posY + 0.5f), 0.01f, (float) 1 / 2)) {
                    int damage = Player.calculateDamage();
                    a.takingDamage(damage, Player.criticalStrikeActive, null);
                    removeHit();
                }
                if (Player.collisionWithHitbox(new Vector2(posX, posY), new Vector2(a.posX + 0.25f, a.posY + 1.25f), 0.01f, (float) 1 / 2)) {
                    int damage = Player.calculateDamage();
                    a.takingDamage(damage, Player.criticalStrikeActive, null);
                    removeHit();
                }
            }
        } else {
            if (Intersector.overlaps(new Circle(posX + 0.5f, posY + 0.5f, 0.25f), Player.hitbox) && !Player.invincible) {
                int damage = 25;
                Player.takeDamage(damage);
                removeHit();
            }
        }
    }

}
