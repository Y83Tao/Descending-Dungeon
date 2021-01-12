package com.mygdx.game.Enemies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.mygdx.game.*;
import com.mygdx.game.FileData.FloorManager;
import com.mygdx.game.Tools.Effects;
import com.mygdx.game.Tools.HitNumbers;
import com.mygdx.game.Tools.Tiles;
import com.mygdx.game.Tools.ToolClass;
import com.mygdx.game.UserInterface.ActionDescription;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemies extends ApplicationAdapter {

    public float posX, posY;
    public String name;
    Animation[] animations;
    private Animation animation;
    float[][] animationSize;
    public ArrayList<Effects> enemyEffects = new ArrayList<>();
    private TextureRegion enemy;
    public int currentHP, maxHealth, heldExperience, animationSizeInt, healthLostInt;
    public float speed, healthBarYOffset, initialSpeed;
    private boolean directionX;
    private boolean attack;
    private boolean canAttack;
    public boolean detectedPlayer;
    private boolean damage;
    private boolean stunned;
    private boolean hit;
    private boolean scan;
    private boolean critHit;
    private boolean canStun = true; //direction right is true, left is false;

    String attackType, directionString;

    private List<GridCell> cellList;

    private float walkTime, attackTime, idleTime, stunnedTime, deathTime, actionTimer, hitPoints;
    public float staggerDamageIntervals, distanceFromPlayer, actionIntervals;
    int damagePoints, damageDealing;

    private ShapeRenderer areaTool;
    private float X, Y;
    Texture healthFrame, healthActive, healthLost;
    NinePatch healthBar, healthBarLost;
    ShaderProgram shader, frozenShader, chillShader;
    public Rectangle hitBox;
    boolean hitBoxDebug;

    boolean impaired, frozen, wet, chill;
    Color colorChange;
    private float radiusDetect, radiusLoseDetection, disableMove, disableAttack,
            radiusDetectTimer, radiusLostDetectTimer, colorChangeTimer, slowDuration,
            vulnerableAmount, vulnerableTimer;

    Enemies(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;

        walkTime = 0f;
        idleTime = 0f;
        speed = 0.025f;
        directionString = "Right";

        disableMove = 0f;
        disableAttack = 0f;
        radiusDetect = 7f;
        radiusLoseDetection = 16f;
        colorChange = null;

        directionX = true;
        areaTool = new ShapeRenderer();

        healthFrame = new Texture("Hud/HudFrame.png");
        healthFrame.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        healthActive = new Texture("Hud/HealthActive.png");
        healthLost = new Texture("Hud/HealthLost.png");
        healthBar = new NinePatch(healthActive);
        healthBarLost = new NinePatch(new Texture("Hud/enemyHealthLost.png"));
        detected = ToolClass.detected;
        questionMark = ToolClass.questionMark;
        String vertexShader = "attribute vec4 " +   ShaderProgram.POSITION_ATTRIBUTE  +  ";\n" +  //
        "attribute vec4 " +   ShaderProgram.COLOR_ATTRIBUTE  +  ";\n" + //
        "attribute vec2 "  +  ShaderProgram.TEXCOORD_ATTRIBUTE +   "0;\n" + //
        "uniform mat4 u_projTrans;\n"+  //
        "varying vec4 v_color;\n" + //
        "varying vec2 v_texCoords;\n"+  //
        "\n" + //
        "void main()\n" + //
        "{\n"+  //
        "   v_color = " +   ShaderProgram.COLOR_ATTRIBUTE +   ";\n"+  //
        "   v_texCoords = " +   ShaderProgram.TEXCOORD_ATTRIBUTE +   "0;\n"+  //
        "   gl_Position =  u_projTrans * " +   ShaderProgram.POSITION_ATTRIBUTE  +  ";\n" + //
        "}\n";

        String fragmentShader = "#ifdef GL_ES\n" + //
        "#define LOWP lowp\n" +  //
        "precision mediump float;\n" + //
        "#else\n" + //
        "#define LOWP \n" + //
        "#endif\n" + //
        "varying LOWP vec4 v_color;\n" + //
        "varying vec2 v_texCoords;\n" + //
        "uniform sampler2D u_texture;\n"+  //
        "void main()\n"+ //
        "{\n" + //
        "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords).a;\n" + //
        "}";
        String frozenFragmentShader =
                "#ifdef GL_ES\n" + //
                        "#define LOWP lowp\n" +  //
                        "precision mediump float;\n" + //
                        "#else\n" + //
                        "#define LOWP \n" + //
                        "#endif\n" + //
                        "varying LOWP vec4 v_color;\n" + //
                        "varying vec2 v_texCoords;\n" + //
                        "uniform sampler2D u_texture;\n"+  //
                "void main()\n"+ //
                "{\n" + //
                "  gl_FragColor = texture2D(u_texture, v_texCoords);\n   " +
                "  gl_FragColor.rgb = mix(gl_FragColor.rgb, vec3(0.5,0.9,1), 0.75);\n" + //
                "}";
        String chillFragmentShader =
                "#ifdef GL_ES\n" + //
                        "#define LOWP lowp\n" +  //
                        "precision mediump float;\n" + //
                        "#else\n" + //
                        "#define LOWP \n" + //
                        "#endif\n" + //
                        "varying LOWP vec4 v_color;\n" + //
                        "varying vec2 v_texCoords;\n" + //
                        "uniform sampler2D u_texture;\n"+  //
                        "void main()\n"+ //
                        "{\n" + //
                        "  gl_FragColor = texture2D(u_texture, v_texCoords);\n   " +
                        "  gl_FragColor.rgb = mix(gl_FragColor.rgb, vec3(0.5,0.9,0.5), 0.3);\n" + //
                        "}";

       shader = new ShaderProgram(vertexShader, fragmentShader);
       chillShader = new ShaderProgram(vertexShader, chillFragmentShader);
       frozenShader = new ShaderProgram(vertexShader, frozenFragmentShader);
       hitBox = new Rectangle();
        healthLostInt = currentHP;
    }

    void Pathing() {
        cellList = Tiles.finder.findPath((int) posX, (int) posY, (int) Player.posX, (int) Player.posY, Tiles.pathGrid);
    }

    void directions() {   //FIX DIRECTIONAL DAMAGE//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   //     float positionX = posX + 0.5f;
        if (posX < Player.posX) {
            directionString = "RIGHT";
            directionX = true;
        } else {
            directionString = "LEFT";
            directionX = false;
        }
    }

     boolean scanForWallBetweenPlayerAndEnemy(){
        X = posX - 0.5f;
        Y = posY;
        int breakTimer = 0;
        ArrayList<Vector2> pastValues = new ArrayList<>();
        while (true) {
            breakTimer += Gdx.graphics.getDeltaTime();
           // System.out.println("scanning");
            float diffXd = (Player.posX) - X;
            float diffYd = (Player.posY) - Y;
            for (int i = 0; i < pastValues.size(); i++){
                if (pastValues.get(i).x == diffXd && pastValues.get(i).y == diffYd) return false;
            }
            pastValues.add(new Vector2(diffXd, diffYd));
            float angled = (float) Math.atan2(diffYd, diffXd);


            X += 0.1f * Math.cos(angled);
            Y += 0.1f * Math.sin(angled);
            if ((int) (X) == (int) Player.posX && (int) Y == (int) Player.posY) {
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
            if (!Tiles.miscDoorCollisionChecker(X, Y, hitBox)) {
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

    float questionMarkTimer = 0, detectedTimer = 0;
    Animation questionMark, detected;
    public void areaToolDisplay(boolean debug) {
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        areaTool.setProjectionMatrix(PlayingField.camera.combined);

        areaTool.begin(ShapeRenderer.ShapeType.Filled);
        ////////////////detection Range
        if (!Player.isInvisible) {
            if (Intersector.overlaps(new Circle (posX, posY, radiusDetect), Player.hitbox) && !detectedPlayer) {
                if (debug) {
                    areaTool.setColor(new Color(0, 0, 1, 0.35f));
                }
                if (radiusDetect > 500) detectedPlayer = true;
                else
                detectedPlayer = scanForWallBetweenPlayerAndEnemy();
            } else {
                if (debug) {
                    areaTool.setColor(new Color(0, 0, 1, 0.2f));
                }
            }
            if (debug) {
                areaTool.circle(posX + 0.5f, posY, 7, 10);
            }
        } else {
            if (Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(2f, 2) && !detectedPlayer) {
                detectedPlayer = scanForWallBetweenPlayerAndEnemy();
            }
            else if (Intersector.overlaps(new Circle (posX, posY, radiusDetect), Player.hitbox) && !detectedPlayer) {
                questionMarkTimer += Gdx.graphics.getDeltaTime();
                PlayingField.spriteBatch.draw( questionMark.getKeyFrame(questionMarkTimer, false), posX, posY + 2f, 1, 1);
                detectedTimer = 0;
            } else{
                questionMarkTimer = 0;
            }
            if (!(Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(4f, 2))) {
                detectedPlayer = false;
            }
        }

        if (Player.isInvisible && detectedPlayer){
            detectedTimer += Gdx.graphics.getDeltaTime();
            PlayingField.spriteBatch.draw( detected.getKeyFrame(detectedTimer, false), posX, posY + 2f, 1, 1);
            detectedPlayer = true;
            questionMarkTimer = 0;
        }



        //////////////////loseDetection Range
        if (Intersector.overlaps(new Circle (posX, posY, radiusLoseDetection), Player.hitbox)) {
            if (debug) {
                areaTool.setColor(new Color(1, 1, 0, 0.2f));
            }
        } else {
            if (debug) {
                areaTool.setColor(new Color(1, 1, 0, 0.1f));
            }
            if (radiusDetect > 500) detectedPlayer = true;
            else {
            detectedPlayer = false;}
        }
        if (debug) {
            areaTool.circle(posX + 0.5f, posY, 16, 10);
        }
        areaTool.setColor(1, 1, 1, 0.5f);
        if (debug) {
            if (cellList != null) {
                if (cellList.size() > 1) {
                    for (GridCell a : cellList) {
                        areaTool.circle(a.x + 1, a.y, 0.2f);
                    }
                }
            }
        }
        areaTool.end();
    }

    public Boolean CircleRectCollision(float circleX, float circleY, float radius)
    {
        if (Intersector.overlaps(new Circle(circleX, circleY, radius), Player.hitbox)){
            Player.insideHitBox = true;

            if (PlayingField.debugHitbox) {
                PlayingField.shapeRenderer.begin();
                PlayingField.shapeRenderer.setColor(Color.RED);
                PlayingField.shapeRenderer.circle(circleX, circleY, radius, 30);
                PlayingField.shapeRenderer.end();
                PlayingField.shapeRenderer.setColor(Color.WHITE);
            }
            return true;
        } else {
            if (PlayingField.debugHitbox) {
                PlayingField.shapeRenderer.begin();
                PlayingField.shapeRenderer.circle(circleX, circleY, radius, 30);
                PlayingField.shapeRenderer.end();
            }
            for (WorldObjects a : PlayingField.worldObjects) {
                if (!a.destroyed && Intersector.overlaps(new Circle(circleX, circleY, radius), a.collisionBox)) a.destroyObject = true;
            }

            return false;
        }

    }



    public void hitbox(float circleX, float circleY, float radius) {
        //attackBoxMelee
        if (attackType.equals("Melee")) {
            switch (directionString) {
                case "RIGHT":
                        if (CircleRectCollision(posX + 0.5f + circleX, posY + circleY, radius))
                        Player.takeDamage(damageDealing);
                    break;
                case "LEFT":
                        if (CircleRectCollision(posX + 0.5f - circleX, posY + circleY, radius))
                        Player.takeDamage(damageDealing);
                    break;
            }
        }
    }


    public void stats() {
        float health =  (float) this.currentHP / maxHealth * 1.475f;
        if (health < 0) {
            health = 0;
        }

        if (healthLostInt < currentHP){
            healthLostInt ++;
        } else if (healthLostInt > currentHP){
            healthLostInt --;
        } else {healthLostInt = currentHP;}
        float healthLost =  (float) healthLostInt / maxHealth * 1.475f;

        if (health > 0 && !name.equals("DarkGuardian")) {
            healthBarLost.draw(PlayingField.spriteBatch, posX - 0.25f, posY + 1.5f + healthBarYOffset, healthLost, 0.15f);
            healthBar.draw(PlayingField.spriteBatch, posX - 0.25f, posY + 1.5f + healthBarYOffset, health, 0.15f);
            PlayingField.spriteBatch.draw(healthFrame, posX - 0.25f, posY + 1.5f + healthBarYOffset, 1.5f, 0.15f);
        }
        if (frozen) PlayingField.spriteBatch.setShader(frozenShader);
        else if (chill) PlayingField.spriteBatch.setShader(chillShader);
        if (hit) {
            hitPoints += Gdx.graphics.getDeltaTime();

            if (hitPoints <= 0.15) {
                PlayingField.spriteBatch.setColor(Color.RED.r, Color.RED.g, Color.RED.b, 1);
                PlayingField.spriteBatch.setShader(shader);}

            if (hitPoints > 5) {
                hit = false;
                critHit = false;
                hitPoints = 0;
            }
        }
        Buttons.inGameText.setColor(1, 1, 1, 1);
    }

    public void takingDamage(int damagePoints, boolean critHit, Color overrideColor) {
        System.out.println("VULNERABLE" + vulnerableAmount);
        int bonusDmg = 0;
        if (vulnerableAmount != 0f) { bonusDmg = (int) (damagePoints * vulnerableAmount);}

        this.damagePoints = damagePoints + bonusDmg;
        this.critHit = critHit;
        currentHP -= damagePoints + bonusDmg;
        if (bonusDmg > 0) PlayingField.hitNumbers.add(new HitNumbers(bonusDmg, posX, posY, critHit, damagePoints + "  (+" + bonusDmg + ")", Color.GOLD));
        else if (overrideColor != null) {PlayingField.hitNumbers.add(new HitNumbers(damagePoints, posX, posY, critHit, "", overrideColor));}
        else {PlayingField.hitNumbers.add(new HitNumbers(damagePoints, posX, posY, critHit, "", null));}

        if (currentHP <= (maxHealth - staggerDamageIntervals) && canStun) {
            stunned = true;
            stunnedTime = 0;
            staggerDamageIntervals += staggerDamageIntervals;
        }
        hit = true;
        hitPoints = 0;
        detectedPlayer = true;
    }

    public static float appleX, appleY, size = 0.5f;

    @Override
    public void render() {
     //   hitbox(appleX, appleY, size);
        hitBox.set(posX, posY, hitBox.width, hitBox.height);
       System.out.println(" X = " + appleX + "\n" +
               " Y = " + appleY + "\n" +
             " Size = " + size + "\n"
      );
        animationSizeInt = 1;
        animation = animations[animationSizeInt];
        float diffX = Player.posX + 0.35f - posX;
        float diffY = Player.posY - posY;

        distanceFromPlayer = (float) (Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));

        if (impaired) {enemyImpairment();}

        if (disableAttack > 0) {canAttack = false; attack = false;}
        if (!frozen) {
            switch (name) {
                case ("skeletonBrawler"):
                    skeletonBrawler();
                    break;
                case ("skeletonSharpShooter"):
                    skeletonSharpShooter();
                    break;
                case ("skeletonKnight"):
                    skeletonKnight();
                    break;
                case ("dungeonMauler"):
                    dungeonMauler();
                    break;
                case ("angelicaMinion"):
                    angelicaMinion();
                    break;
                case ("DarkGuardian"):
                    DarkGuardian();
                    break;
                case ("Spirit"):
                    Spirit();
                    break;
            }
        }
        if (currentHP <= 0){ deathAnimation();}
        PlayingField.spriteBatch.begin();
        areaToolDisplay(false);
        stats();

        float hXoff = animationSize[animationSizeInt][2];

        float yXoff = animationSize[animationSizeInt][3];


        if (colorChange != null) {PlayingField.spriteBatch.setColor(colorChange.r, colorChange.g, colorChange.b, 1f);}
        PlayingField.spriteBatch.draw(enemy, !directionX ? posX + 1f - hXoff : posX + hXoff, posY + yXoff, 0, 0, animationSize[animationSizeInt][0], animationSize[animationSizeInt][1], directionX ? 1f :  -1f, 1f, 0);
        PlayingField.spriteBatch.setColor(1, 1, 1, 1f);
        PlayingField.spriteBatch.setShader(null);
        float yOffset = 0;
        if (enemyEffects.size() > 0) {
            for (int i = 0; i < enemyEffects.size(); i++) {
                float yOffsetApplied = 0;
                if (enemyEffects.get(i).isDebuff) {yOffsetApplied = yOffset;}
                if (!enemyEffects.get(i).render(posX, posY + yOffsetApplied)) enemyEffects.remove(i);
                else {
                    if (enemyEffects.get(i).isDebuff) {yOffset -= 1;}
                    Vector3 statChange = enemyEffects.get(i).applyEffects(posX, posY);
                    if (statChange.x > 0){
                        takingDamage((int) statChange.x, false, enemyEffects.get(i).colorType());
                    }
                }
            }
        }
        PlayingField.spriteBatch.end();



    }

    public void enemyImpairment(){
       // System.out.println(wet);

        PlayingField.spriteBatch.begin();
        if (disableMove > 0 || slowDuration > 0){
            String text = "";

            disableMove -= Gdx.graphics.getDeltaTime();

            if (frozen){ text = "Frozen";}
            else if (disableMove > 0) {Buttons.inGameText2.setColor(Color.BROWN);text = "Rooted";}
            else if (wet) {text = "Soaked";}
            else if (chill) {text = " Chilled";}
            Buttons.inGameText2.getData().setScale(0.0035f);
            Buttons.inGameText2.draw(PlayingField.spriteBatch, text, posX - 0.035f, posY + 2.15f + healthBarYOffset);
            Buttons.inGameText2.getData().setScale(0.0065f);
            Buttons.inGameText2.setColor(1, 1, 1, 1);
        } else {chill = false; wet = false;}

        if (disableMove > 0) {
            disableMove -= Gdx.graphics.getDeltaTime();

            if (frozen){ PlayingField.spriteBatch.setShader(frozenShader);}
            else {PlayingField.spriteBatch.setColor(Color.BROWN);}

            float stunDuration =  (float) (disableMove/ disableMoveMax) * 1.475f ;
            healthBar.draw(PlayingField.spriteBatch, posX - 0.25f, posY + 1.5f + healthBarYOffset + 0.25f, stunDuration, 0.065f);
        } else { disableMove = 0; frozen = false;}

        PlayingField.spriteBatch.end();
        PlayingField.spriteBatch.setShader(null);
        PlayingField.spriteBatch.setColor(1, 1, 1, 1);

        if (vulnerableTimer > 0) {
            vulnerableTimer -= Gdx.graphics.getDeltaTime();
        } else { vulnerableTimer = 0; vulnerableAmount = 0;}
        if (disableAttack > 0) {
            disableAttack -= Gdx.graphics.getDeltaTime();
        } else { disableAttack = 0; }
        if (radiusDetectTimer > 0){
            radiusDetectTimer -= Gdx.graphics.getDeltaTime();
        } else {radiusDetectTimer = 0; radiusDetect = 7;}
        if (radiusLostDetectTimer > 0){
            radiusLostDetectTimer -= Gdx.graphics.getDeltaTime();
        } else {radiusLostDetectTimer = 0; radiusLoseDetection = 16;}
        if (colorChangeTimer > 0){
            colorChangeTimer -= Gdx.graphics.getDeltaTime();
        } else {
            colorChangeTimer = 0;
            colorChange = null; }
        if (slowDuration > 0){
            slowDuration -= Gdx.graphics.getDeltaTime();
        } else {slowDuration = 0; speed = initialSpeed; chill = false; wet = false;}

        if (disableMove <= 0 && disableAttack <= 0 && radiusDetectTimer <= 0 &&
                radiusLostDetectTimer <= 0 && colorChangeTimer <= 0 && slowDuration <= 0 && vulnerableTimer <= 0) {
            chill = false; wet = false;
            disableMove = 0; frozen = false; disableAttack = 0; radiusDetectTimer = 0; radiusLostDetectTimer = 0;
            radiusDetect = 7; radiusLoseDetection = 16;
            colorChangeTimer = 0; slowDuration = 0; speed = initialSpeed; colorChange = null;
            vulnerableAmount = 0; vulnerableTimer = 0;

            impaired = false;
        }
    }
    private float disableMoveMax;
    public void freeze(float disableMove){
        if (disableMove > 0) {
            wet = false;
            chill = false;;
            this.disableMoveMax = disableMove;
            this.disableMove = disableMove;
            this.slowDuration = 0;
            speed = initialSpeed;
            this.frozen = true; impaired = true;}
    }

    public void soak(float disableMove){
        if (!frozen && !wet && !chill){
            wet = true;
            slow(1, disableMove);
        }else if (!frozen && chill){
            freeze(disableMove);
        }
    }
    public void chill(float disableMove, float slowAmount){
        if (!frozen && !wet && !chill){
            chill = true;
            slow(slowAmount, disableMove);
        }else if (!frozen && (wet || chill)){
            freeze(disableMove);
        }
    }

    public void root(float disableMove){
        if (!frozen) {
            if (disableMove > 0) {
                enemyEffects.add(new Effects(Effects.effectType.ROOTS, true, disableMove - 2.5f, 0));
                this.disableMoveMax = disableMove;
                this.disableMove = disableMove;
                impaired = true;
            }
        }
    }

    public void stun(float disableMove){
        if (disableMove > 0) {
            this.disableMove = disableMove;
            this.disableAttack = disableMove;
            for (int i = 0; i < enemyEffects.size(); i++) { if (enemyEffects.get(i).type == Effects.effectType.STUNNED) {enemyEffects.remove(enemyEffects.get(i));}}
            enemyEffects.add(new Effects(Effects.effectType.STUNNED, true, disableMove,0));
            impaired = true;}
    }

    public void tempSetDetectionRadius(float detectRadius, float detectTimer){
        this.radiusDetect = detectRadius;
        this.radiusDetectTimer = detectTimer;
        this.impaired = true;
    }
    public void tempSetLoseDetectionRadius(float detectLostThreshold, float radiusLostDetectTimer){
        this.radiusDetect = detectLostThreshold;
        this.radiusLostDetectTimer = radiusLostDetectTimer;
        impaired = true;
    }
    public void slow(float slowAmount, float slowDuration){
        this.speed = speed * slowAmount;
        this.slowDuration = slowDuration;
        impaired = true;
    }
    public void changeColor(Color color, float colorChangeTimer){
        colorChange = color;
        this.colorChangeTimer = colorChangeTimer;
        impaired = true;
    }
    public void disarm(float disarmTimer){
        this.disableAttack = disarmTimer;
        for (int i = 0; i < enemyEffects.size(); i++) { if (enemyEffects.get(i).type == Effects.effectType.DISARM) {enemyEffects.remove(enemyEffects.get(i));}}
        enemyEffects.add(new Effects(Effects.effectType.DISARM, true, disableAttack,0));
        impaired = true;
    }
    public void vulnerable(float vulnerableAmount, float vulnerableTimer){
        this.vulnerableAmount = vulnerableAmount;
        this.vulnerableTimer = vulnerableTimer;
        for (int i = 0; i < enemyEffects.size(); i++) { if (enemyEffects.get(i).type == Effects.effectType.VULNERABLE) {enemyEffects.remove(enemyEffects.get(i));}}
        enemyEffects.add(new Effects(Effects.effectType.VULNERABLE, true, vulnerableTimer,0));
        impaired = true;
    }

    @Override
    public void dispose() {
        healthFrame.dispose();
        healthLost.dispose();
        healthActive.dispose();
        if (areaTool != null) areaTool.dispose();
        /*
        if (enemy != null )enemy.getTexture().dispose();
        if (animation != null) {
            for (int i = 0; i < animation.getKeyFrames().length; i++) {
                animation.getKeyFrames()[i].getTexture().dispose();
            }
        }

         */
    }


    //////////////////////////////////////////////////ENEMY BEHAVIOURS///////////////////////////////////////////////////////////////////////////////////////////////////////////

    float deltaTimer(){
        return Gdx.graphics.getDeltaTime();
    }


    private void follow() {
        if (disableMove <= 0) {
            Pathing();
            //System.out.println(cellList); //////DEBUG
            float deviationX = 0, deviationY = 0;
            for (int i = 0; i < PlayingField.enemies.size(); i++) { ///////////////////////////DEVIATING PATHING
                if (PlayingField.enemies.get(i) != this) {
                    if (Intersector.overlaps(PlayingField.enemies.get(i).hitBox, this.hitBox)) {
                        //  System.out.println("OVERLAPPED " + i);

                        float diffXDeviation = PlayingField.enemies.get(i).posX - posX;
                        float diffYDeviation = PlayingField.enemies.get(i).posY - posY;

                        float angleDeviation = (float) Math.atan2(diffYDeviation, diffXDeviation);
                        deviationX = (float) -Math.cos(angleDeviation);
                        deviationY = (float) -Math.sin(angleDeviation);
                    }
                }
            }


            if (cellList != null) {
                if (cellList.size() >= 1) {

                    float diffX = (cellList.get(0).x + .95f) - posX;
                    float diffY = (cellList.get(0).y + .5f) - posY;

                    float angle = (float) Math.atan2(diffY, diffX);

                    if (Tiles.miscObjectCollisionChecker(hitBox)) {
                        posX += speed * Math.cos(angle) + (speed / 2 * deviationX);
                        posY += speed * Math.sin(angle) + (speed / 2 * deviationY);
                        for (int i = 0; i < PlayingField.doors.size(); i++) {
                            if (!PlayingField.doors.get(i).open) {
                                if (Intersector.overlaps(PlayingField.doors.get(i).collisionBox, hitBox)) {
                                    PlayingField.doors.get(i).destroyed = true;
                                }
                            }
                        }
                    } else {
                        posX += speed / 2 * Math.cos(angle) + (speed / 3 * deviationX);
                        posY += speed / 2 * Math.sin(angle) + (speed / 3 * deviationY);
                    }
                }
            }
        }
    }
    boolean fleeingSpot;
    private Vector2 bestFleeingSpot = new Vector2();
    void fleeing(int radius){
        if (disableMove <= 0) {
            if (!fleeingSpot) {
                float bestDistance = 0;
                for (int x = 0; x < radius; x++) {
                    for (int y = 0; y < radius; y++) {
                        int xPlace = (int) (posX - (radius / 2)) + x;
                        int yPlace = (int) (posY - (radius / 2)) + y;
                        if (Tiles.collisionChecker(xPlace, yPlace)) {
                            float diffX = Player.posX + 0.35f - xPlace;
                            float diffY = Player.posY - yPlace;
                            float distance = (float) (Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));
                            if (distance > bestDistance) {
                                bestFleeingSpot.x = xPlace;
                                bestFleeingSpot.y = yPlace;
                                bestDistance = distance;
                            }
                        }
                    }
                }
                fleeingSpot = true;
            } else {
                cellList = Tiles.finder.findPath((int) posX, (int) posY, (int) bestFleeingSpot.x, (int) bestFleeingSpot.y, Tiles.pathGrid);
                if (cellList != null) {
                    if (cellList.size() >= 1) {

                        float diffX = (cellList.get(0).x + .95f) - posX;
                        float diffY = (cellList.get(0).y + .5f) - posY;

                        float angle = (float) Math.atan2(diffY, diffX);

                        posX += speed * Math.cos(angle);
                        posY += speed * Math.sin(angle);
                    }
                }
                if ((int) posX == (int) bestFleeingSpot.x && (int) posY == (int) bestFleeingSpot.y) fleeingSpot = false;
            }
        }
    }
    public void idle(){
        animation = animations[0];
        idleTime += Gdx.graphics.getDeltaTime();
        enemy = animation.getKeyFrame(idleTime, true);
    }
    public void deathAnimation(){
        deathTime += Gdx.graphics.getDeltaTime();
        if (enemyEffects.size() > 0) {
            for (int i = 0; i < enemyEffects.size(); i++) {
              if (enemyEffects.get(i).pointLight != null)  enemyEffects.get(i).pointLight.remove();
            }enemyEffects.clear();
        }
        hitBox.set(0, 0, 0, 0);

        animation = animations[4];
        animationSizeInt = 4;
        enemy = animation.getKeyFrame(deathTime, false);
        if (deathTime >= animation.getAnimationDuration()) {
            ActionDescription.actionText.add(new ActionDescription.actionType("YOU HAVE SLAIN " + name, Color.WHITE));
            dispose();
            if (name.equals("DarkGuardian")){
                for (int i = 0; i <= ToolClass.unwalkableTileIds.size(); i++){
                    if (ToolClass.unwalkableTileIds.get(i) == 87){
                        ToolClass.unwalkableTileIds.remove(i);
                    }
                }
                Player.isBoss = false;
            }

            Player.gainExperience(heldExperience);
            ToolClass.lootPool(name, posX - 1.75f, posY);
            PlayingField.enemies.remove(this);
        }
    }
    public void stagger(){
        attackTime = 0;
        idleTime = 0;
        walkTime = 0;
        stunnedTime += Gdx.graphics.getDeltaTime();
        animation = animations[3];
        animationSizeInt = 3;
        enemy = animation.getKeyFrame(stunnedTime, true);
        if (stunnedTime >= animation.getAnimationDuration()) {
            stunnedTime = 0;
            stunned = false;
        }
    }
    public void moveToPoint(Vector2 target){
        if (scanForWallBetweenPlayerAndEnemy()) {
            float diffX = target.x - posX;
            float diffY = target.y - posY;
            float angle = (float) Math.atan2(diffY, diffX);
            if (!((int) target.x == (int) posX && (int) target.y == (int) posY)) {
                if (Tiles.collisionChecker((float) (posX + (speed * Math.cos(angle))), (float) (posY + (speed * Math.sin(angle))))) {
                    posX += speed * Math.cos(angle);
                    posY += speed * Math.sin(angle);
                }
            }
        } else { follow(); }
    }

   void hitFrame(Animation animation, float timer, int fullFrame, int engageFrame, float circleX, float circleY, float radius){
        if ((timer >= (animation.getAnimationDuration() / fullFrame) * (engageFrame - 1) &&  timer <= (animation.getAnimationDuration() / fullFrame) * (engageFrame + 1))){
            hitbox(circleX, circleY, radius);
        }
    }

    public void skeletonBrawler() {
        //Action Timer
        actionTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }
        if (Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(1.3f, 2)) { attack = true;}

        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        animation = animations[2];
                        animationSizeInt = 2;
                        enemy = animation.getKeyFrame(attackTime, true);
                        if (attackTime >= 0.7 && attackTime <= 0.8 ){
                                hitbox(0.4779974f, 0.59199595f, 0.9179946f);
                                hitbox(0.4779974f, 1.028992f, 0.9179946f);
                        } else { directions(); }

                        if (attackTime >= animations[2].getAnimationDuration()) {
                            attack = false;
                            attackTime = 0;
                            idleTime = 0;
                            actionTimer = 0;
                            canAttack = false;
                            damage = false;
                        }
                    } else {
                        if (distanceFromPlayer > 0.75) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }
    }
    boolean fleeing;
    public void skeletonSharpShooter(){
        //Action Timer
        actionTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }


      if (distanceFromPlayer < 4.5 && !attack){
            if (scanForWallBetweenPlayerAndEnemy()){
                attack = true;
            }
      } else if (distanceFromPlayer < 3){
          fleeing = true;

      }
       // System.out.println("attack " +attack);
        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        animation = animations[2];
                        animationSizeInt = 2;
                        enemy = animation.getKeyFrame(attackTime, true);
                        if (attackTime >= (animations[2].getAnimationDuration() / 20) * 6) {
                            if (attackTime >= (animations[2].getAnimationDuration() / 20) * 13) {
                                if (!damage) {
                                    float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                                    float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                                    damage = true;
                                    PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.FIREBALL,posX - 0.5f, posY + 0.5f, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                                }
                                if (attackTime >= animations[2].getAnimationDuration()) {
                                    attack = false;
                                    attackTime = 0;
                                    idleTime = 0;
                                    actionTimer = 0;
                                    canAttack = false;
                                    damage = false;
                                    fleeingSpot = false;
                                }
                            }
                        } else { directions(); }
                    } else {
                        if (distanceFromPlayer > 4 || !scanForWallBetweenPlayerAndEnemy()) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else if (fleeing) {
                            fleeing(5);
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                            if (distanceFromPlayer > 3){
                                fleeingSpot = false;
                                fleeing = false;
                            }
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }
    }

    boolean shot1, shot2, shot3;
    public void Spirit(){
        //Action Timer
        actionTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }


        if (distanceFromPlayer < 6.5 && !attack){
            if (scanForWallBetweenPlayerAndEnemy()){
                attack = true;
            }
        } else if (distanceFromPlayer < 5){
            fleeing = true;

        }
        // System.out.println("attack " +attack);
        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        animation = animations[2];
                        animationSizeInt = 2;
                        enemy = animation.getKeyFrame(attackTime, true);
                        directions();
                        if (attackTime > 0.5 && attackTime < 0.7 && !shot1) {
                            float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                            float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                            shot1 = true;
                            PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES,posX - 0.5f, posY + 0.5f, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                        }
                        if (attackTime > 1.7 && attackTime < 1.9 && !shot2) {
                            float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                            float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                            shot2 = true;
                            PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES,posX - 0.5f, posY + 0.5f, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                        }
                        if (attackTime > 2.9 && attackTime < 3.1 && !shot3) {
                            float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                            float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                            shot3 = true;
                            PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES,posX - 0.5f, posY + 0.5f, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                        }


                            if (attackTime >= 3.6) {
                                attack = false;
                                attackTime = 0;
                                idleTime = 0;
                                actionTimer = 0;
                                canAttack = false;
                                damage = false;
                                fleeingSpot = false;
                                shot1 = false; shot2 = false; shot3 = false;
                            }
                    }
                        if (distanceFromPlayer > 4 || !scanForWallBetweenPlayerAndEnemy()) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else if (fleeing) {
                            fleeing(5);
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                            if (distanceFromPlayer > 3){
                                fleeingSpot = false;
                                fleeing = false;
                            }

                    }
                } else {idle();}
            } else { stagger(); } }}



    boolean LevitationForm, retreatState, turretPhase;
    float retreatTimer, turretTimer = 5, retreatActiveTimer, turretActiveTimer;
    int shootingInterval1 = 11, shootingInterval2 = 12;
    public void angelicaMinion(){
        //Action Timer
        actionTimer += deltaTimer();
        retreatTimer += deltaTimer();
        turretTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }

        if (distanceFromPlayer < 2 && !retreatState && retreatTimer >= 5 && !turretPhase){
            retreatState = true;
            retreatTimer = 0;
            attack = false;
            canStun = false;
        }


        if (distanceFromPlayer < 4.5 && !attack && !retreatState && canAttack){
            if (scanForWallBetweenPlayerAndEnemy()){
                Random actionRoll = new Random();
                if (actionRoll.nextInt(10) >= 2 && turretTimer <= 10) {
                    attack = true;
                } else {
                    turretPhase = true;
                    turretTimer = 0;
                    canStun = false;
                }
            }
        }


        // System.out.println("attack " +attack);
        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                    float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                    if (retreatState){
                        speed = 0.08f;
                        fleeing(10);
                        directions();
                        animation = animations[6];
                        animationSizeInt = 2;
                        retreatActiveTimer += deltaTimer();
                        enemy = animation.getKeyFrame(retreatActiveTimer, true);
                        if (retreatActiveTimer >= animations[6].getAnimationDuration()){
                            retreatState = false;
                            retreatActiveTimer = 0;
                            canStun = true;
                        }
                        speed = 0.04f;
                    } else if (turretPhase){
                        directions();
                        animation = animations[5];
                        animationSizeInt = 2;
                        turretActiveTimer += deltaTimer();
                        enemy = animation.getKeyFrame(turretActiveTimer, false);
                        if (turretActiveTimer >= animations[5].getAnimationDuration()){
                            turretActiveTimer = 0;
                            turretPhase = false;
                            canStun = true;
                            shootingInterval1 = 11;
                            shootingInterval2 = 12;
                        }
                        if (turretActiveTimer >= (animations[5].getAnimationDuration() / 34) * 5){
                            if ((turretActiveTimer >= (animations[5].getAnimationDuration() / 34) * shootingInterval1 && turretActiveTimer <= (animations[5].getAnimationDuration() / 34) * shootingInterval2)){
                                PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES, posX - 0.5f, posY + 0.5f, 0.1f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                                shootingInterval1 += 2; shootingInterval2 += 2;
                            }
                        }


                    } else if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        animation = animations[2];
                        animationSizeInt = 2;
                        enemy = animation.getKeyFrame(attackTime, true);
                                if ((attackTime >= (animations[2].getAnimationDuration() / 18) * 5 && attackTime <= (animations[2].getAnimationDuration() / 18) * 6) || (
                                        (attackTime >= (animations[2].getAnimationDuration() / 18) * 10 && attackTime <= (animations[2].getAnimationDuration() / 18) * 11)
                                        )){
                                    damage = true;
                                    PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES, posX - 0.5f, posY + 0.5f, 0.1f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                                }
                                if (attackTime >= animations[2].getAnimationDuration()) {
                                    attack = false;
                                    attackTime = 0;
                                    idleTime = 0;
                                    actionTimer = 0;
                                    canAttack = false;
                                    damage = false;
                                }

                       directions();
                    } else {
                        if (distanceFromPlayer > 4 || !scanForWallBetweenPlayerAndEnemy()) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }
    }


    boolean chargeAttack;
    public void skeletonKnight(){
        //Action Timer
        actionTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }
        if (Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(3f, 2) && distanceFromPlayer <= 5 && canAttack && !attack) { attack = true; chargeAttack = false;}
        else if ( distanceFromPlayer > 5 && distanceFromPlayer < 6 && canAttack && !attack){attack = true; chargeAttack = true;}

        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        if (!chargeAttack){
                        animation = animations[2];
                        animationSizeInt = 2;
                        enemy = animation.getKeyFrame(attackTime, true);
                        if (attackTime >= (animations[2].getAnimationDuration() / 38) * 6) {
                            if (!chargeAttack) {
                                speed = 0.03f;
                                if ((attackTime >= (animations[2].getAnimationDuration() / 38) * 11 && attackTime <= (animations[2].getAnimationDuration() / 38) * 19)
                                        || (attackTime >= (animations[2].getAnimationDuration() / 38) * 25 && attackTime <= (animations[2].getAnimationDuration() / 38) * 29)) {
                                    if ((attackTime >= 1.2 && attackTime <= 1.5) || (attackTime >= 2.6 && attackTime <= 2.7)) {
                                        hitbox(0.45899764f, 0.45899764f, 0.8509955f);
                                        hitbox(0.45899764f, 1.133997f, 0.8509955f);
                                    }
                                    if (attackTime >= 1.7 && attackTime <= 2.3){
                                        directions();
                                    }

                                    if ((attackTime >= 1.1 && attackTime <= 1.7) || (attackTime >= 2.3 && attackTime <= 2.9)) {
                                        speed = 0.11f;
                                        if (!locked) {locked = true; targetLockedForBoss = new Vector2(Player.posX, Player.posY);}
                                        moveToPoint(targetLockedForBoss);
                                    }else {
                                        locked = false;
                                    } }
                                if ((attackTime >= (animations[2].getAnimationDuration() / 38) * 19) && ((attackTime <= (animations[2].getAnimationDuration() / 38) * 25 ))) directions();
                                    if (attackTime >= animations[2].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        locked = false;
                                        idleTime = 0;
                                        actionTimer = 0;
                                        canAttack = false;
                                        damage = false;
                                    }

                            } }}
                            else if(chargeAttack){
                                animation = animations[5];
                                animationSizeInt = 2;
                                enemy = animation.getKeyFrame(attackTime, true);
                                speed = 0.07f;
                                if ((attackTime >= (animations[5].getAnimationDuration() / 22) * 14 && attackTime <= (animations[5].getAnimationDuration() / 22) * 20)) {
                                    if (attackTime >= 1.6 && attackTime <= 1.8) {
                                        hitbox(0.45899764f, 0.45899764f, 0.8509955f);
                                        hitbox(0.45899764f, 1.133997f, 0.8509955f);
                                    }
                                    if ((attackTime >= (animations[5].getAnimationDuration() / 22) * 10 && attackTime <= (animations[5].getAnimationDuration() / 22) * 13)) directions();
                                    if (attackTime >= 1.4 && attackTime <= 2.1) {
                                        speed = 0.15f;
                                        if (!locked) {locked = true; targetLockedForBoss = new Vector2(Player.posX, Player.posY);}
                                        moveToPoint(targetLockedForBoss);
                                    }else {
                                        locked = false;
                                    }
                                }

                                    if (attackTime >= animations[5].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        idleTime = 0;
                                        locked = false;
                                        actionTimer = 0;
                                        canAttack = false;
                                        damage = false;
                                        chargeAttack = false;
                                    }


                        }
                            else { directions(); }
                            speed = 0.025f;

                    } else {
                        if (distanceFromPlayer > 0.75) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }
    }

    Vector2 targetLocked = new Vector2();
    boolean secondAttack;
    public void dungeonMauler(){
        //Action Timer
        actionTimer += deltaTimer();
        if (actionTimer > actionIntervals){
            canAttack = true;
            actionTimer = 0; }
        if (Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(3f, 2) && distanceFromPlayer <= 3 && canAttack && !attack) {
            attack = true;
            if (attackTypeRoll.nextInt(2) == 1){
                secondAttack = true;
            }
        }
        else if ( distanceFromPlayer > 3 && distanceFromPlayer <= 4 && canAttack && !attack ){
            attack = true; chargeAttack = true;
        }

        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer) {
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        if (!chargeAttack && !secondAttack){
                            walkTime = 0;
                            animation = animations[2];
                            animationSizeInt = 2;
                            enemy = animation.getKeyFrame(attackTime, true);
                            if (attackTime > 1.1 && attackTime < 1.4) hitbox(0.18900023f, 0.24300034f, 0.9589941f);

                            if (attackTime >= (animations[2].getAnimationDuration() / 18) * 6) {
                                if (attackTime >= (animations[2].getAnimationDuration() / 18) * 13) {

                                    if (attackTime >= animations[2].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        idleTime = 0;
                                        actionTimer = 0;
                                        canAttack = false;
                                        damage = false;
                                    }
                                }
                            } else { directions(); }}
                        else if (!chargeAttack && secondAttack){
                            walkTime = 0;
                            animation = animations[6];
                            animationSizeInt = 2;
                            enemy = animation.getKeyFrame(attackTime, true);
                            if (attackTime >= 0.5 && attackTime <= 0.6 || attackTime >= 1.3 && attackTime <= 1.4) {
                                hitbox(0.64799523f, 0.83699286f, 1.093998f);
                            } else { directions(); }
                            if ((attackTime >= 0 && attackTime <= 0.5) || (attackTime >= 0.7 && attackTime <= 1.3)) {
                                speed = 0.07f;
                                if (!locked) {locked = true; targetLockedForBoss = new Vector2(Player.posX, Player.posY);}
                                moveToPoint(targetLockedForBoss);
                            }else {
                                locked = false;
                            }
                            if (attackTime >= animations[6].getAnimationDuration()) {
                                attack = false;
                                attackTime = 0;
                                locked = false;
                                secondAttack = false;
                                idleTime = 0;
                                actionTimer = 0;
                                canAttack = false;
                                damage = false;
                            }
                        }
                        else if(chargeAttack){
                            animation = animations[5];
                            animationSizeInt = 2;
                            enemy = animation.getKeyFrame(attackTime, true);
                            speed = 0.05f;
                            if ( attackTime <= 0.9f ) {

                                    speed = 0.1f;
                                    if (!locked) {locked = true; targetLockedForBoss = new Vector2(Player.posX, Player.posY);}
                                    moveToPoint(targetLockedForBoss);

                            }
                            if (attackTime > 0.8 && attackTime < 1.1) hitbox(0.18900023f, 0.24300034f, 0.9589941f);
                                if (attackTime >= animations[5].getAnimationDuration()) {
                                    attack = false;
                                    attackTime = 0;
                                    idleTime = 0;
                                    locked = false;
                                    actionTimer = 0;
                                    canAttack = false;
                                    damage = false;
                                    chargeAttack = false;
                                }


                        }
                        else { directions(); }
                        speed = 0.05f;

                    } else {
                        if (distanceFromPlayer > 0.75) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }
    }

    int attackNum = 0;
    float bossAbilityCoolDown, actionCooldown;
    Random attackTypeRoll = new Random();
    Vector2 targetLockedForBoss = new Vector2(); boolean locked, stepped, seen;
    boolean bossAbility =false;
    public void DarkGuardian(){
        //Action Timer
        if (Tiles.getTileId(Player.posX + 1, Player.posY, 3) == 85 && !stepped){
            ToolClass.unwalkableTileIds.add(87); stepped = true;
        }

        if (seen || detectedPlayer){
            Player.isBoss = true;
            Player.bossHealth = currentHP;
            Player.maxHealth = maxHealth;
            Player.lostHealth = healthLostInt;
        }

        actionTimer -= deltaTimer();
        if (bossAbilityCoolDown > 0){bossAbilityCoolDown -= Gdx.graphics.getDeltaTime();}
        else {bossAbilityCoolDown = 0;}

        if (actionTimer <= 0){
            canAttack = true;
            actionTimer = 0; }
        if (Math.pow((posX - 0.5f - Player.posX), 2) + Math.pow((posY - Player.posY), 2) <= Math.pow(3f, 2) && distanceFromPlayer <= 5 && canAttack && !attack)
        { attack = true; chargeAttack = false;

            if (bossAbilityCoolDown > 0){
                attackNum = attackTypeRoll.nextInt(4);}
            else {
                attackNum = attackTypeRoll.nextInt(5);
                if (attackNum == 4) {
                    bossAbility = true;
                    bossAbilityCoolDown = 10;
                }
            }
        }
        else if ( distanceFromPlayer > 5 && distanceFromPlayer < 6 && canAttack && !attack) {
            int random = attackTypeRoll.nextInt(3);
            attack = true;
            if (random == 2 && bossAbilityCoolDown <= 0){bossAbility = true;}
            else { chargeAttack = true;}
        }

        //Main Render
        if (currentHP > 0) {
            if (!stunned) { //taking damage
                if (detectedPlayer || seen) {

                    seen = true;
                    if (attack && canAttack) {
                        attackTime += deltaTimer();
                        walkTime = 0;
                        if (bossAbility){
                            animation = animations[7];
                            animationSizeInt = 2;
                            enemy = animation.getKeyFrame(attackTime, true);
                            speed = 0.03f;
                            if (distanceFromPlayer < 3) fleeing( 5);
                            else if (distanceFromPlayer > 4) follow();

                            if (((int) (attackTime * 100)) % 40 == 0 ){
                                float aimXdiff = Player.posX + (Player.hitbox.width/2) - (posX);
                                float aimYdiff = Player.posY + (Player.hitbox.height/2) - 1f - (posY);
                                shot1 = true;
                                PlayingField.projectiles.add(new Projectiles(Projectiles.projectileType.PURPLEFLAMES,posX - 0.5f, posY + 0.5f, 0.2f, 100,(float) Math.atan2(aimYdiff, aimXdiff), true, false, 0, 25 ));
                            }
                            System.out.println(attackTime * 100);

                            if (attackTime >= animations[7].getAnimationDuration()) {
                                attack = false;
                                attackTime = 0;
                                locked = false;
                                idleTime = 0;
                                actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(2);
                                canAttack = false;
                                damage = false;
                                bossAbility = false;}
                        } else if (!chargeAttack){
                            switch (attackNum){
                                case 0:
                                    animation = animations[6];
                                    animationSizeInt = 2;
                                    enemy = animation.getKeyFrame(attackTime, true);
                                    speed = 0.04f;

                                    if (attackTime >= 0.8f && attackTime <= 1 || (attackTime >= 2.3f && attackTime <= 2.5)) {
                                        hitbox( -0.75599384f, 0.8119931f, 0.97599393f); }
                                    else if (attackTime >= 0.9f && attackTime <= 1.1 || (attackTime >= 2.4f && attackTime <= 2.6)) {
                                        hitbox( 0.055999935f, 0.75599384f, 1.2280042f);}
                                    else if (attackTime >= 1f && attackTime <= 1.2 || (attackTime >= 2.5f && attackTime <= 2.7)) {
                                        hitbox( 0.6159956f, 1.5400159f, 1.2280042f);
                                    }

                                    if (attackTime >= 1.5f && attackTime <= 1.7 || (attackTime >= 3f && attackTime <= 3.2)) { hitbox( 0.6159956f, 1.5400159f, 1.2280042f); }
                                    else if (attackTime >= 1.6f && attackTime <= 1.8 || (attackTime >= 3.1f && attackTime <= 3.3)) { hitbox( 0.055999935f, 0.75599384f, 1.2280042f); }
                                    else if (attackTime >= 1.7f && attackTime <= 1.9 || (attackTime >= 3.2f && attackTime <= 3.4)) { hitbox( -0.75599384f, 0.8119931f, 0.97599393f);}

                                {directions(); follow();}
                                if (attackTime >= animations[6].getAnimationDuration()) {
                                    attack = false;
                                    attackTime = 0;
                                    locked = false;
                                    idleTime = 0;
                                    actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(3);
                                    canAttack = false;
                                    damage = false; }
                                    break;
                                case 1:
                                    animation = animations[8];
                                    animationSizeInt = 2;
                                    enemy = animation.getKeyFrame(attackTime, true);
                                    speed = 0.04f;
                                    if (attackTime >= 0.7f && attackTime <= 0.9) {

                                        hitbox( 0.75599384f, 1f, 1.30042f);
                                    }
                                    if (attackTime >= 1.1f && attackTime <= 1.3) {
                                        hitbox( 0.75599384f, 1f, 1.30042f);
                                    }
                                    if ((attackTime <= 1.1)) {directions(); follow();}
                                    if (attackTime >= animations[8].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        locked = false;
                                        idleTime = 0;
                                        actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(3);
                                        canAttack = false;
                                        damage = false; }
                                    break;
                                case 2:

                                    animation = animations[9];
                                    animationSizeInt = 2;
                                    enemy = animation.getKeyFrame(attackTime, true);
                                    speed = 0.05f;
                                    if (attackTime >= 0.7f && attackTime <= 0.9) {

                                        hitbox( 0.3079996f, 0.6159957f, 1.5360186f);
                                    }
                                    if (attackTime >= 1.4f && attackTime <= 1.8) {

                                        hitbox(0.19600025f, 1.1479976f, 1.6480238f);
                                    }
                                    if ((attackTime <= 1.4)) {directions(); follow();}
                                    if (attackTime >= animations[9].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        locked = false;
                                        idleTime = 0;
                                        actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(3);
                                        canAttack = false;
                                        damage = false; }
                                    break;
                                case 3:
                                    animation = animations[5];
                                    animationSizeInt = 2;
                                    enemy = animation.getKeyFrame(attackTime, true);
                                    speed = 0.07f;
                                    if (attackTime >= 0.9f && attackTime <= 1.2) {

                                        hitbox(0.19600025f, 1.1479976f, 1.6480238f);
                                    }
                                    if ((attackTime <= 0.8)) {directions(); follow();}
                                    if (attackTime >= animations[5].getAnimationDuration()) {
                                        attack = false;
                                        attackTime = 0;
                                        locked = false;
                                        idleTime = 0;
                                        actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(3);
                                        canAttack = false;
                                        damage = false; }
                                    break;
                            }
                        }


                        else if(chargeAttack){
                            animation = animations[2];
                            animationSizeInt = 2;
                            enemy = animation.getKeyFrame(attackTime, true);
                            speed = 0.07f;

                                if (attackTime >= 1.1 && attackTime <= 1.3) {
                                    hitbox(1.0359924f, 1.1199963f, 1.3960121f);
                                }
                                if (attackTime <= 1.1) directions();
                                if (attackTime >= 0.5 && attackTime <= 1.1) {
                                    speed = 0.15f;
                                    if (!locked) {locked = true; targetLockedForBoss = new Vector2(Player.posX, Player.posY);}
                                    moveToPoint(targetLockedForBoss);
                                }else {
                                    locked = false;
                                }


                            if (attackTime >= animations[2].getAnimationDuration()) {
                                attack = false;
                                attackTime = 0;
                                idleTime = 0;
                                locked = false;
                                actionTimer = attackTypeRoll.nextFloat() + attackTypeRoll.nextInt(3);
                                canAttack = false;
                                damage = false;
                                chargeAttack = false;
                            }


                        }
                        else { directions(); }
                        speed = 0.025f;

                    } else {
                        if (distanceFromPlayer > 0.75) {
                            //Walking STATE
                            follow();
                            directions();
                            walkTime += deltaTimer();
                            enemy = animation.getKeyFrame(walkTime, true);
                        } else {idle();}
                    }
                } else {idle();}
            } else { stagger(); } }}}


