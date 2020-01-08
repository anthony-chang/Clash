var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

server.listen(8080, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket){
    console.log("Player connected!");

    // socketID Event
    socket.emit ('socketID', { id: socket.id }); // sends client's own ID to own client

    // newPlayer Event
    socket.broadcast.emit ('newPlayer', { id: socket.id }); // sends client's own ID to all other clients

    // playerMoved Event
    socket.on('playerMoved', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);
        console.log("playerMoved: " + data.id);

        /**
        console.log("playerMoved: " + "ID: " + data.id
            + "X: " + data.x
            + "Y: " + data.y);

        for (var i = 0; i < players.length; i++){
            if (players[i].id === data.id){
                players[i].x = data.x;
                players[i].y = data.y;
            }
        }
         **/
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

function player (id, x, y, health){
    this.id = id;
    this.x = x;
    this.y = y;
    this.health = health;
}