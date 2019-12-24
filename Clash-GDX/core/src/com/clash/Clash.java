package com.clash;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Clash extends ApplicationAdapter {
	SpriteBatch batch;
	Texture p1_img;
	PlayerSprite p1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		p1 = new PlayerSprite(1);
	}

	@Override
	public void render () {
		p1.getInput(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT),
				Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT),
				Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP),
				Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN));

		p1.movePlayer(p1.getVelocityX()*-0.3f, p1.getVelocityY()*-0.3f); //friction

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(p1.getSprite(), p1.getX(), p1.getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		p1_img.dispose();
		batch.dispose();
	}
}
