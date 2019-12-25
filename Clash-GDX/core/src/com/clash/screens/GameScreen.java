package com.clash.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameScreen implements Screen {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Body playerBody;
    private Vector2 movement = new Vector2();
    private float speed = 10000;
    private BodyDef playerBodyDef, wallBodyDef;
    private FixtureDef playerFixtureDef, wallFixtureDef;
    private PolygonShape playerShape;
    private ChainShape wallShape;

    private final static float TIMESTEP = 1/60f;
    private final static int VELOCITYITERATIONS = 30, POSITIONITERATIONS = 15;
    private final static int WIDTH = 160, HEIGHT = 90; //metres

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(WIDTH, HEIGHT); //16:9 aspect ratio

        //player body definitions
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.linearDamping = 2f; //(linear) friction 
        playerBodyDef.position.set(-40, 1);

        //player shape
        playerShape = new PolygonShape();
        playerShape.setAsBox(3, 3);

        //player fixture definitions
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerShape;
        playerFixtureDef.density = 1f;
        playerFixtureDef.restitution = 0.5f;

        //wall body definitions
        wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(0, 0);

        //wall shape
        wallShape = new ChainShape();
        wallShape.createChain(new Vector2[]{
                new Vector2(-WIDTH/2, -HEIGHT/2),
                new Vector2(-WIDTH/2, HEIGHT/2),
                new Vector2(WIDTH/2, HEIGHT/2),
                new Vector2(WIDTH/2, -HEIGHT/2),
                new Vector2(-WIDTH/2, -HEIGHT/2)}); //rectangles have 5 vertices

        //player fixture definitions
        wallFixtureDef = new FixtureDef();
        wallFixtureDef.shape = wallShape;
        wallFixtureDef.friction = 0;
        wallFixtureDef.restitution = 0;

        playerBody = world.createBody(playerBodyDef);
        playerBody.createFixture(playerFixtureDef);
        world.createBody(wallBodyDef).createFixture(wallFixtureDef);
        playerShape.dispose();
        wallShape.dispose();


        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        movement.y = speed;
                        break;
                    case Input.Keys.A:
                        movement.x = -speed;
                        break;
                    case Input.Keys.S:
                        movement.y = -speed;
                        break;
                    case Input.Keys.D:
                        movement.x = speed;
                        break;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                    case Input.Keys.S:
                        movement.y = 0;
                        break;
                    case Input.Keys.A:
                    case Input.Keys.D:
                        movement.x = 0;
                        break;
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);
        playerBody.applyForceToCenter(movement, true);
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
