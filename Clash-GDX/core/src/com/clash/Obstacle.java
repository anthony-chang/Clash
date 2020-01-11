package com.clash;

import com.badlogic.gdx.physics.box2d.*;

/*Dynamic obstacle definitions*/
public class Obstacle {
    Body obstacleBody;
    BodyDef obstacleBodyDef;
    FixtureDef obstacleFixtureDef;
    PolygonShape obstacleShape;
    int ID;

    public Obstacle(int num, int x, int y, int width, int height) {
        ID = num;

        //obstacle body definitions
        obstacleBodyDef = new BodyDef();
        obstacleBodyDef.type = BodyDef.BodyType.DynamicBody;
        obstacleBodyDef.linearDamping = 2f;
        obstacleBodyDef.angularDamping = 2f;
        obstacleBodyDef.position.set(x, y);

        //obstacle shape
        obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(width, height);

        //fixture definitions
        obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.friction = 0;
        obstacleFixtureDef.density = 3f;
        obstacleFixtureDef.restitution = 0.2f;
        obstacleFixtureDef.filter.categoryBits = GameScreen.CATEGORY_MAP;
        obstacleFixtureDef.filter.maskBits = GameScreen.CATEGORY_MAP |
                GameScreen.CATEGORY_PLAYER1 |
                GameScreen.CATEGORY_PLAYER2 |
                GameScreen.CATEGORY_BULLET;  //collides with wall objects, players, and bullets

    }
    public void addObstacleToWorld(World world) {
        obstacleBody = world.createBody(obstacleBodyDef);
        obstacleBody.setUserData("OBSTACLE");
        obstacleBody.createFixture(obstacleFixtureDef);
    }
}
