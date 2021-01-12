package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.Effects;
import com.mygdx.game.Tools.PlayerSpells;
import com.mygdx.game.Tools.TextOnTop;
import com.mygdx.game.Tools.ToolClass;
import com.mygdx.game.UserInterface.ActionDescription;

import java.util.ArrayList;
import java.util.Random;

public class Inventory extends ApplicationAdapter {

    public static ArrayList<slot> slots = new ArrayList<>();
    public static ArrayList<ItemAttributes> itemAttributes = new ArrayList<>();
    Sprite inventorySprite, dmg;
    static Sprite selectedSprite;
    public static Texture  basic_sword, health_potion, bow, arrow, axe, daggers, staff, slotsTexture, slotHighlightTexture,


        ringOfDomination, ringOfFortitude, ringOfTheBlackArts, ringOfFate, redRing, IronRing, LeafyRing, sorcererRing,

        bookOfCombo, bookOfCourage, bookOfDexterity, bookOfNature, bookOfFire, bookOfLightning,  bookOfBlood, bookOfFrost,

        invisiblityPot, cleaningPot, stinkyPot, steelPot, staminaPot,

        scrollOfGhastlySteel, scrollOfRoots, scrollOfSwiftness, scrollOfTheDeadKing, scrollOfTheForgottenShield, scrollOfTheGhostPirate, scrollOfTheIronHeart, scrollOfTheFrozenMalice,
                scrollOfFire, scrollOfRain, scrollOfBloodRain, scrollOfVampirism, scrollOfTheShadows,
        bone, bagOfGold, bagOfRice, cupOfMilk, forgottenPackage, magnifyingGlass, page;




    public static Sprite strengthIcon, enduranceIcon, sorceryIcon, luckIcon, upgradeIcon, upgradeIconHovering, upgradeIcon2, upgradeIcon3, upgradeIcon4;
    static int selectedIndex; static boolean managing;
    static boolean clickLetGo, notDroppable;
    static float UIsize = 0.0225f;  /////////////////////////EDIT UI STUFF
    static Items selectedSpriteItem;
    static int instanceStackNumber, selectedItemNumber;
    public static ShaderProgram pixelShader;



    public enum Items{
        EMPTY, BASIC_SWORD, HEALTH_POTION, BOW, ARROW, AXE, DAGGERS, STAFF,
        ringOfDomination, ringOfFortitude, ringOfTheBlackArts, ringOfFate, redRing, IronRing, LeafyRing, sorcererRing,

        bookOfCombo, bookOfCourage, bookOfDexterity, bookOfNature, bookOfFire, bookOfLightning, bookOfBlood, bookOfFrost,

        invisiblityPot, cleaningPot, stinkyPot, steelPot, staminaPot,

        scrollOfGhastlySteel, scrollOfRoots, scrollOfSwiftness, scrollOfTheDeadKing, scrollOfTheForgottenShield, scrollOfTheGhostPirate, scrollOfTheIronHeart, scrollOfTheFrozenMalice,
        scrollOfFire, scrollOfRain, scrollOfBloodRain, scrollOfVampirism, scrollOfTheShadows,

        bone, bagOfGold, bagOfRice, cupOfMilk, forgottenPackage, magnifyingGlass, page;
    }


