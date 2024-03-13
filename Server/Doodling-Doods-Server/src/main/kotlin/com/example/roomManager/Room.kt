package com.example.roomManager

import com.example.playerManager.Player
import kotlinx.serialization.Serializable

//This is the room class, where all the game data is passed with
@Serializable
data class Room(
    var name:String,
    var pass:String,
    var players: ArrayList<Player>,
    var noOfPlayersInRoom: Int = 0,
    var noOfGuessedAnswersInCurrentRound: Int = 0,
    var createdBy: Player,
    var maxPlayers: Int = 10,
    var cords:String  =  "",
    var visibility: Boolean = true,
    var currentPlayer: Player? = null,
    var rounds: Int = 3,
    var currentWordToGuess:String? = null,
    var gameStarted:Boolean = false,
    var gameMode:String = "Guess The Word",
    var wordList: ArrayList<String>,
    var guessedPlayers: ArrayList<String>,
    var timer: Int = 0,
    var messages: ArrayList<ChatMessages>,
    var numberOfRoundsOver: Int = 0,
    var gameOver:Boolean = false,
    var iosCords: ArrayList<IosCords>,
    var isWordChosen: Boolean = false,
    var wordType:String,
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
    var currentWordToGuess:String? = null,
    var gameMode:String = "Guess The Word",
    var wordList: ArrayList<String>,
    var guessedPlayers: ArrayList<String>

)


@Serializable
data class ChatMessages(
    var player: String,
    var room:String,
    var msgID: String,
    val msg: String,
    val msgColor: String,
    var visible: Boolean,
    var lifeCycle:Boolean = true
)

@Serializable
data class IosCords(
    var points: ArrayList<Point>,
    var color: RGB,
    var lineWidth: Double
)

@Serializable
data class Point(
    var x: Double,
    var y: Double
)

@Serializable
data class RGB(
    var red: Double,
    var green: Double,
    var blue: Double
)


