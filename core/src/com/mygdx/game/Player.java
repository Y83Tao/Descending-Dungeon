package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.mygdx.game.Enemies.Enemies;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.*;
import com.mygdx.game.UserInterface.ActionDescription;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.PlayingField.camera;

public class Player extends ApplicationAdapter implements InputProcessor {


    public static Animation

            //Main
    mainBodyFrontMove, mainBodySideMove, mainBodyBackMove,

            //Base Animation
    bodyFront, bodySide, bodyBack,
    bodyFrontMove, bodySideMove, bodyBackMove,

    headFront, headSide, headBack,

    legsFront, legsSide, legsBack,
    legsFrontMove, legsSideMove, legsBackMove,

        //Sword Animations
    bodyFrontHoldSword, bodySideHoldSword, bodyBackHoldSword,
    bodyFrontSwordAttack1, bodyFrontSwordAttack2, bodySideSwordAttack1, bodySideSwordAttack2, bodyBackSwordAttack1, bodyBackSwordAttack2,
    headFrontSwordAttack1, headFrontSwordAttack2, headSideSwordAttack1, headSideSwordAttack2, headBackSwordAttack1, headBackSwordAttack2,

    bodySideSwordSecondary, bodySideSwordUltimate,

       //Axe Animations
    bodyFrontHoldAxe, bodySideHoldAxe, bodyBackHoldAxe,
    bodyFrontAxeAttack, bodySideAxeAttack, bodyBackAxeAttack,
    headFrontAxeAttack, headSideAxeAttack, headBackAxeAttack,

    bodyAxeSecondary, bodyAxeUltimate,
    headAxeSecondary, headAxeUltimate,
    legsAxeSecondary, legsAxeUltimate,

    //Daggers Animations
    bodyFrontHoldDaggers, bodySideHoldDaggers, bodyBackHoldDaggers,
    bodyFrontDaggersAttack1, bodyFrontDaggersAttack2, bodySideDaggersAttack1, bodySideDaggersAttack2, bodyBackDaggersAttack1, bodyBackDaggersAttack2,
    headFrontDaggersAttack1, headFrontDaggersAttack2, headSideDaggersAttack1, headSideDaggersAttack2, headBackDaggersAttack1, headBackDaggersAttack2,

    bodyFrontDaggersSecondary,  bodySideDaggersSecondary,  bodyBackDaggersSecondary,
    headFrontDaggersSecondary,  headSideDaggersSecondary,  headBackDaggersSecondary,

    headDaggersUltimate, bodyDaggersUltimate, legsDaggersUltimate,

    //Bow Animations
    bodyFrontHoldBow, bodySideHoldBow, bodyBackHoldBow,
    bodyFrontBowAttack, bodySideBowAttack, bodyBackBowAttack,

    //Staff Animations
    bodyFrontHoldStaffRun, bodySideHoldStaffRun, bodyBackHoldStaffRun,
    bodyFrontStaffAttack, bodySideStaffAttack, bodyBackStaffAttack,
    bodyStaffSecondary, bodyStaffUltimate
    ;
    public static Texture HudMain,

    swordUltimateIcon, swordSecondaryIcon, dodgeIcon


            ;

    public static Animation bowAimCharge;
    public static Sprite bowAim, staffAim;

    private TextureRegion player;
    private TextureRegion bowAimTextureRegion;
    private static TextureRegion Head;
    private TextureRegion Body;
    private TextureRegion Legs;
    private static float stateTime, attackTime, dodgeTime, aimTime, aimAnimationTime, secondaryCoolDown, ultimateCoolDown, dodgeCoolDown;
    private boolean rotation, lockDirection, isMoving;
    private boolean animationCancel, isUsingMouseToDirectHit;
    private boolean damage;
    private boolean aiming;
    public static boolean criticalStrikeActive;
    public static boolean staffAiming;
    public static float posX, posY ;
    public static float staminaRegenTimer, staminaRegen, dodgeStamina, attackStamina, secondaryStamina, hitInvincibleTimer;
    public static float speed, objectPushSpeed, yChange, xChange;
    public static float initialSpeed = 0.07f;
    float aimX = 0, aimY = 0, aimXdiff = 0, aimYdiff = 0;
    static int health, stamina, attackState, attackFrame, attackFrameLength, damagePoints,
            MaxDamage, MinDamage, criticalChance, criticalMultiplier, experience;
    String action, directionString, attackDirection;
    static Random dmgRoll;
    static boolean invincible;
    static boolean inControl;
    public static boolean isInvisible;
    static boolean isDashing, DeveloperMode;
    static boolean canUseHotKeys = true;
    public static Inventory inventory;

    public static ArrayList<Effects> effects = new ArrayList<>();
    public static ArrayList<PlayerSpells> playerSpells = new ArrayList<>();
    public static ArrayList<activeStatuses> activeStatusIcons = new ArrayList<>();
    public static Rectangle hitbox;
    public static boolean insideHitBox;


    static public float xS, yS, radiusS;

    @Override
    public void create() {
        invincible = false;
        inventory = new Inventory();

        ToolClass.loadPlayerAssets();

        staffAim = new Sprite(new Texture("Objects/staffOutline.png"));
        staffAim.flip(true, false);
        bowAimCharge = new Animation(0.1f, ToolClass.generateTextureRegion(1, 6, "Objects/bowOutlineCharge.png"));


        mainBodySideMove = bodySideMove;
        mainBodyFrontMove = bodyFrontMove;
        mainBodyBackMove = bodyBackMove;

        speed =  0.07f ;

        attackFrame = 3;
        directionString = "RIGHT";
        attackFrameLength = 5;
        stateTime = 0f;
        animationCancel = true;
        attackState = 0;
        action = "Idle";
        staminaRegen = 2;
        dodgeStamina = 10;
        dmgRoll = new Random();
        MaxDamage = 20;
        MinDamage = 10;
        inControl = true;
        criticalMultiplier = 2;
        hitbox = new Rectangle();

        xS = 0; yS = 0; radiusS = 0.5f;
    }

    public void drawHitCircle(float x, float y, float radius){
        PlayingField.shapeRenderer.circle(x, y, radius, 10);

    }


