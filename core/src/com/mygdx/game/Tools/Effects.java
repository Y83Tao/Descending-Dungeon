package com.mygdx.game.Tools;

import box2dLight.PointLight;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Buttons;
import com.mygdx.game.Player;
import com.mygdx.game.PlayingField;

import java.util.Random;

public class Effects extends ApplicationAdapter {

    public enum effectType{
        FIRE, POISON, ROOTS, HEAL, FLASH, BLEED, DISARM, VULNERABLE, STUNNED, FREEZEAREA, FROZEN, INVISEFFECT,
        STAMINARUSH, STINKY, GAINSTAMINA, STRONGEFFECT, LIGHTNING, bloodSplat, vampirism, frostEnhancement, natureEnhancement, comboEnchantment, dexterityEnchantment, courageEnchantment;
    }
    public effectType type;
    float timeDuration, timer, effectTimer, animationTimer;
    float xOffset, yOffset, opacity, radius;
    boolean isThereTimer;
    public boolean isDebuff, isGroundVisual;
    Sprite effect;
    Animation animatedEffect;
    TextureRegion animationRegion;
    public PointLight pointLight;
    Random random;

    public static void addOverEffectPlayer(effectType type, boolean isThereTimer, float timerDur){
        boolean alreadyExists = false;
        for (int i = 0; i < Player.effects.size(); i++){
            if (Player.effects.get(i).type == type)
            {if (Player.effects.get(i).timeDuration == timerDur) Player.effects.get(i).resetTimer();
            else {Player.effects.get(i).timeDuration = timerDur; Player.effects.get(i).resetTimer();}
                alreadyExists = true;
                break;}
        }
        if (!alreadyExists) {Player.effects.add(new Effects(type, isThereTimer, timerDur, 0));}
    }
    public void resetTimer(){
        timer = 0;
    }


