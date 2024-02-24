package com.example.roomManager

import com.example.playerManager.Player
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    var name:String,
    var pass:String,
    var players: ArrayList<Player>,
    var noOfPlayersInRoom: Int = 0,
    var noOfGuessedAnswersInCurrentRound: Int = 0,
    var createdBy: Player,
    var maxPlayers: Int = 10,
    var cords: String = "",
    var visibility: Boolean = true,
    var currentPlayer: Player? = null,
    var rounds: Int = 3,
    var currentWordToGuess:String? = null,
    var gameStarted:Boolean = false
)

@Serializable
data class TestRoom(
    var name:String,
    var pass:String,
    var players: String,
    var noOfPlayersInRoom: Int = 0,
    var noOfGuessedAnswersInCurrentRound: Int = 0,
    var createdBy: String,
    var maxPlayers: Int = 10,
    var cords: String = "",
    var visibility: Boolean = true,
    var currentPlayer: Player? = null,
    var rounds: Int = 3,
    var currentWordToGuess:String? = null
)
