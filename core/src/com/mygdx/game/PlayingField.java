package com.mygdx.game;


import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.HitNumbers;
import com.mygdx.game.Tools.TextOnTop;
import com.mygdx.game.Tools.Tiles;
import com.mygdx.game.Tools.ToolClass;
import com.mygdx.game.UserInterface.ActionDescription;
import com.mygdx.game.UserInterface.Minimap;
import com.crashinvaders.vfx.effects.BloomEffect;

import com.crashinvaders.vfx.VfxManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class PlayingField extends ApplicationAdapter implements Screen {

    public static Game game;
    public static TiledMap tiledMap;
    public static OrthographicCamera camera;
    static TiledMapRenderer tiledMapRenderer;
    static TiledMapTileLayer layer;
    public static SpriteBatch spriteBatch , HudStatic;
    public static Batch Hud;
    static boolean showInventory;
    static boolean newOrLoad;
    public static boolean pause;
    static Player player;
    public static ArrayList<EnemyTypesSpawn> enemies = new ArrayList<>();
    public static ArrayList<TextOnTop> text = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();
    public static ArrayList<Projectiles> projectiles = new ArrayList<>();
    public static ArrayList<Item.propObjects> propObjects = new ArrayList<>();
    public static ArrayList<Item.itemContainer> containers = new ArrayList<>();
    public static ArrayList<HitNumbers> hitNumbers = new ArrayList<>();
    public static ArrayList<Item.interactableObject> interactableObjects = new ArrayList<>();
    public static ArrayList<WorldObjects> worldObjects = new ArrayList<>();
    public static ArrayList<WorldObjects.Door> doors = new ArrayList<>();
    public static boolean reset;

    static Vector2 Spawn = null, Spawn2 = null, Exit = null, Exit2 = null;
    private FloorManager floorManager;

    public static RayHandler rayHandler;
    public static PointLight torch1, torch2;
    public static World world;
    public static ShapeRenderer shapeRenderer;
    public static Minimap miniMap;
    Box2DDebugRenderer box2DDebugRenderer;
    ScreenViewport viewport;
    public static boolean loadedIn, debugHitbox, isRenderingGraphics;

    public static Stage pauseStage, deathStage;
    public static Table table;
    public static TextButton backToMenu, resume, settings, deathBackToMenu;

    private BloomEffect postProcessorEffect;
    private FilmGrainEffect filmGrainEffect;
    private VfxManager vfxManager;
    private FxaaEffect fxaaEffect;

    public static boolean resumedGame;


    private boolean enableFilmGrain, enableBloom;

    PlayingField(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        //postProcessorEffect = new BloomEffect();
        //postProcessorEffect.setBlurPasses(32);
        //postProcessorEffect.setBaseIntensity(1.2f);
        //filmGrainEffect = new FilmGrainEffect();
        //filmGrainEffect.setNoiseAmount(0.2f);
        //filmGrainEffect.setSeed(1);
        //fxaaEffect = new FxaaEffect();
        //fxaaEffect.setReduceMin(0.5f);
        //fxaaEffect.setReduceMul(0.5f);
        //fxaaEffect.setSpanMax(0.5f);

        Player.isBoss = false;
       // vfxManager.addEffect(postProcessorEffect);
       // vfxManager.addEffect(filmGrainEffect);
       // vfxManager.addEffect(fxaaEffect);

        ToolClass.loadMiscTexturesAndValues();
        ToolClass.loadEnemies();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w/16f,h/16f);
        box2DDebugRenderer = new Box2DDebugRenderer();
        camera.update();
        camera.zoom = 0.28f;
              //  0.25000003f; 0.055 speed
              //0.28000003f; 0.07 speed

        spriteBatch = new SpriteBatch();
        Hud = new SpriteBatch();
        HudStatic = new SpriteBatch();

        player = new Player();
        player.create();

        world = new World(new Vector2(0, 0), false);
        rayHandler = new RayHandler(world, 200, 200);

        rayHandler.getLightMapTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(30);
        rayHandler.useDiffuseLight(true);
        //rayHandler.setAmbientLight(0.15f);
        torch1 = new PointLight(rayHandler, 500, new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f ), 15f, 0 , 0);
        torch1.setSoftnessLength(1);
        torch2 = new PointLight(rayHandler, 500, new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5f ), 4, 0 , 0);
        torch2.setSoftnessLength(1);

        floorManager = new FloorManager(newOrLoad);
        miniMap = new Minimap();
        floorChange();
        if (FloorManager.playerData.assignedNumber != null) FloorManager.playerData.deployItemAttributes();
        Inventory.updateAllEquipables();
        // RayHandler.useDiffuseLight(true);

        showInventory = false;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        Pixmap pm = new Pixmap(Gdx.files.internal("Hud/icons/cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
        ToolClass.setUpStagesMenu();


    }

    static Body body;
    static BodyDef bodyDef;
    public static Body bodyCreate(float  x, float y, float width, float height){
        if (world != null) {
            bodyDef = new BodyDef();

            body = world.createBody(bodyDef);
            PolygonShape squareShape = new PolygonShape();
            squareShape.setAsBox(width, height, new Vector2(x, y), 0f);
            body.setUserData("wall");

            FixtureDef fdef = new FixtureDef();
            fdef.shape = squareShape;
            fdef.density = 1.0f;


            return world.createBody(bodyDef).createFixture(fdef).getBody();
        } return null;
    }

    public static void floorChange(){ //OPTIMISE / DISPOSE UNDERLYING OBJECTS THAT HAVENT DISPOSED

        Player.invincible = true;
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;

        if (tiledMap != null) tiledMap.dispose();
        tiledMap = new TmxMapLoader().load(FloorManager.filePath, params); //
        tiledMapRenderer  = new OrthogonalTiledMapRenderer(tiledMap, 1/16f); //
        layer = (TiledMapTileLayer) tiledMap.getLayers().get(0); //
        Tiles.setUpPathing();

        TiledMapTileLayer layerSpawn = (TiledMapTileLayer) tiledMap.getLayers().get(3);

        if (PlayingField.enemies != null) {
            for (int i = 0; i < enemies.size(); i++){
                enemies.get(i).dispose(); } enemies.clear(); } ////////// JAVA MAY BE CRASHING FROM DISPOSING TOO MUCH, FIX IF CRASHES OCCUR WHEN ENTERING NEW FLOOR
        if (PlayingField.items != null) {
          //  for (int i = 0; i < items.size(); i++) {items.get(i).dispose();}
            items.clear();
        }
        if (PlayingField.containers != null) {
          //  for (int i = 0; i < containers.size(); i++){containers.get(i).dispose();}
            containers.clear();
        }
        if (PlayingField.propObjects != null) {
          //  for (int i = 0; i < propObjects.size(); i++){propObjects.get(i).dispose();}
            propObjects.clear();
        }
        if (PlayingField.interactableObjects != null) {
          //  for (int i = 0; i < interactableObjects.size(); i++){interactableObjects.get(i).dispose();}
            interactableObjects.clear();
        }
        if (PlayingField.worldObjects != null) {
            for (int i = 0; i < worldObjects.size(); i++){worldObjects.get(i).dispose();}
            worldObjects.clear();
        }
        if (PlayingField.doors != null){
            doors.clear();
        }

        for (int y = 0; y < layerSpawn.getWidth(); y++) {
            for (int x = 0; x < layerSpawn.getHeight(); x++) {
                if (Tiles.getTileId(x, y, 3) == 220) {Exit2 = new Vector2(x, y);}
                if (Tiles.getTileId(x, y, 3) == 219) {Exit = new Vector2(x, y);}
                if (Tiles.getTileId(x, y, 3) == 235) {Spawn = new Vector2(x, y);}
                if (Tiles.getTileId(x, y, 3) == 236) {Spawn2 = new Vector2(x, y);}
                if (Tiles.getTileId(x, y, 3) == 235) {
                    PlayingField.propObjects.add(new Item.propObjects(x, y, Item.objects.STAIRSUP));
                    PlayingField.propObjects.add(new Item.propObjects(x, y, Item.objects.DUSTEFFECT));
                }
                if (Tiles.getTileId(x, y, 3) == 15) {PlayingField.propObjects.add(new Item.propObjects(x, y, Item.objects.SKULL));}
            }
        }
        System.out.println(Exit2);
        System.out.println(Exit);
        System.out.println(Spawn);
        System.out.println(Spawn2);


        if (FloorManager.generatedFloor){
            Boolean dontSpawnObject = false;
            for (int y = 0; y < layerSpawn.getWidth(); y++) {
                for (int x = 0; x < layerSpawn.getHeight(); x++) {
                    if (Tiles.getTileId(x, y, 3) == 101){
                        ToolClass.spawnEnemiesFloor(x, y, FloorManager.data.getFloorPlayer(), true);
                    } else if (Tiles.getTileId(x, y, 3) == 102){
                        ToolClass.spawnEnemiesFloor(x, y, FloorManager.data.getFloorPlayer(), false);
                    }
                    //Loot pool
                    Random random = new Random();
                    if (Tiles.getTileId(x, y, 3) == 90) {PlayingField.items.add(new Item(x -  1.75f, y + 0.5f, ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)], -1, false));}
                    if (Tiles.getTileId(x, y, 3) == 91) {
                        Inventory.Items newItem = ToolClass.getBlueLootPool()[random.nextInt(ToolClass.getBlueLootPool().length)];
                        if (Inventory.isWeapon(newItem)) {
                            Inventory.itemAttributes.add(new ItemAttributes(Inventory.itemAttributes.size()));
                            PlayingField.items.add(new Item(x - 1.75f, y + 0.5f, newItem , Inventory.itemAttributes.size() - 1, false));
                            ToolClass.applyAttributes(newItem, Inventory.itemAttributes.size() - 1);
                        } else {
                            PlayingField.items.add(new Item(x - 1.75f, y + 0.5f, newItem , -1, false));
                        }
                    }
                    if (Tiles.getTileId(x, y, 3) == 92) {PlayingField.items.add(new Item(x - 1.75f, y + 0.5f, ToolClass.getLegendaryLootPool()[random.nextInt(ToolClass.getLegendaryLootPool().length)], -1, false));}
                    if (Tiles.getTileId(x, y, 3) == 88)
                    {ToolClass.randomizeChest(x, y);  }
                    if (Tiles.getTileId(x, y, 3) == 89) {
                        ToolClass.randomizeChest(x, y);
                    }
                    if (Tiles.getTileId(x, y, 3) == 93) {
                        ToolClass.dropPotion(x, y);
                    }
                    if (Tiles.getTileId(x, y, 3) == 103){
                        PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.ENCHANTMENTCIRCLE, null, false, x, y));
                    }
                    if (Tiles.getTileId(x, y, 3) == 81){
                        PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.ANVIL, null, false, x, y));
                    }
                    if (Tiles.getTileId(x, y, 3) == 82){
                        PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.HOODEDSTATUE, null, false, x, y));
                    } if (Tiles.getTileId(x, y, 3) == 83){
                        PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.TRAPCHEST, null, false, x, y));
                    }if (Tiles.getTileId(x, y, 3) == 68){
                        PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "Spirit", 50));
                    } if (Tiles.getTileId(x, y, 3) == 67){
                        if (random.nextInt(2) == 0){ PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.BIGHPPOTOPN, null, false, x, y));
                        } else { PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.BIGSKILLPOINTPOTION, null, false, x, y));}
                    }


                    if (Tiles.getTileId(x, y, 3) == 84)
                        if (Tiles.getTileId(x, y -1, 3) == 84)PlayingField.containers.add(new Item.itemContainer(x, y, Item.itemContainer.ChestType.COFFIN, Inventory.Items.AXE, Inventory.Items.DAGGERS, Inventory.Items.STAFF, Inventory.Items.EMPTY, -1, -1, -1, -1));

                    if ((Tiles.getTileId(x, y, 3) == 105 || Tiles.getTileId(x, y, 3) == 106) && !dontSpawnObject){

                        Random changeForWorldObject = new Random();

                            if (Tiles.getTileId(x, y, 3) == 105) {
                                if (changeForWorldObject.nextInt(100) < 10) {
                                    switch (changeForWorldObject.nextInt(2)) {
                                        case 0:
                                            PlayingField.worldObjects.add(new WorldObjects(x - 0.5f, y, false, WorldObjects.InteractableObjectType.BARREL));
                                            break;
                                        case 1:
                                            PlayingField.worldObjects.add(new WorldObjects(x - 0.5f, y + 0.25f, false, WorldObjects.InteractableObjectType.CRATE));
                                            break;
                                    }
                                }

                            }else {
                                if (changeForWorldObject.nextInt(100) < 5) {
                                    switch (changeForWorldObject.nextInt(2)) {
                                        case 0:
                                            PlayingField.worldObjects.add(new WorldObjects(x - 0.75f, y, false, WorldObjects.InteractableObjectType.VASE));
                                            break;
                                        case 1:
                                            PlayingField.worldObjects.add(new WorldObjects(x - 0.5f, y, false, WorldObjects.InteractableObjectType.THINVASE));
                                            break;
                                    }
                                    dontSpawnObject = true;
                                }
                            }


                    } else  {dontSpawnObject = false;}


                }
            }
            FloorManager.saveObjectData();
            FloorManager.generatedFloor = false;
            Minimap.resetMiniMap();
        } else{
            System.out.println("LOADED ENEMIES FLOOR " + FloorManager.data.getFloorPlayer());
            FloorManager.loadObjectData();
            FloorManager.playerData.deployItems();
            for (int i = 0; i < FloorManager.floorData.getFloor(FloorManager.data.getFloorPlayer()).length; i++){
                PlayingField.enemies.add(new EnemyTypesSpawn(FloorManager.floorData.getFloor(FloorManager.data.getFloorPlayer())[i].x, FloorManager.floorData.getFloor(FloorManager.data.getFloorPlayer())[i].y, FloorManager.floorData.getFloorName(FloorManager.data.getFloorPlayer())[i], (int) FloorManager.floorData.getFloor(FloorManager.data.getFloorPlayer())[i].z));
            }
            for (int i = 0; i < FloorManager.floorData.getFloorItemArray(FloorManager.data.getFloorPlayer()).length; i++){
              PlayingField.items.add(new Item( FloorManager.floorData.getFloorItemArray(FloorManager.data.getFloorPlayer())[i].x,  FloorManager.floorData.getFloorItemArray(FloorManager.data.getFloorPlayer())[i].y,  FloorManager.floorData.getFloorItemArrayName(FloorManager.data.getFloorPlayer())[i], (int) FloorManager.floorData.getFloorItemArray(FloorManager.data.getFloorPlayer())[i].z, false));
            }
            for (int i = 0; i < FloorManager.floorData.getFloorContainerArray(FloorManager.data.getFloorPlayer()).length; i++){
               PlayingField.containers.add(new Item.itemContainer(FloorManager.floorData.getFloorContainerArray(FloorManager.data.getFloorPlayer())[i].x, FloorManager.floorData.getFloorContainerArray(FloorManager.data.getFloorPlayer())[i].y,    FloorManager.floorData.getFloorChestTypeArray(FloorManager.data.getFloorPlayer())[i], FloorManager.floorData.getFloorContainerItemArrayName(FloorManager.data.getFloorPlayer())[i][0],
                  FloorManager.floorData.getFloorContainerItemArrayName(FloorManager.data.getFloorPlayer())[i][1], FloorManager.floorData.getFloorContainerItemArrayName(FloorManager.data.getFloorPlayer())[i][2], FloorManager.floorData.getFloorContainerItemArrayName(FloorManager.data.getFloorPlayer())[i][3],
                       FloorManager.floorData.getFloorContainerItemNumberArray(FloorManager.data.getFloorPlayer())[i][0],   FloorManager.floorData.getFloorContainerItemNumberArray(FloorManager.data.getFloorPlayer())[i][1],   FloorManager.floorData.getFloorContainerItemNumberArray(FloorManager.data.getFloorPlayer())[i][2],   FloorManager.floorData.getFloorContainerItemNumberArray(FloorManager.data.getFloorPlayer())[i][3]
                ));
            }
            for (int i = 0; i < FloorManager.floorData.getWorldObjectType(FloorManager.data.getFloorPlayer()).length; i++){
                boolean destroyed;
                destroyed = FloorManager.floorData.getWorldObjectValues(FloorManager.data.getFloorPlayer())[i].z == 1;
                PlayingField.worldObjects.add(new WorldObjects(FloorManager.floorData.getWorldObjectValues(FloorManager.data.getFloorPlayer())[i].x, FloorManager.floorData.getWorldObjectValues(FloorManager.data.getFloorPlayer())[i].y
                , destroyed, FloorManager.floorData.getWorldObjectType(FloorManager.data.getFloorPlayer())[i]));
            }
            for (int i = 0; i < FloorManager.floorData.getInteractableType(FloorManager.data.getFloorPlayer()).length; i++){
                Vector3 apple = FloorManager.floorData.getInteractableXYZ(FloorManager.data.getFloorPlayer())[i];
                boolean applez = false;
                if (apple.z == 0) {applez = true;}
                PlayingField.interactableObjects.add(new Item.interactableObject(
                        FloorManager.floorData.getInteractableType(FloorManager.data.getFloorPlayer())[i],
                        FloorManager.floorData.getInteractableItem(FloorManager.data.getFloorPlayer())[i],
                        applez, apple.x, apple.y)
                );

            }


            System.out.println(FloorManager.data.getFloorPlayer() + " + < < < < < ");
            Minimap.obtainMiniMap(FloorManager.data.getFloorPlayer());
        }
        if (world != null) world.dispose();
        if (world != null) world = new World(new Vector2(0, 0), false);

        for (int y = 0; y < layerSpawn.getWidth(); y++) {
            for (int x = 0; x < layerSpawn.getHeight(); x++) {

                if (Tiles.getTileId(x, y, 3) == 104){
                    if (Tiles.getTileId(x + 1, y, 3) != 104 && Tiles.getTileId(x + 1, y, 3) != 164 && Tiles.getTileId(x + 1, y, 3) != 202 ) {
                        PlayingField.doors.add(new WorldObjects.Door(x, y, false, false, false));
                    } else {
                        PlayingField.doors.add(new WorldObjects.Door(x, y, false, true, false));
                    }
                }


                  if (Tiles.getTileId(x, y, 3) == 164 ) {
                      if (Tiles.getTileId(x + 1, y , 3) != 164 && Tiles.getTileId(x + 1, y, 3) != 104 ){
                          bodyCreate(x + 1 , y +0.5f, 0.5f, 0.1f);
                      }
                      if (Tiles.getTileId(x - 1 , y , 3) != 164 && Tiles.getTileId(x - 1, y , 3) != 104 ){
                          bodyCreate(x - 0.25f, y +0.5f, 0.5f, 0.1f);
                      }
                      bodyCreate(x + 0.5f, y +0.5f, 0.5f, 0.1f);

                  }
                  if ( Tiles.getTileId(x, y, 3) == 202 || Tiles.getTileId(x, y, 3) == 186) {
                      if ( Tiles.getTileId(x, y - 1, 0) != -1)
                          bodyCreate(x + 0.5f, y +0.75f, 0.1f, 0.5f);
                      else {
                          bodyCreate(x + 0.5f, y + 0.4f, 0.1f, 0.5f);
                      }
                  }
                if (Tiles.getTileId(x , y, 0) == -1) {
                   if (Tiles.getTileId(x - 1, y, 0) != -1 && (Tiles.getTileId(x, y + 1, 2) != -1) ) {
                       if (Tiles.getTileId(x, y + 1, 0) == -1)
                        bodyCreate(x + 1f, y + 1.2f, 0.6f, 0.6f);
                    }
                    else if (Tiles.getTileId(x + 1, y, 0) != -1 && (Tiles.getTileId(x, y + 1, 2) != -1)) {
                       if (Tiles.getTileId(x, y + 1, 0) == -1)
                        bodyCreate(x, y + 1.2f, 0.6f, 0.6f);
                    } else if (Tiles.getTileId(x, y + 1, 0) != -1  && (Tiles.getTileId(x, y + 1, 2) != -1)) {
                       bodyCreate(x, y , 0.6f, 0.6f);
                   } else if (Tiles.getTileId(x, y - 1, 0) != -1 && (Tiles.getTileId(x, y + 1, 2) != -1)){
                       bodyCreate(x, y + 1.5f, 0.6f, 0.6f);
                    } else if (Tiles.getTileId(x - 1, y - 1, 0) != -1 && Tiles.getTileId(x, y - 1, 0) == -1 && Tiles.getTileId(x + 1, y + 1, 0) == -1)  {
                       bodyCreate(x, y + 1.2f, 0.6f, 0.6f);
                   }else if (Tiles.getTileId(x + 1, y + 1, 0) != -1 && Tiles.getTileId(x, y + 1, 0) == -1 && Tiles.getTileId(x - 1, y - 1, 0) == -1)  {
                       bodyCreate(x, y + 1.2f, 0.6f, 0.6f);
                   }else if (Tiles.getTileId(x + 1, y - 1, 0) != -1 && Tiles.getTileId(x, y - 1, 0) == -1 && Tiles.getTileId(x - 1, y + 1, 0) == -1)  {
                       bodyCreate(x, y + 1.2f, 0.6f, 0.6f);
                   }else if (Tiles.getTileId(x - 1, y + 1, 0) != -1 && Tiles.getTileId(x, y + 1, 0) == -1 && Tiles.getTileId(x + 1, y - 1, 0) == -1)  {
                       bodyCreate(x + 1f, y + 0.75f, 0.6f, 0.6f);
                       bodyCreate(x + 0.25f, y, 0.6f, 0.6f);
                       bodyCreate(x + 1f, y + 1f, 0.6f, 0.6f);
                   }
                    else{
                        if (Tiles.getTileId(x, y + 1, 0) != -1 && Tiles.getTileId(x - 1, y, 0) == -1 && Tiles.getTileId(x + 1, y, 0) == -1) bodyCreate(x, y, 0.6f, 0.6f);

                   }
                }
                /*
                          if ( (Tiles.getTileId(x + 1 , y , 3) == -1 && (  Tiles.getTileId(x +1, y , 1) != -1 || (Tiles.getTileId(x -1, y , 1) != -1 || Tiles.getTileId(x, y -1 , 1) != -1  || Tiles.getTileId(x - 1, y +1 , 1) != -1 || Tiles.getTileId(x - 1, y +2 , 1) != -1
                            || Tiles.getTileId(x + 1, y +1 , 1) != -1 || Tiles.getTileId(x + 1, y +2 , 1) != -1 ) )))
                 */
            }
        }

        if (!FloorManager.data.isSpawnAtEntrance()){
            Player.changePosition( Exit.x,  (Exit.y));
            }
        else {
            Player.changePosition( Spawn.x, (Spawn.y));
        }
        reset = true;
        Player.invincible = false;
        //System.out.println(Player.posX + "  " + Player.posY);

    }

    class renderOrder{
        public int index;
        public float posY;
        public String type;
        renderOrder( float y, String type, int index){
            posY = y;
            this.type = type;
            this.index = index;
        }
    }


    boolean gameFilesWiped;
    @Override
    public void render(float delta) {
        if (ToolClass.assetManager.getProgress() != 1.0){
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
            ToolClass.assetManager.update();
            Hud.begin();
            Buttons.inGameText.getData().setScale(1.0f);
            Buttons.inGameText.draw(PlayingField.Hud, "Loading...", 40, 700);
            Buttons.inGameText.draw(PlayingField.Hud, (int) (ToolClass.assetManager.getProgress() * 100) + " % ", 40, 600);
            Buttons.inGameText.getData().setScale(0.0065f);
            Hud.end();
            ToolClass.assetManager.clear();

        } else if (pause){
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pause = false;
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            pauseStage.act(Gdx.graphics.getDeltaTime());

            pauseStage.getViewport().update(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 2, true);
            pauseStage.draw();

            Gdx.input.setInputProcessor(pauseStage);


        } else if (FloorManager.playerData.health <= 0){
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (!gameFilesWiped){
                gameFilesWiped = true;
                FileHandle fileHandle = Gdx.files.local("Saves/GameData.json");
                if(fileHandle.exists()) { fileHandle.delete(); }
                fileHandle = Gdx.files.local("Saves/PlayerData.json");
                if(fileHandle.exists()) { fileHandle.delete(); }
                fileHandle = Gdx.files.local("Saves/EnemyData.json");
                if(fileHandle.exists()) { fileHandle.delete(); }
                fileHandle = Gdx.files.local("Saves/FloorData.json");
                if(fileHandle.exists()) { fileHandle.delete(); }


                ToolClass.assetManager.clear();
            }


            deathStage.act(Gdx.graphics.getDeltaTime());
            deathStage.getViewport().update(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 2, true);
            deathStage.draw();
            Gdx.input.setInputProcessor(deathStage);

        }
        else {
       // if (ToolClass.assetManager != null) ToolClass.assetManager.dispose();
        Gdx.input.setInputProcessor(null);
        Gdx.gl.glClearColor(25/255f, 25/255f, 25/255f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        camera.update();
        tiledMapRenderer.setView(camera);
          //  vfxManager.cleanUpBuffers();
           // filmGrainEffect.update(Gdx.graphics.getDeltaTime());
          //  vfxManager.beginInputCapture();
        tiledMapRenderer.render(new int[]{0});
        //GROUND VISUALS
            if (Player.effects.size() > 0) {
                for (int i = 0; i < Player.effects.size(); i++){
                    if (Player.effects.get(i).isGroundVisual) {
                        spriteBatch.begin();
                        if (!Player.effects.get(i).render(Player.posX, Player.posY)) Player.effects.remove(i);
                        spriteBatch.end();
                    }
                }
            }

            tiledMapRenderer.render(new int[]{2, 3});

        // for (EnemyTypesSpawn a: enemies){ if(a != null) a.areaToolDisplay(true); }
     //   System.out.println(camera.zoom );

        for (int i = 0; i < interactableObjects.size(); i++){
            interactableObjects.get(i).renderInteractable();
        }

        List<renderOrder> renderOrder = new ArrayList<>();
        renderOrder.add(new renderOrder(Player.posY, "PLAYER", 0));
        for (int i = 0; i < enemies.size(); i++){ renderOrder.add(new renderOrder(enemies.get(i).posY, "ENEMY", i)); }
        for (int i = 0; i < items.size(); i++){ renderOrder.add(new renderOrder(items.get(i).y, "ITEM", i)); }
        for (int i = 0; i < containers.size(); i++){ renderOrder.add(new renderOrder(containers.get(i).y, "CONTAINER", i)); }
        for (int i = 0; i < propObjects.size(); i++){ renderOrder.add(new renderOrder(propObjects.get(i).y, "PROP", i)); }
        for (int i = 0; i < worldObjects.size(); i++){ renderOrder.add(new renderOrder(worldObjects.get(i).posY, "WORLD_OBJECTS", i)); }
        for (int i = 0; i < Player.playerSpells.size(); i++){ renderOrder.add(new renderOrder(Player.playerSpells.get(i).posY, "SPELL", i)); }
        for (int i = 0; i < doors.size(); i++){
            if (doors.get(i).isVertical) {
                renderOrder.add(new renderOrder(doors.get(i).posY, "DOOR", i));
            } else {
                renderOrder.add(new renderOrder(doors.get(i).posY + 0.8f, "DOOR", i));
            }

        }
        renderOrder.sort((z1, z2) -> {
            if (z1.posY < z2.posY)
                return 1;
            if (z1.posY > z2.posY)
                return -1;
            return 0;
        });
        for (Item.propObjects a: propObjects){
            spriteBatch.begin();
            a.renderShadow();
            spriteBatch.end();
        }
        isRenderingGraphics = true;
        if (renderOrder.size() > 0 ) {

            for (int i = 0; i < renderOrder.size(); i++){
                if (reset) {reset = false; break;}
                if (renderOrder.get(i).type.equals("ENEMY")){
                    if (renderOrder.get(i).index < enemies.size()) enemies.get(renderOrder.get(i).index).render();
                }
                if (renderOrder.get(i).type.equals("PLAYER")){
                    spriteBatch.begin();
                    player.render();
                    spriteBatch.end();
                }
                if (renderOrder.get(i).type.equals("ITEM")){
                   if (renderOrder.get(i).index < items.size() ) {
                       items.get(renderOrder.get(i).index).render();
                   }
                }
                if (renderOrder.get(i).type.equals("SPELL")){
                    if (Player.playerSpells.size() > 0 && renderOrder.get(i).index < Player.playerSpells.size() ) {
                        spriteBatch.begin();
                        Player.playerSpells.get(renderOrder.get(i).index).render();
                        spriteBatch.end();
                    }
                }
                if (renderOrder.get(i).type.equals("CONTAINER")){
                        spriteBatch.begin();
                        containers.get(renderOrder.get(i).index).drawStorage();
                        spriteBatch.end();

                }
                if (renderOrder.get(i).type.equals("PROP")){
                    spriteBatch.begin();
                    propObjects.get(renderOrder.get(i).index).renderProps();
                    spriteBatch.end();
                }
                if (renderOrder.get(i).type.equals("WORLD_OBJECTS")){
                    spriteBatch.begin();
                    worldObjects.get(renderOrder.get(i).index).render();
                    spriteBatch.end();
                }
                if (renderOrder.get(i).type.equals("DOOR")){

                        spriteBatch.begin();
                        doors.get(renderOrder.get(i).index).render();
                        spriteBatch.end();

                }
            }
            for (int i = 0; i < projectiles.size(); i++) {
                spriteBatch.begin();
                projectiles.get(i).firing();
                spriteBatch.end();}

           // for (int i = 0; i < projectiles.size(); i++) {projectiles.get(i).outlineRender();}
            } isRenderingGraphics = false;

            if (text.size() > 0){
                for (int i = 0; i < text.size(); i++){
                    text.get(i).queue();
                    if (text.get(i).activate) {
                        spriteBatch.begin();
                        text.get(i).displayText();
                        spriteBatch.end();
                    }
                }
            }
          //  vfxManager.endInputCapture();
           // vfxManager.applyEffects();
          //  vfxManager.renderToScreen();

        renderOrder.clear();
        if (debugHitbox) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            if (Player.insideHitBox) shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin();
            player.drawHitCircle(Player.posX + Player.xS, Player.posY + Player.yS, Player.radiusS);
             System.out.println("CIRCLE X = " + Player.xS + "\nCIRCLE Y = " +Player.yS + "\nRADIUS = "+ Player.radiusS);

            shapeRenderer.rect(Player.hitbox.x, Player.hitbox.y, Player.hitbox.width, Player.hitbox.height);
            for (int i = 0; i < enemies.size(); i++) {
                shapeRenderer.rect(enemies.get(i).hitBox.x, enemies.get(i).hitBox.y, enemies.get(i).hitBox.width, enemies.get(i).hitBox.height);}
            for (int i = 0; i < worldObjects.size(); i++) {
                shapeRenderer.rect(worldObjects.get(i).collisionBox.x, worldObjects.get(i).collisionBox.y, worldObjects.get(i).collisionBox.width, worldObjects.get(i).collisionBox.height);}
            for (int i = 0; i < doors.size(); i++){
                shapeRenderer.rect(doors.get(i).collisionBox.x, doors.get(i).collisionBox.y, doors.get(i).collisionBox.width, doors.get(i).collisionBox.height);
            }
            for (int i = 0; i < projectiles.size(); i++){
                shapeRenderer.circle(projectiles.get(i).posX + 0.5f, projectiles.get(i).posY + 0.5f, 0.25f);
            }
            shapeRenderer.end();
            shapeRenderer.setColor(Color.WHITE);
            Player.insideHitBox = false;
        }



        if (Player.isInvisible){
            torch1.setColor(new Color(Color.BLUE.r + 0.25f,  Color.BLUE.g + 0.25f,  Color.BLUE.b + 0.25f, 1f));
        } else {torch1.setColor(Color.ORANGE);}


        if (world != null) {
            torch1.setActive(true);
            torch2.setActive(true);
            torch1.setPosition(Player.posX + 1, Player.posY);
            torch2.setPosition(Player.posX + 1, Player.posY);

            rayHandler.setCombinedMatrix(camera.combined);
            rayHandler.updateAndRender();
        }

            for (int i = 0; i < hitNumbers.size(); i++){
                hitNumbers.get(i).render();
            }
        for (int i = 0; i < interactableObjects.size(); i++){
            interactableObjects.get(i).showControl();
        }
        // box2DDebugRenderer.render(world, camera.combined);



        HudStatic.setProjectionMatrix(camera.combined);
        HudStatic.begin();
        miniMap.render();
        Player.Hud();
        HudStatic.end();

        spriteBatch.begin();
        for (Item.itemContainer a: containers){
            if (a.open) a.opened();
            a.renderKey();
        }
        for (Item a : items){
            a.renderKey();
        }
        if (showInventory) {player.inventory.showInventory(); Player.inventory.inventoryControls();}
        spriteBatch.end();

        for (ItemAttributes a: Inventory.itemAttributes) {
                if (a.cooldownTimer > 0) {
                    a.cooldownTimer -= Gdx.graphics.getDeltaTime();
                    System.out.println("CD " + a.cooldownTimer);
                    if (a.cooldownTimer < 0) {
                        a.cooldownTimer = 0;
                    }
                }
        }

        miniMap.updateVisibleGrid(layer);
        miniMap.updateMiniMap();
        Hud.begin();
        player.statsAndDisplay();
        ActionDescription.render();
        Hud.end();



        if (Gdx.input.justTouched()){

            Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Vector3 location = Tiles.tileLocation(worldCoordinates, true);
            System.out.println(FloorManager.data.getFloorPlayer() + " < FLOOR");
            System.out.println(FloorManager.data.getFloorsCompleted() + " < FLOORS COMPLETED");
            if (location != null) {
              // System.out.println("X = " + location.x + "   Y = " + location.y);
               // Vector3 locationF = Tiles.tileLocation(location, false);
               // System.out.println(Tiles.getTileId((int) worldCoordinates.x, (int) worldCoordinates.y , 3));
            }
        }
        spriteBatch.setProjectionMatrix(camera.combined);
        loadedIn = true;
    //System.out.println(enemies.size());

}}


    public void deleteGameInstance(){
        if (tiledMap != null) tiledMap.dispose();
        if (PlayingField.enemies != null) {
            for (int i = 0; i < enemies.size(); i++){
                enemies.get(i).dispose(); } enemies.clear(); }
        if (PlayingField.items != null) {
            for (int i = 0; i < items.size(); i++) {items.get(i).dispose();}
            items.clear();
        }
        if (PlayingField.containers != null) {
            for (int i = 0; i < containers.size(); i++){containers.get(i).dispose();}
            containers.clear();
        }
        if (PlayingField.propObjects != null) {
            for (int i = 0; i < propObjects.size(); i++){propObjects.get(i).dispose();}
            propObjects.clear();
        }
        if (PlayingField.interactableObjects != null) {
            for (int i = 0; i < interactableObjects.size(); i++){interactableObjects.get(i).dispose();}
            interactableObjects.clear();
        }
        if (PlayingField.worldObjects != null) {
            for (int i = 0; i < worldObjects.size(); i++){worldObjects.get(i).dispose();}
            worldObjects.clear();
        }
        if (PlayingField.doors != null){
            doors.clear();
        }
        player.dispose();
    }



    @Override
    public void resize(int width, int height) {
            camera.viewportWidth =  Math.round(Gdx.graphics.getWidth() / 16);
            camera.viewportHeight = Math.round(Gdx.graphics.getHeight() / 16);


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
        //rayHandler.dispose();
    }
}
