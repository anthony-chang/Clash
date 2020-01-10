package com.clash.server;

import com.badlogic.gdx.Gdx;
import com.clash.PlayerBody;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import com.badlogic.gdx.math.Vector2;

public class Server {
    PlayerBody thisPlayer, opponentPlayer;

    Socket socket;

    // other variables
    int total_players;
    int simpleID;
    String socketID;

    // position, movement and health of other player
    public Vector2 opponent_position = new Vector2();
    public Vector2 opponent_movement = new Vector2();
    public int opponent_health;

    // bullet of other player
    public int bullet_ID;
    public float bullet_sourceX;
    public float bullet_sourceY;
    public float bullet_targetX;
    public float bullet_targetY;
    public boolean bullet_AUTO_AIM;
    public boolean newBullet = false;

    public Server(PlayerBody p) {
        thisPlayer = p;
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
                    socketID = id;
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
                    Gdx.app.log("SocketIO","New Player Connected: " + id);

                }
                catch (JSONException e){
                    Gdx.app.log("SocketIO","Error getting New PlayerID");
                }
            }
        }).on("countPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    total_players = data.getInt("total_players");
                    Gdx.app.log("SocketIO", "Total players connected: " + total_players);
                }
                catch (JSONException e){
                    Gdx.app.log("SocketIO","Error getting total number of players");
                }
            }
        }).on("simpleID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    simpleID = data.getInt("simpleID");
                    Gdx.app.log("SocketIO", "My simpleID: " + simpleID);
                }
                catch (JSONException e){
                    Gdx.app.log("SocketIO","Error getting simpleID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO","Player Disconnected: " + id);
                }
                catch (JSONException e){
                    Gdx.app.log("SocketIO","Error getting disconnected PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];

                try{
                    // movement
                    Double positionX = data.getDouble("positionX");
                    Double positionY = data.getDouble("positionY");
                    Double velocityX = data.getDouble("velocityX");
                    Double velocityY = data.getDouble("velocityY");

                    opponent_position.x = positionX.floatValue();
                    opponent_position.y = positionY.floatValue();
                    opponent_movement.x = velocityX.floatValue();
                    opponent_movement.y = velocityY.floatValue();

                    // health
                    int health = data.getInt("health");

                    opponent_health = health;

                    // print to console
                    //System.out.println("opponent_health: " + opponent_health);
                    //System.out.println ("opponent_pos: " + opponent_position.x + " " + opponent_position.y);
                }
                catch(JSONException e){
                    Gdx.app.log("SocketIO","Error getting playerMoved data");
                }

            }
        }).on("bulletShot", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                newBullet = true;
                try{
                    // movement
                    bullet_ID = data.getInt("ID");
                    double bullet_sourceX_0 = data.getDouble("thisPlayerPositionX");
                    double bullet_sourceY_0 = data.getDouble("thisPlayerPositionY");
                    double bullet_targetX_0 = data.getDouble("opponentPlayerPositionX");
                    double bullet_targetY_0 = data.getDouble("opponentPlayerPositionY");
                    bullet_AUTO_AIM = data.getBoolean("AUTO_AIM");

                    // convert to float
                    bullet_sourceX = (float) bullet_sourceX_0;
                    bullet_sourceY = (float) bullet_sourceY_0;
                    bullet_targetX = (float) bullet_targetX_0;
                    bullet_targetY = (float) bullet_targetY_0;

                    // print to console
                    //System.out.println("opponent_health: " + opponent_health);
                    //System.out.println ("opponent_pos: " + opponent_position.x + " " + opponent_position.y);
                    System.out.println(bullet_sourceX + ", " + bullet_sourceY);
                    System.out.println(bullet_targetX + ", " + bullet_targetY);
                    System.out.println("--------------------------------------");
                }
                catch(JSONException e){
                    Gdx.app.log("SocketIO","Error getting bulletShot data");
                }

            }
        });
    }

    // gives the number of players in the server
    public int getTotalPlayers() {
        return total_players;
    }

    // gives the client's simpleID
    public int getSimpleID() {
        return simpleID;
    }

    // gives the client's socketID
    public String getSocketID() {
        return socketID;
    }

    public Socket getSocket(){
        return socket;
    }
}
