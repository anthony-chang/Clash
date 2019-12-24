package com.clash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerSprite {
    private Texture playerImage;
    private Sprite playerSprite;
    public float x, y, vx, vy, ax, ay;
    public boolean movingRight, movingLeft, movingUp, movingDown;

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
        movingRight = movingLeft = movingUp = movingDown = false;
    }
    public Sprite getSprite() {
        return playerSprite;
    }
    public void movePlayer() {
        if(movingLeft) {
            movePlayer(-1, 0);
        }
        if(movingRight) {
            movePlayer(1, 0);
        }
        if(movingUp) {
            movePlayer(0, 1);
        }
        if(movingDown) {
            movePlayer(0, -1);
        }
    }
    public void movePlayer(float accelerationX, float accelerationY) {
        ax = accelerationX*0.2f;
        ay = accelerationY*0.2f;

        vx += ax;
        vy += ay;

        x += vx;
        y += vy;

        playerSprite.setPosition(x, y);
    }
}
