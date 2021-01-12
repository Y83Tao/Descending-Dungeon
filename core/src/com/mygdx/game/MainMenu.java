package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class MainMenu implements Screen {
    private Game game;

    private SpriteBatch spriteBatch;
    private Stage stage;
    private Buttons buttons ;
    private Table table;
    private TextButton Start, Setting, Exit;
    FileHandle fileHandle = Gdx.files.local("Saves/GameData.json");


    public MainMenu(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        spriteBatch = new SpriteBatch();
        buttons = new Buttons();
        table = new Table();
        String text;
        final boolean newOrLoad;
        if (fileHandle.exists()) {text = "Continue"; newOrLoad = false;}
        else {text = "New"; newOrLoad = true;}
        Start = buttons.makeButton(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, (float) 1, (float) 1,  text);
        table.add(Start).width(350).height(80).pad(15.5f);; table.row();
        Start.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                    PlayingField.newOrLoad = newOrLoad;
                    if (!newOrLoad) { PlayingField.resumedGame = true;}
                    game.setScreen(new PlayingField(game));
                    dispose();
            }
        });
        if (fileHandle.exists()){
            Start = buttons.makeButton(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, (float) 1, (float) 1,  "New");
            table.add(Start).width(350).height(80).pad(15.5f);; table.row();
            Start.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y) {
                    PlayingField.newOrLoad = true;
                    game.setScreen(new PlayingField(game));
                    dispose();
                }
            });
        }

        Setting = buttons.makeButton(Gdx.graphics.getWidth() / 2,  (int) ( Gdx.graphics.getHeight() / 2.65), (float)  1, (float)  1, "Settings");
        Setting.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Settings(game));
                dispose();

            }
        });


        table.add(Setting).width(350).height(80).pad(15.5f);
        table.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        stage.getViewport().update(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 2, true);
        spriteBatch.begin();

        Buttons.inGameText.getData().setScale(1f);
        Buttons.inGameText.draw(spriteBatch, "- Descending Dungeon -\nDeveloped By Yun Tao", 50, 700);
        Buttons.inGameText.getData().setScale(0.5f);

        Buttons.inGameText.draw(spriteBatch, "Early Pre-Alpha Build", 50, 600);
        spriteBatch.end();

        Buttons.inGameText.getData().setScale(0.0065f);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        buttons.dispose();
        game.dispose();
    }
}
