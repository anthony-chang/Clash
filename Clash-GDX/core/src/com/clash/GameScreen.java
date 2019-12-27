package com.clash;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    //Collision filtering categories
    final static short CATEGORY_PLAYER1 = (short) 1;
    final static short CATEGORY_PLAYER2 = (short) 2;
    final static short CATEGORY_BULLET = (short) 4;
    final static short CATEGORY_MAP = (short) 16;

    private final static float TIMESTEP = 1/60f;
    private final static int VELOCITYITERATIONS = 30, POSITIONITERATIONS = 15;
    final static int WIDTH = 160, HEIGHT = 90; //metres

    private final boolean accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private PlayerBody p1;
    private Wall border;

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionDetection());
        debugRenderer = new Box2DDebugRenderer(); //TODO remove later
        camera = new OrthographicCamera(WIDTH, HEIGHT); //16:9 aspect ratio

        p1 = new PlayerBody(1);
        border = new Wall(WIDTH, HEIGHT);

        /**Testing Movable objects**/
        Obstacle obstacle1 = new Obstacle();
        obstacle1.addObstacleToWorld(world);

        /**Testing Immovable wall objects**/ //TODO: implement JSON file to create levels
        Wall wall1 = new Wall(new Vector2[] {
                new Vector2(-1, 3),
                new Vector2(1, 3),
                new Vector2(1, -3),
                new Vector2(-1, -3),
                new Vector2(-1, 3)
        });
        wall1.addWallWorld(world);

        p1.addPlayerToWorld(world);
        border.addWallWorld(world);

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        p1.move(0, 1);
                        break;
                    case Input.Keys.A:
                        p1.move(-1, 0);
                        break;
                    case Input.Keys.S:
                        p1.move(0, -1);
                        break;
                    case Input.Keys.D:
                        p1.move(1, 0);
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
                if(p1.ammo > 0) {
                    --p1.ammo;
                    //convert mouse (x, y) in pixels (with origin at top left) to (x, y) in metres (with origin at centre)
                    float x_metres = ((float) screenX) / ((float) Gdx.graphics.getWidth()) * WIDTH - WIDTH / 2f;
                    float y_metres = HEIGHT / 2f - ((float) screenY) / ((float) Gdx.graphics.getHeight()) * HEIGHT;
                    Bullet bullet = new Bullet(1, p1.getPosition().x, p1.getPosition().y, x_metres, y_metres);
                    bullet.addBulletToWorld(world);
                }
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

        deleteBullets();

        p1.updateAmmo(Gdx.graphics.getRawDeltaTime());

        if(accelerometerAvailable) { //mobile controls
            p1.moveUsingAccelerometer(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY());
        }
        p1.playerBody.applyForceToCenter(p1.movement, true);
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        /*Remove if camera not centred about player*/
        camera.position.set(p1.getPosition().x, p1.getPosition().y, 0);
        camera.update();

        debugRenderer.render(world, camera.combined); //TODO remove later
    }
    private void deleteBullets() {
        if(world.getBodyCount() > 0) {
            Array<Body> bodies = new Array<Body>();
            world.getBodies(bodies);
            for (int i = 0; i < bodies.size; ++i) {
                if (bodies.get(i).getUserData().equals("DELETE")) {
                    world.destroyBody(bodies.get(i));
                }
            }
        }
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
        p1.playerShape.dispose();
        border.wallShape.dispose();
    }
}
