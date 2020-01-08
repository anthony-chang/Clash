package com.clash.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.clash.GameScreen;
import com.clash.PlayerBody;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {
    World world;
    PlayerBody p1, p2;

    Socket socket;

    // other variables
    int total_players;
    int simpleID;
    String socketID;

    public Server(World world, PlayerBody p1, PlayerBody p2) {
        this.world = world;
        this.p1 = p1;
        this.p2 = p2;
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

    public void updateServer(){
        JSONObject data = new JSONObject();
        /**
        try{
            data.put("x", player.getX());
            data.put("y", player.getY());
            socket.emit("playerMoved", data);
        }
        catch (JSONException e){
            Gdx.app.log("SocketIO", "Error sending update data");
        }
         **/
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
                /**
                try{
                    String playerID = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if (friendlyPlayers.get(playerID) != null){
                        friendlyPlayers.get(playerID).setPosition(x.floatValue(), y.floatValue());
                    }
                }
                catch(JSONException e){
                    Gdx.app.log("SocketIO","Error getting playerMoved data");
                }
                 **/
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
}
