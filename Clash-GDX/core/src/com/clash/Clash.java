package com.clash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

public class Clash extends Game {
	public static final String TITLE = "Clash", VERSION = "0.00";

	public static TitleScreen titleScreen = new TitleScreen();

	/**Server Variables**/
	private Socket socket;

	@Override
	public void create () {
		setScreen(titleScreen);

		/** Server code**/
		connectSocket();
		configSocketEvents();
	}


	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.getScreen().dispose();
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void connectSocket(){
		try{
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO","Connected");
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO","My id: " + id);
				}
				catch (JSONException e){
					Gdx.app.log("SocketIO","Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO","New Player Connect: " + id);
				}
				catch (JSONException e){
					Gdx.app.log("SocketIO","Error getting New PlayerID");
				}
			}
		});
	}
}
