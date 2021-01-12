package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Buttons;
import com.mygdx.game.Player;
import com.mygdx.game.PlayingField;

public class TextOnTop {

    Color color;
    String text;
    float hoverY, opacity;
    public boolean activate;

    public TextOnTop(Color color, String text){
        hoverY = 0;
        opacity = 1;
        this.color = color;
        this.text = text;
    }

    public void displayText(){

        if (hoverY < 0.6f) {hoverY += 0.01f;}
        else {
            PlayingField.text.remove(this);
        }
        if (hoverY > 0.5f) {opacity -= 0.2f;}
        Buttons.inGameText.setColor(color.r, color.g, color.b, opacity);
        Buttons.inGameText.getData().setScale(0.005f);
        Buttons.inGameText.draw(PlayingField.spriteBatch, text, Player.posX + 0.35f, Player.posY + 1.75f + hoverY);
        Buttons.inGameText.setColor(255, 255, 255, 1);
        Buttons.inGameText.getData().setScale(0.0065f);

    }

    public void queue() {
        if (!activate) {
            if (PlayingField.text.size() == 1) {
                activate = true;
            } else {

                for (int i = 0; i < PlayingField.text.size(); i++) {
                    if (PlayingField.text.get(i).hoverY != 0) {
                        if (PlayingField.text.get(i).hoverY < 0.3) {
                            activate = false;
                            break;
                        } else {
                            activate = true;
                        }
                    } else {
                        activate = true;
                    }

                }

            }


        }
    }

}
