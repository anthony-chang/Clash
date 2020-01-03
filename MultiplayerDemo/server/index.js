var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(8080, function(){
    console.log("Server is now running...");
});

io.on('connection', function(socket){
    console.log("Player connected!");

    // this sends it to all connected sockets
    socket.emit('socketID', {id: socket.id});

    // this sends it to all other connected sockets but not the socket itself
    socket.broadcast.emit('newPlayer', {id: socket.id});

    socket.on('disconnect', function(socket){
        console.log("Player disconnected");
    })
});