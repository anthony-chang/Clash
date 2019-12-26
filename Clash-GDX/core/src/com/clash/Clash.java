package com.clash;

import com.badlogic.gdx.Game;
import com.clash.screens.TitleScreen;

public class Clash extends Game {
	public static final String TITLE = "Clash", VERSION = "0.00";
	
	@Override
	public void create () {
		setScreen(new TitleScreen());
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