    Inventory(){


        Texture inventory = new Texture("Hud/Inventory.png");

        inventory.setFilter(Texture.TextureFilter.Linear
                , Texture.TextureFilter.Linear);
        inventorySprite = new Sprite(inventory);
        dmg = new Sprite(new Texture("Hud/icons/attackDamage.png"));
        dmg.getTexture().setFilter(Texture.TextureFilter.Linear
                , Texture.TextureFilter.Linear);
        basic_sword = new Texture("Items/Sword.png");
        health_potion = new Texture("Items/healingPotion.png");
        bow = new Texture("Items/bow.png");
        arrow = new Texture("Items/arrow.png");
        axe = new Texture("Items/axe.png");
        staff = new Texture("Items/staff.png");
        daggers = new Texture("Items/daggers.png");
        slotsTexture = new Texture("Hud/InventorySlot.png");
        slotHighlightTexture = new Texture("Hud/InventorySlotHighlight.png");

        ringOfDomination = new Texture("Items/ringOfDomination.png");
        ringOfFortitude = new Texture("Items/ringOfFortitude.png");
        ringOfTheBlackArts = new Texture("Items/ringOfTheBlackArts.png");
        ringOfFate = new Texture("Items/ringOfFate.png");
        redRing = new Texture("Items/redRing.png");
        IronRing = new Texture("Items/IronRing.png");
        LeafyRing = new Texture("Items/LeafyRing.png");
        sorcererRing = new Texture("Items/sorcererRing.png");
        bookOfCombo = new Texture("Items/bookOfCombo.png");
        bookOfCourage = new Texture("Items/bookOfCourage.png");
        bookOfDexterity = new Texture("Items/bookOfDexterity.png");
        bookOfNature = new Texture("Items/bookOfNature.png");
        bookOfFire = new Texture("Items/fireBook.png");
        bookOfLightning = new Texture("Items/electricBook.png");
        bookOfBlood = new Texture("Items/bookOfBlood.png");
        bookOfFrost = new Texture("Items/bookOfFrost.png");


        invisiblityPot = new Texture("Items/invisibilityPotion.png");
        cleaningPot = new Texture("Items/cleaningPotion.png");
        stinkyPot = new Texture("Items/stinkyPotion.png");
        steelPot = new Texture("Items/steelPotion.png");
        staminaPot = new Texture("Items/staminaPotion.png");
        scrollOfGhastlySteel = new Texture("Items/scrollOfGhastlySteel.png");
        scrollOfRoots = new Texture("Items/scrollOfRoots.png");
        scrollOfSwiftness = new Texture("Items/scrollOfSwiftness.png");
        scrollOfTheDeadKing = new Texture("Items/scrollOfTheDeadWarlord.png");
        scrollOfTheForgottenShield = new Texture("Items/scrollOfTheForgottenShield.png");
        scrollOfTheGhostPirate = new Texture("Items/scrollOfTheGhostPirate.png");
        scrollOfTheIronHeart = new Texture("Items/scrollOfTheIronHeart.png");
        scrollOfTheFrozenMalice = new Texture("Items/scrollOfFrozenMalice.png");

        scrollOfFire = new Texture("Items/scrollOfFire.png");
        scrollOfRain  = new Texture("Items/scrollOfRain.png");
        scrollOfBloodRain = new Texture("Items/scrollOfBloodRain.png");
        scrollOfVampirism = new Texture("Items/scrollOfVampirism.png");
        scrollOfTheShadows  = new Texture("Items/scrollOfInvisibility.png");

        bone = new Texture("Items/bone.png");
        bagOfGold = new Texture("Items/bagOfGold.png");
        bagOfRice = new Texture("Items/bagOfRice.png");
        cupOfMilk = new Texture("Items/cupOfMilk.png");
        forgottenPackage = new Texture("Items/forgottenPackage.png");
        magnifyingGlass = new Texture("Items/magnifyingGlass.png");
        page = new Texture("Items/page.png");

        arrow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        health_potion.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ringOfDomination.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ringOfFortitude.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ringOfTheBlackArts.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ringOfFate.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        redRing.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        IronRing.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        LeafyRing.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sorcererRing.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfCombo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfCourage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfDexterity.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfNature.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfFire.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfLightning.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        invisiblityPot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cleaningPot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        stinkyPot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        steelPot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        staminaPot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfGhastlySteel.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfRoots.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfSwiftness.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheDeadKing.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheForgottenShield.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheGhostPirate.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheIronHeart.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheFrozenMalice.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         bone.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         bagOfGold.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         bagOfRice.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         cupOfMilk.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         forgottenPackage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         magnifyingGlass.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         page.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfFire.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfRain.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfBloodRain.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfVampirism.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scrollOfTheShadows.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfBlood.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bookOfFrost.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        strengthIcon = new Sprite(new Texture("Hud/icons/strengthIcon.png"));
        strengthIcon.setScale(0.035f);
        enduranceIcon = new Sprite(new Texture("Hud/icons/enduranceIcon.png"));
        enduranceIcon.setScale(0.035f);
        sorceryIcon = new Sprite(new Texture("Hud/icons/sorceryIcon.png"));
        sorceryIcon.setScale(0.035f);
        luckIcon = new Sprite(new Texture("Hud/icons/luckIcon.png"));
        luckIcon.setScale(0.035f);
        upgradeIcon = new Sprite(new Texture("Hud/icons/clickIcon.png"));
        upgradeIcon.setScale(0.035f);
        upgradeIcon2 = new Sprite(new Texture("Hud/icons/clickIcon.png")); upgradeIcon2.setScale(0.035f);
        upgradeIcon3 = new Sprite(new Texture("Hud/icons/clickIcon.png")); upgradeIcon3.setScale(0.035f);
        upgradeIcon4 = new Sprite(new Texture("Hud/icons/clickIcon.png")); upgradeIcon4.setScale(0.035f);

        upgradeIconHovering = new Sprite(new Texture("Hud/icons/clickIconHovering.png"));
        upgradeIconHovering.setScale(0.035f);


        int[][] positions = new int[][]{{1, 1} , {2, 1}, {3, 1}, {4, 1},
                {1, 0} , {2, 0}, {3, 0}, {4, 0},
                {1, -1} , {2, -1}, {3, -1}, {4, -1},
                {1, -2} , {2, -2}, {3, -2}, {4, -2},
        };
        for (int i = 0; i < 16; i++){
            slots.add(new slot(i, positions[i][0], positions[i][1], 1, false, false, false, false));
        }
        slots.add(new slot(16, -4, 1, 1, true, false, false, false));
        slots.add(new slot(17, -4, 0, 1, false, true, false, false));
        slots.add(new slot(18, -4, -1, 1, false, false, true, false));
    }

