package com.mygdx.game.Tools;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.mygdx.game.Enemies.Enemies;
import com.mygdx.game.Player;
import com.mygdx.game.PlayingField;
import com.mygdx.game.Projectiles;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.ArrayList;
import java.util.List;

public class PlayerSpells {
    public enum Spells{
        ghastlySteel, roots, freeze, ironHeart, deadWarlord, pureSight, swiftness, pirate, forgottenShield, explosionEffect, purpleExplosionEffect, bloodRain, rain, vampirism, shadowState;
    }

    Animation Idle, Move, Attack, animation;
    private TextureRegion anim;
    PointLight light;
    Enemies enemyInstance;
    Spells spell;
    float duration;
        static float savedRadius;
    float initialDuration;
    public float posY;
    float posX;
    float speed, width, height;
    boolean groundVisual;
    private List<GridCell> cellList;
    Vector2 targetVector;


    public PlayerSpells(Spells spell, float duration, float x, float y){
        this.speed = 0.04f;
        this.spell = spell;
        this.duration = duration;
        this.initialDuration = duration;
        this.posY = y;
        this.posX = x;
        switch (spell){
            case ghastlySteel:
                Idle = ToolClass.ghostSwordsIdle;
                Move = ToolClass.ghostSwordsMove;
                Attack = ToolClass.ghostSwordsAttack;
                width = 2; height = 2;
                break;
            case swiftness:

                break;
            case explosionEffect:
                animation = ToolClass.fireballExplosion;
                light = new PointLight(PlayingField.rayHandler, 10, new Color(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1f ), 4f, 0 , 0);
                break;
            case purpleExplosionEffect:
                animation = ToolClass.staffAAExplosion;
                light = new PointLight(PlayingField.rayHandler, 10, new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 1f ), 4f, 0 , 0);
                break;
            case bloodRain:
                animation = ToolClass.bloodRain;
                break;
            case rain:
                animation = ToolClass.rain;
                break;

        }
    }

    public static void spawnGhostSword(int duration){
        Player.activeStatusIcons.add(new Player.activeStatuses(Player.activeStatuses.Status.GHOSTSWORD, duration ));
        Player.playerSpells.add(new PlayerSpells(PlayerSpells.Spells.ghastlySteel, duration, Player.posX, Player.posY));
    }
    public static void ironHeart(int duration){
        Player.addStatus(Player.activeStatuses.Status.IRONHEART, duration);
        Player.effects.add(new Effects(Effects.effectType.STRONGEFFECT, true, duration, 0));
        addSpell(Spells.ironHeart, duration, Player.posX, Player.posY);
    }

    public static void shadowState(int duration){
        Player.addStatus(Player.activeStatuses.Status.SHADOWSTATE, duration);
        addSpell(Spells.shadowState, duration, Player.posX, Player.posY);
    }

    public static void root(int duration, int radius){
        for (int i = 0; i < PlayingField.enemies.size(); i++){
            if (Intersector.overlaps(new Circle(Player.posX, Player.posY, radius), PlayingField.enemies.get(i).hitBox)){
                PlayingField.enemies.get(i).root(duration);
            }
        }
    }
    public static void staminaRush(int duration){
        Player.addStatus(Player.activeStatuses.Status.STAMINARUSH, duration);
        Effects.addOverEffectPlayer(Effects.effectType.STAMINARUSH, true,  duration);
        addSpell(Spells.swiftness, duration, Player.posX, Player.posY);
    }
    public static void bloodRain(int duration, float radius){
        Player.addStatus(Player.activeStatuses.Status.BLOODRAIN, duration);
        Player.playerSpells.add(new PlayerSpells(Spells.bloodRain, duration, Player.posX, Player.posY));
        savedRadius = radius;
    }
    public static void rain(int duration, float radius){
        Player.addStatus(Player.activeStatuses.Status.RAIN, duration);
        Player.playerSpells.add(new PlayerSpells(Spells.rain, duration, Player.posX, Player.posY));
        savedRadius = radius;
    }

    public static void vampirism(int duration, float radius){
        Player.addStatus(Player.activeStatuses.Status.VAMPIRISM, duration);
        addSpell(Spells.vampirism, duration, Player.posX, Player.posY);
        Player.effects.add(new Effects(Effects.effectType.vampirism, true, duration, radius));
        savedRadius = radius;
    }

    public static void freezeArea(int radius, int duration){
        Player.effects.add(new Effects(Effects.effectType.FREEZEAREA, true, 3, radius));
        Circle area = new Circle(0.9199994f + Player.posX,  0.45999983f + Player.posY, (radius / 2) - 0.25f);
        for (Enemies a: PlayingField.enemies){
            if (Intersector.overlaps(area ,a.hitBox)){
                a.chill(duration, 0.5f);
            } }
    }

    public static void fireSpell(){
        Vector3 worldCoordinates = PlayingField.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float diffXd = worldCoordinates.x - 1f -  (Player.posX) ;
        float diffYd = worldCoordinates.y - 1f - (Player.posY) ;
        PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.FIREBALL, Player.posX, Player.posY, 0.2f, 100,(float) Math.atan2(diffYd, diffXd), false, false, 0, 40 ));
    }



    public static void addSpell(Spells spell, float duration, float x, float y){

        boolean alreadyExists = false;
        for (int i = 0; i < Player.playerSpells.size(); i++){
            if (Player.playerSpells.get(i).spell == spell)
            {if (Player.playerSpells.get(i).duration == duration) Player.playerSpells.get(i).resetTimer();
            else {Player.playerSpells.get(i).duration = duration; Player.playerSpells.get(i).resetTimer();}
                alreadyExists = true;
                break;}
        }
        if (!alreadyExists) {Player.playerSpells.add(new PlayerSpells(spell, duration, x, y));}
    }

    public void resetTimer(){
        duration = initialDuration;
    }

    void PathingEnemy() {
        float diffX = enemyInstance.posX - posX;
        float diffY = enemyInstance.posY - posY;

        float angle = (float) Math.atan2(diffY, diffX);

        posX += speed * Math.cos(angle) + (speed / 2 * deviationX);
        posY += speed * Math.sin(angle) + (speed / 2 * deviationY);
    }
    void PathingPlayer() {
        float diffX = Player.posX - posX;
        float diffY = Player.posY - posY;

        float angle = (float) Math.atan2(diffY, diffX);

        posX += speed * Math.cos(angle) + (speed / 2 * deviationX);
        posY += speed * Math.sin(angle) + (speed / 2 * deviationY);
    }

    void updateEnemyPos(){
        for (int i = 0; i < PlayingField.enemies.size(); i++) {
            if (PlayingField.enemies.get(i) == enemyInstance){
                enemyInstance = PlayingField.enemies.get(i);
                break;
            }
        }
    }
    void damageTarget(int damage){
        for (int i = 0; i < PlayingField.enemies.size(); i++) {
            if (PlayingField.enemies.get(i) == enemyInstance){
                PlayingField.enemies.get(i).takingDamage(damage, false, Color.WHITE);
                break;
            }
        }
    }

    public static boolean shadowState;
    public void render(){
        duration -= Gdx.graphics.getDeltaTime();

        switch (spell){
            case ghastlySteel:
                ghastlySteel();
                break;
            case roots: break;
            case freeze:
                remove();
                break;
            case pirate:
                pirate();
                break;
            case ironHeart:
                Player.damageReduction2 = 0.7f;
                break;
            case pureSight:

                break;
            case swiftness:
                staminaRush();
                break;
            case purpleExplosionEffect:
            case explosionEffect:
                explosionEffect();
                break;
            case deadWarlord: break;
            case forgottenShield: break;
            case bloodRain:
                bloodRain();
                break;
            case rain:
                rain();
                break;
            case vampirism:
                vampirism();
                break;
            case shadowState:
                shadowState = true;
                break;
        }

        if (duration <= 0) {remove();}
    }

    boolean disposed;
    public void remove(){
        switch (spell){
            case ironHeart:
                Player.damageReduction2 = 0f;
                break;
            case shadowState:
                shadowState = false;
                break;}
        if (!disposed && light != null) {
            light.setActive(false);
            light.dispose();
            disposed = true;
        }
        Player.playerSpells.remove(this);
    }

    boolean targetAquired, attack;
    float timer;
    void staminaRush(){
        if (tickTimer(1f)){
            Player.refillStamina(15);
        }
    }

    void vampirism(){
        if (tickTimer(1f)){
            Rectangle area = new Rectangle(Player.posX - savedRadius / 3.25f, Player.posY - savedRadius / 4f, savedRadius, savedRadius / 1.75f);

            int enemyCount = 0;
            for (Enemies a: PlayingField.enemies){
                if (Intersector.overlaps(area ,a.hitBox)){
                    enemyCount ++;
                    a.takingDamage(10, false, Color.RED);
                }
            }
            Player.addHealth((enemyCount * 10));
        }
    }

    void bloodRain(){
        timer += Gdx.graphics.getDeltaTime();
        anim = animation.getKeyFrame(timer, false);
        PlayingField.spriteBatch.draw(anim, posX - 4, posY - 2, 8, 8);
        if (tickTimer(1.5f)){
            Circle area = new Circle(posX , posY + 1, (savedRadius / 2) - 0.25f);
            for (Enemies a: PlayingField.enemies){
                if (Intersector.overlaps(area ,a.hitBox)){
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
                } }
        }

        if (timer >= animation.getAnimationDuration()){
            remove();
        }
    }

    void rain(){
        timer += Gdx.graphics.getDeltaTime();
        anim = animation.getKeyFrame(timer, false);
        PlayingField.spriteBatch.draw(anim, posX - 4, posY - 2, 8, 8);
        if (tickTimer(1f)){
            Circle area = new Circle(posX , posY + 1, (savedRadius / 2) - 0.25f);
            for (Enemies a: PlayingField.enemies){
                if (Intersector.overlaps(area ,a.hitBox)){
                    a.soak(5);
                } }
        }

        if (timer >= animation.getAnimationDuration()){
            remove();
        }
    }

    void explosionEffect(){
        timer += Gdx.graphics.getDeltaTime();
        anim = animation.getKeyFrame(timer, false);
        light.setPosition(posX + 1.5f, posY + 2f);
        light.setDistance(light.getDistance() + timer);
        light.setColor(new Color(light.getColor().r, light.getColor().g, light.getColor().b, 1 - timer * 2));
        if (spell != Spells.purpleExplosionEffect)
        PlayingField.spriteBatch.draw(anim, posX, posY, 5, 5);
        else
            PlayingField.spriteBatch.draw(anim, posX + 1, posY + 1.5f, 2.5f, 2.5f);
        if (timer >= animation.getAnimationDuration()){
            remove();
        }

    }

    void pirate(){

    }



    void ghastlySteel(){
        timer += Gdx.graphics.getDeltaTime();
        System.out.println(targetAquired);
        if (!targetAquired) {
            float distanceFromSwords = 99999;
            for (int i = 0; i < PlayingField.enemies.size(); i++) {
                if (Intersector.overlaps(new Circle(Player.posX, Player.posY, 7), PlayingField.enemies.get(i).hitBox)){
                    if (scanForWallbetweenTarget(PlayingField.enemies.get(i).posX, PlayingField.enemies.get(i).posY)) {
                        float diffX = PlayingField.enemies.get(i).posX - posX;
                        float diffY = PlayingField.enemies.get(i).posY - posY;
                        float distance = (float) (Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));
                        if (distanceFromSwords > distance) {
                            distanceFromSwords = distance;
                            targetVector = new Vector2(PlayingField.enemies.get(i).posX, PlayingField.enemies.get(i).posY);
                            targetAquired = true;
                            enemyInstance = PlayingField.enemies.get(i);
                        }
                    }
                }
            }
            deviatePosFrom(Spells.ghastlySteel);
            if (!(Intersector.overlaps(new Circle(posX + 1f, posY + 0.5f, 0.5f), Player.hitbox)) || (deviationX > 0 || deviationY > 0)){
                follow(true);
                Idle();
            } else { Idle(); }
        } else {
            updateEnemyPos();
            if (Intersector.overlaps(new Circle(posX, posY, 0.5f), new Circle(enemyInstance.posX, enemyInstance.posY, 0.1f))) {
                if (!attack) {timer = 0;}
                attack = true;
               }
            else {
                if (!Intersector.overlaps(new Circle(posX, posY, 1f), new Circle(enemyInstance.posX, enemyInstance.posY, 0.1f))){
                attack = false;}
                follow(false);
            }
            if (!attack){ follow(false);
                Idle();}
            else {
                anim = Attack.getKeyFrame(timer, true);
                PlayingField.spriteBatch.draw(anim, posX, posY, width, height);
                if (timer > 0.5f && !tick) {damageTarget(15); tick = true;}
                else if (timer >= Attack.getAnimationDuration()) {tick = false; timer = 0;} } }
        if (targetAquired) {if (enemyInstance.currentHP <= 0 || !Intersector.overlaps(new Circle(Player.posX, Player.posY, 7),
                new Circle(posX + 1f, posY + 0.5f, 0.5f))) {targetAquired = false; tick = false;}}
    }

    boolean tick; float tickTimer;
    boolean tickTimer(float interval){
        if (!tick){ tickTimer = interval;  tick = true;}
        else if (tickTimer <= 0) {tick = false; return true;}
        else {tickTimer -= Gdx.graphics.getDeltaTime();}
        return false;
    }

    public void freeze(int radius){
        Circle area = new Circle(0.9199994f + Player.posX,  0.45999983f + Player.posY, radius);
        for (Enemies a: PlayingField.enemies){
            if (Intersector.overlaps(area ,a.hitBox)){
                a.freeze(5);
            } }
        anim = Move.getKeyFrame(timer, true);
        remove();
    }

    private void Idle(){
        anim = Idle.getKeyFrame(timer, true);
        PlayingField.spriteBatch.draw(anim, posX, posY, width, height);
    }

    public void deviatePosFrom(Spells spell ){
        deviationX = 0; deviationY = 0;
        for (int i = 0; i < Player.playerSpells.size(); i++) {
            if (Player.playerSpells.get(i) != this && Player.playerSpells.get(i).spell == spell) {
                if (Intersector.overlaps(new Circle(Player.playerSpells.get(i).posX, Player.playerSpells.get(i).posY, 0.25f), new Circle(posX, posY, 0.25f) )) {
                    float diffXDeviation = Player.playerSpells.get(i).posX - posX;
                    float diffYDeviation = Player.playerSpells.get(i).posY - posY;

                    float angleDeviation = (float) Math.atan2(diffYDeviation, diffXDeviation);
                    deviationX = (float) -Math.cos(angleDeviation);
                    deviationY = (float) -Math.sin(angleDeviation);
                }
            }
        }
    }

    float deviationX = 0, deviationY = 0;
    private void follow(boolean followPlayer) {
            if (followPlayer) {PathingPlayer();}
            else {
                PathingEnemy();
/*
                if (cellList != null) {
                    if (cellList.size() >= 1) {

                        float diffX = (cellList.get(0).x) - posX;
                        float diffY = (cellList.get(0).y) - posY;

                        float angle = (float) Math.atan2(diffY, diffX);

                        posX += speed * Math.cos(angle) + (speed / 2 * deviationX);
                        posY += speed * Math.sin(angle) + (speed / 2 * deviationY);
                    }
                }

 */
            }

    }
    private float X, Y;
    boolean scanForWallbetweenTarget(float x, float y){
        X = Player.posX - 0.5f;
        Y = Player.posY;
        int breakTimer = 0;
        ArrayList<Vector2> pastValues = new ArrayList<>();
        while (true) {
            breakTimer += Gdx.graphics.getDeltaTime();
            // System.out.println("scanning");
            float diffXd = x - X;
            float diffYd = y - Y;
            for (int i = 0; i < pastValues.size(); i++){
                if (pastValues.get(i).x == diffXd && pastValues.get(i).y == diffYd) return false;
            }
            pastValues.add(new Vector2(diffXd, diffYd));
            float angled = (float) Math.atan2(diffYd, diffXd);


            X += 0.1f * Math.cos(angled);
            Y += 0.1f * Math.sin(angled);
            if ((int) (X) == (int) x && (int) Y == (int) y) {
                X = posX - 0.5f;
                Y = posY;
                pastValues.clear();
                return true;
            }
            if (!Tiles.collisionChecker(X, Y)) {
                X = posX - 0.5f;
                Y = posY;
                pastValues.clear();
                return false;
            }
            if (!Tiles.miscDoorCollisionChecker(X, Y, new Rectangle(posX, posY, 0.2f, 0.2f))) {
                X = posX - 0.5f;
                Y = posY;
                pastValues.clear();
                return false;
            }
            if (breakTimer > 1){
                pastValues.clear();
                return false;
            }
        }
    }


}