    void controls(OrthographicCamera camera){
       // speed = 0.045f * (100 * Gdx.graphics.getDeltaTime());

        if (!action.equals("Attack") && !action.equals("Roll")) {action = "Idle";}

        if (FloorManager.playerData.stamina != FloorManager.playerData.maxStamina) {
            staminaRegenTimer += Gdx.graphics.getDeltaTime();
            if (staminaRegenTimer >= staminaRegen) {FloorManager.playerData.stamina ++;}
        } else {
            staminaRegenTimer = 0;
        }

        if (inControl) {

            if (!FloorManager.playerData.gotStarterItems) {

                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.BASIC_SWORD, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfGhastlySteel, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfVampirism, -1, true));
                    FloorManager.playerData.gotStarterItems = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.DAGGERS, -1, true));

                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.BOW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfRoots, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfSwiftness, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfTheShadows, -1, true));
                    FloorManager.playerData.gotStarterItems = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.AXE, -1, true));

                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.steelPot, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfTheIronHeart, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfBloodRain, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfBloodRain, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfBloodRain, -1, true));
                    FloorManager.playerData.gotStarterItems = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.STAFF, -1, true));

                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.HEALTH_POTION, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfRain, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfRain, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfTheFrozenMalice, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.scrollOfTheFrozenMalice, -1, true));

                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.cleaningPot, -1, true));
                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.invisiblityPot, -1, true));
                    FloorManager.playerData.gotStarterItems = true;
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.D)){
                DeveloperMode = true;

                ActionDescription.actionText.add(new ActionDescription.actionType("Developer Mode Enabled", Color.WHITE));
            }

            if (DeveloperMode) {

                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    PlayingField.text.add(new TextOnTop(new Color(0, 50, 0, 1), "TEST"));
                    ToolClass.randomizeChest((int) posX, (int) posY);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                    PlayingField.text.add(new TextOnTop(new Color(0, 50, 0, 1), "God mode on"));
                    invincible = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                    Inventory.Items item = Inventory.slot.randomItem();
                    System.out.println("Created " + item.name());
                    if (item != Inventory.Items.EMPTY)
                        PlayingField.items.add(new Item(posX, posY, item, -1, true));

                    PlayingField.items.add(new Item(posX, posY, Inventory.Items.ARROW, -1, true));
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                    //PlayingField.enemies.add(new EnemyTypesSpawn(posX, posY, "skeletonBrawler", 100));
                /*
                PlayingField.worldObjects.add(new WorldObjects(posX, posY, false, WorldObjects.InteractableObjectType.BARREL));
                PlayingField.worldObjects.add(new WorldObjects(posX, posY, false, WorldObjects.InteractableObjectType.VASE));
                PlayingField.worldObjects.add(new WorldObjects(posX, posY, false, WorldObjects.InteractableObjectType.THINVASE));
                             PlayingField.worldObjects.add(new WorldObjects(posX, posY, false, WorldObjects.InteractableObjectType.CRATE));
                PlayingField.worldObjects.add(new WorldObjects(posX, posY, false, WorldObjects.InteractableObjectType.MATERIALSCRATE));


                 */

                    PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.BIGHPPOTOPN, null, false, posX, posY));
                    // PlayingField.interactableObjects.add(new Item.interactableObject(Item.interactableObject.InteractableType.ANVIL, null, false, posX, posY));
                    //PlayingField.rayHandler.setAmbientLight(1, 1, 1, 1);
                    //PlayingField.enemies.add(new EnemyTypesSpawn(posX, posY, "dungeonMauler", 300));
                }
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    /*
                 PlayingField.rayHandler.setAmbientLight(1, 1, 1, 1);

                for (int i = 0; i < PlayingField.enemies.size(); i++){
                    PlayingField.enemies.get(i).dispose(); }
                PlayingField.enemies.clear();
                 for (Enemies a: PlayingField.enemies){
                      a.vulnerable(0.5f, 5f);
                      a.disarm(5f);
                 }

                      effects.add(new Effects(Effects.effectType.FREEZEAREA, true, 5, 15));
                        PlayerSpells.staminaRush(15);
                       PlayerSpells.root(5, 5);
                       PlayerSpells.spawnGhostSword(15);
                       PlayerSpells.ironHeart(15);

                */
                    // addStatus(activeStatuses.Status.STEELPOT, 5);
                    // goInvisible(5);
                    // PlayerSpells.freezeArea(7, 5);

                    // effects.add(new Effects(Effects.effectType.FREEZEAREA, true, 5, 15));
                    takeDamage(1);
                    // PlayerSpells.freezeArea(5, 5);
                    // PlayerSpells.root(5, 5);
                    // PlayerSpells.spawnGhostSword(15);
                    // PlayerSpells.ironHeart(15);

                    // PlayingField.enemies.add(new EnemyTypesSpawn(posX, posY, "angelicaMinion", 100));
                }

                //System.out.println("SPELLS " + playerSpells.size());
                if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                    PlayingField.enemies.add(new EnemyTypesSpawn(posX, posY, "DarkGuardian", 50));
                    // ActionDescription.actionText.add(new ActionDescription.actionType("NIgger", Color.VIOLET));
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                    if (Inventory.selectedItemNumber != -1) {
                        Inventory.itemAttributes.get(Inventory.selectedItemNumber).upgradeNumber = 1;
                    }
                    if (Inventory.selectedItemNumber != -1) {
                        PlayingField.text.add(new TextOnTop(new Color(50, 0, 0, 1), "Applied Fire To Held Weapon"));
                        Inventory.itemAttributes.get(Inventory.selectedItemNumber).effectType = Effects.effectType.dexterityEnchantment;
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.U)) PlayingField.debugHitbox = !PlayingField.debugHitbox;


                if (Gdx.input.isKeyPressed(Input.Keys.UP)) for (Enemies a : PlayingField.enemies) a.appleY += 0.001f;
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) for (Enemies a : PlayingField.enemies) a.appleX += 0.001f;
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) for (Enemies a : PlayingField.enemies) a.appleX -= 0.001f;
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) for (Enemies a : PlayingField.enemies) a.appleY -= 0.001f;
                if (Gdx.input.isKeyJustPressed(Input.Keys.T)) for (Enemies a : PlayingField.enemies) a.size += 0.001f;
                if (Gdx.input.isKeyJustPressed(Input.Keys.G)) for (Enemies a : PlayingField.enemies) a.size -= 0.001f;



                if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                    PlayingField.items.add(new Item((int) Player.posX, (int) Player.posY, Inventory.Items.HEALTH_POTION, -1, false));
                    System.out.println(PlayingField.items.size());
                }


                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    PlayingField.camera.zoom += 0.01f;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    FloorManager.descendFloor();
                    //PlayingField.camera.zoom -= 0.01f;
                }
                               /*
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) yS += 0.01f;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xS += 0.01f;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xS -= 0.01f;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yS -= 0.01f;
            if (Gdx.input.isKeyPressed(Input.Keys.T)) radiusS += 0.01f;
            if (Gdx.input.isKeyPressed(Input.Keys.G)) radiusS -= 0.01f;

                                */
                System.out.println(Item.size);


                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    PlayerSpells.fireSpell();
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (Tiles.collisionChecker(posX - speed - 0.25f, posY)) {
                    if (Tiles.doorCollisionChecker(posX - speed - 0.25f, posY, hitbox)) {
                        if (Tiles.playerObjectCollisionChecker(hitbox)) {posX -= speed; xChange -= speed;}
                        else {posX -= objectPushSpeed; xChange -= objectPushSpeed;}

                        if (collisionWithEnemies()) posX += speed/2;
                    }
                    if (animationCancel) {
                        action = "Moving";
                    }
                    if (!lockDirection) directionString = "LEFT";
                    rotation = true;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                rotation = false;
                if (Tiles.collisionChecker(posX + speed + 0.25f, posY)) {
                    if (Tiles.doorCollisionChecker(posX + speed + 0.25f, posY, hitbox)){
                        if (Tiles.playerObjectCollisionChecker(hitbox)) {posX += speed; xChange += speed;}
                        else {posX += objectPushSpeed; xChange += objectPushSpeed;}

                        if (collisionWithEnemies()) posX -= speed/2;

                       }
                    if (animationCancel) {
                        action = "Moving";
                    }
                }
                if (!lockDirection) directionString = "RIGHT";

            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if ( Tiles.collisionChecker(posX, posY - speed) ) {
                    if (Tiles.doorCollisionChecker(posX, posY - speed, hitbox)) {
                        if (Tiles.playerObjectCollisionChecker(hitbox) && Tiles.playerObjectCollisionChecker(hitbox))
                           {posY -= speed; yChange -= speed;}
                        else {posY -= objectPushSpeed; yChange -= objectPushSpeed;}

                        if (collisionWithEnemies()) posY += speed/2;

                        if (animationCancel) {
                            action = "Moving";
                        }
                        if (!lockDirection) directionString = "DOWN";
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if ( Tiles.collisionChecker(posX, posY + speed)) {
                    if (Tiles.doorCollisionChecker(posX, posY + speed, hitbox)) {
                        if (Tiles.playerObjectCollisionChecker(hitbox) && Tiles.playerObjectCollisionChecker(hitbox))
                             {posY += speed; yChange += speed;}
                        else {posY += objectPushSpeed; yChange += objectPushSpeed;}

                        if (collisionWithEnemies()) posY -= speed/2;

                        if (animationCancel) {
                            action = "Moving";
                        }
                    }
                }
                if (!lockDirection) directionString = "UP";
            }

            if (canUseHotKeys) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && FloorManager.playerData.hotKeyItems[0] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }

                        if (FloorManager.playerData.hotKeyItems[0] == a.item) { a.useItemByCall(); break;}
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && FloorManager.playerData.hotKeyItems[1] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }
                        if (FloorManager.playerData.hotKeyItems[1] == a.item) {a.useItemByCall(); break;}
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && FloorManager.playerData.hotKeyItems[2] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }
                        if (FloorManager.playerData.hotKeyItems[2] == a.item) {
                            a.useItemByCall();
                            break;
                        }
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && FloorManager.playerData.hotKeyItems[3] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }
                        if (FloorManager.playerData.hotKeyItems[3] == a.item) {
                            a.useItemByCall();
                        break;}
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.X) && FloorManager.playerData.hotKeyItems[4] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }
                        if (FloorManager.playerData.hotKeyItems[4] == a.item) {a.useItemByCall();
                       break;} }

                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.C) && FloorManager.playerData.hotKeyItems[5] != null) {
                    for (Inventory.slot a : Inventory.slots) {
                        if (a.itemNumber != -1){
                            if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                                continue;
                            }
                        }
                        if (FloorManager.playerData.hotKeyItems[5] == a.item) {a.useItemByCall();
                          break;}}
                }
            }

            float initialAttackStamina = attackStamina;
            if (Inventory.slots.get(17).itemNumber != -1){
                if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.dexterityEnchantment){
                    attackStamina /= 2;
                }
            }

          if (Inventory.slots.get(17).item == Inventory.Items.BASIC_SWORD || Inventory.slots.get(17).item == Inventory.Items.AXE  || Inventory.slots.get(17).item == Inventory.Items.DAGGERS  ) {
              if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched() && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !action.equals("Attack") && (stamina > attackStamina)) {
                  action = "Attack";
                  animationCancel = false;
                  inControl = true;
                  isUsingMouseToDirectHit = true;
                  justHit = false;
                  useStamina((int) attackStamina);
                  if (isUsingMouseToDirectHit) attackDirection = setDirectionByMouse();
                  else attackDirection = directionString;
              }
                  if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && Gdx.input.justTouched() && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !action.equals("Attack")) {
                      activateSecondary();}
                  if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !action.equals("Attack")) {
                        activateUltimate();}

          } else if (Inventory.slots.get(17).item == Inventory.Items.STAFF) {
              if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                  action = "Attack";
                  attackDirection = setDirectionByMouse();
                  animationCancel = false;
                  Player.speed = 0.0175f;
              }
          }

            else if (Inventory.slots.get(17).item == Inventory.Items.BOW){
              boolean hasArrow = false;
              for (Inventory.slot a: Inventory.slots){
                  if (a.item == Inventory.Items.ARROW){
                      hasArrow = true;
                      break;
                  }
              }
              if (hasArrow) {
                  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                      action = "Attack";
                      animationCancel = false;
                      attackDirection = setDirectionByMouse();
                      Player.speed = 0.0175f;
                  } else if (action.equals("Attack")) {
                      Player.speed = Player.initialSpeed;
                      if (aimTime < bowAimCharge.getAnimationDuration() / 3) {
                          PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.ARROW, Player.posX, Player.posY, 0.08f, 2, (((float) Math.atan2(aimYdiff, aimXdiff))), false, false, 0, 5));
                      } else if (aimTime < bowAimCharge.getAnimationDuration() - 0.3f)
                          PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.ARROW, Player.posX, Player.posY, 0.15f, 5, (((float) Math.atan2(aimYdiff, aimXdiff))), false, false, 0, 10));
                      else if (aimTime > bowAimCharge.getAnimationDuration() - 0.3f)
                          PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.ARROW, Player.posX, Player.posY, 0.3f, 15, (((float) Math.atan2(aimYdiff, aimXdiff))), false, false, 0, 30));

                      for (Inventory.slot a: Inventory.slots){
                          if (a.item == Inventory.Items.ARROW){
                              a.removeItem();
                              break;
                          }
                      }
                      action = "Idle";
                      animationCancel = true;
                      aiming = false;
                      aimTime = 0;
                      aimAnimationTime = 0;

                  }
              } else {
                  if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) ){
                      PlayingField.text.add(new TextOnTop(Color.WHITE, "No Arrows"));
                  }
              }

          }
            attackStamina = initialAttackStamina;

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                if (dodgeCoolDown >= dodgeCoolDownTime && !(xChange == 0.0f && yChange == 0.0f) && FloorManager.playerData.stamina >= dodgeStamina) {
                    inControl = false;
                    useStamina((int) dodgeStamina);
                    if (Inventory.slots.get(17).item == Inventory.Items.BOW) {
                        Player.speed = Player.initialSpeed;
                        animationCancel = true;
                        aiming = false;
                        staffAiming = false;
                        aimTime = 0;
                        aimAnimationTime = 0;
                    }

                    justHit = false;
                    dodgeCoolDown = 0;
                    isDashing = true;
                    animationCancel = false;
                    yDashSpeed = yChange;
                    xDashSpeed = xChange;
                } else if (!(xChange == 0.0f && yChange == 0.0f)) {
                    PlayingField.text.add(new TextOnTop(Color.WHITE, "Dodge On Cooldown"));
                }

            }
            if ((Math.round(posX) == PlayingField.Exit.x && Math.round(posY) == PlayingField.Exit.y) || Math.round(posX) == PlayingField.Exit2.x && Math.round(posY) == PlayingField.Exit2.y)
            PlayingField.spriteBatch.draw(ToolClass.keyE, PlayingField.Exit.x + 0.75f, PlayingField.Exit.y + 1.5f, 0.5f, 0.5f);
            if (FloorManager.data.getFloorPlayer() != 1 && (Math.round(posX) == PlayingField.Spawn.x && Math.round(posY) == PlayingField.Spawn.y) || Math.round(posX) == PlayingField.Spawn2.x && Math.round(posY) == PlayingField.Spawn2.y)
                PlayingField.spriteBatch.draw(ToolClass.keyE, PlayingField.Spawn.x + 0.75f, PlayingField.Spawn.y + 1.5f, 0.5f, 0.5f);

            if (Gdx.input.isKeyJustPressed(Input.Keys.E) && ((Math.round(posX) == PlayingField.Exit.x && Math.round(posY) == PlayingField.Exit.y) || Math.round(posX) == PlayingField.Exit2.x && Math.round(posY) == PlayingField.Exit2.y)) {
                FloorManager.descendFloor();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.E) &&((Math.round(posX) == PlayingField.Spawn.x && Math.round(posY) == PlayingField.Spawn.y) || (Math.round(posX) == PlayingField.Spawn2.x && Math.round(posY) == PlayingField.Spawn2.y))) {
                System.out.println(FloorManager.data.getFloorPlayer());
                if (FloorManager.data.getFloorPlayer() != 1) {
                    FloorManager.goUpFloor();
                }
            } else {
                for (int i = 0; i < PlayingField.items.size(); i++) {
                    if (PlayingField.items.get(i).itemSprite.getBoundingRectangle().contains(Player.posX + 1, Player.posY + 0.15f)) {
                        PlayingField.items.get(i).itemCollectable = true;
                        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                            PlayingField.items.get(i).pickUpItem();
                        }
                    } else {
                        PlayingField.items.get(i).itemCollectable = false;
                    }
                }
            }

            for (int i = 0; i < PlayingField.containers.size(); i++) {
                if (PlayingField.containers.get(i).chestClosed.getBoundingRectangle().contains(Player.posX + 1, Player.posY + 0.15f) || PlayingField.containers.get(i).chestOpened.getBoundingRectangle().contains(Player.posX + 1, Player.posY + 0.15f)) {
                    PlayingField.containers.get(i).collectable = true;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        PlayingField.containers.get(i).open = !PlayingField.containers.get(i).open;
                    }
                } else {
                    PlayingField.containers.get(i).collectable = false;
                    PlayingField.containers.get(i).open = false;
                }
            }
            for (int i = 0; i < PlayingField.interactableObjects.size(); i++){
                if (PlayingField.interactableObjects.get(i).sprite.getBoundingRectangle().contains(Player.posX + 1, Player.posY + 0.15f)){
                    PlayingField.interactableObjects.get(i).openable = true;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) PlayingField.interactableObjects.get(i).opened = !PlayingField.interactableObjects.get(i).opened ;
                } else{
                    PlayingField.interactableObjects.get(i).opened = false;
                    PlayingField.interactableObjects.get(i).openable = false;
                }
            }
            for (int i = 0; i < PlayingField.doors.size(); i++){
                if (PlayingField.doors.get(i).doorClosed.getBoundingRectangle().contains(Player.posX + 1, Player.posY + 0.15f)){
                    PlayingField.doors.get(i).interactable = true;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) PlayingField.doors.get(i).open = !PlayingField.doors.get(i).open;
                } else{
                    PlayingField.doors.get(i).interactable = false;
                }
            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.I) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                PlayingField.showInventory = !PlayingField.showInventory;
            }




        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            PlayingField.pause = !PlayingField.pause;
        }


        yChange = 0; xChange = 0;
        camera.position.x = posX + 0.25f ;
        camera.position.y = posY + 0.1f;
        camera.update();

    }

    static float damageReduction;
    public static void goInvisible(float invisibleDuration){
        addStatus(activeStatuses.Status.INVISIBILITY, invisibleDuration);
        effects.add(new Effects(Effects.effectType.INVISEFFECT, true, invisibleDuration, 0));
        isInvisible = true;
        for (int i = 0; i < PlayingField.enemies.size(); i++){
            PlayingField.enemies.get(i).detectedPlayer = false;
        }
    }

    public static void addHealth(int addHp){
        FloorManager.playerData.health += addHp;
        if (FloorManager.playerData.health > FloorManager.playerData.maxHealth){FloorManager.playerData.health = FloorManager.playerData.maxHealth;}
        PlayingField.hitNumbers.add(new HitNumbers(0, posX, posY, Player.criticalStrikeActive, " +"+ addHp, Color.GREEN));
    }

    public static void damageReduction(float amount, int time){
        damageReduction = amount;
        activeStatusIcons.add(new activeStatuses(activeStatuses.Status.STEELPOT, time));
    }


    public void useStamina(int amount){
        FloorManager.playerData.stamina -= amount;
        staminaRegenTimer = 0;
    }

    public static void refillStamina(int amount){
        Player.effects.add(new Effects(Effects.effectType.GAINSTAMINA, true, 0.7f, 0));
        if ((FloorManager.playerData.stamina + amount) > FloorManager.playerData.maxStamina){
            FloorManager.playerData.stamina = FloorManager.playerData.maxStamina;
        } else {FloorManager.playerData.stamina += amount;}
    }





    public String setDirectionByMouse() {
        Vector3 worldCoordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float diffX = (posX + 0.5f) - worldCoordinates.x;
        float diffY = (posY) - worldCoordinates.y;
        float angleTowards = -(float) Math.atan2(diffY, diffX) * 100;
        //System.out.println("ANGLE = " + angleTowards);
        if (angleTowards <= 100 && angleTowards >= -80) return "LEFT";
        else if (angleTowards <= 237 && angleTowards > 100) return "UP";
        else if (angleTowards < -80 && angleTowards > -255) return "DOWN";
        else return "RIGHT";
    }


    public boolean collisionWithEnemies() {
        boolean collided = false;
        for (Enemies a: PlayingField.enemies) {
            if (a.currentHP > 0) {
                if (Intersector.overlaps(new Rectangle(a.posX, a.posY, a.hitBox.width, a.hitBox.height/2), new Rectangle(posX + 0.5f, posY, hitbox.width / 2, hitbox.height / 2))) {
                    float diffX = a.posX - (posX + 0.5f);
                    float diffY = a.posY - (posY);
                    float angleTowards = (float) Math.atan2(diffY, diffX);
                    float moveTowardsX = (float) Math.cos(angleTowards);
                    float moveTowardsY = (float) Math.sin(angleTowards);
                    if (Tiles.collisionChecker(a.posX + ((speed)  * moveTowardsX), a.posY + ((speed)  * moveTowardsY))) {
                        a.posX += (speed/2) * moveTowardsX;
                        a.posY += (speed/2) * moveTowardsY;
                        collided = true;
                    }
                }
            }
        }
        return collided;
    }

    void statsAndDisplay(){ //DISPLAY UNDER BATCH IN PLAYERFIELD
        //temporary
        /*
        Buttons.font.draw(PlayingField.Hud, "Health " + health, 20, 700);
        Buttons.font.draw(PlayingField.Hud, "Stamina " + stamina, 20, 650);
        Buttons.font.getData().setScale(0.3f);
        Buttons.font.draw(PlayingField.Hud, "Fps: " + Gdx.graphics.getFramesPerSecond(), 20, 600);
        Buttons.font.draw(PlayingField.Hud, "Experience: " + FloorManager.playerData.experience, 20, 570);
        Buttons.font.draw(PlayingField.Hud, "Level: " + FloorManager.playerData.level, 20, 540);
        Buttons.font.draw(PlayingField.Hud, "LevelUpThreshold: " + FloorManager.playerData.leveUpThreshHold, 20, 510);
        Buttons.font.draw(PlayingField.Hud, "Floor: " + FloorManager.data.getFloorPlayer(), 20, 480);
        Buttons.font.draw(PlayingField.Hud, "Floors Compl: " + FloorManager.data.getFloorsCompleted(), 20, 450);
        Buttons.font.draw(PlayingField.Hud, "Resolution: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight(), 20, 420);
        Buttons.font.getData().setScale(0.5f);

         */
    }

    public static class activeStatuses {
        public enum Status{
            STEELPOT, INVISIBILITY, CLEANINGPOT, DIRTYPOT, BAGOFRICE, CUPOFMILK, GHOSTSWORD, IRONHEART, STAMINARUSH, BLOODRAIN, RAIN, SHADOWSTATE, VAMPIRISM;
        }
        private float duration, timer;
        boolean tick = true;
        Texture texture;
        Status status;
        public activeStatuses(Status status, float duration){
            this.status = status;
            this.duration = duration;
            switch (status) {
                case STEELPOT: texture = ToolClass.steelPotIcon; break;
                case INVISIBILITY: texture = ToolClass.invisbilityPotIcon; break;
                case CLEANINGPOT: texture = ToolClass.cleaningPotIcon; break;
                case DIRTYPOT: texture = ToolClass.stinkyPotIcon; break;
                case BAGOFRICE: texture = ToolClass.bagOfRiceIcon; break;
                case CUPOFMILK: texture = ToolClass.cupOfMilkIcon; break;
                case GHOSTSWORD:  texture = ToolClass.ghostSwordIcon; break;
                case IRONHEART:  texture = ToolClass.strongHeartIcon; break;
                case STAMINARUSH: texture = ToolClass.staminaRushIcon; break;
                case BLOODRAIN: texture = ToolClass.bloodRainIcon; break;
                case RAIN: texture = ToolClass.rainIcon; break;
                case SHADOWSTATE: texture = ToolClass.shadowStateIcon; break;
                case VAMPIRISM: texture = ToolClass.vampirismIcon; break;
            }
        }

        void renderIcon(float x, float y){
            Buttons.inGameText.draw(PlayingField.HudStatic, "" + (float)Math.round((duration - timer) * 10) / 10 , x, y - 0.25f);
            PlayingField.HudStatic.draw(texture, x, y, 0.5f, 0.5f);
            timer += Gdx.graphics.getDeltaTime();
            if (timer >= duration) {
                switch (status) {
                    case STEELPOT: damageReduction = 0; break;
                    case INVISIBILITY: isInvisible = false; break;
                }

                Player.activeStatusIcons.remove(this);
            }
            switch (status) {
                case BAGOFRICE:
                    if (tickTimer(5))
                    Player.addHealth(9);
                    break;
                case CUPOFMILK:
                    if (tickTimer(5)) Player.addHealth(25);
                    break;
            }
        }

        boolean tickTimer(int interval){
            if (((int) timer) % interval == 0 && tick){ tick = false; return true;}
            else if (((int) timer) % 5 != 0 ) {tick = true;}
            return false;
        }

        void resetTimer(){
            timer = 0;
        }
    }

    public static void addStatus(activeStatuses.Status status, float duration){
        boolean alreadyExists = false;
        for (int i = 0; i < activeStatusIcons.size(); i++){
            if (activeStatusIcons.get(i).status == status)
                {if (activeStatusIcons.get(i).duration == duration) activeStatusIcons.get(i).resetTimer();
                else {activeStatusIcons.get(i).duration = duration; activeStatusIcons.get(i).resetTimer();}
                alreadyExists = true;
                break;}
        }
        if (!alreadyExists) {activeStatusIcons.add(new activeStatuses(status, duration));}
    }

    private static float secondaryYOffset = 0, ultimateYOffset = 0, dodgeYOffset = 0;
    public static float secondaryCoolDownTime = 5, ultimateCoolDownTime = 15, dodgeCoolDownTime = 1;
    private static float leftAlpha = 0.9f, middleAlpha = 0.9f, rightAlpha = 0.9f;


    public static void updateInfo(){
        if (FloorManager.playerData.health >  health){ health ++; }
        else if (FloorManager.playerData.health < health){ health --; }
        else {health = FloorManager.playerData.health;}

        if (FloorManager.playerData.stamina >  stamina){ stamina ++; }
        else if (FloorManager.playerData.stamina < stamina){ stamina --; }
        else {stamina = FloorManager.playerData.stamina;}
    }

    public static void Hud(){
        float r= 0.85f, g=0.5f, b=0.3f;

        Vector3 worldCoordinates;

        updateInfo();
        PlayingField.HudStatic.setColor(1, 1,1, 1);
        worldCoordinates = new Vector3(50f , 150f, 0); camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(ToolClass.playerFrame, worldCoordinates.x, worldCoordinates.y, 2f, 2f);
        PlayingField.HudStatic.setColor(1, 1,1, 0.75f);
        if (Head != null)
        PlayingField.HudStatic.draw(Head,worldCoordinates.x - 3.05f, worldCoordinates.y - 3.95f, 8f, 8f );
        PlayingField.HudStatic.setColor(1, 1,1, 1);
        worldCoordinates = new Vector3(170f , 75f, 0); camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(ToolClass.playerInfoBackBar, worldCoordinates.x, worldCoordinates.y, 5f, 0.5f);
        PlayingField.HudStatic.draw(ToolClass.redHpLostBar, worldCoordinates.x + 0.225f, worldCoordinates.y, ((float) health / FloorManager.playerData.maxHealth) * 4.55f, 0.5f);
        PlayingField.HudStatic.draw(ToolClass.redHpBar, worldCoordinates.x + 0.225f, worldCoordinates.y, ((float) FloorManager.playerData.health / FloorManager.playerData.maxHealth) * 4.55f, 0.5f);
        PlayingField.HudStatic.draw(ToolClass.playerInfoBar, worldCoordinates.x, worldCoordinates.y, 5f, 0.5f);
        Buttons.inGameText.getData().setScale(0.0061f);
        Buttons.inGameText.draw(PlayingField.HudStatic, FloorManager.playerData.health + "/" + FloorManager.playerData.maxHealth, worldCoordinates.x + 0.51f, worldCoordinates.y + 0.41f);
        Buttons.inGameText.getData().setScale(0.0065f);
        PlayingField.HudStatic.draw(ToolClass.playerInfoBackBar, worldCoordinates.x, worldCoordinates.y - 0.6f, 5f, 0.5f);
        PlayingField.HudStatic.draw(ToolClass.staminaBar, worldCoordinates.x + 0.225f, worldCoordinates.y - 0.6f, ((float) stamina / FloorManager.playerData.maxStamina) * 4.55f, 0.5f);
        PlayingField.HudStatic.draw(ToolClass.playerInfoBar, worldCoordinates.x, worldCoordinates.y - 0.6f, 5f, 0.5f);
        Buttons.inGameText.getData().setScale(0.0061f);
        Buttons.inGameText.draw(PlayingField.HudStatic, FloorManager.playerData.stamina + "/" + FloorManager.playerData.maxStamina, worldCoordinates.x + 0.51f, worldCoordinates.y - 0.2f);
        Buttons.inGameText.getData().setScale(0.0065f);
        PlayingField.HudStatic.draw(ToolClass.floorIcon, worldCoordinates.x + 0.1f, worldCoordinates.y - 1.1f, 0.35f, 0.35f);
        Buttons.inGameText.draw(PlayingField.HudStatic, FloorManager.data.getFloorPlayer() + "", worldCoordinates.x + 0.6f, worldCoordinates.y- 0.83f );





        PlayingField.HudStatic.setColor(1, 1,1, 0.3f);
        if (secondaryCoolDown < secondaryCoolDownTime) {
            secondaryCoolDown += Gdx.graphics.getDeltaTime();
            if (secondaryYOffset > -0.4f) secondaryYOffset -= 0.1; else {secondaryYOffset = -0.4f;}
            worldCoordinates = new Vector3(552.5f, Gdx.graphics.getHeight() - 120f, 0); camera.unproject(worldCoordinates);
            Buttons.inGameText.setColor(PlayingField.HudStatic.getColor());
            Buttons.inGameText.draw(PlayingField.HudStatic, "" + (float)Math.round((secondaryCoolDownTime - secondaryCoolDown) * 10) / 10, worldCoordinates.x, worldCoordinates.y);
            Buttons.inGameText.setColor(new Color (1, 1, 1, 1));
            leftAlpha = 0.5f;
        } else {if (secondaryYOffset < 0f) secondaryYOffset += 0.1; else {secondaryYOffset = 0; leftAlpha = 0.9f;}}
        if (ultimateCoolDown < ultimateCoolDownTime) {
            ultimateCoolDown += Gdx.graphics.getDeltaTime();
            if (ultimateYOffset > -0.4f) ultimateYOffset -= 0.1; else {ultimateYOffset = -0.4f;}
            worldCoordinates = new Vector3(655f, Gdx.graphics.getHeight() - 130f, 0); camera.unproject(worldCoordinates);
            Buttons.inGameText.setColor(PlayingField.HudStatic.getColor());
            Buttons.inGameText.draw(PlayingField.HudStatic, "" + (float)Math.round((ultimateCoolDownTime - ultimateCoolDown) * 10) / 10, worldCoordinates.x, worldCoordinates.y);
            Buttons.inGameText.setColor(new Color (1, 1, 1, 1));
            middleAlpha = 0.5f;
        } else {if (ultimateYOffset < 0f) ultimateYOffset += 0.1f; else {ultimateYOffset = 0; middleAlpha = 0.9f;}}
        if (dodgeCoolDown < dodgeCoolDownTime) {
            dodgeCoolDown += Gdx.graphics.getDeltaTime();
            if (dodgeYOffset > -0.4f) dodgeYOffset -= 0.1; else {dodgeYOffset = -0.4f;}
            worldCoordinates = new Vector3(760.5f, Gdx.graphics.getHeight() - 120f, 0); camera.unproject(worldCoordinates);
            Buttons.inGameText.setColor(PlayingField.HudStatic.getColor());
            Buttons.inGameText.draw(PlayingField.HudStatic, "" + (float)Math.round((dodgeCoolDownTime - dodgeCoolDown) * 10) / 10, worldCoordinates.x, worldCoordinates.y);
            Buttons.inGameText.setColor(new Color (1, 1, 1, 1));
            rightAlpha = 0.5f;
        } else {if (dodgeYOffset < 0f) dodgeYOffset += 0.1f; else {dodgeYOffset = 0; rightAlpha = 0.9f;}}

        //// Left
        PlayingField.HudStatic.setColor(1, 1,1, leftAlpha);
        worldCoordinates = new Vector3(512f , Gdx.graphics.getHeight() - 23f, 0); camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(swordSecondaryIcon, worldCoordinates.x, worldCoordinates.y + secondaryYOffset, 1.86f, 1.86f);

        //// Middle
        PlayingField.HudStatic.setColor(1, 1,1, middleAlpha);
        worldCoordinates = new Vector3(618.45f, Gdx.graphics.getHeight() - 36f, 0); camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(swordUltimateIcon, worldCoordinates.x, worldCoordinates.y + ultimateYOffset, 1.86f, 1.86f);

        //// Right
        PlayingField.HudStatic.setColor(1, 1,1, rightAlpha);
        worldCoordinates = new Vector3(724.5f, Gdx.graphics.getHeight() - 23f, 0); camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(dodgeIcon, worldCoordinates.x, worldCoordinates.y + dodgeYOffset, 1.86f, 1.86f);

        PlayingField.HudStatic.setColor(r, g, b, 0.9f);
        worldCoordinates = new Vector3(300, Gdx.graphics.getHeight(), 0);
        camera.unproject(worldCoordinates);
        PlayingField.HudStatic.draw(HudMain, worldCoordinates.x, worldCoordinates.y, 13f, 4);


        int yoffset = 0, xoffset = 0;
        for (int i = 0; i < activeStatusIcons.size(); i++){
            if (i % 4 == 0 && i != 0) {yoffset --; xoffset = 0;}

            if (Gdx.graphics.getWidth() > 1000 && Gdx.graphics.getHeight() > 1000) {
                worldCoordinates = new Vector3(Gdx.graphics.getWidth() - 300, 390, 0);}
            else {
                worldCoordinates = new Vector3(Gdx.graphics.getWidth() - 250, 390, 0);}

            camera.unproject(worldCoordinates);
            activeStatusIcons.get(i).renderIcon(worldCoordinates.x +xoffset, worldCoordinates.y + yoffset);
            xoffset ++;
        }

        //Hotkeys

        boolean exists1 = false, exists2 = false, exists3 = false, exists4 = false, exists5 = false, exists6 = false;
        float exists1cooldown = 999,  exists2cooldown = 999, exists3cooldown = 999, exists4cooldown = 999, exists5cooldown = 9999, exists6cooldown = 999;
        for (Inventory.slot a: Inventory.slots){
            int index = -1;
            if (a.item == FloorManager.playerData.hotKeyItems[0]) { index = 0;}
            if (a.item == FloorManager.playerData.hotKeyItems[1]) { index = 1;}
            if (a.item == FloorManager.playerData.hotKeyItems[2]) { index = 2;}
            if (a.item == FloorManager.playerData.hotKeyItems[3]) { index = 3;}
            if (a.item == FloorManager.playerData.hotKeyItems[4]) { index = 4;}
            if (a.item == FloorManager.playerData.hotKeyItems[5]) { index = 5;}

            if (!a.weapon && !a.ring) {
                if (a.itemNumber != -1){
                    if (Inventory.itemAttributes.get(a.itemNumber).cooldownTimer > 0){
                        switch (index){
                            case 0: if (exists1cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                    exists1cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                                } break;
                            case 1: if (exists2cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                exists2cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                            }  break;
                            case 2: if (exists3cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                exists3cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                            }  break;
                            case 3: if (exists4cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                exists4cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                            }  break;
                            case 4: if (exists5cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                exists5cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                            }  break;
                            case 5: if (exists6cooldown > Inventory.itemAttributes.get(a.itemNumber).cooldownTimer ){
                                exists6cooldown = Inventory.itemAttributes.get(a.itemNumber).cooldownTimer;
                            }  break;
                        }
                        continue;
                    }
                }
                switch (index){
                    case 0: exists1 = true; break;
                    case 1: exists2 = true; break;
                    case 2: exists3 = true; break;
                    case 3: exists4 = true; break;
                    case 4: exists5 = true; break;
                    case 5: exists6 = true; break;
                }
            }
        }
        PlayingField.HudStatic.setColor(r, g, b, 1);
        if (FloorManager.playerData.hotKeyItems[0] != null){
            hotKeyDraw(exists1, canUseHotKeys, new Vector3(343.5f, Gdx.graphics.getHeight() - 13f, 0), 0, exists1cooldown);
        }
        if (FloorManager.playerData.hotKeyItems[1] != null){
            hotKeyDraw(exists2, canUseHotKeys, new Vector3(396f, Gdx.graphics.getHeight() - 13f, 0), 1, exists2cooldown);
        }
        if (FloorManager.playerData.hotKeyItems[2] != null){
            hotKeyDraw(exists3, canUseHotKeys, new Vector3(449.5f, Gdx.graphics.getHeight() - 13f, 0), 2, exists3cooldown);
        }
        if (FloorManager.playerData.hotKeyItems[3] != null){
            hotKeyDraw(exists4, canUseHotKeys, new Vector3(848f, Gdx.graphics.getHeight() - 13f, 0), 3, exists4cooldown);
        }
        if (FloorManager.playerData.hotKeyItems[4] != null){
            hotKeyDraw(exists5, canUseHotKeys, new Vector3(900.5f, Gdx.graphics.getHeight() - 13f, 0), 4, exists5cooldown);
        }
        if (FloorManager.playerData.hotKeyItems[5] != null){
            hotKeyDraw(exists6, canUseHotKeys, new Vector3(953.5f, Gdx.graphics.getHeight() - 13f, 0), 5, exists6cooldown);
        }

     //   PlayingField.HudStatic.setColor(Color.GREEN);




      //  bar.draw(PlayingField.spriteBatch, posX - 0.25f, posY + 1.5f + bar + 0.25f, stunDuration, 0.065f);
        PlayingField.spriteBatch.setColor(1, 1, 1, 1);

        if (isBoss){
            worldCoordinates = new Vector3(425f , 600f, 0);
            PlayingField.camera.unproject(worldCoordinates);
            PlayingField.HudStatic.draw(ToolClass.bossFightHealthBackBar, worldCoordinates.x, worldCoordinates.y, 9f, 1f);
            PlayingField.HudStatic.draw(ToolClass.redHpLostBar, worldCoordinates.x + 0.47f, worldCoordinates.y, ((float) lostHealth / maxHealth) * 7.76f, 0.4f);
            PlayingField.HudStatic.draw(ToolClass.redHpBar, worldCoordinates.x + 0.47f, worldCoordinates.y, ((float) bossHealth / maxHealth) * 7.76f, 0.4f);
            PlayingField.HudStatic.draw(ToolClass.bossFightHealthBar, worldCoordinates.x, worldCoordinates.y, 9f, 1f);
        }

    }
    public static boolean isBoss;
    public static int bossHealth, maxHealth, lostHealth;

    public static void hotKeyDraw(boolean exists, boolean canUseHotKeys, Vector3 worldCoordinates, int hotKeyNumber, float cooldownTimer){
        float r= 0.85f, g=0.5f, b=0.3f;

        if (!exists || !canUseHotKeys) {
            PlayingField.HudStatic.setColor(r, g, b, 0.35f);
        }




        camera.unproject(worldCoordinates);
        if (FloorManager.playerData.hotKeyItems[hotKeyNumber] != Inventory.Items.EMPTY)
            PlayingField.HudStatic.draw(Inventory.slot.getItemTexture(FloorManager.playerData.hotKeyItems[hotKeyNumber]), worldCoordinates.x, worldCoordinates.y, 0.8f, 0.8f);
        PlayingField.HudStatic.setColor(r, g, b, 1);

        if (!exists && cooldownTimer != 999){
            Buttons.inGameText.getData().setScale(0.01f);
            Buttons.inGameText.setColor(new Color(r, g, b, 1));
            Buttons.inGameText.draw(PlayingField.HudStatic, (int) cooldownTimer + "", worldCoordinates.x + 0.28f, worldCoordinates.y + 0.525f);

            Buttons.inGameText.setColor(new Color(1, 1, 1, 1));

            Buttons.inGameText.getData().setScale(0.0065f);

        }

    }

    public boolean activateSecondary(){
        if (secondaryCoolDown >= secondaryCoolDownTime){
            secondaryCoolDown = 0;
            secondaryAttack = true;
            attackTime = 0;
            action = "Attack";
            animationCancel = false;
            inControl = true;
            isUsingMouseToDirectHit = true;
            justHit = false;
            if (isUsingMouseToDirectHit) attackDirection = setDirectionByMouse();
            return true;
        } else {
            PlayingField.text.add(new TextOnTop(new Color(1, 1, 1, 1), "In Cooldown"));
            return false;
        }
    }
    public boolean activateUltimate(){
        if (ultimateCoolDown >= ultimateCoolDownTime){
            ultimateCoolDown = 0;
            ultimateAttack = true;
            action = "Attack";

            attackTime = 0;
            animationCancel = false;
            inControl = true;
            isUsingMouseToDirectHit = true;
            justHit = false;
            if (dodgeCoolDown >= dodgeCoolDownTime) dodgeCoolDown = 0;
            if (secondaryCoolDown > secondaryCoolDownTime) secondaryCoolDown -= 1;
            if (isUsingMouseToDirectHit) attackDirection = setDirectionByMouse();
            return true;
        } else {
            PlayingField.text.add(new TextOnTop(new Color(1, 1, 1, 1), "In Cooldown"));
            return false;
        }

    }

    public static float damageReduction2;
    public static void takeDamage(int damageTaken){
        if (hitInvincibleTimer <= 0){
            if (!invincible && !swordBlock) {
                int reduceDamage = (int) (damageTaken * damageReduction);
                int reduceDamage2 = (int) (damageTaken * damageReduction2);
                int reduceDamage3 = (int) (damageTaken * (0.015 * (FloorManager.playerData.Strength)));
                FloorManager.playerData.health -= damageTaken - reduceDamage - reduceDamage2 - reduceDamage3;
                if (reduceDamage != 0){
                    PlayingField.hitNumbers.add(new HitNumbers(0, posX, posY, Player.criticalStrikeActive, "-"+ reduceDamage, Color.WHITE));
                }
                if (reduceDamage2 != 0){
                    PlayingField.hitNumbers.add(new HitNumbers(0, posX, posY, Player.criticalStrikeActive, "-"+ reduceDamage2, Color.LIGHT_GRAY));
                }
                hitInvincibleTimer = 1;
            }
            else if (swordBlock){
                PlayingField.hitNumbers.add(new HitNumbers(0, posX, posY, Player.criticalStrikeActive, "Blocked", null));
            }
        }
    }


    public static void gainExperience(int experience){
        FloorManager.playerData.experience += experience;
        if (FloorManager.playerData.experience >= FloorManager.playerData.leveUpThreshHold){
            do {
                FloorManager.playerData.leveUpThreshHold += (int) ((FloorManager.playerData.leveUpThreshHold / (FloorManager.playerData.level + 1)) * 1.45);
                FloorManager.playerData.level++;
                FloorManager.playerData.skillPoints++;
                PlayingField.text.add(new TextOnTop(Color.YELLOW, "Leveled Up!"));
            } while (FloorManager.playerData.experience >= FloorManager.playerData.leveUpThreshHold);
        }
        PlayingField.text.add(new TextOnTop(new Color(0, 255, 0, 1), "" + experience + " XP Gained"));
    }

    static void changePosition(float x, float y){
        posX = x; posY = y;
    }

    static boolean collisionWithHitbox(Vector2 box1, Vector2 box2, float radiusBox1, float radiusBox2){
        float xD = box1.x - box2.x;      // delta x
        float yD = box1.y - box2.y;      // delta y
        float sqDist = xD * xD + yD * yD;  // square distance
        return sqDist <= (radiusBox1+radiusBox2) * (radiusBox1+radiusBox2);
    }

    public static int calculateDamage(){
        damagePoints = dmgRoll.nextInt(   (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).damagePoints + 5)  - (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).damagePoints - 5))  + (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).damagePoints - 5);
        if (criticalStrikeActive) criticalStrikeActive = false;
        if (dmgRoll.nextInt(100) < FloorManager.playerData.criticalChance) {
            damagePoints *= (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).criticalStrikeMultiplier);
            criticalStrikeActive = true;
        }
        return damagePoints;
    }

    boolean secondAttack = false;
    static boolean swordBlock = false;
    static boolean secondaryAttack = false;
    static boolean ultimateAttack = false;
    boolean justHit = false;
    float yDashSpeed, xDashSpeed;
    @Override
    public void render() {
        controls(camera);
        canUseHotKeys = true;

        //PLACEHOLDER---------------------------------------------------------------------------------------------------------------------------------------



        hitbox.set(posX + 0.65f, posY, 0.8f, 1.3f);
        float yOffset = 0;
        float speedChange = 0.045f;
        aimX = 0; aimY = 0; aimXdiff = 0; aimYdiff = 0;
        stateTime += Gdx.graphics.getDeltaTime();
        if (Inventory.slots.get(17).item == Inventory.Items.BOW || Inventory.slots.get(17).item == Inventory.Items.STAFF) {
            Vector3 worldCoordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            aimXdiff = worldCoordinates.x - 1f - (posX);
            aimYdiff = worldCoordinates.y - 0.75f - (posY);
            float radius = 0;
            if (Inventory.slots.get(17).item == Inventory.Items.BOW) radius = 0.85f;
            if (Inventory.slots.get(17).item == Inventory.Items.STAFF) radius = 1f;
            aimX = (float) (Math.cos(Math.atan2(aimYdiff, aimXdiff)) * radius) + posX;
            aimY = (float) (Math.sin(Math.atan2(aimYdiff, aimXdiff)) * radius) + posY;
        }
        if (player != null) {
            if (player.isFlipX()) player.flip(true, false);
        }

        /////////////////////////////////////////////////////////////////////
        if (Head != null) if (Head.isFlipX()) Head.flip(true, false);
        if (Body != null) if (Body.isFlipX()) Body.flip(true, false);
        if (Legs != null) if (Legs.isFlipX()) Legs.flip(true, false);
        ////////////////////////////////////////////////////////////////////

        if ((action.equals("Moving") || action.equals("Idle")) && !isDashing) {
            if (action.equals("Idle")) {stateTime = 0f;}
            switch (directionString) {
                case "DOWN":
                    Head = headFront.getKeyFrame(stateTime, false);
                    Body = mainBodyFrontMove.getKeyFrame(stateTime, true);
                    Legs = legsFrontMove.getKeyFrame(stateTime, true);
                    break;
                case "UP":
                    Head = headBack.getKeyFrame(stateTime, false);
                    Body = mainBodyBackMove.getKeyFrame(stateTime, true);
                    Legs = legsBackMove.getKeyFrame(stateTime, true);
                    break;
                case "RIGHT":
                    Head = headSide.getKeyFrame(stateTime, false);
                    Body = mainBodySideMove.getKeyFrame(stateTime, true);
                    Legs = legsSideMove.getKeyFrame(stateTime, true);
                    break;
                case "LEFT":
                    Head = headSide.getKeyFrame(stateTime, false);
                    Body = mainBodySideMove.getKeyFrame(stateTime, true);
                    Legs = legsSideMove.getKeyFrame(stateTime, true);

                    if (!Head.isFlipX()) Head.flip(true, false);
                    if (!Body.isFlipX()) Body.flip(true, false);
                    if (!Legs.isFlipX()) Legs.flip(true, false);
                    break;
            }

            if (stateTime >= legsSideMove.getAnimationDuration()) stateTime = 0;
            else if (stateTime >= legsSideMove.getAnimationDuration()/ 6 * 2 &&
                     stateTime <= legsSideMove.getAnimationDuration() / 6 * 3) {yOffset = 0.05f;}
            else if (stateTime >= legsSideMove.getAnimationDuration() / 6 * 5 &&
                    stateTime <= legsSideMove.getAnimationDuration() / 6 * 6) {yOffset = 0.05f;}
        } else if (action.equals("Attack")) {
            canUseHotKeys = false;
            if (Inventory.slots.get(17).item == Inventory.Items.BASIC_SWORD) {
                attackTime += Gdx.graphics.getDeltaTime();
                speed = speedChange;
                if (stateTime >= legsSideMove.getAnimationDuration()) stateTime = 0;
                isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

                if (!secondaryAttack && !ultimateAttack) {
                    if (attackTime > 1.0f){
                        if (secondAttack) secondAttack = false;
                        setFromAttackToIdle();
                    }
                    if (attackTime > 0.5f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && FloorManager.playerData.stamina >= attackStamina){
                        attackTime = 0;
                        attackDirection = setDirectionByMouse();
                        useStamina((int) attackStamina);
                        justHit = false;
                        secondAttack = !secondAttack;
                    }
                    if (attackTime > 0.5f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                          if (activateSecondary()) attackTime = 0;
                    }
                    switch (attackDirection) {
                        case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                            if (secondAttack) {
                                Head = headFrontSwordAttack2.getKeyFrame(attackTime, false);
                                Body = bodyFrontSwordAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.0399994f, 0.15999998f, 0.75999975f, null);
                                    justHit = true;
                                }
                            } else {
                                Head = headFrontSwordAttack1.getKeyFrame(attackTime, false);
                                Body = bodyFrontSwordAttack1.getKeyFrame(attackTime, false);
                               if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                   hitHere(1.0399994f, 0.15999998f, 0.75999975f, null);
                                   justHit = true;
                                }
                            }
                            Legs = legsFront.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsFrontMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "UP":
                            if (secondAttack) {
                                Head = headBackSwordAttack2.getKeyFrame(attackTime, false);
                                Body = bodyBackSwordAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.0399994f, 1.5099989f, 0.75999975f, null);
                                    justHit = true;
                                }
                            } else {
                                Head = headBackSwordAttack1.getKeyFrame(attackTime, false);
                                Body = bodyBackSwordAttack1.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.0399994f, 1.5099989f, 0.75999975f, null);
                                    justHit = true;
                                }
                            }
                            Legs = legsBack.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsBackMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "RIGHT":
                            if (secondAttack) {
                                Head = headSideSwordAttack2.getKeyFrame(attackTime, false);
                                Body = bodySideSwordAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.4999989f, 0.7999996f, 0.7299998f, null);
                                    justHit = true;
                                }
                            } else {
                                Head = headSideSwordAttack1.getKeyFrame(attackTime, false);
                                Body = bodySideSwordAttack1.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.4999989f, 0.7999996f, 0.7299998f, null);
                                    justHit = true;
                                }
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "LEFT":
                            if (secondAttack) {
                                Head = headSideSwordAttack2.getKeyFrame(attackTime, false);
                                Body = bodySideSwordAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(0.6399997f, 0.7999996f, 0.7299998f, null);
                                    justHit = true;
                                }
                            } else {
                                Head = headSideSwordAttack1.getKeyFrame(attackTime, false);
                                Body = bodySideSwordAttack1.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(0.6399997f, 0.7999996f, 0.7299998f, null);
                                    justHit = true;
                                }
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            if (!Head.isFlipX()) Head.flip(true, false);
                            if (!Body.isFlipX()) Body.flip(true, false);
                            if (!Legs.isFlipX()) Legs.flip(true, false);
                            break;
                    }
                } else if (secondaryAttack){
                    swordBlock = true;
                    attackTime += Gdx.graphics.getDeltaTime();
                    if (attackTime > 0.8f){
                        secondaryAttack = false;
                        setFromAttackToIdle();
                    }
                    Head = headSide.getKeyFrame(attackTime, false);
                    Body = bodySideSwordSecondary.getKeyFrame(attackTime, false);
                    Legs = legsSide.getKeyFrame(stateTime, true);
                    if (isMoving) {
                        Legs = legsSideMove.getKeyFrame(stateTime, true);
                    }
                } else if (ultimateAttack){
                    attackTime += Gdx.graphics.getDeltaTime();
                    if (attackTime > 1.65f){
                        ultimateAttack = false;
                        setFromAttackToIdle();
                    }
                    Head = headSide.getKeyFrame(attackTime, false);
                    Body = bodySideSwordUltimate.getKeyFrame(attackTime, false);
                    Legs = legsSide.getKeyFrame(stateTime, true);
                    if (isMoving) {
                        Legs = legsSideMove.getKeyFrame(stateTime, true);
                    }
                }
            }
            else if (Inventory.slots.get(17).item == Inventory.Items.AXE) {
                attackTime += Gdx.graphics.getDeltaTime();
                speed = speedChange;
                if (stateTime >= legsSideMove.getAnimationDuration()) stateTime = 0;
                isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

                if (!secondaryAttack && !ultimateAttack) {
                    if (attackTime > 1.0f){
                        if (secondAttack) secondAttack = false;
                        setFromAttackToIdle();
                    }
                    if (attackTime > 0.8f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && FloorManager.playerData.stamina >= attackStamina){
                        attackTime = 0;
                        attackDirection = setDirectionByMouse();
                        useStamina((int) attackStamina);
                        justHit = false;
                        secondAttack = !secondAttack;
                    }
                    if (attackTime > 0.5f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                        if (activateSecondary()) attackTime = 0;
                    }
                    switch (attackDirection) {
                        case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                            Head = headFrontAxeAttack.getKeyFrame(attackTime, false);
                            Body = bodyFrontAxeAttack.getKeyFrame(attackTime, false);
                            Legs = legsFront.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsFrontMove.getKeyFrame(stateTime, true);
                            }
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                hitHere(1.0599993f, 0.020000074f, 0.85999966f, applyOnHitEffect.axeBasic);
                                justHit = true;
                            }
                            break;
                        case "UP":
                            Head = headBackAxeAttack.getKeyFrame(attackTime, false);
                            Body = bodyBackAxeAttack.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.0599993f, 1.419999f, 0.85999966f, applyOnHitEffect.axeBasic);
                                    justHit = true;
                                }
                            Legs = legsBack.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsBackMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "RIGHT":
                            Head = headSideAxeAttack.getKeyFrame(attackTime, false);
                            Body = bodySideAxeAttack.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.4999989f, 0.7999996f, 0.7299998f, applyOnHitEffect.axeBasic);
                                    justHit = true;
                                }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "LEFT":
                             Head = headSideAxeAttack.getKeyFrame(attackTime, false);
                             Body = bodySideAxeAttack.getKeyFrame(attackTime, false);
                             if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                 hitHere(0.6399997f, 0.7999996f, 0.7299998f, applyOnHitEffect.axeBasic);
                                 justHit = true;
                             }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            if (!Head.isFlipX()) Head.flip(true, false);
                            if (!Body.isFlipX()) Body.flip(true, false);
                            if (!Legs.isFlipX()) Legs.flip(true, false);
                            break;
                    }
                } else if (secondaryAttack){
                    attackTime += Gdx.graphics.getDeltaTime() /2;
                    if (attackTime > 1.4f){
                        secondaryAttack = false;
                        setFromAttackToIdle();
                    }
                    Head = headAxeSecondary.getKeyFrame(attackTime, false);
                    Body = bodyAxeSecondary.getKeyFrame(attackTime, false);
                    if (attackTime < 0.6 || attackTime > 1.1f) {
                        Legs = legsSideMove.getKeyFrame(stateTime, false);
                    } else {
                        Legs = legsAxeSecondary.getKeyFrame(attackTime, false);
                    }
                    if (attackTime > 0.6 && !justHit){
                        hitHere(0.0100001935f, 1.0299994f, 0.5899999f, applyOnHitEffect.axeSecondary);
                           hitHere(1.0399995f, 1.0299994f, 0.5899999f, applyOnHitEffect.axeSecondary);
                               hitHere(1.9799986f, 1.0299994f, 0.5899999f, applyOnHitEffect.axeSecondary);
                        justHit = true;
                    }

                } else if (ultimateAttack){
                    attackTime += Gdx.graphics.getDeltaTime() / 2;
                    if (attackTime > 1.5f){
                        ultimateAttack = false;
                        setFromAttackToIdle();
                    }
                    Head = headAxeUltimate.getKeyFrame(attackTime, false);
                    Body = bodyAxeUltimate.getKeyFrame(attackTime, false);
                    Legs = legsAxeUltimate.getKeyFrame(attackTime, false);
                }
            }
            else if (Inventory.slots.get(17).item == Inventory.Items.DAGGERS) {
                attackTime += Gdx.graphics.getDeltaTime();
                speed = speedChange;
                if (stateTime >= legsSideMove.getAnimationDuration()) stateTime = 0;
                isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);

                if (!secondaryAttack && !ultimateAttack) {
                    if (attackTime > 1.0f){
                        if (secondAttack) secondAttack = false;
                        setFromAttackToIdle();
                    }
                    if (attackTime > 0.5f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && FloorManager.playerData.stamina >= attackStamina){
                        attackTime = 0;
                        attackDirection = setDirectionByMouse();
                        useStamina((int) attackStamina);
                        justHit = false;
                        secondAttack = !secondAttack;
                    }
                    if (attackTime > 0.5f && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                        if (activateSecondary()) attackTime = 0;
                    }
                    switch (attackDirection) {
                        case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                            if (secondAttack) {
                                Head = headFrontDaggersAttack2.getKeyFrame(attackTime, false);
                                Body = bodyFrontDaggersAttack2.getKeyFrame(attackTime, false);
                            } else {
                                Head = headFrontDaggersAttack1.getKeyFrame(attackTime, false);
                                Body = bodyFrontDaggersAttack1.getKeyFrame(attackTime, false);
                            }
                            if (attackTime > 0.2 && attackTime < 0.4 && !justHit) {
                                hitHere(1.0599993f,  0.2600001f, 0.76999974f, applyOnHitEffect.daggerBasic);
                                justHit = true;
                            }
                            Legs = legsFront.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsFrontMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "UP":
                            if (secondAttack) {
                                Head = headBackDaggersAttack2.getKeyFrame(attackTime, false);
                                Body = bodyBackDaggersAttack2.getKeyFrame(attackTime, false);
                            } else {
                                Head = headBackDaggersAttack1.getKeyFrame(attackTime, false);
                                Body = bodyBackDaggersAttack1.getKeyFrame(attackTime, false);
                            }
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                hitHere(1.0499994f, 1.459999f, 0.76999974f, applyOnHitEffect.daggerBasic);
                                justHit = true;
                            }
                            Legs = legsBack.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsBackMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "RIGHT":
                            if (secondAttack) {
                                Head = headSideDaggersAttack2.getKeyFrame(attackTime, false);
                                Body = bodySideDaggersAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.4999989f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerBasic);
                                    justHit = true;
                                }
                            } else {
                                Head = headSideDaggersAttack1.getKeyFrame(attackTime, false);
                                Body = bodySideDaggersAttack1.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(1.4999989f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerBasic);
                                    justHit = true;
                                }
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {   Legs = legsSideMove.getKeyFrame(stateTime, true);    }
                            break;
                        case "LEFT":
                            if (secondAttack) {
                                Head = headSideDaggersAttack2.getKeyFrame(attackTime, false);
                                Body = bodySideDaggersAttack2.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(0.6399997f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerBasic);
                                    justHit = true;    }
                            } else {
                                Head = headSideDaggersAttack1.getKeyFrame(attackTime, false);
                                Body = bodySideDaggersAttack1.getKeyFrame(attackTime, false);
                                if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                    hitHere(0.6399997f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerBasic);
                                    justHit = true; }
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            if (!Head.isFlipX()) Head.flip(true, false);
                            if (!Body.isFlipX()) Body.flip(true, false);
                            if (!Legs.isFlipX()) Legs.flip(true, false);
                            break;
                    }
                } else if (secondaryAttack){
                    if (attackTime > 0.8f){
                        secondaryAttack = false;
                        setFromAttackToIdle();
                    }
                    switch (attackDirection) {
                        case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                            Head = headFrontDaggersSecondary.getKeyFrame(attackTime, false);
                            Body = bodyFrontDaggersSecondary.getKeyFrame(attackTime, false);
                            Legs = legsFront.getKeyFrame(stateTime, true);
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit)
                            {    hitHere(1.0599993f,  0.2600001f, 0.76999974f, applyOnHitEffect.daggerSecondary); justHit = true;}
                            if (isMoving) {
                                Legs = legsFrontMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "UP":
                            Head = headBackDaggersSecondary.getKeyFrame(attackTime, false);
                            Body = bodyBackDaggersSecondary.getKeyFrame(attackTime, false);
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                hitHere(1.0499994f, 1.459999f, 0.76999974f, applyOnHitEffect.daggerSecondary); justHit = true;}
                            Legs = legsBack.getKeyFrame(stateTime, true);
                            if (isMoving)
                            break;
                        case "RIGHT":
                            Head = headSideDaggersSecondary.getKeyFrame(attackTime, false);
                            Body = bodySideDaggersSecondary.getKeyFrame(attackTime, false);
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit) {
                                hitHere(1.4999989f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerSecondary); justHit = true;
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            break;
                        case "LEFT":
                            Head = headSideDaggersSecondary.getKeyFrame(attackTime, false);
                            Body = bodySideDaggersSecondary.getKeyFrame(attackTime, false);
                            if (attackTime > 0.3 && attackTime < 0.5 && !justHit) { hitHere(0.6399997f, 0.7999996f, 0.7299998f, applyOnHitEffect.daggerSecondary);
                                justHit = true;
                            }
                            Legs = legsSide.getKeyFrame(stateTime, true);
                            if (isMoving) {
                                Legs = legsSideMove.getKeyFrame(stateTime, true);
                            }
                            if (!Head.isFlipX()) Head.flip(true, false);
                            if (!Body.isFlipX()) Body.flip(true, false);
                            if (!Legs.isFlipX()) Legs.flip(true, false);
                            break;
                    }
                } else if (ultimateAttack){
                    attackTime += Gdx.graphics.getDeltaTime();
                    if (attackTime > 3.2f){
                        ultimateAttack = false;
                        setFromAttackToIdle();
                    }
                    Head = headSide.getKeyFrame(attackTime, false);
                    Body = bodyDaggersUltimate.getKeyFrame(attackTime, false);
                    Legs = legsSide.getKeyFrame(stateTime, true);
                    if (isMoving) {
                        Legs = legsSideMove.getKeyFrame(stateTime, true);
                    }
                }
            }

            else if (Inventory.slots.get(17).item == Inventory.Items.BOW){
                yOffset = 0;
                aimTime += Gdx.graphics.getDeltaTime();
                bowAimTextureRegion = bowAimCharge.getKeyFrame(aimTime, false);
                aimAnimationTime += Gdx.graphics.getDeltaTime();

                aimTime += Gdx.graphics.getDeltaTime() * (0.05 * (FloorManager.playerData.Endurance));
                aimAnimationTime += Gdx.graphics.getDeltaTime() * (0.05 * (FloorManager.playerData.Endurance));

                switch (attackDirection) {
                    case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                        Head = headFront.getKeyFrame(aimAnimationTime, false);
                        Body = bodyFrontBowAttack.getKeyFrame(aimAnimationTime, false);
                        Legs = legsFront.getKeyFrame(0, true);
                        break;
                    case "UP":
                        Head = headBack.getKeyFrame(aimAnimationTime, false);
                        Body = bodyBackBowAttack.getKeyFrame(aimAnimationTime, false);
                        Legs = legsBack.getKeyFrame(0, true);
                        break;
                    case "RIGHT":
                        Head = headSide.getKeyFrame(aimAnimationTime, false);
                        Body = bodySideBowAttack.getKeyFrame(aimAnimationTime, false);
                        Legs = legsSide.getKeyFrame(0, true);
                        break;
                    case "LEFT":
                        Head = headSide.getKeyFrame(aimAnimationTime, false);
                        Body = bodySideBowAttack.getKeyFrame(aimAnimationTime, false);
                        Legs = legsSide.getKeyFrame(0, true);
                        if (!Head.isFlipX()) Head.flip(true, false);
                        if (!Body.isFlipX()) Body.flip(true, false);
                        if (!Legs.isFlipX()) Legs.flip(true, false);
                        break;
                }


                aiming = true;
            }
            else if (Inventory.slots.get(17).item == Inventory.Items.STAFF){
                attackTime += Gdx.graphics.getDeltaTime();

                switch (attackDirection) {
                    case "DOWN":  //10 frames is 1.0f, 1 frame is 0.1f
                        Head = headFront.getKeyFrame(attackTime, false);
                        Body = bodyFrontStaffAttack.getKeyFrame(attackTime, false);
                        Legs = legsFront.getKeyFrame(0, true);
                        break;
                    case "UP":
                        Head = headBack.getKeyFrame(attackTime, false);
                        Body = bodyBackStaffAttack.getKeyFrame(attackTime, false);
                        Legs = legsBack.getKeyFrame(0, true);
                        break;
                    case "RIGHT":
                        Head = headSide.getKeyFrame(attackTime, false);
                        Body = bodySideStaffAttack.getKeyFrame(attackTime, false);
                        Legs = legsSide.getKeyFrame(0, true);
                        break;
                    case "LEFT":
                        Head = headSide.getKeyFrame(attackTime, false);
                        Body = bodySideStaffAttack.getKeyFrame(attackTime, false);
                        Legs = legsSide.getKeyFrame(0, true);
                        if (!Head.isFlipX()) Head.flip(true, false);
                        if (!Body.isFlipX()) Body.flip(true, false);
                        if (!Legs.isFlipX()) Legs.flip(true, false);
                        break;
                }

                if (attackTime > 0.25f) {
                    if (!damage) {
                        PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.FIREBALL, Player.posX, Player.posY, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), false, false, 0, 25 ));
                        damage = true;
                    }if (attackTime >= 0.5f) {setFromAttackToIdle();} }
            }

            else  {
                if (player != null)
                if (directionString.equals("LEFT")) player.flip(true, false);

                yOffset = 0;
                setFromAttackToIdle();
            }

        }

        if (isDashing){
            int dashMultiplier = 2;
            if (yDashSpeed == 0 || xDashSpeed == 0) dashMultiplier = 3;
            dodgeTime += Gdx.graphics.getDeltaTime();
            invincible = true;
            if (Tiles.collisionChecker(posX, posY + yDashSpeed * dashMultiplier) && Tiles.collisionChecker(posX + xDashSpeed * dashMultiplier, posY)) {
                posY += yDashSpeed * dashMultiplier;
                posX += xDashSpeed * dashMultiplier;}
            if (dodgeTime >= 0.25f) { isDashing= false; setFromRollToIdle(); }
        }


        if (player != null && PlayingField.spriteBatch.isDrawing())

            PlayingField.spriteBatch.setColor(2f, 2f, 0f, 1);
        System.out.println( "INV " +(Math.floor(hitInvincibleTimer * 100)));
        if (hitInvincibleTimer > 0){
            hitInvincibleTimer -= Gdx.graphics.getDeltaTime();
            if ((Math.floor(hitInvincibleTimer * 100)) % 4 == 0){
                PlayingField.spriteBatch.setColor(1, 1,  1, 0f);
            } else PlayingField.spriteBatch.setColor(1, 1,  1, 1);
        } else {hitInvincibleTimer = 0;}

        if (isInvisible) {PlayingField.spriteBatch.setColor(1, 1,  1, 0.45f);}



        if (Legs != null && Head != null && Body != null) {
            PlayingField.spriteBatch.draw(Legs, posX - 1.45f, posY - yOffset - 1.5f, 0, 0, 5f, 4.5f, 1, 1.05f, 0);
            PlayingField.spriteBatch.draw(Head, posX - 1.45f, posY - yOffset - 1.5f, 0, 0, 5f, 4.5f, 1, 1.05f, 0);
            PlayingField.spriteBatch.draw(Body, posX - 1.45f, posY - yOffset - 1.5f, 0, 0, 5f, 4.5f, 1, 1.05f, 0);
        }

        //PlayingField.spriteBatch.draw(player ,  posX - 1.45f,  posY - yOffset - 1.75f, 0, 0,  5f,  4.5f, 1, 1.05f, 0);
        //PlayingField.spriteBatch.draw(player ,  posX - 1.45f,  posY - yOffset - 1.75f, 0, 0,  5f,  4.5f, 1, 1.05f, 0);
            PlayingField.spriteBatch.setColor(1, 1, 1, 1);
        if (effects.size() > 0) {
            for (int i = 0; i < effects.size(); i++){
                if (!effects.get(i).isGroundVisual) {
                    if (!effects.get(i).render(posX, posY)) effects.remove(i);
                }
            }
        }

        if (aiming)  PlayingField.spriteBatch.draw(bowAimTextureRegion ,  aimX, aimY + 0.25f, 1, 0.5f,  1f,  1f, -1, -1, (((float) Math.atan2(aimYdiff, aimXdiff) ) * MathUtils.radiansToDegrees));
        if (staffAiming) {
            staffAim.setPosition(aimX - 9.5f, aimY - 9.75f);
            staffAim.setScale(0.06f);
            staffAim.setRotation((float) Math.atan2(aimYdiff, aimXdiff)  * MathUtils.radiansToDegrees);
            staffAim.draw(PlayingField.spriteBatch);
        }

        if (consecutiveHitTimer > 0){
            consecutiveHitTimer -= Gdx.graphics.getDeltaTime();
            if (consecutiveHitTimer < 0){
                consecutiveHitTimer = 0;
            }
        } else {consecutiveHits = 0;}
        System.out.println(consecutiveHits + " CONM ");
    }

    public enum applyOnHitEffect{
        axeSecondary, axeBasic, daggerBasic, daggerSecondary;
    }

    public float consecutiveHitTimer, consecutiveHits;
    public void hitHere(float circleX, float circleY, float radius, applyOnHitEffect effect) {
        for (Enemies a: PlayingField.enemies) {
            if (Intersector.overlaps(new Circle(Player.posX + circleX, Player.posY + circleY, radius), a.hitBox)) {
                consecutiveHits ++; consecutiveHitTimer = 2.5f;
                int damage = calculateDamage();
                if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.FIRE) {
                    boolean isThereFire = false;
                    for (Effects b : a.enemyEffects) {
                        if (b.type == Effects.effectType.FIRE) {
                            isThereFire = true;
                        }
                    }
                    if (!isThereFire) a.enemyEffects.add(new Effects(Effects.effectType.FIRE, true, 5f, 0));
                }
                else if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.LIGHTNING) {
                     a.takingDamage((int) (damage * 0.05), false, Color.CYAN);
                     for (Enemies b: PlayingField.enemies) {
                        if (Intersector.overlaps(new Circle(a.posX, a.posY, 4), b.hitBox) && b != a) {
                            b.takingDamage(5, false, Color.CYAN);
                            b.enemyEffects.add(new Effects(Effects.effectType.LIGHTNING, true, 5f, 0));
                        }
                     }
                     a.enemyEffects.add(new Effects(Effects.effectType.LIGHTNING, true, 5f, 0));
                }
                else if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.bloodSplat) {
                    Player.addHealth((int) (damage * 0.1f));
                    a.enemyEffects.add(new Effects(Effects.effectType.bloodSplat, true, 5f, 0));
                }
                else if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.frostEnhancement) {
                    if (consecutiveHits % 2 == 0){
                        a.takingDamage((int) (damage * 0.1), false, Color.CYAN);
                        a.chill(2, 0.9f);
                    }
                } else if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.comboEnchantment){
                    if (consecutiveHits < 5){
                        damage += ((damage * 0.1) * consecutiveHits);
                    } else {damage += ((damage * 0.1) * 5);}
                } else if (Inventory.itemAttributes.get(Inventory.slots.get(17).itemNumber).effectType == Effects.effectType.courageEnchantment) {
                    if (a.currentHP - damage <= 0){

                    }
                }


                if (effect != null){
                    switch (effect){
                        case axeSecondary:
                            int bleedCount2 = 0;
                            for (int i = 0; i < a.enemyEffects.size(); i++){
                                if (a.enemyEffects.get(i).type == Effects.effectType.BLEED){
                                    a.enemyEffects.get(i).resetDuration();
                                    bleedCount2 ++;
                                }
                            }
                            if (bleedCount2 != 0){
                                if (bleedCount2 == 1) {a.vulnerable(0.1f, 5);}
                                else if (bleedCount2 == 2) {a.vulnerable(0.2f, 5);}
                                else {a.vulnerable(0.3f, 5);}


                                    for (int i = 0; i < a.enemyEffects.size(); i++){
                                        if (a.enemyEffects.get(i).type == Effects.effectType.BLEED){
                                            a.enemyEffects.remove(a.enemyEffects.get(i));
                                        }
                                    }
                            }
                            damage = (int) (damage * 0.1f);
                            break;
                        case axeBasic:
                            int bleedCount = 0;
                            for (int i = 0; i < a.enemyEffects.size(); i++){
                                if (a.enemyEffects.get(i).type == Effects.effectType.BLEED){
                                    a.enemyEffects.get(i).resetDuration();
                                    bleedCount ++;
                                }
                            }
                            if (bleedCount < 3) {
                                a.enemyEffects.add(new Effects(Effects.effectType.BLEED, true, 5, 0));
                            }
                            break;
                        case daggerBasic:
                            if (criticalStrikeActive){
                                damage *= 2;
                            }
                            break;
                        case daggerSecondary:
                            if (a.currentHP - damage < a.maxHealth * 0.05f) {
                                a.currentHP = 0;
                                justHit = false;
                                dodgeCoolDown = 0;
                                isDashing = true;
                                animationCancel = false;
                                secondaryCoolDown = secondaryCoolDownTime;

                                Vector3 worldCoordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                                float diffX = worldCoordinates.x- (posX + 0.5f) ;
                                float diffY = worldCoordinates.y - (posY + 0.5f)  ;
                                float angleTowards = (float) Math.atan2(diffY, diffX);
                                xDashSpeed = (float) (speed * 2 * Math.cos(angleTowards));
                                yDashSpeed = (float) (speed * 2 * Math.sin(angleTowards));
                            }
                            break;
                    }
                }

                if (Inventory.slots.get(17).item == Inventory.Items.BASIC_SWORD || Inventory.slots.get(17).item == Inventory.Items.AXE || Inventory.slots.get(17).item == Inventory.Items.DAGGERS){
                damage += damage * ((2.5 * (FloorManager.playerData.Strength)) / 100);}

                a.takingDamage(damage, criticalStrikeActive, null);
                if (criticalStrikeActive) criticalStrikeActive = false;

                if (a.currentHP <= 0){
                    if (PlayerSpells.shadowState){
                        Player.goInvisible(1);
                        PlayerSpells.shadowState(5);
                    }
                }
            }
        }
        for (WorldObjects a : PlayingField.worldObjects) {
            if (Intersector.overlaps(new Circle(Player.posX + circleX, Player.posY + circleY, radius), a.collisionBox)) {
                if (!a.destroyed) a.destroyObject = true;
            }
        }
    }

    public void playAnimation(){

    }

    public void setFromAttackToIdle() {
        action = "Idle";
        inControl = true;
        stateTime = 0;
        attackTime = 0;

        swordBlock = false;
        animationCancel = true;
        lockDirection = false;
        speed = initialSpeed;
        justHit = false;
        damage = false; }
        public void setFromRollToIdle(){
            stateTime = 0;
            dodgeTime = 0;
            inControl = true;
            invincible = false;
            lockDirection = false;
            justHit = false;
            animationCancel = true;
            speed = initialSpeed;
        }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        if(amount == 1){
            camera.zoom += .2f;
        }
        else if(amount == -1){
            camera.zoom -= .2f;
        }

        return false;

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
    public void dispose() {

    }
}
