package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Enemies.EnemyTypesSpawn;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.Effects;
import com.mygdx.game.Tools.TextOnTop;
import com.mygdx.game.Tools.Tiles;
import com.mygdx.game.Tools.ToolClass;
import com.mygdx.game.UserInterface.ActionDescription;

import javax.tools.Tool;
import java.util.Random;

import static com.mygdx.game.Inventory.*;

public class Item extends ApplicationAdapter {

    public float x, y;
    public int itemNumber;
    public Inventory.Items itemType;
    boolean itemCollectable, dropSequence;
    Sprite itemSprite;
    public enum objects{
        STAIRSUP, TABLE, SKULL, DUSTEFFECT
    }

    public Item(float x, float y, Inventory.Items item, int itemNumber, boolean dropSequence) {
        itemType = item;
        this.itemNumber = itemNumber;
        this.dropSequence = dropSequence;
        System.out.println(itemNumber);
        this.x = x;
        this.y = y;
        if (item != Items.EMPTY)
            itemSprite = new Sprite(Inventory.slot.getItemTexture(item));

        if (Inventory.isScroll(item)){
            itemAttributes.add(new ItemAttributes(itemAttributes.size()));
            this.itemNumber = itemAttributes.size() - 1;
        }

        if (itemNumber == -1) {
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
                    itemAttributes.add(new ItemAttributes(itemAttributes.size()));
                    this.itemNumber = itemAttributes.size() - 1;
                    itemAttributes.get(this.itemNumber).damagePoints = damagePoints;
                    itemAttributes.get(this.itemNumber).criticalStrikeMultiplier = criticalStrikeMultipiler;
                    break;
                case HEALTH_POTION:
                case ARROW:
                    break;
            }
        }

