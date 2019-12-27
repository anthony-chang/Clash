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
        }
        if(objectB.equals("BULLET")) {
            contact.getFixtureB().getBody().setUserData("DELETE");
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
