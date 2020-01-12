package com.clash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MapGenerator {
    Array<Wall> walls = new Array<Wall>();
    Array<Obstacle> obstacles = new Array<Obstacle>();

    public MapGenerator(String fileName) {
        JsonReader reader = new JsonReader();
        JsonValue base = reader.parse(Gdx.files.internal(fileName));
        for(JsonValue i : base.get("Walls")) {
            walls.add(new Wall(
                    i.getInt("x"),
                    i.getInt("y"),
                    i.getInt("width")/2,
                    i.getInt("height")/2
            ));
        }
        int cnt = 0;
        for(JsonValue i : base.get("Obstacles")) {
            obstacles.add(new Obstacle(
                    cnt++,
                    i.getInt("x"),
                    i.getInt("y"),
                    i.getInt("width")/2,
                    i.getInt("height")/2
            ));
        }
    }
}
