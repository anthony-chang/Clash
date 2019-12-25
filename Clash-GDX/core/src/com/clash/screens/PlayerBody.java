package com.clash.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PlayerBody {
    Body playerBody;
    Vector2 movement = new Vector2();
    float speed = 10000;
    BodyDef playerBodyDef;
    FixtureDef playerFixtureDef;
    PolygonShape playerShape;

    public PlayerBody() {
        //player body definitions
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.linearDamping = 2f; //(linear) friction
        playerBodyDef.fixedRotation = true; //prevent rotating
        playerBodyDef.position.set(-40, 1);

        //player shape
        playerShape = new PolygonShape();
        playerShape.setAsBox(3, 3);

        //player fixture definitions
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 1f;
        playerFixtureDef.restitution = 0.5f;

    }
    public void move(int x, int y) {
        if(x != 0)
            movement.x = (x < 0) ? -speed:speed; //left:right
        if(y != 0)
            movement.y = (y < 0) ? -speed:speed; //down:up
    }
    public void setMovement(float x, float y) {
        movement.x = x;
        movement.y = y;
    }
    public void moveUsingAccelerometer(float accelerometerX, float accelerometerY) {
        //swap accelerometer x and y since phone is in landscape mode
        movement.x = accelerometerY*2000;
        movement.y = -accelerometerX*2000;

    }
}