        itemSprite.setPosition((float) (x - 13.65), (float) (y - 15.75));
    }



    public void pickUpItem(){
        if (Player.inventory.canPutItem(itemType)) {
            System.out.println(itemNumber);
            putItem(itemType, itemNumber);
            PlayingField.items.remove(this);
        } else if (!Player.inventory.canPutItem(itemType)){
            PlayingField.text.add(new TextOnTop(new Color(255, 255, 255, 1), "No Space"));
        }
    }


    boolean begin, negX; float xVelocity = 0.01f, yVelocity = 0.1f, landingPointX, landingPointY, time; Random random = new Random();
    public void drop(){
        time += Gdx.graphics.getDeltaTime();
        if (!begin ) {
            time = random.nextFloat();
            negX= random.nextBoolean();
            landingPointX = x; landingPointY = y;  y += random.nextFloat(); begin = true;}

        xVelocity += (Gdx.graphics.getDeltaTime() / 10);
        yVelocity -= Gdx.graphics.getDeltaTime();
        if (time >= 2f) dropSequence = false;
        if (y < landingPointY){
            dropSequence = false;
        }
        if (negX) {x += xVelocity;}
        else {x -= xVelocity;}
        y += yVelocity;
    }
    public static float size = 32.5f;
    public void render(){
        if (dropSequence){
            drop();
        }

        PlayingField.spriteBatch.setShader(pixelShader);
        PlayingField.spriteBatch.begin();
        itemSprite.setSize(size, size);
        itemSprite.setPosition((float) (x - 13.65), (float) (y - 15.75));
        itemSprite.setScale(UIsize);
        itemSprite.draw(PlayingField.spriteBatch);
        PlayingField.spriteBatch.end();

        PlayingField.spriteBatch.setShader(null);
    }
    public void renderKey(){
        if (itemCollectable){
            PlayingField.spriteBatch.draw(ToolClass.keyE, x + 2.125f, y + 0.75f, 0.5f, 0.5f);
        }
    }@Override
    public void dispose(){
        itemSprite.getTexture().dispose();
    }



    public static class propObjects extends ApplicationAdapter{
        Sprite object, shadowObject;
        TextureRegion animationRegion;
        Animation animation;
        Item.objects objectType;
        float x, y, timer;
        float xOffset, yOffset;
        propObjects(float x, float y, Item.objects object){
            this.x = x;
            this.y = y;
            this.objectType = object;
            switch (object){
                case STAIRSUP: this.object = new Sprite(ToolClass.stairsUp);

                    this.object.setScale(0.079f);
                    xOffset = -15f; yOffset = -7.5f;
                    break;
                case SKULL: this.object = new Sprite(ToolClass.skull);
                    this.shadowObject = new Sprite(ToolClass.shadowSkull);
                    this.object.setScale(0.079f);
                    this.shadowObject.setScale(0.079f);
                    yOffset = -2.735f;
                    xOffset = -2.5f;
                    break;
                case DUSTEFFECT:
                    animation = ToolClass.dustEffect;
                    timer = 0;
                    this.y -= 100;
                    break;
            }
        }
        void renderProps(){
            switch (objectType) {
                case SKULL:
                case STAIRSUP:
                        object.setPosition(x + xOffset, y + yOffset);
                        object.draw(PlayingField.spriteBatch);
                break;
                case DUSTEFFECT:
                    timer += Gdx.graphics.getDeltaTime();
                    animationRegion = animation.getKeyFrame(timer, true);
                    PlayingField.spriteBatch.draw(animationRegion, x - 2, y  + 97 , 8, 8);
                    break;
            }
        }
        void renderShadow(){
            switch (objectType){
                case SKULL: shadowObject.setPosition(x + -4.55f, y + -1.95f);
                shadowObject.draw(PlayingField.spriteBatch);
                break;
            }
        }


        public void dispose(){
            object.getTexture().dispose();
            if (shadowObject != null) shadowObject.getTexture().dispose();
        }
    }

    public static class interactableObject{
        public enum InteractableType{
            ANVIL, ENCHANTMENTCIRCLE, HOODEDSTATUE, FIRETRAP, TRAPCHEST, BIGEMPTYPOTION, BIGHPPOTOPN, BIGSKILLPOINTPOTION
        }
        Sprite sprite, background;
        public InteractableType interactableType;
        public Inventory.Items ifTraderItem;
        Inventory.slot upgradeItemSlot, putMaterialsSlot;
        public boolean isUsed, opened, openable, pickUpIsCoolDown;
        public float x, y, pickUpCoolDown;

        interactableObject(InteractableType interactableType, Inventory.Items ifTraderItem, boolean isUsed, float x, float y){
            this.interactableType = interactableType;
            this.ifTraderItem = ifTraderItem;
            this.isUsed = isUsed;
            this.x = x;
            this.y = y;
            switch (interactableType){
                case ANVIL: sprite = new Sprite(ToolClass.anvil);
                    sprite.setPosition(x - 23.5f, y - 23.25f);
                    sprite.setScale(0.079f);
                    upgradeItemSlot = new Inventory.slot(51, 0, 0, 0, false, false, false, true);
                    putMaterialsSlot = new Inventory.slot(52, 0, 0, 0, false, false, false, true);
                    break;
                case ENCHANTMENTCIRCLE:
                    sprite = new Sprite(ToolClass.enchantmentCircle);
                    sprite.setPosition(x - 15.5f, y - 15.5f);
                    sprite.setScale(0.079f);
                    upgradeItemSlot = new Inventory.slot(53, 0, 0, 0, false, false, false, true);
                    putMaterialsSlot = new Inventory.slot(54, 0, 0, 0, false, false, false, true);
                    break;
                case HOODEDSTATUE: sprite = new Sprite(ToolClass.hoodedStatue);
                    sprite.setPosition(x - 23.55f, y - 23.25f);
                    sprite.setScale(0.079f);break;
                case TRAPCHEST:
                    sprite = new Sprite(ToolClass.chestClosed);
                    float xOffset = -7.5f, yOffset = -7.5f, sizeAmplifier = 3f;
                    sprite.setPosition( x + xOffset, y + yOffset);
                    sprite.setScale(UIsize * sizeAmplifier);
                    break;
                case BIGHPPOTOPN:
                    sprite = new Sprite(ToolClass.bigPotionHp);
                    sprite.setPosition(x - 15.5f, y - 15.25f);
                    sprite.setScale(0.079f);
                    break;
                case BIGEMPTYPOTION:
                    sprite = new Sprite(ToolClass.bigPotion);
                    sprite.setPosition(x - 15.5f, y - 15.25f);
                    sprite.setScale(0.079f);
                    break;
                case BIGSKILLPOINTPOTION:
                    sprite = new Sprite(ToolClass.bigPotionSkillPoint);
                    sprite.setPosition(x - 15.5f, y - 15.25f);
                    sprite.setScale(0.079f);
                    break;
            }
            background = new Sprite(ToolClass.container);
        }

        public String numberToNumberals(int num){
            switch(num) {
                case 0:
                    return "I";
                case 1:
                    return "II";
                case 2:
                    return "III";
                case 3:
                    return "IV";
                case 4:
                    return "V";
            }
            return "";
        }

        public void renderInteractable(){
            switch (interactableType){
                case ANVIL:
                case BIGEMPTYPOTION:
                case BIGHPPOTOPN:
                case TRAPCHEST:
                case HOODEDSTATUE:
                case ENCHANTMENTCIRCLE:
                case BIGSKILLPOINTPOTION:
                    simpleRendering(); break;
                case FIRETRAP: renderTrap(); break;
            }
        }

        public Effects.effectType extractEffect(Items item){
            switch (item){
                case bookOfFire:
                    return Effects.effectType.FIRE;
                case bookOfCombo:
                    return Effects.effectType.comboEnchantment;
                case bookOfNature:
                    return Effects.effectType.natureEnhancement;
                case bookOfBlood:
                    return Effects.effectType.bloodSplat;
                case bookOfLightning:
                    return Effects.effectType.LIGHTNING;
                case bookOfDexterity:
                    return Effects.effectType.dexterityEnchantment;
                case bookOfCourage:
                    return Effects.effectType.courageEnchantment;
            }
            return null;
        }

        public void showControl(){
            PlayingField.spriteBatch.begin();
            if (openable){
                if (interactableType != InteractableType.BIGEMPTYPOTION) PlayingField.spriteBatch.draw(ToolClass.keyE, x + 0.25f, y + 0.75f, 0.5f, 0.5f);
            }
            if (opened){
                if (interactableType != InteractableType.TRAPCHEST && interactableType != InteractableType.BIGEMPTYPOTION && interactableType != InteractableType.BIGHPPOTOPN &&
                    interactableType != InteractableType.BIGSKILLPOINTPOTION) PlayingField.spriteBatch.draw(background, x - 0.75f, y + 1.5f, 2.5f, 2.5f);
                switch (interactableType){
                    case ANVIL:
                        renderSlotAndFunctionality(upgradeItemSlot, (x - 15.5f)  , (y - 12.5f));
                        renderSlotAndFunctionality(putMaterialsSlot, (x - 15.5f)  , (y - 14f));
                        if (upgradeItemSlot != null){
                            if (upgradeItemSlot.item != Items.EMPTY){

                                Buttons.inGameText.draw(PlayingField.spriteBatch, numberToNumberals(itemAttributes.get(upgradeItemSlot.itemNumber).upgradeNumber) ,x + 0.9f, y + 3.55f);
                            }
                        }
                        if (putMaterialsSlot != null){
                            if (putMaterialsSlot.item != Items.EMPTY)
                            Buttons.inGameText.draw(PlayingField.spriteBatch, numberToNumberals(itemAttributes.get(putMaterialsSlot.itemNumber).upgradeNumber) ,x + 0.9f, y + 2.1f);
                        }
                        if (upgradeItemSlot != null && putMaterialsSlot != null)
                         if (upgradeItemSlot.item == putMaterialsSlot.item && upgradeItemSlot.item != Items.EMPTY) {
                             if (itemAttributes.get(upgradeItemSlot.itemNumber).upgradeNumber != 4){
                              if (itemAttributes.get(upgradeItemSlot.itemNumber).upgradeNumber == itemAttributes.get(putMaterialsSlot.itemNumber).upgradeNumber) {

                                  Buttons.inGameText.draw(PlayingField.spriteBatch, "Infuse?", x - 0.75f, y + 1.5f);
                                  if (Inventory.upgradeButton(upgradeIcon, x - 7.75f, y - 5f)) {
                                      putMaterialsSlot.item = Items.EMPTY;

                                      itemAttributes.get(upgradeItemSlot.itemNumber).upgradeNumber++;

                                      ActionDescription.actionText.add(new ActionDescription.actionType("Upgraded " + upgradeItemSlot.item + " to " + numberToNumberals(itemAttributes.get(upgradeItemSlot.itemNumber).upgradeNumber) + "!", Color.YELLOW));
                                  }

                              }} else {
                                 Buttons.inGameText.draw(PlayingField.spriteBatch, "Weapon Already Maxed Out", x - 0.75f, y + 1.5f);}
                         }



                        break;
                    case ENCHANTMENTCIRCLE:
                       // Buttons.inGameText.draw(PlayingField.spriteBatch, "apples" ,x - 0.75f, y + 1.5f);
                        renderSlotAndFunctionality(upgradeItemSlot, (x - 15.5f)  , (y - 12.5f));
                        renderSlotAndFunctionality(putMaterialsSlot, (x - 15.5f)  , (y - 14f));
                        if (upgradeItemSlot != null && putMaterialsSlot != null){
                            if (upgradeItemSlot.item != Items.EMPTY)
                            if (itemAttributes.get(upgradeItemSlot.itemNumber).effectType == null && putMaterialsSlot.item != Items.EMPTY){
                                Buttons.inGameText.draw(PlayingField.spriteBatch, "Apply " + putMaterialsSlot.item + "?", x - 0.75f, y + 1.5f);
                                if (Inventory.upgradeButton(upgradeIcon, x - 7.75f, y - 5f)) {
                                    itemAttributes.get(upgradeItemSlot.itemNumber).effectType = extractEffect(putMaterialsSlot.item);
                                    putMaterialsSlot.item = Items.EMPTY;
                                }

                            }
                        }

                        break;
                    case TRAPCHEST:
                        int maxCount = 0;
                        for (int x = (int) this.x - 5; x <= (int) this.x + 5; x++){
                            for (int y = (int) this.y - 5; y <= (int) this.y + 5; y++) {
                                if (maxCount < 5) {
                                    if (Tiles.collisionChecker(x, y)) {
                                        Random random = new Random();
                                        if (random.nextInt(15) < 1 || (y == (this.y + 5) - (5-maxCount))) {
                                            PlayingField.enemies.add(new EnemyTypesSpawn(x + 1, y, "Spirit", 50));
                                            maxCount ++;
                                        }
                                    }
                                }
                            }
                        }
                        Random random = new Random();
                        PlayingField.containers.add(new Item.itemContainer(x, y, Item.itemContainer.ChestType.WOODENCHEST, ToolClass.getLegendaryLootPool()[random.nextInt(ToolClass.getLegendaryLootPool().length)],
                                ToolClass.getLegendaryLootPool()[random.nextInt(ToolClass.getLegendaryLootPool().length)], ToolClass.getBlueLootPool()[random.nextInt(ToolClass.getBlueLootPool().length)], Items.EMPTY, -1, -1, -1, -1));
                        remove();
                        break;
                    case BIGHPPOTOPN:
                        FloorManager.playerData.maxHealth += 15;
                        FloorManager.playerData.health = FloorManager.playerData.maxHealth;
                        ActionDescription.actionText.add(new ActionDescription.actionType("You feel a surge of strength ripple through your body", Color.WHITE));
                        PlayingField.interactableObjects.add(new interactableObject(InteractableType.BIGEMPTYPOTION, null, false, x, y));
                        remove();
                        break;
                    case BIGSKILLPOINTPOTION:
                        FloorManager.playerData.skillPoints += 1;
                        ActionDescription.actionText.add(new ActionDescription.actionType("You feel more knowledgeable", Color.WHITE));
                        PlayingField.interactableObjects.add(new interactableObject(InteractableType.BIGEMPTYPOTION, null, false, x, y));
                        remove();
                        break;
                }
            } else {
                if (upgradeItemSlot != null){
                    if (upgradeItemSlot.item != Items.EMPTY) {
                        PlayingField.items.add(new Item((int) Player.posX, (int) Player.posY, upgradeItemSlot.item, upgradeItemSlot.itemNumber, false));
                        upgradeItemSlot.item = Inventory.Items.EMPTY;
                        upgradeItemSlot.itemNumber = -1;
                    }
                }

            }
            PlayingField.spriteBatch.end();
        }

        public void remove(){

            PlayingField.interactableObjects.remove(this);
        }
        public void pickUpCoolDownTimer(){
            pickUpCoolDown += Gdx.graphics.getDeltaTime();
            if (pickUpCoolDown > 0.25){
                pickUpCoolDown = 0;
                pickUpIsCoolDown = false;
            }
        }
        public void simpleRendering(){
            PlayingField.spriteBatch.begin();
            sprite.draw(PlayingField.spriteBatch);
            PlayingField.spriteBatch.end();
        }
        public void renderTrap(){

        }

        public void renderSlotAndFunctionality(Inventory.slot slot, float x, float y){
            slot.slotTexture.setPosition((x)  , (y));
            if (slot.slotItem != null) {
                slot.slotItem.setPosition((x)  , (y));
                slot.slotItem.setScale(Inventory.UIsize);
            }
            slot.slotTexture.setScale(Inventory.UIsize);
            slot.slotTexture.draw(PlayingField.spriteBatch) ;

            slot.getSlot();
            if (slot.slotItem != null && slot.item != Items.EMPTY)  slot.slotItem.draw(PlayingField.spriteBatch);

            if (!managing && !clickLetGo && !pickUpIsCoolDown) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isTouched() && slot.item != Inventory.Items.EMPTY) {
                    Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                    if (slot.slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && slot.item != Inventory.Items.EMPTY) {
                        if  (putItem(slot.item, slot.itemNumber)) {
                            slot.item = Inventory.Items.EMPTY;
                            slot.itemNumber = -1;
                        }
                    }

                }
            }
            if (pickUpIsCoolDown) pickUpCoolDownTimer();
        }


        public void dispose(){
            sprite.getTexture().dispose();
            background.getTexture().dispose();
        }

    }


    public static class itemContainer extends ApplicationAdapter{

        public enum ChestType{
            WOODENCHEST, SPECTRALCHEST, COFFIN
        }
        Texture background;
        Sprite chestOpened, chestClosed;
        public Inventory.slot slots1, slots2, slots3, slots4;
        public Inventory.slot[] container;
        float xOffset, yOffset, sizeAmplifier;
        boolean open, collectable;
        public float x, y;
        public ChestType chestType;
        public itemContainer(float x, float y, ChestType chestType, Inventory.Items item1, Inventory.Items item2, Inventory.Items item3, Inventory.Items item4, int itemNumber1, int itemNumber2, int itemNumber3, int itemNumber4){
            this.x = x;
            this.y = y;
            this.chestType = chestType;
            container = new Inventory.slot[4];
            slots1 = new Inventory.slot(50, 0, 0, 0, false, false, false, true);
            slots2 = new Inventory.slot(50, 1, 0, 0,false, false, false, true);
            slots3 = new Inventory.slot(50, 0, 1, 0,false, false, false, true);
            slots4 = new Inventory.slot(50, 1, 1, 0,false, false, false, true);
            slots1.item = item1;
            slots2.item = item2;
            slots3.item = item3;
            slots4.item = item4;

            if (itemNumber1 == -1 && (item1 == Items.BASIC_SWORD || item1 == Items.BOW || item1 == Items.AXE || item1 == Items.STAFF || item1 == Items.DAGGERS)) {
                itemAttributes.add(new ItemAttributes(itemAttributes.size()));
                slots1.itemNumber = itemAttributes.size() - 1;
                applyAttributes(slots1.item, slots1.itemNumber);
            } else{
                slots1.itemNumber = itemNumber1;
            }
            if (itemNumber2 == -1 && (item2 == Items.BASIC_SWORD || item2 == Items.BOW || item1 == Items.AXE || item1 == Items.STAFF || item1 == Items.DAGGERS)) {
                itemAttributes.add(new ItemAttributes(itemAttributes.size()));
                slots2.itemNumber = itemAttributes.size() - 1;
                applyAttributes(slots2.item, slots2.itemNumber);
            } else{
                slots2.itemNumber = itemNumber2;
            }
            if (itemNumber3 == -1 && (item3 == Items.BASIC_SWORD || item3 == Items.BOW || item1 == Items.AXE || item1 == Items.STAFF || item1 == Items.DAGGERS)){
                itemAttributes.add(new ItemAttributes(itemAttributes.size()));
                slots3.itemNumber = itemAttributes.size() - 1;
                applyAttributes(slots3.item, slots3.itemNumber);
            } else{
                slots3.itemNumber = itemNumber3;
            }
            if (itemNumber4 == -1 && (item4 == Items.BASIC_SWORD || item4 == Items.BOW || item1 == Items.AXE || item1 == Items.STAFF || item1 == Items.DAGGERS)){
                itemAttributes.add(new ItemAttributes(itemAttributes.size()));
                slots4.itemNumber = itemAttributes.size() - 1;
                applyAttributes(slots4.item, slots4.itemNumber);
            } else{
                slots4.itemNumber = itemNumber4;
            }

            container[0] = slots1;
            container[1] = slots2;
            container[2] = slots3;
            container[3] = slots4;
            switch (chestType){
                case WOODENCHEST:
                    chestClosed = new Sprite(ToolClass.chestClosed);
                    chestOpened = new Sprite(ToolClass.chestOpened);
                    xOffset = -7.5f; yOffset = -7.5f; sizeAmplifier = 3f;
                    break;
                case COFFIN:
                    chestClosed = new Sprite(ToolClass.coffinClosed);
                    chestOpened = new Sprite(ToolClass.coffinOpened);
                    xOffset = -15.5f; yOffset = -20f; sizeAmplifier = 3f;
                    break;
            }
            chestClosed.setPosition( x + xOffset, y + yOffset);
            chestClosed.setScale(UIsize * sizeAmplifier);
            chestOpened.setPosition( x + xOffset, y + yOffset);
            chestOpened.setScale(UIsize * sizeAmplifier);
            background = ToolClass.container;
            open = false;
        }
        void applyAttributes(Items item, int itemNumber){
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
                        itemAttributes.get(itemNumber).damagePoints = damagePoints;
                        itemAttributes.get(itemNumber).criticalStrikeMultiplier = criticalStrikeMultipiler;
                        break;
                    case HEALTH_POTION:
                    case ARROW:
                        break;
                }
        }


        public void opened(){
            PlayingField.spriteBatch.draw(background, x - 0.75f, y + 0.75f, 2.5f, 2.5f);
            for (Inventory.slot a: container){
                a.drawItem();
                a.slotTexture.setPosition((x - 15.95f) + ((float) (a.x * (Inventory.UIsize / 0.0225))) , (y -14.5f) + ((float) (a.y * (Inventory.UIsize / 0.0225))) );
                a.slotTexture.setScale(Inventory.UIsize);
                a.slotTexture.draw(PlayingField.spriteBatch);
            }
            for (Inventory.slot a: container){
                if (a.item != Inventory.Items.EMPTY){
                a.drawItem();
                a.slotItem.setPosition(x + a.x - 15.95f, y + a.y - 14.5f);
                a.slotItem.setScale(Inventory.UIsize);
                a.slotItem.draw(PlayingField.spriteBatch);
            }}
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                for (Inventory.slot a : container) {
                    a.getSlot();
                }
            } else {
                for (Inventory.slot a : container) {
                    a.slotTexture.setTexture(slotsTexture);
                } }

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isTouched()) {
                for (Inventory.slot a : container) {
                Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                if (a.slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && a.item != Inventory.Items.EMPTY) {
                    if (putItem(a.item, a.itemNumber)) {a.item = Inventory.Items.EMPTY; a.itemNumber = -1;}
                }
            }
        }}
        public void renderKey(){
            if (collectable){
                PlayingField.spriteBatch.draw(ToolClass.keyE, x + 0.25f, y + 0.75f, 0.5f, 0.5f);
            }
        }

        public void drawStorage(){
            if (!open) {
                chestClosed.draw(PlayingField.spriteBatch);
            } else {
                chestOpened.draw(PlayingField.spriteBatch);
            }
        }
        @Override
        public void dispose(){
            background.dispose();
            chestOpened.getTexture().dispose();
            chestClosed.getTexture().dispose();
        }


    }

}
