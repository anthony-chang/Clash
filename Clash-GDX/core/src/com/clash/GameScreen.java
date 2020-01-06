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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    /**Settings stuff**/
    static boolean AUTO_AIM = false; //set to true for bullets to target opponent automatically
                                    //set to false for bullets to target mouse location

    /**Collision filtering categories**/
    final static short CATEGORY_PLAYER1 = (short) 1;
    final static short CATEGORY_PLAYER2 = (short) 2;
    final static short CATEGORY_BULLET = (short) 4;
    final static short CATEGORY_MAP = (short) 16;

    /**Simulation properties and screen size**/
    private final static float TIMESTEP = 1/60f;
    private final static int VELOCITYITERATIONS = 30, POSITIONITERATIONS = 15;
    static int WIDTH_PIXELS = Gdx.graphics.getWidth(), HEIGHT_PIXELS = Gdx.graphics.getHeight();
    final static int WIDTH = WIDTH_PIXELS/10, HEIGHT = HEIGHT_PIXELS/10; //metres


    /**Accelerometer stuff**/
    private final boolean accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
    private Vector3 initialAccelerometerState;
    Matrix4 calibrationMatrix;

    /**Game objects**/
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera viewCamera;
    private Viewport viewPort;
    private PlayerBody p1, p2;
    private Wall border;
    private MapGenerator map;

    /**Player Objects**/
    private SpriteBatch players = new SpriteBatch();


    /**HUD objects**/
    private SpriteBatch hud = new SpriteBatch();
    private Texture bulletTexture = new Texture("bullet.png");

    @Override
    public void show() {
        /**set up world and camera**/
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionDetection());
        debugRenderer = new Box2DDebugRenderer(); //TODO remove later
        viewCamera = new OrthographicCamera(WIDTH, HEIGHT); //16:9 aspect ratio
        viewCamera.position.set(WIDTH/2, HEIGHT/2, 0);
        viewPort = new FitViewport(WIDTH, HEIGHT, viewCamera);
        players.setProjectionMatrix(viewCamera.combined);

        /**set up accelerometer calibration**/
        //takes in accelerometer data when play button is pressed, and sets that as the "zero" position
        //apply a rotation matrix for inputs all inputs afterwards
        if(accelerometerAvailable) {
            initialAccelerometerState = new Vector3(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());
            Vector3 temp = new Vector3(0, 0, 1);
            Vector3 temp2 = new Vector3(initialAccelerometerState).nor(); //normalize vector
            Quaternion rotateQuaternion = new Quaternion().setFromCross(temp, temp2); //bruh moment
            Matrix4 mat = new Matrix4(Vector3.Zero, rotateQuaternion, new Vector3(1f, 1f, 1f));
            calibrationMatrix = mat.inv(); //invert the matrix so it can be applied later
        }

        /**Set up the objects in the world**/
        p1 = new PlayerBody(1);
        p1.addPlayerToWorld(world);
        p2 = new PlayerBody(2);
        p2.addPlayerToWorld(world);
        border = new Wall(WIDTH, HEIGHT);
        border.addWallWorld(world);

        //create the map using the JSON files
        if (LevelMenu.getMap() == "Sieve"){
            map = new MapGenerator("maps/map_1.json");
        }
        else if (LevelMenu.getMap() == "Open Field"){
            map = new MapGenerator("maps/map_2.json");
        }
        else if (LevelMenu.getMap() == "Maze"){
            map = new MapGenerator("maps/map_3.json");
        }
        else if (LevelMenu.getMap() == "Ball Pit"){
            map = new MapGenerator("maps/map_4.json");
        }
        else if (LevelMenu.getMap() == "Boxy"){
            map = new MapGenerator("maps/map_5.json");
        }
        else if (LevelMenu.getMap() == "Pillars"){
            map = new MapGenerator("maps/map_6.json");
        }
        else { // safety
            map = new MapGenerator("maps/map_1.json");
        }

        for(Wall i : map.walls) {
            i.addWallWorld(world);
        }
        for(Obstacle i : map.obstacles) {
            i.addObstacleToWorld(world);
        }

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
                    Bullet bullet;
                    if(AUTO_AIM) {
                        bullet = new Bullet(1, p1.getPositionMetres().x, p1.getPositionMetres().y, p2.getPositionMetres().x, p2.getPositionMetres().y, AUTO_AIM);
                    }
                    else {
                        //convert mouse (x, y) in pixels (with origin at top left) to (x, y) in metres (with origin at centre)
                        float x_metres = ((float) screenX) / ((float) Gdx.graphics.getWidth()) * WIDTH - WIDTH / 2f;
                        float y_metres = HEIGHT / 2f - ((float) screenY) / ((float) Gdx.graphics.getHeight()) * HEIGHT;
                        bullet = new Bullet(1, p1.getPositionMetres().x, p1.getPositionMetres().y, x_metres, y_metres, AUTO_AIM);
                    }
                    bullet.addBulletToWorld(world);
                }
                /**Testing Player 2 bullets**/ //TODO remove later
                //p2 just fires when p1 fires
                //auto aim at player 1
                /*if(p2.ammo > 0) {
                    --p2.ammo;
                    Bullet bullet2 = new Bullet(2, p2.getPositionMetres().x, p2.getPositionMetres().y, p1.getPositionMetres().x, p1.getPositionMetres().y, true);
                    bullet2.addBulletToWorld(world);
                }*/
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
        //Ammo indicator
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

        /**Render the players and their health bars**/
        players.setProjectionMatrix(viewCamera.combined);
        players.begin();
        p1.draw(players);
        p2.draw(players);
        players.end();

        /**Update player characteristics**/
        updateBodies(); //clear the screen of bullets that have collided with things, and update player health

        float deltaTime = Gdx.graphics.getRawDeltaTime();
        p1.updateAmmo(deltaTime); //update the ammo of the players
        p2.updateAmmo(deltaTime);

        if(accelerometerAvailable) { //mobile controls
            Vector2 temp = calibrateAccelerometerXYZ(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());
            p1.moveUsingAccelerometer(temp.x, temp.y);
        }
        p1.playerBody.applyForceToCenter(p1.movement, true); //move the player
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS); //simulate the physics

        /**Centre camera about player**/
        viewCamera.position.set(p1.getPositionMetres().x, p1.getPositionMetres().y, 0);
        viewCamera.update();

        debugRenderer.render(world, viewCamera.combined); //TODO remove later

    }
    private Vector2 calibrateAccelerometerXYZ(float x, float y, float z) {
        Vector3 temp = new Vector3(x, y, z);
        temp.mul(calibrationMatrix);
        return new Vector2(temp.x, temp.y);
    }
    private void updateBodies() {
        if(world.getBodyCount() > 0) {
            Array<Body> bodies = new Array<Body>();
            world.getBodies(bodies);
            for (int i = 0; i < bodies.size; ++i) {
                if (bodies.get(i).getUserData().equals("DELETE")) { //flagged for deletion
                    world.destroyBody(bodies.get(i));
                }
                else if (bodies.get(i).getUserData().equals("PLAYER1_DECREMENT_HEALTH")) { //flagged player 1 as hit
                    --p1.health;
                    p1.playerBody.setUserData("PLAYER1");
                }
                else if(bodies.get(i).getUserData().equals("PLAYER2_DECREMENT_HEALTH")) {//flagged player 2 as hit
                    --p2.health;
                    p2.playerBody.setUserData("PLAYER2");
                }

                if(p1.health == 0) {
                    System.out.println("Player 2 wins");
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
                }
                else if(p2.health == 0) {
                    System.out.println("Player 1 wins");
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
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
        for(Texture i:p1.healthBar)
            i.dispose();
        p2.playerShape.dispose();
        for(Texture i:p2.healthBar)
            i.dispose();

        bulletTexture.dispose();
        border.wallShape.dispose();
        Array<Body> array = new Array<Body>();
        world.getBodies(array);
        for (int i = 0; i < array.size; ++i) {
            world.destroyBody(array.get(i));
        }
    }
}
