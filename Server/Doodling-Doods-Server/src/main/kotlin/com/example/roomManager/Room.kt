package com.example.roomManager

import com.example.playerManager.Player

data class Room(
    val name:String,
    val pass:String,
    val players: ArrayList<Player>,
    val admin: Boolean,
    val maxPlayers: Int = 10
)
