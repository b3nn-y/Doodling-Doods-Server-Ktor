package com.example.roomManager

import com.example.playerManager.Player

data class Room(
    var name:String,
    var pass:String,
    var players: ArrayList<Player>,
    var noOfPlayersInRoom: Int = 0,
    var noOfGuessedAnswersInCurrentRound: Int = 0,
    var createdBy: Player,
    var admin: Boolean,
    var maxPlayers: Int = 10,
    var cords: String,
    var visibility: Boolean,
    var currentPlayer: Player,
    var rounds: Int = 3
)
