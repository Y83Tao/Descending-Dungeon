package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.TextOnTop;

public class TestingField extends ApplicationAdapter {

    Sprite itemSprite;
    SpriteBatch sprite;

    public static TiledMap tiledMap;
    static OrthographicCamera camera;
    static TiledMapRenderer tiledMapRenderer;
    static TiledMapTileLayer layer;
    @Override
    public void create(){
        itemSprite = new Sprite(new Texture("Sprites/swordSprites/PlayerIdleClaymore.png"));
        itemSprite.setPosition(0, 0);
        itemSprite.setScale(3);
        sprite = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.zoom = 0.2f;
        camera.setToOrtho(false,1280f,768f);
        tiledMap = new TmxMapLoader().load("Map/Floor1.tmx"); //
        tiledMapRenderer  = new OrthogonalTiledMapRenderer(tiledMap); //
        layer = (TiledMapTileLayer) tiledMap.getLayers().get(0); //
    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        sprite.begin();
        //itemSprite.setScale(2);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(new int[]{0, 2, 3});

        itemSprite.draw(sprite);
        sprite.end();
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.translate(0, 2f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){

            camera.translate(2f, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){

            camera.translate(0, -2f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){

            camera.translate(-2f, 0);
        }

    }
}
