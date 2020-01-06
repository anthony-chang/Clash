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
    socket.emit ('socketID', { id: socket.id }); // sends client's own ID to client

    // newPlayer Event
    socket.broadcast.emit ('newPlayer', { id: socket.id }); // sends client's own ID to all other clients

    // getPlayers Event
    //socket.emit ('getPlayers', players); // sends the players array

    socket.on('disconnect', function(){
        console.log("Player disconnected");

        // playerDisconnected Event
        socket.broadcast.emit('playerDisconnected', { id: socket.id }); // sends client's own ID to all other clients

        for (var i = 0; i < players.length; i++){
            if (players[i].id == socket.id){
                players.splice(i,1);
            }
        }
    });
    players.push(new player(socket.id, 0, 0, 6));
});

function player (id, x, y, health){
    this.id = id;
    this.x = x;
    this.y = y;
    this.health = health;
}