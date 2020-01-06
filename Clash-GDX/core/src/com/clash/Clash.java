package com.clash;

import com.badlogic.gdx.Game;


public class Clash extends Game {
	public static final String TITLE = "Clash", VERSION = "0.00";

	public static TitleScreen titleScreen = new TitleScreen();

	@Override
	public void create () {
		setScreen(titleScreen);
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
