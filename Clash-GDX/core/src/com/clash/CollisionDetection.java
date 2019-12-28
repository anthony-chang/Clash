package com.clash;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionDetection implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        String objectA = (String) contact.getFixtureA().getBody().getUserData();
        String objectB = (String) contact.getFixtureB().getBody().getUserData();

        if(objectA.equals("BULLET")) {
            contact.getFixtureA().getBody().setUserData("DELETE");
            if(objectB.equals("PLAYER1")) {
                //System.out.println("Player 1 hit");
                contact.getFixtureB().getBody().setUserData("PLAYER1_DECREMENT_HEALTH");
            }
            else if (objectB.equals("PLAYER2")) {
                //System.out.println("Player 2 hit");
                contact.getFixtureB().getBody().setUserData("PLAYER2_DECREMENT_HEALTH");
            }
        }
        if(objectB.equals("BULLET")) {
            contact.getFixtureB().getBody().setUserData("DELETE");
            if(objectA.equals("PLAYER1")) {
                //System.out.println("Player 1 hit");
                contact.getFixtureA().getBody().setUserData("PLAYER1_DECREMENT_HEALTH");
            }
            else if (objectA.equals("PLAYER2")) {
                //System.out.println("Player 2 hit");
                contact.getFixtureA().getBody().setUserData("PLAYER2_DECREMENT_HEALTH");
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
