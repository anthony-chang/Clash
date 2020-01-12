package com.multiplayerdemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class PlayerBody extends Sprite {
    Vector2 previousPosition;
    public PlayerBody (Texture texture) {
        super(texture);
        previousPosition = new Vector2(getX(), getY());
    }

    public boolean hasMoved() {
        if (previousPosition.x != getX() || previousPosition.x != getY()){
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }
}
