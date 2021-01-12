package com.mygdx.game.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Buttons;
import com.mygdx.game.PlayingField;

import java.util.ArrayList;

public class ActionDescription {

    public static ArrayList<actionType> actionText = new ArrayList<>();

    public static void render(){

        int maxSize = 5;
        if (actionText.size() <= 5){
            maxSize = actionText.size();
        }

        for (int i = 0; i < maxSize; i++){
            if (actionText.size() > i) {
                if (Gdx.graphics.getWidth() > 1000 && Gdx.graphics.getHeight() > 1000)
                    Buttons.font.getData().setScale(0.2f);
                 else  Buttons.font.getData().setScale(0.3f);
                Buttons.font.setColor(actionText.get(i).getColor().r, actionText.get(i).getColor().g, actionText.get(i).getColor().b, actionText.get(i).getAlpha());
                Buttons.font.draw(PlayingField.Hud, actionText.get(i).getString().toUpperCase(), 20, 40 + (i * 40));
                Buttons.font.setColor(1, 1, 1, 1);
                Buttons.font.getData().setScale(0.5f);
                actionText.get(i).timer();
            }
        }

    }

    public static class actionType{

        Color color;
        String text;
        float timer;

        public actionType(String description, Color color){
            text = description;
            this.color = color;
        }

        String getString(){ return text; }
        Color getColor() {return color;}

        public void timer(){
            timer += Gdx.graphics.getDeltaTime();
            if (timer >= 4){
                actionText.remove(this);
            }
        }

        public float getAlpha(){
            if (timer > 1){
                return 1 - (timer - 1) / 3;
            }
            return 1;
        }

    }



}
