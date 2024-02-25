package com.game.doodlingdoods.filesForServerCommunication

//This class is used to send the client with data, when the request if the room is available, with its pass.
data class RoomAvailability(
    var roomAvailable: Boolean,
    var roomPass: String
)
