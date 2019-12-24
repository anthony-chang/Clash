package com.clash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerSprite {
    Texture playerImage;
    Sprite playerSprite;
    private float x, y, vx, vy, ax, ay;

    public PlayerSprite(int playerNumber) {
        if(playerNumber == 1) {
            playerImage = new Texture("p1.png");
            x = Gdx.graphics.getWidth()/4 - playerImage.getWidth()/2;
            y = Gdx.graphics.getHeight()/2 - playerImage.getHeight()/2;
        }
        else {
            playerImage  = new Texture("p2.png");
            x = Gdx.graphics.getWidth()*3/4 - playerImage.getWidth()/2;
            y = Gdx.graphics.getHeight()/2 - playerImage.getHeight()/2;
        }
        playerSprite = new Sprite(playerImage);
        playerSprite.setPosition(x, y);
    }
    public Sprite getSprite() {
        return playerSprite;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getVelocityX() {
        return vx;
    }
    public float getVelocityY() {
        return vy;
    }
    public void getInput(boolean left, boolean right, boolean up, boolean down) {
        if(left) {
            movePlayer(-1, 0);
        }
        if(right) {
            movePlayer(1, 0);
        }
        if(up) {
            movePlayer(0, 1);
        }
        if(down) {
            movePlayer(0, -1);
        }
    }
    public void movePlayer(float accelerationX, float accelerationY) {
        ax = accelerationX*0.1f;
        ay = accelerationY*0.1f;

        vx += ax;
        vy += ay;

        x += vx;
        y += vy;

        playerSprite.setPosition(x, y);
    }
}
