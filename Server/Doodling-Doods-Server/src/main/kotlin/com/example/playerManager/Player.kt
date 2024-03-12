package com.example.playerManager

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

//this is the player class, that we will get from the client once the join, we store this and manage the game, and score
@Serializable
data class Player(
    var name:String,
    var joinType:String,
    var roomName: String,
    var roomPass: String,
    var score: Int = 0,
    var noOfGuessedAnswers: Int = 0,
    var guest: Boolean = true,
    var admin: Boolean = false,
    var profile:Int = 0
)
