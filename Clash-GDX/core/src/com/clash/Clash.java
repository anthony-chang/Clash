package com.clash;

import com.badlogic.gdx.Game;
import io.socket.client.IO;
import io.socket.client.Socket;

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
}
