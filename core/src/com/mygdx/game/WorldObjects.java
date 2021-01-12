package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Tools.Tiles;
import com.mygdx.game.Tools.ToolClass;
import org.w3c.dom.css.Rect;

import static com.mygdx.game.Inventory.UIsize;

public class WorldObjects extends ApplicationAdapter {
    public enum InteractableObjectType {
        BARREL, VASE, THINVASE, CRATE, MATERIALSCRATE;
    }
    public Rectangle collisionBox;
    private Animation objectAnimation;
    private TextureRegion objectTexture;
    public InteractableObjectType interactableObjectType;
    public boolean destroyObject, destroyed;
    public float posX, posY, animationTimer, removeTimer, speed, scaleX, scaleY, xOffset, yOffset; int fullFrame;
    WorldObjects(float posX, float posY, boolean destroyed,InteractableObjectType interactableObjectType){
        this.posX = posX;
        this.posY = posY;
        this.destroyed = destroyed;
        this.interactableObjectType = interactableObjectType;
        collisionBox = new Rectangle();
        switch(interactableObjectType){
            case BARREL:
                objectAnimation = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Objects/barrel.png"));
                collisionBox.set(posX + 0.8f, posY, 0.6f, 0.6f);
                fullFrame = 5;  xOffset = 0.8f;
                speed = 0.03f;scaleX = 2.25f;scaleY = 2.25f;
                if (!destroyed) objectTexture = objectAnimation.getKeyFrame(0, false);
                else objectTexture = objectAnimation.getKeyFrame(objectAnimation.getAnimationDuration(), false);
                break;
            case VASE:
                objectAnimation = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Objects/vase.png"));
                collisionBox.set(posX + 0.8f, posY, 0.6f, 0.6f);
                fullFrame = 5; xOffset = 0.8f;
                speed = 0.03f;scaleX = 2.25f;scaleY = 2.25f;
                if (!destroyed) objectTexture = objectAnimation.getKeyFrame(0, false);
                else objectTexture = objectAnimation.getKeyFrame(objectAnimation.getAnimationDuration(), false);
                break;
            case THINVASE:
                objectAnimation = new Animation(0.1f, ToolClass.generateTextureRegion(1, 4, "Objects/thinVase.png"));
                collisionBox.set(posX + 0.8f, posY, 0.6f, 0.6f);
                fullFrame = 5;  xOffset = 0.8f;
                speed = 0.03f;scaleX = 1.5f;scaleY = 2f;
                if (!destroyed) objectTexture = objectAnimation.getKeyFrame(0, false);
                else objectTexture = objectAnimation.getKeyFrame(objectAnimation.getAnimationDuration(), false);
                break;
            case CRATE:
                objectAnimation = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Objects/crate.png"));
                collisionBox.set(posX + 1f, posY, 1f, 1f);
                fullFrame = 5;  xOffset = 0.6f;
                speed = 0.01f;scaleX = 2.25f;scaleY = 2.25f;
                if (!destroyed) objectTexture = objectAnimation.getKeyFrame(0, false);
                else objectTexture = objectAnimation.getKeyFrame(objectAnimation.getAnimationDuration(), false);
                break;
            case MATERIALSCRATE:
                objectAnimation = new Animation(0.1f, ToolClass.generateTextureRegion(1, 5, "Objects/crateOfMaterials.png"));
                collisionBox.set(posX + 0.8f, posY, 0.6f, 0.6f);
                fullFrame = 5;  xOffset = 0.8f; yOffset = -0.5f;
                speed = 0.03f;scaleX = 2f;scaleY = 2f;
                if (!destroyed) objectTexture = objectAnimation.getKeyFrame(0, false);
                else objectTexture = objectAnimation.getKeyFrame(objectAnimation.getAnimationDuration(), false);
                break;
        }
    }
    public void collisionMovement(float x, float y){ //////FIX MOVING OBJECTS THROUGH DOORS IF POSSIBLE
        if (!destroyed) {
            float diffX = x - (posX + 1.25f);
            float diffY = y - (posY + 0.25f);
            float angleTowards = (float) Math.atan2(diffY, diffX);
            float moveTowardsX = (float) -Math.cos(angleTowards);
            float moveTowardsY = (float) -Math.sin(angleTowards);
            if (Tiles.collisionChecker(posX +  (0.75f * moveTowardsX), posY + (speed * moveTowardsY))) {
                    posX += speed * moveTowardsX;
                    posY += speed * moveTowardsY;

            }
        }
        /*
        PlayingField.shapeRenderer.begin();
        PlayingField.shapeRenderer.line(x, y, posX + 1.25f, posY + 0.25f);
        PlayingField.shapeRenderer.end();
         */
    }
    public void render(){
        collisionBox.setPosition(posX + xOffset, posY);
        PlayingField.spriteBatch.draw(objectTexture, posX, posY - 0.1f + yOffset, 0, 0, scaleX, scaleY,1, 1, 0);
        if (destroyObject){
            animationTimer += Gdx.graphics.getDeltaTime();
            objectTexture = objectAnimation.getKeyFrame(animationTimer, false);
            if (animationTimer >= objectAnimation.getAnimationDuration()){
                destroyObject = false;
                destroyed = true;
            }
        }
        if (!destroyed) {
            for (int i = 0; i < PlayingField.worldObjects.size(); i++) {
                if (!PlayingField.worldObjects.get(i).destroyed && PlayingField.worldObjects.get(i) != this) {
                    if (Intersector.overlaps(PlayingField.worldObjects.get(i).collisionBox, collisionBox)) {
                        PlayingField.worldObjects.get(i).collisionMovement(posX + 1.25f, posY + 0.25f);
                    }
                }
            }
        }
        /*
        if (destroyed) {
            removeTimer += Gdx.graphics.getDeltaTime();
            if (removeTimer >= 10){
                PlayingField.worldObjects.remove(this);
            }
        }

         */
    }
    public void dispose(){
        for (int i = 0; i <objectAnimation.getKeyFrames().length; i++){
            objectAnimation.getKeyFrames()[i].getTexture().dispose();
        }
        objectTexture.getTexture().dispose();
    }

