package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.*;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.FileData.FloorManager;

import java.util.ArrayList;
import java.util.Random;

public class ToolClass {

    public static Texture keyE, chestOpened, chestClosed, container, arrow, healEffect, staminaGainEffect, magicBall, stairsUp, skull, shadowSkull,
            coffinOpened, coffinClosed, doorOpenedV, doorClosedV, doorOpenedH, doorClosedH, itemText,
            bleed, stunned, frozen,
            anvil, enchantmentCircle, bigPotion, bigPotionHp, bigPotionSkillPoint, hoodedStatue,

            steelPotIcon, invisbilityPotIcon, stinkyPotIcon, cleaningPotIcon, bagOfRiceIcon, cupOfMilkIcon, ghostSwordIcon, strongHeartIcon, staminaRushIcon, pirateIcon, bloodRainIcon,
            rainIcon, shadowStateIcon, vampirismIcon,

            playerFrame, playerInfoBar, playerInfoBackBar, redHpBar, redHpLostBar, staminaBar, manaBar, floorIcon, bossFightHealthBar, bossFightHealthBackBar
            ;
    public static ArrayList<Integer> unwalkableTileIds = new ArrayList<>();
    public static ArrayList<Float> unwalkableTileGridId = new ArrayList<>();
    public static AssetManager assetManager = new AssetManager();
    public static String vertexShader;
    public static Animation bullet, fireball, explosion, burning, disarm, vulnerable, detected, questionMark, goInvis, stinky, staminaRush, strongAnim, roots, freezeArea,

            ghostSwordsMove, ghostSwordsIdle, ghostSwordsAttack, dustEffect, bloodRain, rain, fireBall, fireballExplosion, staffAA, staffAAExplosion, vampirism,
            lightningChain, lightningHit, bloodSplat;


