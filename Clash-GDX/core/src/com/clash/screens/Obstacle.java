package com.clash.screens;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/*Dynamic obstacle definitions*/
public class Obstacle {
    BodyDef obstacleBodyDef;
    FixtureDef obstacleFixtureDef;
    PolygonShape obstacleShape;

    public Obstacle() {
        //obstacle body definitions
        obstacleBodyDef = new BodyDef();
        obstacleBodyDef.type = BodyDef.BodyType.DynamicBody;
        obstacleBodyDef.linearDamping = 2f;
        obstacleBodyDef.angularDamping = 2f;
        obstacleBodyDef.position.set(0, 0);

        //obstacle shape
        obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(1, 6);

        //fixture definitions
        obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.friction = 0;
        obstacleFixtureDef.density = 0.3f;
        obstacleFixtureDef.restitution = 0.2f;

    }
}
