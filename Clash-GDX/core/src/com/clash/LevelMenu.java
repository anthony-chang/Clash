package com.clash;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelMenu implements Screen {

    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private static List list;
    private ScrollPane scrollPane;
    private TextButton play, back;

    @Override
    public void render (float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void show () {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        list = new List<String>(skin);
        list.setItems(new String[] {"Sieve", "Open Field", "Maze", "Ball Pit", "Boxy"});

        scrollPane = new ScrollPane(list, skin);

        play = new TextButton("PLAY", skin, "big");
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });
        play.pad(15);

        back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen());
            }
        });
        play.pad(10);

        // putting stuff together
        table.add().width(table.getWidth() / 3);
        table.add("SELECT LEVEL").width(table.getWidth() / 3).center();
        table.add().width(table.getWidth() / 3).row();

        table.add(back);
        table.add(scrollPane).expandY();
        table.add(play);

        stage.addActor(table);
    }

    @Override
    public void hide () {

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }

    public static String getMap(){
        String map_selection = (String) list.getSelected();
        return map_selection;
    }

}
