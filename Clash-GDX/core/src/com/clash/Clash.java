package com.clash;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Clash extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private Texture p1_img;
	private PlayerSprite p1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		p1 = new PlayerSprite(1);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		p1.movePlayer();
		p1.movePlayer(p1.vx*-0.3f, p1.vy*-0.3f); //friction

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(p1.getSprite(), p1.x, p1.y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		p1_img.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
			p1.movingLeft = true;
		if(keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
			p1.movingRight = true;
		if(keycode == Input.Keys.W || keycode == Input.Keys.UP)
			p1.movingUp = true;
		if(keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
			p1.movingDown = true;

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
			p1.movingLeft = false;
		if(keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
			p1.movingRight = false;
		if(keycode == Input.Keys.W || keycode == Input.Keys.UP)
			p1.movingUp = false;
		if(keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
			p1.movingDown = false;

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

}
