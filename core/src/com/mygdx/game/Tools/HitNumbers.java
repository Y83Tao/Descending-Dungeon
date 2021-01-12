package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Buttons;
import com.mygdx.game.PlayingField;
import com.mygdx.game.UserInterface.ActionDescription;

import java.util.Random;

public class HitNumbers {

    private float posX, posY, posXOffset, posYOffset, xVelocity, yVelocity, opacity, time;
    private int number;
    private boolean isCrit;
    private String overrideString;
    private Color overrideColor;
    public HitNumbers(int number, float posX, float posY, boolean isCrit, String overrideString, Color overrideColor){
        Random roll = new Random();
        this.number = number;
        this.overrideString = overrideString;
        this.posX = posX + roll.nextFloat() + 0.5f;
        this.posY = posY + roll.nextFloat() + 1f;
        this.isCrit = isCrit;
        this.overrideColor = overrideColor;
        xVelocity = 0.01f;
        yVelocity = 0.04f;
        opacity = 1f;
        posXOffset = roll.nextInt(10) / 10;
        posYOffset = roll.nextInt(10) / 10;
        //System.out.println(posXOffset);
        if (isCrit){
            ActionDescription.actionText.add(new ActionDescription.actionType("CRITICALLY HIT FOR " + number + "!", Color.RED));
        }
    }
    public void render(){
        PlayingField.spriteBatch.begin();
        time += Gdx.graphics.getDeltaTime();
        if (time > 0.55) PlayingField.hitNumbers.remove(this);
        xVelocity += 0.0001f;
        yVelocity -= 0.005f;
        if (time > 0.4) opacity -= 0.05f;
        //System.out.println("Y = " +  yVelocity);
        //System.out.println("X = " +  xVelocity);
        opacity -= 0.01f;
        posXOffset += xVelocity;
        posYOffset += yVelocity;
        if (overrideString.length() > 2){
            if (overrideColor != null) {Buttons.inGameText.setColor(overrideColor.r, overrideColor.g, overrideColor.b, opacity);}
            else {Buttons.inGameText.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, opacity);}
            Buttons.inGameText.draw(PlayingField.spriteBatch, overrideString, posX + posXOffset, posY + posYOffset);
        } else if (!isCrit) {
            if (overrideColor != null) {Buttons.inGameText.setColor(overrideColor.r, overrideColor.g, overrideColor.b, opacity);}
            else {Buttons.inGameText.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, opacity);}
            Buttons.inGameText.draw(PlayingField.spriteBatch, number + "", posX + posXOffset, posY + posYOffset);
        }
        else {
            Buttons.inGameText.setColor(Color.RED.r, Color.RED.g, Color.RED.b, opacity);
            Buttons.inGameText.draw(PlayingField.spriteBatch, number + "!", posX + posXOffset, posY + posYOffset);
        }
        Buttons.inGameText.setColor(1, 1, 1, 1);
        PlayingField.spriteBatch.end();
    }

}