    public static class slot{
        public int index, x, y, stackNumber, itemNumber;
        public Items item;
        boolean chestPiece, weapon, ring, container, hotKeySelect;
        Sprite slotTexture, slotItem;
        slot(int index, int x, int y, int stackNumber, boolean chestPiece, boolean weapon, boolean ring, boolean container){
            this.index = index;
            this.x = x;
            this.y = y;
            this.stackNumber = stackNumber;
            this.chestPiece = chestPiece;
            this.weapon = weapon;
            this.ring = ring;
            this.container = container;
            slotTexture = new Sprite(slotsTexture);
            item = Items.EMPTY;
        }
        public void getSlot(){
            Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y)) {
                slotTexture.setTexture(slotHighlightTexture);
                if (item != Items.EMPTY){
                    PlayingField.spriteBatch.draw( ToolClass.itemText, worldCoordinates.x, worldCoordinates.y, 5, 3);
                    Buttons.inGameText2.getData().setScale(0.0035f);
                    Buttons.inGameText2.draw(PlayingField.spriteBatch, ToolClass.getText(item, itemNumber), worldCoordinates.x + 0.5f, worldCoordinates.y + 2.5f);
                    Buttons.inGameText2.getData().setScale(0.0065f);

            }
            } else{
               slotTexture.setTexture(slotsTexture);
            }
        }
        public void drawItem(){
               if (item != Items.EMPTY)  slotItem = new Sprite(getItemTexture(item));
        }
        public void removeItem(){
            if (isStackable(item)){
                if (stackNumber > 1){
                    stackNumber --;
                } else {
                    item = Items.EMPTY;
                    itemNumber = -1;
                }
            } else {
                item = Items.EMPTY;
                itemNumber = -1;
            }
        }
        public static Items[] itemSelection= Items.values();

        public static  Random random = new Random();

        public static Items randomItem(){
            return itemSelection[random.nextInt(itemSelection.length)];
        }

        public static Texture getItemTexture(Items item){
            switch (item) {
                case EMPTY: break;
                case BASIC_SWORD: return basic_sword;
                case HEALTH_POTION: return health_potion;
                case BOW: return bow;
                case ARROW: return arrow;
                case AXE: return axe;
                case DAGGERS: return daggers;
                case STAFF: return staff;
                case bone: return bone;
                case page: return page;
                case redRing: return redRing;
                case IronRing: return IronRing;
                case bookOfNature: return bookOfNature;
                case cleaningPot: return cleaningPot;
                case bookOfCombo: return bookOfCombo;
                case bookOfBlood: return bookOfBlood;
                case bookOfFrost: return bookOfFrost;
                case staminaPot: return staminaPot;
                case ringOfFate: return ringOfFate;
                case bookOfFire: return bookOfFire;
                case stinkyPot: return stinkyPot;
                case LeafyRing: return LeafyRing;
                case cupOfMilk: return cupOfMilk;
                case bagOfRice: return bagOfRice;
                case bagOfGold: return bagOfGold;
                case steelPot: return steelPot;
                case sorcererRing: return sorcererRing;
                case bookOfCourage: return bookOfCourage;
                case scrollOfRoots: return scrollOfRoots;
                case invisiblityPot: return invisiblityPot;
                case bookOfDexterity: return bookOfDexterity;
                case bookOfLightning: return bookOfLightning;
                case magnifyingGlass: return magnifyingGlass;
                case ringOfFortitude: return ringOfFortitude;
                case scrollOfTheDeadKing: return scrollOfTheDeadKing;
                case forgottenPackage: return forgottenPackage;
                case ringOfDomination: return ringOfDomination;
                case scrollOfSwiftness: return scrollOfSwiftness;
                case ringOfTheBlackArts: return ringOfTheBlackArts;
                case scrollOfGhastlySteel: return scrollOfGhastlySteel;
                case scrollOfTheIronHeart: return scrollOfTheIronHeart;
                case scrollOfTheGhostPirate: return scrollOfTheGhostPirate;
                case scrollOfTheForgottenShield: return scrollOfTheForgottenShield;
                case scrollOfTheFrozenMalice: return scrollOfTheFrozenMalice;
                case scrollOfFire: return scrollOfFire;
                case scrollOfRain: return scrollOfRain;
                case scrollOfBloodRain: return scrollOfBloodRain;
                case scrollOfVampirism: return scrollOfVampirism;
                case scrollOfTheShadows: return scrollOfTheShadows;


            }
            return null;
        }





        public void pickUpItem(){
            Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item != Items.EMPTY) {
                    selectedSprite = new Sprite(getItemTexture(item));
                    selectedSpriteItem = item;
                    selectedItemNumber = itemNumber;
                    itemNumber = -1;
                    instanceStackNumber = stackNumber;
                    stackNumber = 1;
                    item = Items.EMPTY;
                    selectedIndex = index;
                }
            }
            public void useItem(){
                Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item != Items.EMPTY) {
                    itemAction(item);
                }
            }

            public void itemAction(Items itemType){
                switch (itemType) {
                    case EMPTY:
                        break;
                    case DAGGERS:
                    case STAFF: case AXE:
                    case BOW:
                    case BASIC_SWORD:
                        Items itemInstance = item; int instanceNumber = itemNumber;
                        if (this.weapon) {if (putItem(item, itemNumber)) this.item = Items.EMPTY;  this.itemNumber = -1;}
                        else {
                            if (slots.get(17).item == Items.EMPTY) {
                                slots.get(17).equipWeapon(item); slots.get(17).itemNumber = this.itemNumber; this.item = Items.EMPTY; this.itemNumber = -1;}
                            else {this.item = slots.get(17).item; this.itemNumber = slots.get(17).itemNumber; slots.get(17).equipWeapon(itemInstance); slots.get(17).itemNumber = instanceNumber;}
                        }
                        updateEquippables();
                        break;
                    case ringOfDomination:
                    case ringOfFortitude:
                    case ringOfTheBlackArts:
                    case ringOfFate:
                    case redRing:
                    case IronRing:
                    case LeafyRing:
                    case sorcererRing:
                        Items itemInstance2 = item; int instanceNumber2 = itemNumber;
                        if (this.ring) {if (putItem(item, itemNumber)) this.item = Items.EMPTY;  this.itemNumber = -1;}
                        else {
                            if (slots.get(18).item == Items.EMPTY) {
                                slots.get(18).equipRing(item); slots.get(18).itemNumber = this.itemNumber; this.item = Items.EMPTY; this.itemNumber = -1;}
                            else {this.item = slots.get(18).item; this.itemNumber = slots.get(18).itemNumber; slots.get(18).equipRing(itemInstance2); slots.get(18).itemNumber = instanceNumber2;}
                        }
                        updateEquippables(); break;
                    case ARROW:
                        break;
                    case HEALTH_POTION:
                        if (FloorManager.playerData.healthPotionProficiency < 10) {
                            FloorManager.playerData.healthPotionProficiency ++;
                            if (FloorManager.playerData.healthPotionProficiency >= 10){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Health Potion"));}
                        }
                        if (stackNumber > 1) {stackNumber --;}
                        else item = Items.EMPTY;
                        PlayingField.player.effects.add(new Effects(Effects.effectType.HEAL, true, 0.7f, 0));
                        Player.addHealth(50);
                        break;
                    case invisiblityPot:
                        if (FloorManager.playerData.invisibilityPotionProficiency < 5) {
                            FloorManager.playerData.invisibilityPotionProficiency ++;
                            if (FloorManager.playerData.invisibilityPotionProficiency >= 5){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Invisibility Potion"));}
                        }
                        Player.goInvisible(5);
                        if (stackNumber > 1) {
                            stackNumber--;
                        } else item = Items.EMPTY;
                        break;
                    case cleaningPot:
                        if (FloorManager.playerData.cleaningPotionProficiency < 3) {
                            FloorManager.playerData.cleaningPotionProficiency ++;
                            if (FloorManager.playerData.cleaningPotionProficiency >= 3){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Cleaning Potion"));}
                        }
                        for (int i = 0; i < PlayingField.enemies.size(); i++) {
                            PlayingField.enemies.get(i).tempSetDetectionRadius(3, 15);
                        }
                        Player.addStatus(Player.activeStatuses.Status.CLEANINGPOT, 15);
                        if (stackNumber > 1) {
                            stackNumber--;
                        } else item = Items.EMPTY;

                        break;
                    case stinkyPot: // FIX FPS LOSS
                        if (FloorManager.playerData.stinkyPotionProficiency < 2) {
                            FloorManager.playerData.stinkyPotionProficiency ++;
                            if (FloorManager.playerData.stinkyPotionProficiency >= 2){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Stinky Potion"));}
                        }
                        for (int i = 0; i < PlayingField.enemies.size(); i++) {
                            PlayingField.enemies.get(i).tempSetDetectionRadius(100, 15);
                            PlayingField.enemies.get(i).tempSetLoseDetectionRadius(100, 15);
                        }
                        Player.addStatus(Player.activeStatuses.Status.DIRTYPOT, 15);
                        Player.effects.add(new Effects(Effects.effectType.STINKY, true, 15, 0));
                        if (stackNumber > 1) {
                            stackNumber--;
                        } else item = Items.EMPTY;
                        break;
                    case steelPot:
                        if (FloorManager.playerData.steelPotionProficiency < 8) {
                            FloorManager.playerData.steelPotionProficiency ++;
                            if (FloorManager.playerData.steelPotionProficiency >= 8){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Steel Potion"));}
                        }
                        Player.damageReduction(0.45f, 5);
                        if (stackNumber > 1) {stackNumber --;}
                        else item = Items.EMPTY;
                        break;
                    case staminaPot:
                        if (FloorManager.playerData.staminaPotionProficiency < 7) {
                            FloorManager.playerData.staminaPotionProficiency ++;
                            if (FloorManager.playerData.staminaPotionProficiency >= 7){PlayingField.text.add(new TextOnTop(Color.WHITE, "Mastered Stamina Potion"));}
                        }
                        Player.refillStamina(40);
                        if (stackNumber > 1) {stackNumber --;}
                        else item = Items.EMPTY;
                        break;
                    case bagOfRice:
                        Player.addStatus(Player.activeStatuses.Status.BAGOFRICE, 60);
                        if (stackNumber > 1) {stackNumber --;}
                        else item = Items.EMPTY;
                        break;
                    case cupOfMilk:
                        Player.addStatus(Player.activeStatuses.Status.CUPOFMILK, 10);
                        if (stackNumber > 1) {stackNumber --;}
                        else item = Items.EMPTY;
                        break;
                    case scrollOfSwiftness:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.staminaRush(10);
                            if (FloorManager.playerData.scrollOfSwiftness < 3) {
                                item = Items.EMPTY;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfGhastlySteel:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.spawnGhostSword(15);
                            if (FloorManager.playerData.scrollOfGhastlySteel < 3) {
                                item = Items.EMPTY;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfTheIronHeart:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.ironHeart(20);
                            if (FloorManager.playerData.scrollOfTheIronHeart < 3) {
                                item = Items.EMPTY;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfTheGhostPirate:

                        break;
                    case scrollOfTheForgottenShield:
                        break;
                    case scrollOfRoots:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.root(5, 8);
                            if (FloorManager.playerData.scrollOfRoots < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfRoots++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfTheFrozenMalice:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.freezeArea(8, 3);
                            if (FloorManager.playerData.scrollOfTheFrozenMalice < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfTheFrozenMalice++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfFire:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0){
                            PlayerSpells.fireSpell();
                            if (FloorManager.playerData.scrollOfFire < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfFire++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfRain:
                        System.out.println(itemAttributes.get(itemNumber).cooldownTimer);
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.rain(5, 5);
                            if (FloorManager.playerData.scrollOfRain < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfRain++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfBloodRain:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.bloodRain(5, 5);
                            if (FloorManager.playerData.scrollOfBloodRain < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfBloodRain++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfVampirism:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.vampirism(5, 5);
                            if (FloorManager.playerData.scrollOfVampirism < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfVampirism++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case scrollOfTheShadows:
                        if (itemAttributes.get(itemNumber).cooldownTimer == 0) {
                            PlayerSpells.shadowState(5);
                            if (FloorManager.playerData.scrollOfTheShadows < 3) {
                                item = Items.EMPTY;
                                FloorManager.playerData.scrollOfTheShadows++;
                            } else {
                                itemAttributes.get(itemNumber).cooldownTimer = 5;
                            }
                        }
                        break;
                    case bagOfGold:
                        FloorManager.playerData.gold += random.nextInt(30) + 30 + (FloorManager.playerData.Luck * 5);
                        item = Items.EMPTY;

                }
            }

            public void setHotKeySelect(){
                Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item != Items.EMPTY) {
                    hotKeySelect = true;
                }
            }
            public void useItemByCall(){
                itemAction(item);
            }

            public void resetPlayerAnim(){
                Player.mainBodyFrontMove = Player.bodyFrontMove;
                Player.mainBodySideMove = Player.bodySideMove;
                Player.mainBodyBackMove = Player.bodyBackMove;
            }

            public void updateEquippables(){
                if (this.weapon) if (this.item == Items.EMPTY){
                    Player.speed = 0.07f;
                    Player.initialSpeed = 0.07f;

                    resetPlayerAnim();
                } else {
                    equipWeapon(this.item);
                }

                if (this.ring) if (this.item == Items.EMPTY) {
                    FloorManager.playerData.Strength -= FloorManager.playerData.bonusStrength;
                    FloorManager.playerData.Sorcery -= FloorManager.playerData.bonusSorcery;
                    FloorManager.playerData.Endurance -= FloorManager.playerData.bonusEndurance;
                    FloorManager.playerData.Luck -= FloorManager.playerData.bonusLuck;
                    FloorManager.playerData.maxHealth -= FloorManager.playerData.bonusHealth;
                    FloorManager.playerData.maxStamina -= FloorManager.playerData.bonusStamina;

                    FloorManager.playerData.maxHealth -= 10 * FloorManager.playerData.bonusStrength;
                    FloorManager.playerData.maxStamina -= 10 * FloorManager.playerData.bonusEndurance;

                    if (FloorManager.playerData.health > FloorManager.playerData.maxHealth) {FloorManager.playerData.health = FloorManager.playerData.maxHealth;}
                    if (FloorManager.playerData.stamina > FloorManager.playerData.maxStamina) {FloorManager.playerData.stamina = FloorManager.playerData.maxStamina;}

                    FloorManager.playerData.bonusHealth = 0;
                    FloorManager.playerData.bonusStamina = 0;
                    FloorManager.playerData.bonusStrength = 0;
                    FloorManager.playerData.bonusSorcery = 0;
                    FloorManager.playerData.bonusEndurance = 0;
                    FloorManager.playerData.bonusLuck = 0;
                } else if (!PlayingField.resumedGame) {equipRing(this.item);}
                else {PlayingField.resumedGame = false;}
            }

            public boolean equipWeapon(Items item){
                System.out.println(item + "< WANTED WEAPON");
                Player.staffAiming = false;
                switch (item) {
                    case BASIC_SWORD:
                        slotItem = new Sprite(basic_sword);

                        this.item = item;
                        Player.speed = 0.06f;
                        Player.initialSpeed = 0.06f;

                        Player.mainBodyFrontMove = Player.bodyFrontHoldSword;
                        Player.mainBodySideMove = Player.bodySideHoldSword;
                        Player.mainBodyBackMove = Player.bodyBackHoldSword;

                        Player.ultimateCoolDownTime = 25;
                        Player.secondaryCoolDownTime = 0.5f;
                        Player.secondaryStamina = 0;
                        Player.dodgeStamina = 15;
                        Player.attackStamina = 10;

                        return true;
                    case BOW:
                        slotItem = new Sprite(bow);
                        this.item = item;
                        Player.mainBodyFrontMove = Player.bodyFrontHoldBow;
                        Player.mainBodySideMove = Player.bodySideHoldBow;
                        Player.mainBodyBackMove = Player.bodyBackHoldBow;

                        Player.speed = 0.07f;
                        Player.initialSpeed = 0.07f;

                        return true;
                    case AXE:
                        slotItem = new Sprite(axe);
                        this.item = item;
                        Player.mainBodyFrontMove = Player.bodyFrontHoldAxe;
                        Player.mainBodySideMove = Player.bodySideHoldAxe;
                        Player.mainBodyBackMove = Player.bodyBackHoldAxe;

                        Player.ultimateCoolDownTime = 1;
                        Player.secondaryCoolDownTime = 01f;
                        Player.secondaryStamina = 10;
                        Player.dodgeStamina = 15;
                        Player.attackStamina = 10;

                        Player.speed = 0.06f;
                        Player.initialSpeed = 0.06f;

                        return true;
                    case DAGGERS:
                        slotItem = new Sprite(daggers);
                        this.item = item;
                        Player.mainBodyFrontMove = Player.bodyFrontHoldDaggers;
                        Player.mainBodySideMove = Player.bodySideHoldDaggers;
                        Player.mainBodyBackMove = Player.bodyBackHoldDaggers;

                        Player.ultimateCoolDownTime = 30f;
                        Player.secondaryCoolDownTime = 4f;
                        Player.secondaryStamina = 10;
                        Player.dodgeStamina = 5;
                        Player.attackStamina = 5;

                        Player.speed = 0.07f;
                        Player.initialSpeed = 0.07f;

                        return true;

                    case STAFF:
                        slotItem = new Sprite(staff);
                        this.item = item;
                        Player.mainBodyFrontMove = Player.bodyFrontHoldStaffRun;
                        Player.mainBodySideMove = Player.bodySideHoldStaffRun;
                        Player.mainBodyBackMove = Player.bodyBackHoldStaffRun;
                        Player.staffAiming = true;

                        Player.speed = 0.07f;
                        Player.initialSpeed = 0.07f;

                        return true;
                }
                return false;
            }
            public boolean equipRing(Items item){
                switch (item){
                    case ringOfDomination:
                        slotItem = new Sprite(ringOfDomination);
                        this.item = item;
                        FloorManager.playerData.bonusHealth = 100;
                        FloorManager.playerData.bonusStrength = 2;
                        FloorManager.playerData.maxHealth += FloorManager.playerData.bonusHealth;
                        FloorManager.playerData.Strength += FloorManager.playerData.bonusStrength;
                        FloorManager.playerData.maxHealth += 10 * FloorManager.playerData.bonusStrength;
                        return true;
                    case ringOfFortitude:
                        slotItem = new Sprite(ringOfFortitude);
                        this.item = item;
                        FloorManager.playerData.bonusEndurance = 1;
                        FloorManager.playerData.bonusStamina = 15;
                        FloorManager.playerData.maxStamina += FloorManager.playerData.bonusStamina;
                        FloorManager.playerData.Endurance += FloorManager.playerData.bonusEndurance;
                        FloorManager.playerData.maxStamina += 10 * FloorManager.playerData.bonusEndurance;
                        return true;
                    case ringOfTheBlackArts:
                        slotItem = new Sprite(ringOfTheBlackArts);
                        this.item = item;
                        FloorManager.playerData.bonusSorcery = 3;
                        FloorManager.playerData.Sorcery += FloorManager.playerData.bonusSorcery;
                        return true;
                    case ringOfFate:
                        slotItem = new Sprite(ringOfFate);
                        this.item = item;
                        FloorManager.playerData.bonusLuck = 2;
                        FloorManager.playerData.Luck += FloorManager.playerData.bonusLuck;
                        return true;
                    case redRing:
                        slotItem = new Sprite(redRing);
                        this.item = item;
                        FloorManager.playerData.bonusHealth = 25;
                        FloorManager.playerData.bonusStrength = 1;
                        FloorManager.playerData.maxHealth += FloorManager.playerData.bonusHealth;
                        FloorManager.playerData.Strength += FloorManager.playerData.bonusStrength;
                        FloorManager.playerData.maxHealth += 10 * FloorManager.playerData.bonusStrength;
                        return true;
                    case IronRing:
                        slotItem = new Sprite(IronRing);
                        this.item = item;
                        FloorManager.playerData.bonusStamina = 10;
                        FloorManager.playerData.bonusLuck = 1;
                        FloorManager.playerData.maxStamina += FloorManager.playerData.bonusStamina;
                        FloorManager.playerData.Luck += FloorManager.playerData.bonusLuck;
                        return true;
                    case LeafyRing:
                        slotItem = new Sprite(LeafyRing);
                        this.item = item;
                        FloorManager.playerData.bonusEndurance = 1;
                        FloorManager.playerData.Endurance += FloorManager.playerData.bonusEndurance;
                        FloorManager.playerData.maxStamina += 10 * FloorManager.playerData.bonusEndurance;
                        return true;
                    case sorcererRing:
                        slotItem = new Sprite(sorcererRing);
                        this.item = item;
                        FloorManager.playerData.bonusSorcery = 1;
                        FloorManager.playerData.Sorcery += FloorManager.playerData.bonusSorcery;
                        return true;
                }

                return false;
            }
            public void equipHelmet(Items item){

            }

            public boolean isStackable(Items item) {
                switch (item) {
                    case EMPTY:
                    case BOW:
                    case AXE:
                    case STAFF:
                    case DAGGERS:
                    case BASIC_SWORD:
                        return false;
                    case HEALTH_POTION:
                    case ARROW:
                    case invisiblityPot:
                    case cleaningPot:
                    case stinkyPot:
                    case steelPot:
                    case staminaPot:
                    case bone:
                    case bagOfGold:
                    case bagOfRice:
                    case cupOfMilk:
                    case magnifyingGlass:
                        return true;
                }
                return false;
            }





            public boolean placeItem() {
                Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item == selectedSpriteItem  && isStackable(item)){
                    if (stackNumber < 20) {
                       if (instanceStackNumber > 1) {
                           instanceStackNumber--;
                           stackNumber++;
                           return true;
                       } else {
                           stackNumber++;
                           selectedSprite = null;
                           selectedSpriteItem = null;
                           managing = false;
                           clickLetGo = false;
                           return true;
                       }
                   } else {
                           notDroppable = true;
                           return false;
                    }
                }
                if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item == Items.EMPTY) {
                    if (this.weapon) {
                        if (equipWeapon(selectedSpriteItem)) {
                            itemNumber = selectedItemNumber;
                            selectedItemNumber = -1;
                            selectedSprite = null;
                            selectedSpriteItem = null;
                            managing = false;
                            clickLetGo = false;
                            return true;
                        } else {
                            notDroppable = true;
                            return false;
                        }
                    }
                    if (this.ring) {
                        if (equipRing(selectedSpriteItem)) {
                            itemNumber = selectedItemNumber;
                            selectedItemNumber = -1;
                            selectedSprite = null;
                            selectedSpriteItem = null;
                            managing = false;
                            clickLetGo = false;
                            return true;
                        } else {
                            notDroppable = true;
                            return false;
                        }
                    }


                    slotItem = new Sprite(getItemTexture(selectedSpriteItem));
                    item = selectedSpriteItem;
                    itemNumber = selectedItemNumber;
                    selectedItemNumber = -1;
                    stackNumber = instanceStackNumber;
                    instanceStackNumber = 1;
                    selectedSprite = null;
                    selectedSpriteItem = null;
                    managing = false;
                    clickLetGo = false;
                    return true;
                } else if (slotTexture.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && item != Items.EMPTY && item != selectedSpriteItem) {
                    if (this.weapon || this.ring) {
                        notDroppable = true;
                        return false;
                    }

                    int itemNumberInstance = selectedItemNumber; /////////////////////////////////////////////////////////////////////////////////////////////?FIX WEAPON SWITCH PROBLEM WHERE YOU CANT SWITCH WEAPONS WHEN A WEAPON IS IN THE WEAPON SLOT////////////////

                    slotItem = new Sprite(getItemTexture(selectedSpriteItem));
                    itemInstance = selectedSpriteItem;
                    pickUpItem();
                    item = itemInstance;
                    itemNumber = itemNumberInstance;
                    stackNumber = instanceStackNumber;
                    instanceStackNumber = 1;
                    return true;
                }
                return false;
            }

        }
    public static Inventory.Items itemInstance = null;


    public void showInventory(){
        //System.out.println("size = "+ itemAttributes.size());
        inventorySprite.setPosition( Player.posX -274.55f, Player.posY - 119);
        inventorySprite.setScale(UIsize + 0.003f);
        inventorySprite.draw(PlayingField.spriteBatch);
        for (slot a: slots) {
            a.drawItem();
            a.slotTexture.setPosition((Player.posX - 13.65f) + ((float) (a.x * (UIsize / 0.0225))) , (Player.posY -15.15f) + ((float) (a.y * (UIsize / 0.0225))) );
            a.slotTexture.setScale(UIsize);
            a.slotTexture.draw(PlayingField.spriteBatch);


            if (a.hotKeySelect){
                    Player.canUseHotKeys = false;
                    Buttons.inGameText.draw(PlayingField.spriteBatch, "Apply Hotkey On " + a.item.name(), Player.posX - 2f, Player.posY + 4f);
                    if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {FloorManager.playerData.setHotKey(a.item, 0); a.hotKeySelect = false;  Player.canUseHotKeys = true;}
                    else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {FloorManager.playerData.setHotKey(a.item, 1); a.hotKeySelect = false; Player.canUseHotKeys = true;}
                    else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {FloorManager.playerData.setHotKey(a.item, 2); a.hotKeySelect = false;  Player.canUseHotKeys = true;}
                    else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {FloorManager.playerData.setHotKey(a.item, 3); a.hotKeySelect = false;  Player.canUseHotKeys = true;}
                    else if (Gdx.input.isKeyPressed(Input.Keys.X)) {FloorManager.playerData.setHotKey(a.item, 4); a.hotKeySelect = false;  Player.canUseHotKeys = true;}
                    else if (Gdx.input.isKeyPressed(Input.Keys.C)) {FloorManager.playerData.setHotKey(a.item, 5); a.hotKeySelect = false;  Player.canUseHotKeys = true;}
            }

        } for (slot a: slots){
            if (a.item != Items.EMPTY) {
                a.slotItem.setPosition((Player.posX - 13.65f) + a.x, (Player.posY - 15.15f) + a.y);
                a.slotItem.setScale(UIsize);
                if (a.itemNumber != -1){
                    if (itemAttributes.get(a.itemNumber).cooldownTimer > 0) {
                        a.slotItem.setColor(Color.DARK_GRAY);
                    }
                }
                a.slotItem.draw(PlayingField.spriteBatch);
                a.slotItem.setColor(new Color(1, 1, 1,1));
                if (a.stackNumber > 1){
                    Buttons.inGameText.draw(PlayingField.spriteBatch, a.stackNumber + "" , Player.posX + 2.55f + a.x, Player.posY + 0.65f + a.y);
                }
                if (a.itemNumber != -1){
                    if (itemAttributes.get(a.itemNumber).cooldownTimer > 0) {
                        Buttons.inGameText.draw(PlayingField.spriteBatch, (int) (itemAttributes.get(a.itemNumber).cooldownTimer) + "", (Player.posX + 2.4f) + a.x, (Player.posY  + 0.8f) + a.y);
                    }
                }
            }
        }
        if (selectedSpriteItem != null) {
            Buttons.inGameText.draw(PlayingField.spriteBatch, selectedSpriteItem + "", Player.posX + 7.5f, Player.posY + 1f );
            Buttons.inGameText.draw(PlayingField.spriteBatch, selectedItemNumber + "", Player.posX + 7.5f, Player.posY + 0.6f );
        }

        Buttons.inGameText.draw(PlayingField.spriteBatch, "\nLevel : " + FloorManager.playerData.level +  "\nExp : " + FloorManager.playerData.experience + "/" + FloorManager.playerData.leveUpThreshHold +
                "\nSkill Points : " + FloorManager.playerData.skillPoints +
                "\nGold : " + FloorManager.playerData.gold + "\nFps : " + Gdx.graphics.getFramesPerSecond(), Player.posX - 6f, Player.posY + 2.25f);

        strengthIcon.setPosition((Player.posX - 4.75f)  , (Player.posY -10.4f));
        enduranceIcon.setPosition((Player.posX - 3.75f)  , (Player.posY -10.4f));
        sorceryIcon.setPosition((Player.posX - 2.75f)  , (Player.posY -10.4f));
        luckIcon.setPosition((Player.posX - 1.75f)  , (Player.posY -10.4f));

        strengthIcon.draw(PlayingField.spriteBatch);
        Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));



        if (FloorManager.playerData.bonusStrength > 0) Buttons.inGameText.setColor(Color.GREEN.r, Color.GREEN.g,Color.GREEN.b, 1f);
        Buttons.inGameText.draw(PlayingField.spriteBatch, (FloorManager.playerData.Strength) + "", Player.posX + 3.65f, Player.posY - 2.25f);
        enduranceIcon.draw(PlayingField.spriteBatch);
        Buttons.inGameText.setColor(1, 1,1, 0.5f);

        if (FloorManager.playerData.bonusEndurance > 0) Buttons.inGameText.setColor(Color.GREEN.r, Color.GREEN.g,Color.GREEN.b, 1f);
        Buttons.inGameText.draw(PlayingField.spriteBatch, (FloorManager.playerData.Endurance) + "", Player.posX + 4.65f, Player.posY - 2.25f);
        sorceryIcon.draw(PlayingField.spriteBatch);
        Buttons.inGameText.setColor(1, 1,1, 0.5f);

        if (FloorManager.playerData.bonusSorcery > 0) Buttons.inGameText.setColor(Color.GREEN.r, Color.GREEN.g,Color.GREEN.b, 1f);
        Buttons.inGameText.draw(PlayingField.spriteBatch, (FloorManager.playerData.Sorcery) + "", Player.posX + 5.65f, Player.posY - 2.25f);
        luckIcon.draw(PlayingField.spriteBatch);
        Buttons.inGameText.setColor(1, 1,1, 0.5f);

        if (FloorManager.playerData.bonusLuck > 0) Buttons.inGameText.setColor(Color.GREEN.r, Color.GREEN.g,Color.GREEN.b, 1f);
        Buttons.inGameText.draw(PlayingField.spriteBatch, (FloorManager.playerData.Luck) + "", Player.posX + 6.65f, Player.posY - 2.25f);
        Buttons.inGameText.setColor(1, 1,1, 1f);


        Buttons.inGameText.getData().setScale(0.0055f);
        if (strengthIcon.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ){
                PlayingField.spriteBatch.draw( ToolClass.itemText, worldCoordinates.x, worldCoordinates.y, 7, 3);
                Buttons.inGameText.draw(PlayingField.spriteBatch, " Strength " + (FloorManager.playerData.Strength) + "\n\n"
                        + " Increases Max Heatlh, Melee Damage \n and Damage Reduction\n" +
                        "\n HP BONUS: " + (10 * (FloorManager.playerData.Strength)) + "  MELEE DMG: %"  + (2.5 * (FloorManager.playerData.Strength)) +
                        "  \n DMG REDUC: %" + (1.5 * (FloorManager.playerData.Strength)), worldCoordinates.x + 0.5f, worldCoordinates.y + 2.5f);
        }
        if (enduranceIcon.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ){
            PlayingField.spriteBatch.draw( ToolClass.itemText, worldCoordinates.x - 1, worldCoordinates.y, 7, 3);
            Buttons.inGameText.draw(PlayingField.spriteBatch, " Endurance " + (FloorManager.playerData.Endurance) + "\n\n"
                    + " Increases Max Stamina and Bows Are \n Drawn Faster\n" +
                    "\n STAMINA BONUS: " + (10 * (FloorManager.playerData.Endurance)) + "  BOW DRAW REDUC: %"  + (5 * (FloorManager.playerData.Endurance)),
                    worldCoordinates.x - 0.5f, worldCoordinates.y + 2.5f);
        }
        if (sorceryIcon.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ){

            PlayingField.spriteBatch.draw( ToolClass.itemText, worldCoordinates.x - 2, worldCoordinates.y, 7, 3);
            Buttons.inGameText.draw(PlayingField.spriteBatch, " Sorcery " + (FloorManager.playerData.Sorcery) + "\n\n"
                            + " Increases Scroll Stats and \n Elemental Damage \n" +
                            "\n Elemental Damage Bonus: %" + (5 * (FloorManager.playerData.Sorcery)),
                    worldCoordinates.x - 1.5f, worldCoordinates.y + 2.5f);
        }
        if (luckIcon.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ){
            PlayingField.spriteBatch.draw( ToolClass.itemText, worldCoordinates.x - 3, worldCoordinates.y, 7, 3);
            Buttons.inGameText.draw(PlayingField.spriteBatch, " Luck " + (FloorManager.playerData.Luck) + "\n\n"
                            + " Increases critical chance and \n loot drops\n" +
                            "\n CRIT BONUS: %" + (5 * (FloorManager.playerData.Luck)) + "  \n DROP CHANCE BONUS: %"  + (2.5 * (FloorManager.playerData.Luck)),
                    worldCoordinates.x - 2.5f, worldCoordinates.y + 2.5f);
        }

        Buttons.inGameText.getData().setScale(0.0065f);

        //fix bonus hp, stamina and crit chance.
        // implement elemental damge bonus, and increase scroll stats
        if(FloorManager.playerData.skillPoints > 0){
            if (upgradeButton(upgradeIcon, Player.posX - 4.75f, Player.posY - 10f))
            {FloorManager.playerData.Strength ++; FloorManager.playerData.skillPoints--;
             FloorManager.playerData.maxHealth += 10;}
            if (upgradeButton(upgradeIcon2, Player.posX - 3.75f, Player.posY - 10f))
            {FloorManager.playerData.Endurance ++; FloorManager.playerData.skillPoints--;
             FloorManager.playerData.maxStamina += 10;}
            if (upgradeButton(upgradeIcon3, Player.posX - 2.75f, Player.posY - 10f)) {FloorManager.playerData.Sorcery ++; FloorManager.playerData.skillPoints--;}
            if (upgradeButton(upgradeIcon4, Player.posX - 1.75f, Player.posY - 10f))
            {FloorManager.playerData.Luck ++; FloorManager.playerData.skillPoints--;
            FloorManager.playerData.criticalChance += 5;}
        }
    }
    public static boolean upgradeButton(Sprite sprite, float x, float y){
        Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        if (sprite.getBoundingRectangle().contains(worldCoordinates.x, worldCoordinates.y) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            upgradeIconHovering.setPosition(x, y);
            upgradeIconHovering.draw(PlayingField.spriteBatch);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched()){
                return true;
            } return false;
        } else {
            sprite.setColor(1, 1, 1, 0.5f);
            sprite.setPosition(x, y);
            sprite.draw(PlayingField.spriteBatch);
            return false;
        }
    }

    public boolean canPutItem(Items item){
        for (slot a: slots){
            if (((a.item == Items.EMPTY) || (item == a.item && a.stackNumber < 20 && a.isStackable(a.item)))  && !a.ring && !a.weapon && !a.chestPiece && !a.container) {
                return true;}
        }
        return false;
    }

    public static boolean putItem(Items item, int itemNumber){
        for (slot a: slots) {
            if (a.item == item && !a.ring && !a.weapon && !a.chestPiece && !a.container) {
                if (a.isStackable(item) && a.stackNumber < 20) {
                    ActionDescription.actionText.add(new ActionDescription.actionType("PICKED UP " + item.name(), Color.WHITE));
                    a.stackNumber++;
                    return true;
                }
            }
        }

        for (slot a: slots){
            if (a.item == Items.EMPTY && !a.ring && !a.weapon && !a.chestPiece && !a.container){
                a.item = item;
                a.itemNumber = itemNumber;
                ActionDescription.actionText.add(new ActionDescription.actionType("PICKED UP " + item.name(), Color.WHITE));
                return true;
            }
        }
        return false;
    }

    public static void updateAllEquipables(){

        for (Inventory.slot a : slots) {
            a.updateEquippables();
        }
    }

    public static boolean isWeapon(Items item){
        switch (item) {
            case BASIC_SWORD:
            case DAGGERS:
            case BOW:
            case AXE:
            case STAFF:
                return true;
        }
        return false;
    }

    public static boolean isScroll(Items item){
        switch (item) {
            case scrollOfSwiftness:
                return true;
            case scrollOfTheDeadKing:
                return true;
            case scrollOfRoots:
                return true;
            case scrollOfGhastlySteel:
                return true;
            case scrollOfTheIronHeart:
                return true;
            case scrollOfTheGhostPirate:
                return true;
            case scrollOfTheForgottenShield:
                return true;
            case scrollOfTheFrozenMalice:
                return true;
            case scrollOfFire:
                return true;
            case scrollOfRain:
                return true;
            case scrollOfBloodRain:
                return true;
            case scrollOfVampirism:
                return true;
            case scrollOfTheShadows:
                return true;
        }
        return false;
    }

    public boolean isBook(Items item){
        switch (item){
            case bookOfFire:
            case bookOfCombo:
            case bookOfNature:
            case bookOfLightning:
            case bookOfDexterity:
            case bookOfCourage:
                return true;
        }
        return false;
    }


    boolean hotKeySelect;
    public void inventoryControls(){
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            for (Inventory.slot a : slots) {
                a.getSlot();

            }

        } else {
            for (Inventory.slot a : slots) {
                a.slotTexture.setTexture(slotsTexture);
            } }

        if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !managing){
            for (Inventory.slot a : slots) {
                a.pickUpItem();
                if (a.hotKeySelect) {a.hotKeySelect = false;  Player.canUseHotKeys = true;}
            }
            managing = true;
        }

        if (Gdx.input.justTouched()  && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !managing){
            for (Inventory.slot a : slots) {
                a.useItem();
                if (a.hotKeySelect) {a.hotKeySelect = false;  Player.canUseHotKeys = true;}
            }
        }
        if (Gdx.input.justTouched()  && Gdx.input.isButtonPressed(Input.Buttons.MIDDLE) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !managing){
            for (Inventory.slot a : slots) {
                if (a.hotKeySelect){ a.hotKeySelect = false;  Player.canUseHotKeys = true;}
                a.setHotKeySelect();
            }
        }



        if (managing && !Gdx.input.justTouched()) {clickLetGo = true;}
        if (selectedSprite != null){
            Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            selectedSprite.setPosition((worldCoordinates.x - 16f), (worldCoordinates.y -16f));
            selectedSprite.setScale(UIsize);
            if (selectedItemNumber != -1){
                if (itemAttributes.get(selectedItemNumber).cooldownTimer > 0) {
                    selectedSprite.setColor(Color.DARK_GRAY);
                }
            }
            selectedSprite.draw(PlayingField.spriteBatch);
            selectedSprite.setColor(new Color(1, 1, 1, 1));
            if (selectedItemNumber != -1){
                if (itemAttributes.get(selectedItemNumber).cooldownTimer > 0) {
                    Buttons.inGameText.draw(PlayingField.spriteBatch, (int) (itemAttributes.get(selectedItemNumber).cooldownTimer) + "", worldCoordinates.x, worldCoordinates.y);
                }
            }


            if (selectedItemNumber > -1 && itemAttributes.size() > 0) {
                System.out.println(itemAttributes.get(selectedItemNumber).effectType);
            }
            if (instanceStackNumber > 1){
                Buttons.inGameText.draw(PlayingField.spriteBatch, instanceStackNumber + "" ,(worldCoordinates.x + 0.15f), (worldCoordinates.y - 0.18f));
            }
            if (Gdx.input.justTouched() && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && managing && clickLetGo){
                boolean drop = false;
                for (Inventory.slot a : slots) {
                    if (a.placeItem() || notDroppable) { drop = false; notDroppable = false; break;}
                    drop = true;
                }

                if (drop && (isWeapon(selectedSpriteItem) || isBook(selectedSpriteItem))) {
                    for (Item.interactableObject a : PlayingField.interactableObjects) {
                        if (a.interactableType == Item.interactableObject.InteractableType.ANVIL) {
                            drop = false;
                            if (isWeapon(selectedSpriteItem)) {
                                if ((a.upgradeItemSlot.placeItem() || notDroppable)) {
                                    a.pickUpIsCoolDown = true;
                                    notDroppable = false;
                                    break;
                                } else if (a.putMaterialsSlot.placeItem() || notDroppable) {
                                    a.pickUpIsCoolDown = true;
                                    notDroppable = false;
                                    break;

                                }
                            }
                        } else if (a.interactableType == Item.interactableObject.InteractableType.ENCHANTMENTCIRCLE){
                            if (isWeapon( selectedSpriteItem)) {
                                if ((a.upgradeItemSlot.placeItem() || notDroppable)) {
                                    a.pickUpIsCoolDown = true;
                                    notDroppable = false;
                                    break;
                                }
                            } else if  (isBook(selectedSpriteItem)){
                                if (a.putMaterialsSlot.placeItem() || notDroppable) {
                                a.pickUpIsCoolDown = true;
                                notDroppable = false;
                                break;
                            }}
                        }
                    }
                }


                updateAllEquipables();
                if (drop) {
                        PlayingField.items.add(new Item((int) Player.posX - 1, (int) Player.posY, selectedSpriteItem, selectedItemNumber, false));
                        if (instanceStackNumber > 1) {
                            instanceStackNumber --;
                        } else {
                            selectedSprite = null;
                            selectedSpriteItem = null;
                            managing = false;
                            clickLetGo = false;
                        }
                }
            }

        } else if (managing) {managing = false;}

        }
        public void cancelControls(){

        }




}
