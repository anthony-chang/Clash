package com.multiplayerdemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MultiplayerDemo extends ApplicationAdapter {
	SpriteBatch batch;
	private Socket socket;
	String id;
	PlayerBody player;
	Texture playerShip;
	Texture friendlyShip;
	HashMap<String, PlayerBody> friendlyPlayers;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerShip = new Texture("blue_player.png");
		friendlyShip = new Texture("red_player.png");
		friendlyPlayers = new HashMap<String, PlayerBody>();

		// initialize socket and socket events
		connectSocket();
		configSocketEvents();
	}

	public void handleInput(float dt){
		if (player != null){
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				player.setPosition(player.getX() + (-200 * dt), player.getY());
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setPosition(player.getX() + (200 * dt), player.getY());
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
				player.setPosition(player.getX(), player.getY() + (200 * dt));
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.setPosition(player.getX(), player.getY() - (200 * dt));
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput(Gdx.graphics.getDeltaTime());

		batch.begin();
		if (player != null){
			player.draw(batch);
		}
		for (HashMap.Entry<String, PlayerBody> entry : friendlyPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		playerShip.dispose();
		friendlyShip.dispose();
	}

	public void connectSocket(){
		try{
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO","Connected");
				player = new PlayerBody(playerShip);
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					id = data.getString("id");
					Gdx.app.log("SocketIO", "My id: " + id);
				}
				catch(JSONException e){
					Gdx.app.log("SocketIO","Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					id = data.getString("id");
					Gdx.app.log("SocketIO", "New player connect: " + id);
					friendlyPlayers.put(id, new PlayerBody(friendlyShip));
				}
				catch(JSONException e){
					Gdx.app.log("SocketIO","Error getting New PlayerID");
				}
			}
		});
	}
}
