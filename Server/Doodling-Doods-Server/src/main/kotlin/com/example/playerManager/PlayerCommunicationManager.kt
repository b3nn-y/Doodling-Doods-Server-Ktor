package com.example.playerManager

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class PlayerCommunicationManager {
    private val playerSockets = ConcurrentHashMap<String, WebSocketSession>()

    fun connectPlayer(action: String){
        
    }

}