    public static class Door{
        float posX, posY;
        Sprite doorOpened, doorClosed;
        public boolean open, interactable, isVertical, destroyed;
        Body body; BodyDef bodyDef;
        public Rectangle collisionBox;
        Door(float posX, float posY, boolean open, boolean isVertical, boolean destroyed){
            this.posX = posX;
            this.posY = posY;
            this.open = open;
            this.isVertical = isVertical;
            this.destroyed = destroyed;
            collisionBox = new Rectangle();
            if (isVertical){
                doorOpened = new Sprite(ToolClass.doorOpenedV);
                doorOpened.setPosition(posX - 15.5f, posY - 15.5f);
                doorOpened.setScale(0.065f);
                doorClosed = new Sprite(ToolClass.doorClosedV);
                doorClosed.setPosition(posX - 15.5f, posY - 15.5f);
                doorClosed.setScale(0.065f);
                bodyDef = new BodyDef();

                collisionBox.set(posX, posY, 1f, 1f);

                body = PlayingField.world.createBody(bodyDef);
                PolygonShape squareShape = new PolygonShape();
                squareShape.setAsBox(0.6f, 0.1f, new Vector2(posX + 0.5f, posY + 0.5f), 0f);
                body.setUserData("wall");
                body.setActive(true);
                FixtureDef fdef = new FixtureDef();
                fdef.shape = squareShape;
                fdef.density = 1.0f;

                body = PlayingField.world.createBody(bodyDef).createFixture(fdef).getBody();
            } else{
                doorOpened = new Sprite(ToolClass.doorOpenedH);
                doorOpened.setPosition(posX - 15.15f, posY - 14.75f);
                doorOpened.setScale(0.065f);
                doorClosed = new Sprite(ToolClass.doorClosedH);
                doorClosed.setPosition(posX - 15.5f, posY - 15.15f);
                doorClosed.setScale(0.065f);
                bodyDef = new BodyDef();

                collisionBox.set(posX + 0.375f, posY, 0.2f, 1.65f);

                body = PlayingField.world.createBody(bodyDef);
                PolygonShape squareShape = new PolygonShape();
                squareShape.setAsBox(0.1f, 1f, new Vector2(posX + 0.5f, posY + 0.5f), 0f);
                body.setUserData("wall");
                body.setActive(true);
                FixtureDef fdef = new FixtureDef();
                fdef.shape = squareShape;
                fdef.density = 1.0f;

                body = PlayingField.world.createBody(bodyDef).createFixture(fdef).getBody();
            }
        }

        public void render(){
            if (!destroyed) {
                if (open) {
                    doorOpened.draw(PlayingField.spriteBatch);
                    body.setActive(false);
                } else {
                    doorClosed.draw(PlayingField.spriteBatch);
                    body.setActive(true);
                }
                if (interactable) {
                    PlayingField.spriteBatch.draw(ToolClass.keyE, posX + 0.25f, posY + 0.75f, 0.5f, 0.5f);
                }
            } else {
                open = true;
                body.setActive(false);
            }

        }


    }
    public class FOWLights{
        float posX, posY;
        boolean activated;


    }



}
