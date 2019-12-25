package com.clash.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Walls {
    public BodyDef wallBodyDef;
    public FixtureDef wallFixtureDef;
    public ChainShape wallShape;

    public Walls(int screenWidth, int screenHeight) {
        //wall body definitions
        wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(0, 0);

        //wall shape
        wallShape = new ChainShape();
        wallShape.createChain(new Vector2[]{
                new Vector2(-screenWidth/2, -screenHeight/2),
                new Vector2(-screenWidth/2, screenHeight/2),
                new Vector2(screenWidth/2, screenHeight/2),
                new Vector2(screenWidth/2, -screenHeight/2),
                new Vector2(-screenWidth/2, -screenHeight/2)}); //rectangles have 5 vertices

        //player fixture definitions
        wallFixtureDef = new FixtureDef();
        wallFixtureDef.shape = wallShape;
        wallFixtureDef.friction = 0;
        wallFixtureDef.restitution = 0;

        //wallShape.dispose();
    }
}