    public static TextureRegion[] generateTextureRegion(int row, int col, String file){
        Texture texture = new Texture(file);
        assetManager.load(file, Texture.class);
        TextureRegion[][] region = TextureRegion.split(texture, texture.getWidth()/col, texture.getHeight()/row);

        TextureRegion[] textureRegions;
        textureRegions = new TextureRegion[row * col];
        int index = 0;
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                textureRegions[index++] = region[i][j];
            }
        }
        return textureRegions;
    }


    //RUN THIS BEFORE SPAWNING ENEMIES
    //1 - Idle, 2 - Walk, 3 - Attack, 4 - Hit, 5 - Dead, 6+ - miscAnimations UNLESS OVERRIDE
    public static Animation[] skeleton, skeletonShooter, skeletonKnight, dungeonMauler, angelicaMinion, darkGuardian, spirit;
    public static float[][] sizeBase, boss1Size, spiritSize; //sizeW, sizeH, horizontalOffset, yOffset
    public static float angelicaHBYOffset, skeletonsHBYoffset;
    public static float dungeonMaulerHBYOffset;

    public static void loadEnemies(){
        sizeBase = new float[][]{{4.9f, 5f, -1.75f, -1.5f}, {4.9f, 5f,  -1.75f, -1.5f}, {4.9f, 5f,  -1.75f, -1.5f}, {4.9f, 5f, -1.75f, -1.5f}, {4.9f, 5f, -1.75f, -1.5f}};
        //Skeleton Brawler
        TextureRegion[] skeletonIdle = ToolClass.generateTextureRegion( 1, 9, "EnemySprites/skeleton1/SkeletonIdle.png");
        TextureRegion[] skeletonWalk = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/skeleton1/SkeletonWalk.png");
        TextureRegion[] skeletonAttack = ToolClass.generateTextureRegion( 1, 11, "EnemySprites/skeleton1/SkeletonAttack.png");
        TextureRegion[] skeletonHit = ToolClass.generateTextureRegion( 1, 9, "EnemySprites/skeleton1/SkeletonHit.png");
        TextureRegion[] skeletonDead = ToolClass.generateTextureRegion( 1, 20, "EnemySprites/skeleton1/SkeletonDeath.png");
        skeleton = new Animation[]{new Animation(0.1f, skeletonIdle), new Animation(0.1f, skeletonWalk), new Animation(0.1f, skeletonAttack), new Animation(0.1f, skeletonHit),
                new Animation(0.1f, skeletonDead)};
        skeletonsHBYoffset = 0.35f;
        //skeletonShooter
        TextureRegion[] skeletonShooterIdle = ToolClass.generateTextureRegion( 1, 9, "EnemySprites/skeletonSharpShooter/SkeletonShooterIdle.png");
        TextureRegion[] skeletonShooterWalk = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/skeletonSharpShooter/SkeletonShooterWalk.png");
        TextureRegion[] skeletonShooterAttack = ToolClass.generateTextureRegion( 1, 20, "EnemySprites/skeletonSharpShooter/SkeletonShooterAimShoot.png");
        TextureRegion[] skeletonShooterHit = ToolClass.generateTextureRegion( 1, 9, "EnemySprites/skeletonSharpShooter/SkeletonShooterHit.png");
        TextureRegion[] skeletonShooterDead = ToolClass.generateTextureRegion( 1, 22, "EnemySprites/skeletonSharpShooter/SkeletonShooterDeath.png");
        skeletonShooter = new Animation[]{new Animation(0.1f, skeletonShooterIdle), new Animation(0.1f, skeletonShooterWalk), new Animation(0.1f, skeletonShooterAttack), new Animation(0.1f, skeletonShooterHit),
                new Animation(0.1f, skeletonShooterDead)};

        //skeletonKnight
        TextureRegion[] skeletonKnightIdle = ToolClass.generateTextureRegion( 1, 10, "EnemySprites/skeletonKnight/SkeletonHeavyIdle.png");
        TextureRegion[] skeletonKnightWalk = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/skeletonKnight/SkeletonHeavyWalk.png");
        TextureRegion[] skeletonKnightAttack = ToolClass.generateTextureRegion( 1, 38, "EnemySprites/skeletonKnight/SkeletonHeavyAttack.png");
        TextureRegion[] skeletonKnightHit = ToolClass.generateTextureRegion( 1, 8, "EnemySprites/skeletonKnight/SkeletonHeavyHit.png");
        TextureRegion[] skeletonKnightDead = ToolClass.generateTextureRegion( 1, 25, "EnemySprites/skeletonKnight/SkeletonHeavyDeath.png");
        TextureRegion[] skeletonKnightChargeAttack = ToolClass.generateTextureRegion( 1, 22, "EnemySprites/skeletonKnight/SkeletonHeavyChargeAttack.png");
        skeletonKnight = new Animation[]{new Animation(0.1f, skeletonKnightIdle), new Animation(0.1f, skeletonKnightWalk), new Animation(0.1f, skeletonKnightAttack), new Animation(0.1f, skeletonKnightHit),
                new Animation(0.1f, skeletonKnightDead), new Animation(0.1f, skeletonKnightChargeAttack)};

        //DungeonMauler
        TextureRegion[] maulerIdle = ToolClass.generateTextureRegion( 1, 9, "EnemySprites/dungeonMauler/maulerIdle.png");
        TextureRegion[] maulerWalk = ToolClass.generateTextureRegion( 1, 6, "EnemySprites/dungeonMauler/maulerRun.png");
        TextureRegion[] maulerAttack = ToolClass.generateTextureRegion( 1, 18, "EnemySprites/dungeonMauler/maulerAttack.png");
        TextureRegion[] maulerHit = ToolClass.generateTextureRegion( 1, 37, "EnemySprites/dungeonMauler/maulerStagger.png");
        TextureRegion[] maulerDead = ToolClass.generateTextureRegion( 1, 18, "EnemySprites/dungeonMauler/maulerDeath.png");
        TextureRegion[] maulerSlamAttack = ToolClass.generateTextureRegion( 1, 13, "EnemySprites/dungeonMauler/maulerSlamAttack.png");
        TextureRegion[] maulerHitAttack = ToolClass.generateTextureRegion( 1, 19, "EnemySprites/dungeonMauler/maulerHitAttack.png");
        dungeonMauler = new Animation[]{new Animation(0.1f, maulerIdle), new Animation(0.1f, maulerWalk), new Animation(0.1f, maulerAttack), new Animation(0.1f, maulerHit),
                new Animation(0.1f, maulerDead), new Animation(0.1f, maulerSlamAttack), new Animation(0.1f, maulerHitAttack)};
        dungeonMaulerHBYOffset = 0.75f;
        ////////OVERRIDE 1 - Idle, 2 - LevitateMove,  4 - LevitateAttack,  5 - LevitateHit,  6 - LevitateDeath, 7 - LevitateTurretPhase, 8 - LevitateMoveFast, 9 - LevitateAnim
        TextureRegion[] angelicaMinionIdle = ToolClass.generateTextureRegion( 1, 1, "EnemySprites/angelicaMinion/fireGuy.png");
        TextureRegion[] angelicaMinionLevitateTransition = ToolClass.generateTextureRegion( 1, 10, "EnemySprites/angelicaMinion/fireGuyLevitateTransition.png");
        TextureRegion[] angelicaMinionLevitateMove = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/angelicaMinion/fireGuyLevitateMove.png");
        TextureRegion[] angelicaMinionLevitateMoveFast = ToolClass.generateTextureRegion( 1, 16, "EnemySprites/angelicaMinion/fireGuyLevitateMoveFast.png");
        TextureRegion[] angelicaMinionLevitateAttack = ToolClass.generateTextureRegion( 1, 13, "EnemySprites/angelicaMinion/fireGuyLevitateAttack.png");
        TextureRegion[] angelicaMinionLevitateTurretPhase = ToolClass.generateTextureRegion( 1, 34, "EnemySprites/angelicaMinion/fireGuyLevitateTurretAttack.png");
        TextureRegion[] angelicaMinionLevitateHit = ToolClass.generateTextureRegion( 1, 7, "EnemySprites/angelicaMinion/fireGuyLevitateHit.png");
        TextureRegion[] angelicaMinionLevitateDeath = ToolClass.generateTextureRegion( 1, 8, "EnemySprites/angelicaMinion/fireGuyLevitateDeath.png");
        angelicaMinion = new Animation[]{new Animation(0.1f, angelicaMinionLevitateMove), new Animation(0.1f, angelicaMinionLevitateMove),
                new Animation(0.1f, angelicaMinionLevitateAttack), new Animation(0.1f, angelicaMinionLevitateHit) , new Animation(0.075f, angelicaMinionLevitateDeath) ,
                new Animation(0.1f, angelicaMinionLevitateTurretPhase),  new Animation(0.1f, angelicaMinionLevitateMoveFast) ,new Animation(0.1f, angelicaMinionLevitateTransition)};
        angelicaHBYOffset = 0.75f;

        float[] apple = {4.9f, 5f, -2f, -1.5f};
        boss1Size = new float[][]{apple, apple, apple, apple, apple};
        ////////OVERRIDE 1 - Idle, 2 - Walk,  3 - JumpAttack,  4 - Stagger,  5 - Death, 6 - Attack3, 7 - Attack4, 8 - Attack5, 9 - Attack1, 10 -Attack2
        TextureRegion[] DarkGuardianIdle = ToolClass.generateTextureRegion( 1, 1, "EnemySprites/DarkGuardian/DarkGuardian.png");
        TextureRegion[] DarkGuardianWalk = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/DarkGuardian/DarkGuardianWalk.png");
        TextureRegion[] DarkGuardianJumpAttack = ToolClass.generateTextureRegion( 1, 16, "EnemySprites/DarkGuardian/DarkGuardianJumpAttack.png");
        TextureRegion[] DarkGuardianAttack1 = ToolClass.generateTextureRegion( 1, 16, "EnemySprites/DarkGuardian/DarkGuardianAttack1.png");
        TextureRegion[] DarkGuardianAttack2 = ToolClass.generateTextureRegion( 1, 23, "EnemySprites/DarkGuardian/DarkGuardianAttack2.png");
        TextureRegion[] DarkGuardianAttack3 = ToolClass.generateTextureRegion( 1, 16, "EnemySprites/DarkGuardian/DarkGuardianAttack3.png");
        TextureRegion[] DarkGuardianAttack4 = ToolClass.generateTextureRegion( 1, 38, "EnemySprites/DarkGuardian/DarkGuardianAttack4.png");
        TextureRegion[] DarkGuardianAttack5 = ToolClass.generateTextureRegion( 1, 51, "EnemySprites/DarkGuardian/DarkGuardianAttack5.png");
        TextureRegion[] DarkGuardianStagger = ToolClass.generateTextureRegion( 1, 44, "EnemySprites/DarkGuardian/DarkGuardianKnockDown.png");
        TextureRegion[] DarkGuardianDeath = ToolClass.generateTextureRegion( 1, 37, "EnemySprites/DarkGuardian/DarkGuardianDeath.png");
        darkGuardian = new Animation[]{new Animation(0.1f, DarkGuardianIdle), new Animation(0.1f, DarkGuardianWalk),
                new Animation(0.1f, DarkGuardianJumpAttack), new Animation(0.1f, DarkGuardianStagger) , new Animation(0.1f, DarkGuardianDeath) ,
                new Animation(0.1f, DarkGuardianAttack3),  new Animation(0.1f, DarkGuardianAttack4) ,new Animation(0.1f, DarkGuardianAttack5)
                ,new Animation(0.1f, DarkGuardianAttack1) ,new Animation(0.1f, DarkGuardianAttack2)};
        float[] applez = {4.9f, 5f, -2.15f, -1.5f};
        spiritSize = new float[][]{applez, applez, applez, applez, applez};
        TextureRegion[] spiritWalk = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/Spirit/SpiritMove.png");
        TextureRegion[] spiritAttack = ToolClass.generateTextureRegion( 1, 12, "EnemySprites/Spirit/SpiritAttackMove.png");
        TextureRegion[] spiritDeath = ToolClass.generateTextureRegion( 1, 22, "EnemySprites/Spirit/SpiritDeath.png");
        spirit = new Animation[]{new Animation(0.1f, spiritWalk), new Animation(0.1f, spiritWalk),
                new Animation(0.1f, spiritAttack),new Animation(0.1f, spiritWalk) , new Animation(0.1f, spiritDeath)};



    }
    public static Animation barrel;
    public static void loadMiscTexturesAndValues(){
        keyE = new Texture("buttons/keyE.png");
        chestOpened = new Texture("Objects/chestOpened.png");
        chestClosed = new Texture("Objects/chestClosed.png");
        container = new Texture("Hud/informationTab.png");
        container.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        arrow = new Texture("Objects/shootingArrow.png");
        healEffect = new Texture("Effects/healing.png");
        magicBall = new Texture("Objects/MagicBall.png");
        stairsUp = new Texture("Objects/StairsUpwards.png");
        skull = new Texture("Objects/skull.png");
        shadowSkull = new Texture("Objects/shadowSkull.png");
        coffinOpened = new Texture("Objects/coffinOpened.png");
        coffinClosed = new Texture("Objects/coffinClosed.png");
        doorClosedV = new Texture("Objects/doorClosedVertical.png");
        doorOpenedV = new Texture("Objects/doorOpenedVertical.png");
        doorClosedH = new Texture("Objects/doorClosedHorizontal.png");
        doorOpenedH = new Texture("Objects/doorOpenedHorizontal.png");
        itemText = new Texture("Hud/itemText.png");
        playerFrame = new Texture("Hud/PlayerFrame.png");
        playerInfoBar = new Texture("Hud/PlayerInfoBar.png");
        playerInfoBackBar = new Texture("Hud/PlayerInfoBarBack.png");
        redHpBar = new Texture("Hud/redHpBar.png");
        redHpLostBar = new Texture("Hud/redHpbarlost.png");
        staminaBar = new Texture("Hud/staminaBar.png");
        manaBar = new Texture("Hud/manabar.png");
        floorIcon = new Texture("Hud/floorImage.png");
        anvil = new Texture("Objects/anvil.png");
        enchantmentCircle= (new Texture("Objects/enchantmentCircle.png"));
        hoodedStatue= (new Texture("Objects/angelicaWorshipper.png"));
        bigPotion =(new Texture("Objects/bigBottledPotion.png"));
        bigPotionHp =(new Texture("Objects/bigBottledPotionHp.png"));
        bigPotionSkillPoint =(new Texture("Objects/bigBottledPotionSkillPoint.png"));
        bossFightHealthBar =(new Texture("Hud/BossHealthBar.png"));
        bossFightHealthBackBar =(new Texture("Hud/BossHealthBarBack.png"));
        unwalkableTileIds.add(81);
        unwalkableTileIds.add(67);
        unwalkableTileIds.add(82);

        unwalkableTileIds.add(251);
        unwalkableTileIds.add(237);
        unwalkableTileIds.add(253);
        unwalkableTileIds.add(252);

        unwalkableTileIds.add(199);
        unwalkableTileIds.add(84);
        unwalkableTileIds.add(253);
        unwalkableTileIds.add(202);
        unwalkableTileIds.add(164);
        unwalkableTileIds.add(186);

        unwalkableTileGridId.add(0.722f);
        unwalkableTileGridId.add(0.7111f);
        unwalkableTileGridId.add(0.755f);
        unwalkableTileGridId.add(0.41f);

        //SPELLS
        ghostSwordsIdle = new Animation(0.1f, ToolClass.generateTextureRegion( 1, 9, "Effects/ghostSwordIdle.png"));
        ghostSwordsMove = new Animation(0.1f, ToolClass.generateTextureRegion( 1, 1, "Effects/ghostSwordMove.png"));
        ghostSwordsAttack = new Animation(0.1f, ToolClass.generateTextureRegion( 1, 10, "Effects/ghostSwordAttack.png"));

        roots = new Animation(0.1f, ToolClass.generateTextureRegion( 1, 4, "Effects/rootTransition.png"));


        // EFFECTS
        bleed = new Texture("Effects/blood.png");
        stunned = new Texture("Effects/stunned.png");
        steelPotIcon = new Texture("Hud/icons/steelPotionIcon.png");
        invisbilityPotIcon = new Texture("Hud/icons/invisibilityPotionIcon.png");
        ghostSwordIcon = new Texture("Hud/icons/ghostswords.png");
        cleaningPotIcon = new Texture("Hud/icons/cleaningPotionIcon.png");
        stinkyPotIcon = new Texture("Hud/icons/stinkyPotionIcon.png");
        bagOfRiceIcon = new Texture("Hud/icons/bagOfRiceIcon.png");
        cupOfMilkIcon = new Texture("Hud/icons/cupOfMilkIcon.png");
        strongHeartIcon = new Texture("Hud/icons/IronHeartIcon.png");
        staminaRushIcon  = new Texture("Hud/icons/staminaIcon.png");
        rainIcon  = new Texture("Hud/icons/rainIcon.png");
        bloodRainIcon   = new Texture("Hud/icons/bloodRainIcon.png");
        shadowStateIcon  = new Texture("Hud/icons/shadowState.png");
        vampirismIcon   = new Texture("Hud/icons/vampirismIcon.png");

        staminaGainEffect  = new Texture("Effects/staminaRestore.png");
        disarm = new Animation(0.075f, ToolClass.generateTextureRegion(1, 7, "Effects/disarmed.png"));
        vulnerable = new Animation(0.075f, ToolClass.generateTextureRegion(1, 6, "Effects/vulnerable.png"));
        questionMark = new Animation(0.075f, ToolClass.generateTextureRegion(1, 7, "Effects/questionMark.png"));
        detected = new Animation(0.075f, ToolClass.generateTextureRegion(1, 5, "Effects/detected.png"));
        goInvis = new Animation(0.035f, ToolClass.generateTextureRegion(1, 23, "Effects/invisEffect.png"));
        stinky = new Animation(0.035f, ToolClass.generateTextureRegion(1, 40, "Effects/stinkyEffect.png"));
        staminaRush = new Animation(0.035f, ToolClass.generateTextureRegion(1, 45, "Effects/staminaRush.png"));
        strongAnim = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Effects/strongAnim.png"));
        dustEffect = new Animation(0.035f, ToolClass.generateTextureRegion(18, 10, "Effects/dusteffect.png"));
        bloodRain = new Animation(0.02f, ToolClass.generateTextureRegion(12, 20, "Effects/bloodrain.png"));
        rain = new Animation(0.02f, ToolClass.generateTextureRegion(12, 20, "Effects/rain.png"));
        fireball = new Animation(0.02f, ToolClass.generateTextureRegion(1, 30, "Effects/fireball.png"));
        fireballExplosion = new Animation(0.02f, ToolClass.generateTextureRegion(1, 30, "Effects/explosion.png"));
        staffAA = new Animation(0.02f, ToolClass.generateTextureRegion(1, 30, "Effects/staffAA.png"));
        staffAAExplosion = new Animation(0.02f, ToolClass.generateTextureRegion(1, 30, "Effects/staffAAExplosion.png"));
        lightningChain = new Animation(0.02f, ToolClass.generateTextureRegion(1, 15, "Effects/lightningChain.png"));
        lightningHit = new Animation(0.06f, ToolClass.generateTextureRegion(1, 28, "Effects/lightning.png"));
        bloodSplat = new Animation(0.02f, ToolClass.generateTextureRegion(1, 30, "Effects/bloodSplat.png"));
        vampirism = new Animation(0.02f, ToolClass.generateTextureRegion(1, 60, "Effects/vampirism.png"));

        burning = new Animation(0.01f, ToolClass.generateTextureRegion(1, 30, "Effects/burning.png"));
        freezeArea = new Animation(0.01f, ToolClass.generateTextureRegion(1, 38, "Effects/freezeAreaEffect.png"));


        TextureRegion[] barrelTextureRegion = ToolClass.generateTextureRegion(1, 5, "Objects/barrel.png");
        barrel = new Animation(0.1f, barrelTextureRegion);
    }


    static float area(float x1, float y1, float x2, float y2, float x3, float y3)
    {
        return (float) Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }


        static Body body;
        static BodyDef bodyDef;
        public Body bodyCreate(int x, int y){
            bodyDef = new BodyDef();
            bodyDef.position.set(x, y);

            body = PlayingField.world.createBody(bodyDef);
            PolygonShape squareShape = new PolygonShape();
            squareShape.setAsBox(1, 1, new Vector2(x, y), 0f);
            body.setUserData(this);

            FixtureDef fdef = new FixtureDef();
            fdef.shape = squareShape;
            fdef.density = 10.0f;
            fdef.friction =  0.1f;
            fdef.restitution = 1.15f;

            body.createFixture(fdef).setUserData(this);
            squareShape.dispose();

            return PlayingField.world.createBody(bodyDef).createFixture(fdef).getBody();
        }




    public static boolean isInside(float x1, float y1, float x2, float y2, float x3, float y3, float x, float y) {
        float A = area(x1, y1, x2, y2, x3, y3);
        float A1 = area(x, y, x2, y2, x3, y3);
        float A2 = area(x1, y1, x, y, x3, y3);
        float A3 = area(x1, y1, x2, y2, x, y);
        return (A == A1 + A2 + A3);
    }

    public static void setUpStagesMenu(){
        PlayingField.pauseStage = new Stage();

        PlayingField.table = new Table();
        PlayingField.backToMenu = Buttons.makeButton(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, (float) 1, (float) 1, " Exit  ");
        PlayingField.table.add(PlayingField.backToMenu).width(350).height(80).pad(15.5f);; PlayingField.table.row();
        PlayingField.backToMenu.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {


                PlayingField.game.setScreen(new MainMenu(PlayingField.game));
                PlayingField.pause = false;
            }
        });

        PlayingField.table.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2);
        PlayingField.pauseStage.addActor(PlayingField.table);


        PlayingField.deathStage = new Stage();

        PlayingField.table = new Table();
        PlayingField.deathBackToMenu = Buttons.makeButton(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, (float) 1, (float) 1, " Exit  ");
        PlayingField.table.add(PlayingField.deathBackToMenu).width(350).height(80).pad(15.5f); PlayingField.table.row();
        PlayingField.deathBackToMenu.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                PlayingField.game.setScreen(new MainMenu(PlayingField.game));
                PlayingField.pause = false;
            }
        });

        PlayingField.table.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2);
        PlayingField.deathStage.addActor(PlayingField.table);


    }

    public static void loadPlayerAssets(){

        vertexShader = "varying LOWP vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "void main ()\n" +
                "{\n" +
                "   // texture size\n" +
                "    vec2 sz = vec2 (1024.0, 1024.0);\n" +
                "    // scale 8x\n" +
                "    vec3 step = vec3 (1.0 / 8.0, 1.0 / 8.0, 0.0);\n" +
                "    vec2 tex_pixel = sz * v_texCoords - step.xy / 2.0;\n" +
                "\n" +
                "    vec2 corner = floor (tex_pixel) + 1.0;\n" +
                "    vec2 frac = min ((corner - tex_pixel) * vec2 (8.0, 8.0), vec2 (1.0, 1.0));\n" +
                "\n" +
                "    vec4 c1 = texture2D (u_texture, (floor (tex_pixel + step.zz) + 0.5) / sz);\n" +
                "    vec4 c2 = texture2D (u_texture, (floor (tex_pixel + step.xz) + 0.5) / sz);\n" +
                "    vec4 c3 = texture2D (u_te\n" +
                "xture, (floor (tex_pixel + step.zy) + 0.5) / sz);\n" +
                "    vec4 c4 = texture2D (u_texture, (floor (tex_pixel + step.xy) + 0.5) / sz);\n" +
                "\n" +
                "    c1 *=        frac.x  *        frac.y;\n" +
                "    c2 *= (1.0 - frac.x) *        frac.y;\n" +
                "    c3 *=        frac.x  * (1.0 - frac.y);\n" +
                "    c4 *= (1.0 - frac.x) * (1.0 - frac.y);\n" +
                "\n" +
                "    gl_FragColor = (c1 + c2 + c3 + c4);\n" +
                "}";

            // player + (body part) + (direction) + (action)

        Player.headFront = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Head/playerHeadFront.png"));
        Player.bodyFront = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/playerBodyFront.png"));
        Player.bodyFrontMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/playerBodyFrontMove.png"));
        Player.legsFront = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Legs/playerLegsFront.png"));
        Player.legsFrontMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Legs/playerLegsFrontMove.png"));

        Player.headSide = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Head/PlayerHeadSide.png"));
        Player.bodySide = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodySide.png"));
        Player.bodySideMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/PlayerBodySideMove.png"));
        Player.legsSide = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Legs/PlayerLegsSide.png"));
        Player.legsSideMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Legs/PlayerLegsSideMove.png"));

        Player.headBack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Head/PlayerHeadBack.png"));
        Player.bodyBack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyBack.png"));
        Player.bodyBackMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/PlayerBodyBackMove.png"));
        Player.legsBack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Legs/PlayerLegsBack.png"));
        Player.legsBackMove = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Legs/PlayerLegsBackMove.png"));



        //HoldingWeapons
        Player.bodyFrontHoldSword = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyFrontHoldSword.png"));
        Player.bodySideHoldSword = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodySideHoldSword.png"));
        Player.bodyBackHoldSword = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyBackHoldSword.png"));
        Player.bodyFrontHoldAxe = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyFrontHoldAxe.png"));
        Player.bodySideHoldAxe = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodySideHoldAxe.png"));
        Player.bodyBackHoldAxe = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyBackHoldAxe.png"));
        Player.bodyFrontHoldDaggers = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyFrontDaggers.png"));
        Player.bodySideHoldDaggers = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodySideDaggers.png"));
        Player.bodyBackHoldDaggers = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyBackDaggers.png"));
        Player.bodyFrontHoldBow = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyFrontHoldBow.png"));
        Player.bodySideHoldBow = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodySideHoldBow.png"));
        Player.bodyBackHoldBow = new Animation(0.1f, ToolClass.generateTextureRegion(1, 1, "Sprites/Body/PlayerBodyBackHoldBow.png"));
        Player.bodyFrontHoldStaffRun = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/PlayerBodyFrontStaffMove.png"));
        Player.bodySideHoldStaffRun = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/PlayerBodySideStaffMove.png"));
        Player.bodyBackHoldStaffRun = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/Body/PlayerBodyBackStaffMove.png"));


        //SwordAttack
        Player.bodyFrontSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodyFrontSwordAttack.png"));
        Player.bodyFrontSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodyFrontSwordAttack2.png"));
        Player.bodySideSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodySideSwordAttack.png"));
        Player.bodySideSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodySideSwordAttack2.png"));
        Player.bodyBackSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodyBackSwordAttack.png"));
        Player.bodyBackSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerBodyBackSwordAttack2.png"));
        Player.headFrontSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadFrontSwordAttack.png"));
        Player.headFrontSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadFrontSwordAttack2.png"));
        Player.headSideSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadSideSwordAttack.png"));
        Player.headSideSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadSideSwordAttack2.png"));
        Player.headBackSwordAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadBackSwordAttack.png"));
        Player.headBackSwordAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 10, "Sprites/SwordAttack/PlayerHeadBackSwordAttack2.png"));

        Player.bodySideSwordSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 8, "Sprites/SwordAttack/PlayerBodySideSwordSecondary.png"));
        Player.bodySideSwordUltimate = new Animation(0.15f, ToolClass.generateTextureRegion(1, 11, "Sprites/SwordAttack/PlayerBodySideSwordUltimate.png"));

        //AxeAttack
        Player.bodyFrontAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerBodyFrontAxeAttack.png"));
        Player.bodySideAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerBodySideAxeAttack.png"));
        Player.bodyBackAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerBodyBackAxeAttack.png"));
        Player.headFrontAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerHeadFrontAxeAttack.png"));
        Player.headSideAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerHeadSideAxeAttack.png"));
        Player.headBackAxeAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 11, "Sprites/AxeCombat/PlayerHeadBackAxeAttack.png"));

        Player.headAxeSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 14, "Sprites/AxeCombat/PlayerHeadAxeAbility.png"));
        Player.bodyAxeSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 14, "Sprites/AxeCombat/PlayerBodyAxeAbility.png"));
        Player.legsAxeSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 14, "Sprites/AxeCombat/PlayerLegsAxeAbility.png"));

        Player.headAxeUltimate = new Animation(0.1f, ToolClass.generateTextureRegion(1, 15, "Sprites/AxeCombat/PlayerHeadAxeUltimate.png"));
        Player.bodyAxeUltimate = new Animation(0.1f, ToolClass.generateTextureRegion(1, 15, "Sprites/AxeCombat/PlayerBodyAxeUltimate.png"));
        Player.legsAxeUltimate = new Animation(0.1f, ToolClass.generateTextureRegion(1, 15, "Sprites/AxeCombat/PlayerLegsAxeUltimate.png"));

        //DaggersAttack
        Player.bodyFrontDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyFrontDaggerAttack1.png"));
        Player.bodyFrontDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyFrontDaggerAttack2.png"));
        Player.bodySideDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 8, "Sprites/DaggersCombat/PlayerBodySideDaggerAttack1.png"));
        Player.bodySideDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodySideDaggerAttack2.png"));
        Player.bodyBackDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyBackDaggerAttack1.png"));
        Player.bodyBackDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyBackDaggerAttack2.png"));
        Player.headFrontDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadFrontDaggerAttack1.png"));
        Player.headFrontDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadFrontDaggerAttack2.png"));
        Player.headSideDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 8, "Sprites/DaggersCombat/PlayerHeadSideDaggerAttack1.png"));
        Player.headSideDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadSideDaggerAttack2.png"));
        Player.headBackDaggersAttack1 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadBackDaggerAttack1.png"));
        Player.headBackDaggersAttack2 = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadBackDaggerAttack2.png"));

        Player.bodyFrontDaggersSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyFrontDaggerAbility.png"));
        Player.bodySideDaggersSecondary  = new Animation(0.1f, ToolClass.generateTextureRegion(1, 8, "Sprites/DaggersCombat/PlayerBodySideDaggerAbility.png"));
        Player.bodyBackDaggersSecondary  = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerBodyBackDaggerAbility.png"));
        Player.headFrontDaggersSecondary  = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadFrontDaggerAbility.png"));
        Player.headSideDaggersSecondary  = new Animation(0.1f, ToolClass.generateTextureRegion(1, 8, "Sprites/DaggersCombat/PlayerHeadSideDaggerAbility.png"));
        Player.headBackDaggersSecondary  = new Animation(0.1f, ToolClass.generateTextureRegion(1, 7, "Sprites/DaggersCombat/PlayerHeadBackDaggerAbility.png"));

        Player.headDaggersUltimate = new Animation(0.15f, ToolClass.generateTextureRegion(1, 32, "Sprites/DaggersCombat/PlayerHeadDaggerUltimate.png"));
        Player.bodyDaggersUltimate = new Animation(0.15f, ToolClass.generateTextureRegion(1, 32, "Sprites/DaggersCombat/PlayerBodyDaggerUltimate.png"));
        Player.legsDaggersUltimate = new Animation(0.15f, ToolClass.generateTextureRegion(1, 32, "Sprites/DaggersCombat/PlayerLegsDaggerUltimate.png"));


        //BowAttack
        Player.bodyFrontBowAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/BowCombat/PlayerBodyFrontBowShoot.png"));
        Player.bodySideBowAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/BowCombat/PlayerBodySideBowShoot.png"));
        Player.bodyBackBowAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Sprites/BowCombat/PlayerBodyBackBowShoot.png"));

        //StaffAttack
        Player.bodyFrontStaffAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Sprites/StaffCombat/PlayerBodyFrontStaffAttack.png"));
        Player.bodySideStaffAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Sprites/StaffCombat/PlayerBodySideStaffAttack.png"));
        Player.bodyBackStaffAttack = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Sprites/StaffCombat/PlayerBodyBackStaffAttack.png"));
        Player.bodyStaffSecondary = new Animation(0.1f, ToolClass.generateTextureRegion(1, 9, "Sprites/StaffCombat/PlayerBodySideStaffAbility.png"));
        Player.bodyStaffUltimate = new Animation(0.1f, ToolClass.generateTextureRegion(1, 13, "Sprites/StaffCombat/PlayerBodySideStaffUltimate.png"));


        assetManager.load("Hud/HudMain.png", Texture.class);
        Player.HudMain = new Texture ("Hud/HudMain.png");
        Player.HudMain.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Player.swordUltimateIcon = new Texture ("Hud/AbilityIcons/SwordUltimateIcon.png");
        Player.swordSecondaryIcon = new Texture ("Hud/AbilityIcons/SwordSecondaryIcon.png");
        Player.dodgeIcon = new Texture ("Hud/AbilityIcons/HealingItemIcon.png");


        assetManager.finishLoading();
        assetManager.update();
    }


    public static void spawnEnemiesFloor(float x, float y, int floor, boolean isBigMark){
        Random apple = new Random();
        if (isBigMark)
        switch (floor){
            case 1:
                switch (apple.nextInt(2)){
                    case 0: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonBrawler", 100)); break;
                    case 1: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonSharpShooter", 75)); break;
                }
                break;
            case 2:
                switch (apple.nextInt(3)){
                    case 0: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonBrawler", 100)); break;
                    case 2: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150)); break;
                } break;
            case 3:
                switch (apple.nextInt(4)){
                    case 0: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonBrawler", 100)); break;
                    case 2: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150)); break;
                    case 3: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "dungeonMauler", 300));break;
                }break;
            case 4:
                switch (apple.nextInt(5)){
                    case 2: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150)); break;
                    case 3: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "dungeonMauler", 300)); break;
                    case 4: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "angelicaMinion", 100)); break;
                }break;
            case 5:
                PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "DarkGuardian", 1000));
                break;
        }

        else {
            switch (floor){
                case 1:
                    switch (apple.nextInt(2)){
                        case 1: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonSharpShooter", 75)); break;
                    }
                    break;
                case 2:
                    switch (apple.nextInt(3)){
                        case 1: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonSharpShooter", 75)); break;
                    } break;
                case 3:
                    switch (apple.nextInt(4)){
                        case 1: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonSharpShooter", 75)); break;
                        case 2: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150));
                    }break;
                case 4:
                    switch (apple.nextInt(5)){
                        case 0: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonBrawler", 100)); break;
                        case 1: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonSharpShooter", 75)); break;
                        case 2: PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150)); break;
                    }break;
                case 5:
                    PlayingField.enemies.add(new EnemyTypesSpawn(x, y, "skeletonKnight", 150));
                    break;
            }
        }
    }

    public static String getNumeralLevel(int upgradeNumber){

        switch (upgradeNumber){
            case 0: return "I";
            case 1: return "II";
            case 2: return "III";
            case 3: return "IV";
            case 4: return "V";
        }
        return "Null";
    }
    public static String proficiencyOfItem(int current, int required){
            if (current >= required){
                return "\nProficiency " + current + "|" + required +"\n Mastered";
            }
            else return "\nProficiency " + current + "|" + required;
    }


    public static String getText(Inventory.Items item, int attributeNumber){
        switch (item) {
            case EMPTY: break;
            case BASIC_SWORD:
                return
                        "- Basic Sword " +  getNumeralLevel(Inventory.itemAttributes.get(attributeNumber).upgradeNumber) +" -\n" +
                                "Swift Blade For A Knight\n" +
                                "Dmg " + (Inventory.itemAttributes.get(attributeNumber).damagePoints - 5) + "-" + (Inventory.itemAttributes.get(attributeNumber).damagePoints + 5) + "\n" +
                                "Crit * " + Inventory.itemAttributes.get(attributeNumber).criticalStrikeMultiplier + "\n" +
                                "Enchantments " + Inventory.itemAttributes.get(attributeNumber).effectType;
            case BOW: return
                    "- Bow " +  getNumeralLevel(Inventory.itemAttributes.get(attributeNumber).upgradeNumber) +" -\n" +
                            "Elegant Weaponry For \nDistance\n" +
                            "Dmg " + (Inventory.itemAttributes.get(attributeNumber).damagePoints - 5) + "-" + (Inventory.itemAttributes.get(attributeNumber).damagePoints + 5) + "\n" +
                            "Crit * " + Inventory.itemAttributes.get(attributeNumber).criticalStrikeMultiplier + "\n" +
                            "Enchantment " + Inventory.itemAttributes.get(attributeNumber).effectType;
            case ARROW: return "- Arrow -\n" +
                    "Ammunition for the bow";
            case AXE: return
                    "- Axe " +  getNumeralLevel(Inventory.itemAttributes.get(attributeNumber).upgradeNumber) +" -\n" +
                            "Heavy Hit For \nStronger Wielders\n" +
                            "Dmg " + (Inventory.itemAttributes.get(attributeNumber).damagePoints - 5) + "-" + (Inventory.itemAttributes.get(attributeNumber).damagePoints + 5) + "\n" +
                            "Crit * " + Inventory.itemAttributes.get(attributeNumber).criticalStrikeMultiplier + "\n" +
                            "Enchantment " + Inventory.itemAttributes.get(attributeNumber).effectType;
            case DAGGERS: return
                    "- Daggers " +  getNumeralLevel(Inventory.itemAttributes.get(attributeNumber).upgradeNumber) +" -\n" +
                            "Fine Blade For A \nStrategic Attack\n" +
                            "Dmg " + (Inventory.itemAttributes.get(attributeNumber).damagePoints - 5) + "-" + (Inventory.itemAttributes.get(attributeNumber).damagePoints + 5) + "\n" +
                            "Crit * " + Inventory.itemAttributes.get(attributeNumber).criticalStrikeMultiplier + "\n" +
                            "Enchantment " + Inventory.itemAttributes.get(attributeNumber).effectType;
            case STAFF: return
                    "- Staff " +  getNumeralLevel(Inventory.itemAttributes.get(attributeNumber).upgradeNumber) +" -\n" +
                            "A Sorcerer's Stick With \nBright Powers\n" +
                            "Dmg " + (Inventory.itemAttributes.get(attributeNumber).damagePoints - 5) + "-" + (Inventory.itemAttributes.get(attributeNumber).damagePoints + 5) + "\n" +
                            "Crit * " + Inventory.itemAttributes.get(attributeNumber).criticalStrikeMultiplier + "\n" +
                            "Enchantment " + Inventory.itemAttributes.get(attributeNumber).effectType;
            case bone: return "";
            case page: return "";
            case redRing: return "- Red Ring -\n" +
                    "+25 Max Hp\n"+ "+1 Strength";
            case IronRing: return "- Iron Ring -\n" +
                    "+10 Max Stamina\n+1 Luck";
            case LeafyRing: return "- Leafy Ring -\n" +
                    "+1 Sorcery";

            case ringOfDomination: return "- Ring Of Domination -\n" +
                    "+2 Strength\n+100Hp\nHeal 10 points overtime\nafter being hit";
            case ringOfTheBlackArts: return "";
            case sorcererRing: return "- Sorcerer Ring -\n" +
                    "+1 Sorcery";
            case ringOfFate: return "- Ring Of Fate -\n" +
                    "+2 Luck\nGuarantee Items From Mobs.\n+50% gold from gold bags";
            case bookOfNature: return "";
            case ringOfFortitude: return "- Ring Of Fortitude -\n" +
                    "+1 Endurance\nShortened Stamina Wait Timer\n+15 Max Stamina";


            case HEALTH_POTION:
                return
                        "- Health Potion -\n\n" +
                                "Heals player for 50 \npoints"
                                + proficiencyOfItem(FloorManager.playerData.healthPotionProficiency, 10);
            case cleaningPot: return    "- Cleaning Potion -\n" +
                    "You will smell of\nnothing for 5s"
                    + proficiencyOfItem(FloorManager.playerData.cleaningPotionProficiency, 3);
            case staminaPot: return    "- Stamina Potion -\n" +
                    "Restores 40 points\nof Stamina"
                    + proficiencyOfItem(FloorManager.playerData.staminaPotionProficiency, 7);
            case stinkyPot: return    "- Stinky Potion -\n" +
                    "Everyone will smell \nthis foul stench \nfor 15s"
                    + proficiencyOfItem(FloorManager.playerData.stinkyPotionProficiency, 2);
            case cupOfMilk: return    "- Cup Of Milk -\n" +
                    "Refreshing Nourishment.\nHeals you for 75hp\nover 10s.";
            case bagOfRice: return    "- Bag Of Rice -\n" +
                    "Simple meal for the\naverage person. Heals\n108hp over 60s";
            case bagOfGold: return "";
            case steelPot: return    "- Steel Potion -\n" +
                    "Gain 45% damage\nreduction for 5s"
                    + proficiencyOfItem(FloorManager.playerData.steelPotionProficiency, 8);
            case invisiblityPot: return    "- Invisibility Potion -\n" +
                    "Consumer Gains \nInvisibility for 5s"
                    + proficiencyOfItem(FloorManager.playerData.invisibilityPotionProficiency, 5);
            case bookOfDexterity: return "- Book of Dexteriy -\n" +
                    "Reduces stamina needed for\neach attack by half\n";
            case bookOfLightning: return "- Book of lightning -\n" +
                    "Every attack deals 10%\nbonus damage to all\nnearby targets.";
            case bookOfCombo: return "- Book of Combo -\n" +
                    "Consecutive attacks increases \nweapon damage by 10%.\n(Stacks up to 5 times)";
            case bookOfFire: return "- Book of Fire -\n" +
                    "Each attack applies\nBURNING to target.";
            case magnifyingGlass: return "";
            case forgottenPackage: return "";
            case bookOfCourage: return "- Book of Courage -\n" +
                    "Does nothing";

            case bookOfBlood: return "- Book of Blood -\n" +
                    "Heal for 10% of the\ndamage dealth by your\nweapon.";
            case bookOfFrost: return "- Book of frost -\n" +
                    "Every 2nd attack, your \nweapon applies CHILL\nto target.";


            case scrollOfSwiftness: return "- Scroll Of Swiftness -\n" +
                    "Regenerates stamina \nrapidly for 10s"
                    + proficiencyOfItem(FloorManager.playerData.scrollOfSwiftness, 3);
            case scrollOfTheDeadKing: return "";
            case scrollOfRoots: return "- Scroll Of Roots -\n" +
                    "Roots nearby enemies \nfor 5s"
                    + proficiencyOfItem(FloorManager.playerData.scrollOfRoots, 3);
            case scrollOfGhastlySteel: return
                    "- Scroll Of Ghastly Steel -\n" +
                    "Summons a ghost sword \nto fight with you\nfor 15s" + proficiencyOfItem(FloorManager.playerData.scrollOfGhastlySteel, 3);
            case scrollOfTheIronHeart: return "- Scroll Of The Iron Heart -\n" +
                    "Reduces incoming damage \nby 40% for 10s\n"+ proficiencyOfItem(FloorManager.playerData.scrollOfTheIronHeart, 3);
            case scrollOfTheGhostPirate: return "";
            case scrollOfTheForgottenShield: return "";
            case scrollOfTheFrozenMalice: return "- Scroll Of The Frozen Malice -\n" +
                    "Applies CHILL to \nnearby enemies"+ proficiencyOfItem(FloorManager.playerData.scrollOfTheFrozenMalice, 3);
            case scrollOfFire:return "- Scroll Of Fire -\n" +
                    "Shoots a fireball  "+ proficiencyOfItem(FloorManager.playerData.scrollOfFire, 3);
            case scrollOfRain:return "- Scroll Of Rain -\n" +
                    "Applies SOAKED to an \narea for a short duration" + proficiencyOfItem(FloorManager.playerData.scrollOfRain, 3);
            case scrollOfBloodRain:return "- Scroll Of Blood Rain -\n" +
                    "Applies BLEED to an \narea for a short duration"+ proficiencyOfItem(FloorManager.playerData.scrollOfBloodRain, 3);
            case scrollOfVampirism:return "- Scroll Of Vampirism -\n" +
                    "Nearby enemies take 10 \ndamage and heals you for 10"+ proficiencyOfItem(FloorManager.playerData.scrollOfVampirism, 3);
            case scrollOfTheShadows:return "- Scroll Of The Shadows -\n" +
                    "For 5s, killing enemies \nmake you invisible for 1s\nand resets this effect"+ proficiencyOfItem(FloorManager.playerData.scrollOfTheShadows, 3);
        }
        return null;
    }

    public static void lootDrop(Inventory.Items[] itemSelection, float x, float y){
        Random random = new Random();
        Inventory.Items itemRoll = itemSelection[random.nextInt(itemSelection.length)];
        if (itemRoll != Inventory.Items.EMPTY) {
            PlayingField.items.add(new Item(x, y, itemRoll, -1, true));
        }
    }

    public static void dropPotion(int x, int y){
        lootDrop(
                new Inventory.Items[]
                        {      Inventory.Items.staminaPot,
                                Inventory.Items.cleaningPot,
                                Inventory.Items.stinkyPot,
                                Inventory.Items.steelPot,
                                Inventory.Items.invisiblityPot,
                                Inventory.Items.HEALTH_POTION
                        },
                x, y);}


    public static void randomizeChest(int x, int y){
            Random random = new Random();
            int chance = random.nextInt(100);
            if (chance < 10){
                PlayingField.containers.add(
                        new Item.itemContainer(x, y, Item.itemContainer.ChestType.WOODENCHEST,
                                ToolClass.getLegendaryLootPool()[random.nextInt(ToolClass.getLegendaryLootPool().length)],
                                ToolClass.getLegendaryLootPool()[random.nextInt(ToolClass.getLegendaryLootPool().length)],
                                ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)],
                                ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)], -1, -1, -1, -1));

            } else if (chance < 30){
                PlayingField.containers.add(
                        new Item.itemContainer(x, y, Item.itemContainer.ChestType.WOODENCHEST,
                                ToolClass.getBlueLootPool()[random.nextInt(ToolClass.getBlueLootPool().length)],
                                ToolClass.getBlueLootPool()[random.nextInt(ToolClass.getBlueLootPool().length)],
                                ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)],
                                Inventory.Items.EMPTY, -1, -1, -1, -1));
            } else{
                PlayingField.containers.add(
                        new Item.itemContainer(x, y, Item.itemContainer.ChestType.WOODENCHEST,
                                ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)],
                                ToolClass.getBlueLootPool()[random.nextInt(ToolClass.getBlueLootPool().length)],
                                ToolClass.getGreenLootPool()[random.nextInt(ToolClass.getGreenLootPool().length)],
                                Inventory.Items.EMPTY, -1, -1, -1, -1));
            }


    }

    public static void lootPool(String enemyType, float x, float y){
            Random random = new Random();
        switch (enemyType){
            case "skeletonBrawler":

                if (random.nextInt(100) < 30 + (2.5 * (FloorManager.playerData.Luck))) {
                    lootDrop(
                            new Inventory.Items[]
                                    {Inventory.Items.redRing,
                                            Inventory.Items.steelPot, Inventory.Items.steelPot,
                                            Inventory.Items.bagOfRice,
                                            Inventory.Items.scrollOfTheIronHeart,
                                    },
                            x, y);
                }

                break;
            case "skeletonSharpShooter":

                if (random.nextInt(100) < 20 + (2.5 * (FloorManager.playerData.Luck))) {
                lootDrop(
                  new Inventory.Items[]
                        {      Inventory.Items.LeafyRing,
                                Inventory.Items.scrollOfTheShadows, Inventory.Items.staminaPot,
                                Inventory.Items.cupOfMilk,
                                Inventory.Items.scrollOfSwiftness,
                     },
                x, y);}


                break;
            case "skeletonKnight":
                if (random.nextInt(100) < 35 + (2.5 * (FloorManager.playerData.Luck))) {
                lootDrop(
                        new Inventory.Items[]
                                {      Inventory.Items.redRing,
                                        Inventory.Items.bagOfGold, Inventory.Items.stinkyPot,
                                        Inventory.Items.scrollOfSwiftness,
                                        Inventory.Items.scrollOfBloodRain
                                },
                        x, y);}

                break;
            case "dungeonMauler":
                if (random.nextInt(100) < 50 + (2.5 * (FloorManager.playerData.Luck))) {
                lootDrop(
                        new Inventory.Items[]
                                {      Inventory.Items.ringOfFortitude,
                                        Inventory.Items.staminaPot, Inventory.Items.staminaPot,
                                        Inventory.Items.cupOfMilk,
                                        Inventory.Items.scrollOfSwiftness,
                                },
                        x, y);}

                break;
            case "angelicaMinion":
                if (random.nextInt(100) < 40 + (2.5 * (FloorManager.playerData.Luck))) {
                lootDrop(
                        new Inventory.Items[]
                                {      Inventory.Items.sorcererRing,
                                        Inventory.Items.scrollOfFire, Inventory.Items.staminaPot,
                                        Inventory.Items.cupOfMilk,
                                        Inventory.Items.scrollOfSwiftness,
                                },
                        x, y);}

                break;
            case "boss1":
                    lootDrop(
                            new Inventory.Items[]
                                    {      Inventory.Items.ringOfDomination,
                                    },
                            x, y);
                break;
        }


    }

    public static Inventory.Items[] getGreenLootPool(){
        return new Inventory.Items[]
                {Inventory.Items.bagOfRice,
                        Inventory.Items.cupOfMilk,
                        Inventory.Items.stinkyPot,
                        Inventory.Items.scrollOfSwiftness,
                        Inventory.Items.IronRing,
                        Inventory.Items.cleaningPot,
                        Inventory.Items.staminaPot,
                        Inventory.Items.HEALTH_POTION,
                        Inventory.Items.scrollOfFire,
                };
    }
    public static Inventory.Items[] getBlueLootPool(){
        return new Inventory.Items[]
                {Inventory.Items.BASIC_SWORD,
                        Inventory.Items.BOW,
                        Inventory.Items.STAFF,
                        Inventory.Items.redRing,
                        Inventory.Items.sorcererRing,
                        Inventory.Items.LeafyRing,
                        Inventory.Items.scrollOfTheIronHeart,
                        Inventory.Items.scrollOfGhastlySteel,
                        Inventory.Items.scrollOfRoots,
                        Inventory.Items.scrollOfTheFrozenMalice,
                        Inventory.Items.scrollOfTheShadows,
                        Inventory.Items.scrollOfRain,
                        Inventory.Items.scrollOfBloodRain,
                        Inventory.Items.scrollOfVampirism,
                        Inventory.Items.DAGGERS,
                        Inventory.Items.AXE,
                        Inventory.Items.bagOfGold
                };
    }
    public static Inventory.Items[] getLegendaryLootPool(){
        return new Inventory.Items[]
                {Inventory.Items.bookOfCombo,
                        Inventory.Items.bookOfCourage,
                        Inventory.Items.bookOfDexterity,
                        Inventory.Items.bookOfFire,
                        Inventory.Items.bookOfLightning,
                        Inventory.Items.bookOfNature,
                        Inventory.Items.invisiblityPot,

                        Inventory.Items.ringOfFortitude,
                        Inventory.Items.ringOfDomination,
                        Inventory.Items.ringOfFate,
                        Inventory.Items.ringOfTheBlackArts,
                };
    }
    public static void applyAttributes(Inventory.Items item, int itemNumber){
        int damagePoints = 0;
        float criticalStrikeMultipiler = 0;
        switch (item) {
            case AXE:
                damagePoints = 40;
                criticalStrikeMultipiler = 1.50f;
                break;
            case STAFF:
                damagePoints = 20;
                criticalStrikeMultipiler = 1.50f;
                break;
            case DAGGERS:
                damagePoints = 10;
                criticalStrikeMultipiler = 2.75f;
                break;
            case BASIC_SWORD:
                damagePoints = 25;
                criticalStrikeMultipiler = 2.0f;
                break;
            case BOW:
                damagePoints = 30;
                criticalStrikeMultipiler = 1.5f;
                break;
            case HEALTH_POTION:
            case ARROW:
                break;
        }
        switch (item) {
            case AXE:
            case STAFF:
            case DAGGERS:
            case BASIC_SWORD:
            case BOW:
                Inventory.itemAttributes.get(itemNumber).damagePoints = damagePoints;
                Inventory.itemAttributes.get(itemNumber).criticalStrikeMultiplier = criticalStrikeMultipiler;
                break;
            case HEALTH_POTION:
            case ARROW:
                break;
        }
    }


}
