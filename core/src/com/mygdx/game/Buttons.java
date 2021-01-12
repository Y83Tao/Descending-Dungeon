package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Buttons extends Game {

    public static TextButton.TextButtonStyle textButtonStyle;
    public static BitmapFont font, inGameText, buttonFont, inGameText2;
    private Skin skin;
    private TextureAtlas textureAtlas;

    Buttons(){
        create();
    }

    public static TextButton makeButton(float xPos, float yPos, float width, float height, String Text){
        TextButton button = new TextButton(Text, textButtonStyle);
        button.setTransform(true);
        button.setPosition(xPos, yPos);
        button.setScale(width, height);
        return button;
    }

    @Override
    public void create() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = new BitmapFont();
        font = generator.generateFont(parameter);
        inGameText2 = new BitmapFont();
        inGameText2 = generator.generateFont(parameter);
        inGameText2.getData().markupEnabled = true;
        inGameText2.setUseIntegerPositions(false);

        inGameText2.getData().setScale(0.0045f);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("small_pixel.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;

        buttonFont = font;
        buttonFont.getData().setScale(0.5f);

        inGameText = new BitmapFont();
        inGameText = generator.generateFont(parameter);
        inGameText.getData().markupEnabled = true;
        inGameText.setUseIntegerPositions(false);
        inGameText.getData().setScale(0.0065f);



        skin = new Skin();
        textureAtlas = new TextureAtlas(Gdx.files.internal("buttons/Buttons.pack"));
        skin.addRegions(textureAtlas);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = buttonFont;
        textButtonStyle.font.setUseIntegerPositions(true);
        textButtonStyle.up = skin.getDrawable("ButtonNotPressed");
        textButtonStyle.down = skin.getDrawable("ButtonPressed");
        textButtonStyle.over = skin.getDrawable("ButtonPressed");
    }
}
