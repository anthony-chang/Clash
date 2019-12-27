package com.clash;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private Vector3 initialAccelerometerState;
    Matrix4 calibrationMatrix;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private PlayerBody p1;
    private Wall border;

    private SpriteBatch hud = new SpriteBatch();
    private Texture bulletTexture = new Texture("bullet.png");

    @Override
    public void show() {
        /**set up world and camera**/
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionDetection());
        debugRenderer = new Box2DDebugRenderer(); //TODO remove later
        camera = new OrthographicCamera(WIDTH, HEIGHT); //16:9 aspect ratio

        /**set up accelerometer calibration**/
        //takes in accelerometer data when play button is pressed, and sets that as the "zero" position
        //apply a rotation matrix for inputs all inputs afterwards
        initialAccelerometerState = new Vector3(Gdx.input.getAccelerometerX(), 0, Gdx.input.getAccelerometerZ());
        Vector3 temp = new Vector3(0, 0, 1);
        Vector3 temp2 = new Vector3(initialAccelerometerState).nor(); //normalize vector
        Quaternion rotateQuaternion = new Quaternion().setFromCross(temp, temp2); //bruh moment
        Matrix4 mat = new Matrix4(Vector3.Zero, rotateQuaternion, new Vector3(1f, 1f, 1f));
        calibrationMatrix = mat.inv(); //invert the matrix so it can be applied later

        /**Set up the objects in the world**/
        p1 = new PlayerBody(1);
        border = new Wall(WIDTH, HEIGHT);

        //Testing Movable objects
        Obstacle obstacle1 = new Obstacle();
        obstacle1.addObstacleToWorld(world);

        //Testing Immovable wall objects //TODO: implement JSON file to create levels
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

        /**inline input processor functions**/
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
        /**set background colour**/
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /**Render the HUD**/
        hud.begin();
        int bulletWidth = bulletTexture.getWidth(), bulletHeight = bulletTexture.getHeight();
        for(int i = 0; i < p1.ammo; ++i) { //draw the bullets images on the hud
            hud.draw(bulletTexture, (int) (Gdx.graphics.getWidth() - (bulletWidth * 1.05)), i * bulletHeight);
        }
        if(p1.ammo < p1.MAX_AMMO) //show part of a bullet for reload
            hud.draw(bulletTexture, (int) (Gdx.graphics.getWidth() - (bulletWidth * 1.05)), p1.ammo * bulletHeight,
                    0,
                    0,
                    (int) (bulletWidth*p1.getReloadPercentage()),
                    bulletHeight);
        hud.end();

        /**Update player characteristics**/
        deleteBullets(); //clear the screen of bullets that have collided with things

        p1.updateAmmo(Gdx.graphics.getRawDeltaTime()); //update the ammo of the player

        if(accelerometerAvailable) { //mobile controls
            Vector2 temp = calibrateAccelerometerXYZ(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());
            p1.moveUsingAccelerometer(temp.x, temp.y);
        }
        p1.playerBody.applyForceToCenter(p1.movement, true); //move the player
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS); //simulate the physics

        /**Centre camera about player**/
        camera.position.set(p1.getPosition().x, p1.getPosition().y, 0);
        camera.update();

        debugRenderer.render(world, camera.combined); //TODO remove later
    }
    private Vector2 calibrateAccelerometerXYZ(float x, float y, float z) {
        Vector3 temp = new Vector3(x, y, z);
        temp.mul(calibrationMatrix);
        return new Vector2(temp.x, temp.y);
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
