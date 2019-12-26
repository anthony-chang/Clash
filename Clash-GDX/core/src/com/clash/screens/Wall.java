package com.clash.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/*Static wall definitions*/
public class Wall {
    public BodyDef wallBodyDef;
    public FixtureDef wallFixtureDef;
    public ChainShape wallShape;

    public Wall(int screenWidth, int screenHeight) { //border wall constructor
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
                new Vector2(-screenWidth/2, -screenHeight/2)}); //rectangles have 5 vertices (connect the dots)

        //player fixture definitions
        wallFixtureDef = new FixtureDef();
        wallFixtureDef.shape = wallShape;
        wallFixtureDef.friction = 0;
        wallFixtureDef.restitution = 0;
        wallFixtureDef.filter.categoryBits = GameScreen.CATEGORY_MAP;
        wallFixtureDef.filter.maskBits = GameScreen.CATEGORY_MAP | //collides with other maps objects,
                GameScreen.CATEGORY_BULLET |                        //bullets,
                GameScreen.CATEGORY_PLAYER1 |                       //and players
                GameScreen.CATEGORY_PLAYER2;

        //wallShape.dispose(); //this dispose call will crash the program
    }
}
