var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var bullets = [];

server.listen(8080, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket){
    console.log("Player connected!");

    // socketID Event
    socket.emit ('socketID', { id: socket.id }); // sends client's own ID to own client

    // newPlayer Event
    socket.broadcast.emit ('newPlayer', { id: socket.id }); // sends client's own ID to all other clients

    // bulletShot Event
    socket.on('bulletShot', function(data){
        socket.broadcast.emit('bulletShot', data);
    });

    // playerMoved Event
    socket.on('playerMoved', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);

        /**
         * uncomment for testing purposes
        console.log("playerMoved: " + "ID: " + data.id
                                    + " positionX: " + data.positionX
                                    + " positionY: " + data.positionY
                                    + " movementX: " + data.movementX
                                    + " movementY: " + data.movementY);
        **/

        // update players
        for (var i = 0; i < players.length; i++) {
            if (players[i].id === data.id) {
                players[i].positionX = data.positionX;
                players[i].positionY = data.positionY;
                players[i].movementX = data.movementX;
                players[i].movementY = data.movementY;
                players[i].health = data.health;
            }
        }

    });

    socket.on('disconnect', function(){
        console.log("Player disconnected");

        // playerDisconnected Event
        // sends client's own ID to all other clients
        socket.broadcast.emit('playerDisconnected', { id: socket.id });

        for (var i = 0; i < players.length; i++){
            if (players[i].id == socket.id){
                players.splice(i,1);

                // countPlayers Event
                // sends the number of players on the server to all clients
                io.sockets.emit('countPlayers', { total_players: players.length });
            }
        }
    });

    players.push(new player(socket.id, 0, 0, 6));

    // countPlayers Event
    // sends the number of players on the server to all clients
    io.sockets.emit('countPlayers', { total_players: players.length });

    // simpleID Event
    // sends the client's simpleID to own client (1 or 2)
    // Warning: bug when p1 disconnects and a new player connects (Fix later)
    socket.emit('simpleID', { simpleID: players.length });

});

function player (id, positionX, positionY, movementX, movementY, health){
    this.id = id;
    this.positionX = positionX;
    this.positionY = positionY;
    this.movementX = movementX;
    this.movementY = movementY;
    this.health = health;
}