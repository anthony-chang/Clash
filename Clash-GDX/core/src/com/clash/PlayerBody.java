package com.clash;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PlayerBody {
    //Define player properties
    Body playerBody;
    Vector2 movement = new Vector2();
    float speed = 10000;
    BodyDef playerBodyDef;
    FixtureDef playerFixtureDef;
    PolygonShape playerShape;

    //ammo and reload system
    private final int MAX_AMMO = 3;
    private final float RELOAD_TIME = 1; //1 second to reload
    int ammo;
    float curTime;

    public PlayerBody(int playerNum) {
        ammo = MAX_AMMO;
        curTime = 0;
        //player body definitions
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.linearDamping = 2f; //(linear) friction
        playerBodyDef.fixedRotation = true; //prevent rotating
        playerBodyDef.position.set((playerNum == 1) ? -GameScreen.WIDTH/4:GameScreen.WIDTH*3/4, 0);

        //player shape
        playerShape = new PolygonShape();
        playerShape.setAsBox(3, 3);

        //player fixture definitions
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 1f;
        playerFixtureDef.restitution = 0.5f;
        playerFixtureDef.filter.categoryBits = (playerNum == 1)? GameScreen.CATEGORY_PLAYER1:GameScreen.CATEGORY_PLAYER2; //PLAYER FILTER INDEX
        playerFixtureDef.filter.maskBits = GameScreen.CATEGORY_MAP;
    }
    public void addPlayerToWorld(World world) {
        playerBody = world.createBody(playerBodyDef);
        playerBody.setUserData("PLAYER");
        playerBody.createFixture(playerFixtureDef);
    }
    public Vector2 getPosition() {
        return playerBody.getPosition();
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
    public void updateAmmo(float deltaTime) {
        if(ammo == MAX_AMMO) //don't do anything at max ammo
            return;
        curTime += deltaTime;
        if(curTime > RELOAD_TIME) {
            curTime -= RELOAD_TIME;
            ammo = Math.min(ammo+1, MAX_AMMO); //set cap of ammo
        }
    }
}
