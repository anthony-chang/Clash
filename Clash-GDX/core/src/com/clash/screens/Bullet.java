package com.clash.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Bullet {
    BodyDef bulletBodyDef;
    FixtureDef bulletFixtureDef;
    PolygonShape bulletShape;

    public Bullet(int playerNum, float x, float y, float mouseX, float mouseY) {
        //body definitions
        bulletBodyDef = new BodyDef();
        bulletBodyDef.type = BodyDef.BodyType.DynamicBody;
        bulletBodyDef.position.set(x, y);
        /*Use this declaration if camera is not centred about the player
        bulletBodyDef.linearVelocity.set(getVelocityVector(x, y, mouseX, mouseY));
         */
        bulletBodyDef.linearVelocity.set(getVelocityVector(0, 0, mouseX, mouseY));
        bulletBodyDef.fixedRotation = true; //prevent rotation

        //shape
        bulletShape = new PolygonShape();
        bulletShape.setAsBox(1, 1);

        //fixture definitions
        bulletFixtureDef = new FixtureDef();
        bulletFixtureDef.shape = bulletShape;
        bulletFixtureDef.density = 1f;
        bulletFixtureDef.restitution = 0;
        bulletFixtureDef.filter.categoryBits = GameScreen.CATEGORY_BULLET;
        bulletFixtureDef.filter.maskBits = GameScreen.CATEGORY_MAP | GameScreen.CATEGORY_BULLET; //collides with map objects and other bullets

    }
    private Vector2 getVelocityVector(float startX, float startY, float destX, float destY) {
        float magnitude = (float) Math.sqrt(((destX-startX)*(destX-startX))+((destY-startY)*(destY-startY)));
        return new Vector2((destX-startX)/magnitude*500, (destY-startY)/magnitude*500);
    }
}
