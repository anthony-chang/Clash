package com.clash;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PlayerBody {
    //Define player properties
    private int num;
    int health;
    final static int MAX_HEALTH = 6;
    public Body playerBody;
    Vector2 movement = new Vector2();
    float speed = 8000;
    BodyDef playerBodyDef;
    FixtureDef playerFixtureDef;
    PolygonShape playerShape;
    Texture playerTexture;
    Texture[] healthBar;

    //ammo and reload system
    final static int MAX_AMMO = 5;
    private final float RELOAD_TIME = 1; //1 second to reload
    int ammo;
    float curTime;

    public PlayerBody(int playerNum) {
        num = playerNum;
        health = MAX_HEALTH;
        ammo = MAX_AMMO;
        curTime = 0;
        playerTexture = new Texture((playerNum == 1)? "blue_player.png":"red_player.png");
        //player body definitions
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.linearDamping = 2f; //(linear) friction
        playerBodyDef.fixedRotation = true; //prevent rotating
        playerBodyDef.position.set((playerNum == 1) ? -GameScreen.WIDTH/4:GameScreen.WIDTH/4, 0);

        //player shape
        playerShape = new PolygonShape();
        playerShape.setAsBox(3, 3);

        //player fixture definitions
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 1f;
        playerFixtureDef.restitution = 0.5f;
        playerFixtureDef.filter.categoryBits = (playerNum == 1)? GameScreen.CATEGORY_PLAYER1:GameScreen.CATEGORY_PLAYER2; //PLAYER FILTER INDEX

        playerFixtureDef.filter.maskBits = GameScreen.CATEGORY_PLAYER1 |
                        GameScreen.CATEGORY_PLAYER2 |
                        GameScreen.CATEGORY_MAP |
                        GameScreen.CATEGORY_BULLET;

        //health bar stuff
        healthBar = new Texture[7];
        for(int i = 0; i < 7; ++i) {
            healthBar[i] = new Texture("healthbar_" + i + ".png");
        }
    }
    public void addPlayerToWorld(World world) {
        playerBody = world.createBody(playerBodyDef);
        playerBody.setUserData((num == 1) ? "PLAYER1":"PLAYER2");
        playerBody.createFixture(playerFixtureDef);
    }
    public void move(int x, int y) {
        if(x != 0)
            movement.x = (x < 0) ? -speed:speed; //left:right
        if(y != 0)
            movement.y = (y < 0) ? -speed:speed; //down:up
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
    public float getReloadPercentage() { //gives % until next bullet available
        if(ammo == MAX_AMMO)
            return 1;
        return curTime/RELOAD_TIME;
    }

    public float getPositionX() {
        return playerBody.getPosition().x;
    }
    public float getPositionY() {
        return playerBody.getPosition().y;
    }
    public float getMovementX() {
        return movement.x;
    }
    public float getMovementY() {
        return movement.y;
    }
    public int getHealth() {
        return health;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(healthBar[health],
                (float) (getPositionX() - healthBar[health].getWidth()/10/2 * 1.1),
                getPositionY() + healthBar[health].getHeight()/10,
                healthBar[health].getWidth()/10,
                healthBar[health].getHeight()/10);
        batch.draw(playerTexture,
                getPositionX() - 3,
                getPositionY() - 3,
                6,
                6);
    }
}
