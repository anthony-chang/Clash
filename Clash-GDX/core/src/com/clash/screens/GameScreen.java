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
    private PlayerBody p1;
    private Walls border;


    private final static float TIMESTEP = 1/60f;
    private final static int VELOCITYITERATIONS = 30, POSITIONITERATIONS = 15;
    private final static int WIDTH = 160, HEIGHT = 90; //metres

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(WIDTH, HEIGHT); //16:9 aspect ratio

        p1 = new PlayerBody();
        border = new Walls(WIDTH, HEIGHT);

        p1.playerBody = world.createBody(p1.playerBodyDef);
        p1.playerBody.createFixture(p1.playerFixtureDef);
        world.createBody(border.wallBodyDef).createFixture(border.wallFixtureDef);

        p1.playerShape.dispose();
        border.wallShape.dispose();

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        p1.movement.y = p1.speed;
                        break;
                    case Input.Keys.A:
                        p1.movement.x = -p1.speed;
                        break;
                    case Input.Keys.S:
                        p1.movement.y = -p1.speed;
                        break;
                    case Input.Keys.D:
                        p1.movement.x = p1.speed;
                        break;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                    case Input.Keys.S:
                        p1.movement.y = 0;
                        break;
                    case Input.Keys.A:
                    case Input.Keys.D:
                        p1.movement.x = 0;
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
        p1.playerBody.applyForceToCenter(p1.movement, true);
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
