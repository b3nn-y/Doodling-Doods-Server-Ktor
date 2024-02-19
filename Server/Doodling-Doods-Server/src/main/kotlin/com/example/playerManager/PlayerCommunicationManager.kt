package com.example.playerManager

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class PlayerCommunicationManager {
    private val playerSockets = ConcurrentHashMap<String, WebSocketSession>()

    fun connectPlayer(player: Player, action: String): String? {
        println(player)
        println(action)
        return player.name
    }

}