    public Effects(effectType type, boolean isThereTimer, float timeDuration, float radius){
        xOffset = 0;
        yOffset = 0;
        opacity = 1;
        timer = 0;
        effectTimer = 0;
        this.radius = radius;
        this.isThereTimer = isThereTimer;
        this.type = type;
        this.timeDuration = timeDuration;
        switch (type){
            case HEAL:
                effect = new Sprite(ToolClass.healEffect);
                animatedEffect = null;
                pointLight = new PointLight(PlayingField.rayHandler, 30, new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 1f ), 1.5f, 0 , 0);
                break;
            case GAINSTAMINA:
                effect = new Sprite(ToolClass.staminaGainEffect);
                animatedEffect = null;
                pointLight = new PointLight(PlayingField.rayHandler, 30, new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 1f ), 1.5f, 0 , 0);
                break;
            case FLASH:
                animatedEffect = null;
                pointLight = new PointLight(PlayingField.rayHandler, 4, new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 1f ), 2f, 0 , 0);
                break;
            case BLEED:
                animatedEffect = null;
                effect = new Sprite(ToolClass.bleed);
                effect.setScale(0.05f);
                break;
            case VULNERABLE:
                animatedEffect = ToolClass.vulnerable;
                isDebuff = true;
                break;
            case STRONGEFFECT:
                animatedEffect = ToolClass.strongAnim;
                break;
            case DISARM:
                animatedEffect = ToolClass.disarm;
                isDebuff = true;
                break;
            case FIRE:
                animatedEffect = ToolClass.burning;
                random = new Random();
                pointLight = new PointLight(PlayingField.rayHandler, 4, new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f ), 4f, 0 , 0);
                break;
            case INVISEFFECT:
                animatedEffect = ToolClass.goInvis;
                random = new Random();
                break;
            case FREEZEAREA:
                isGroundVisual = true;
                animatedEffect = ToolClass.freezeArea;
                break;
            case ROOTS:
                animatedEffect = ToolClass.roots;
                break;
            case STAMINARUSH:
                animatedEffect = ToolClass.staminaRush;
                random = new Random();
                pointLight = new PointLight(PlayingField.rayHandler, 4, new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 1f ), 4f, 0 , 0);
                break;
            case STINKY:
                animatedEffect = ToolClass.stinky;
                random = new Random();
                pointLight = new PointLight(PlayingField.rayHandler, 4, new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, 1f ), 4f, 0 , 0);
                break;
            case LIGHTNING:
                animatedEffect = ToolClass.lightningHit;
                random = new Random();
                pointLight = new PointLight(PlayingField.rayHandler, 10, Color.CYAN, 5f, 0 , 0);

                break;
            case bloodSplat:
                animatedEffect = ToolClass.bloodSplat;
                break;

            case vampirism:
                isGroundVisual = true;
                animatedEffect = ToolClass.vampirism;
                break;
        }
    }
    float backTimer= 0; boolean backTimerActive;
    public boolean render(float x, float y){
        if (isThereTimer) {
            timer += Gdx.graphics.getDeltaTime();
            //System.out.println(timer);
            if (timer >= timeDuration) {
                if (pointLight != null ) {pointLight.remove();}
                return false;}
        }

        switch (type) {
            case GAINSTAMINA:
            case HEAL:
                yOffset += 0.01f;
                /////////////////////////////////////////////////////////////////LIGHTS MAY BE CAUSING MEMORY OVERFLOW
                if (timer >= timeDuration - 0.2) {
                    opacity -= 0.05f;
                }
                effect.setPosition(x - 31, y - 15.25f + yOffset);
                effect.setScale(0.05f);
                effect.setColor(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, opacity);
                effect.draw(PlayingField.spriteBatch);
                pointLight.setPosition(x + 1, y + 0.5f);
                pointLight.setColor(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, opacity);
                return true;
            case FLASH:
                yOffset += 0.01f;
                pointLight.setPosition(x + 1, y + 0.5f);
                return true;
            case BLEED:
                yOffset += Gdx.graphics.getDeltaTime();
                xOffset += Gdx.graphics.getDeltaTime();

                effect.draw(PlayingField.spriteBatch);
                effect.setPosition(x + (float) Math.sin(xOffset) - 7.5f, y + (float) Math.cos(yOffset) - 7.5f);
                return true;
            case DISARM:
            case VULNERABLE:
                animationTimer += Gdx.graphics.getDeltaTime();
                animationRegion = animatedEffect.getKeyFrame(animationTimer, false);
                PlayingField.spriteBatch.draw(animationRegion, x + 0.5f, y + 0.5f, 1, 1);
                return true;
            case FIRE:
                animationRegion = animatedEffect.getKeyFrame(timer, true);
                PlayingField.spriteBatch.draw(animationRegion, x - 2f, y -1, 5, 5);
                pointLight.setPosition(x + 1, y + 0.5f);
                pointLight.setDistance(7 + random.nextFloat());
                return true;
            case INVISEFFECT:
                animationRegion = animatedEffect.getKeyFrame(timer, false);
                PlayingField.spriteBatch.draw(animationRegion, x - 1.5f, y -2.5f, 5, 5);
                if (timer >= animatedEffect.getAnimationDuration()) {
                    if (pointLight != null ) {pointLight.remove();}
                    return false;}
                return true;
            case FREEZEAREA:
                animationRegion = animatedEffect.getKeyFrame(timer, false);
                PlayingField.spriteBatch.draw(animationRegion, x - (radius / 2) + 0.9f, y - (radius / 2) + 0.25f, radius, radius);
                if (timer >= animatedEffect.getAnimationDuration()) {
                    if (pointLight != null ) {pointLight.remove();}
                    return false;}
                return true;
            case vampirism:
                animationRegion = animatedEffect.getKeyFrame(timer, true);
                PlayingField.spriteBatch.draw(animationRegion, Player.posX - (radius / 1.15f), Player.posY - (radius / 1.15f), radius * 2, radius * 2);
                return true;
            case STRONGEFFECT:
                animationRegion = animatedEffect.getKeyFrame(timer, false);
                PlayingField.spriteBatch.draw(animationRegion, x - 1.45f, y - yOffset - 1.5f, 0, 0, 5f, 4.5f, 1, 1.05f, 0);
                if (timer >= animatedEffect.getAnimationDuration()) {
                    return false;}
                return true;
            case STAMINARUSH:
                animationRegion = animatedEffect.getKeyFrame(timer, true);
                PlayingField.spriteBatch.draw(animationRegion, x - 1.6f, y -2.25f, 5, 5);
                return true;
            case STINKY:
                animationRegion = animatedEffect.getKeyFrame(timer, true);
                PlayingField.spriteBatch.draw(animationRegion, x - 1.3f, y -2.5f, 5, 5);
                return true;
            case ROOTS:
                if (timer + animatedEffect.getAnimationDuration() >= timeDuration && !backTimerActive){
                    backTimerActive = true;
                    backTimer = animatedEffect.getAnimationDuration();
                } else if (backTimerActive) {
                    backTimer -= Gdx.graphics.getDeltaTime();
                    animationRegion = animatedEffect.getKeyFrame(backTimer, false);
                }
                else {
                animationRegion = animatedEffect.getKeyFrame(timer, false);}
                PlayingField.spriteBatch.draw(animationRegion, x - 2f, y - 1f, 5, 5);
                return true;
            case LIGHTNING:{
                animationRegion = animatedEffect.getKeyFrame(timer, false);
                pointLight.setPosition(x +0.25f , y + 0.25f);
                pointLight.setColor(new Color(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 1 - timer * 3));
                PlayingField.spriteBatch.draw(animationRegion, x - 1.5f, y + 2.9f, 0, 0, 4, 4, 1, 1, 270);
                if (timer > 0.3f) {
                    if (pointLight != null ) {pointLight.remove();}
                    return false;}
                return true;
            }
            case bloodSplat:
                animationRegion = animatedEffect.getKeyFrame(timer, false);
                PlayingField.spriteBatch.draw(animationRegion, x - 2f, y - 1.5f, 5, 5);
                if (timer > animatedEffect.getAnimationDuration()) {
                    return false;}
                return true;


        }
        return true;
    }

    public void resetDuration(){
        timer = 0;
    }

    //x is healthChange, y is
    public Vector3 applyEffects(float x, float y){
        effectTimer += 1;
        switch (type) {
            case HEAL:
                break;
            case FLASH:
                break;
            case FIRE:
                if (effectTimer % 50 == 0){

                    return new Vector3(5, 0, 0);
                }
                break;
            case BLEED:
                if (effectTimer % 50 == 0){
                    return new Vector3(1, 0, 0);

                }
                break;
        }
        return new Vector3(0, 0, 0);
    }
    public Color colorType(){
        switch (type) {
            case FIRE:
                  return Color.YELLOW;

            case BLEED:
                    return Color.RED;
        }

        return null;
    }

    @Override
    public void dispose() {

    }


}
