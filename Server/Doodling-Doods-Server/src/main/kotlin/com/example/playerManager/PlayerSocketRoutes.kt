package com.example.playerManager

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

class PlayerSocketRoutes {
}
fun Route.socket(communicationManager: PlayerCommunicationManager){
    route("/create"){
        webSocket {
            send("Hello")
            println("Hellow")
            val details = receiveDetails()
            val player = communicationManager.connectPlayer(details, "create")
            if (player == null){
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "wrong info"))
            }

        }
    }

    route("/join"){
        webSocket {
//            val details = receiveDetails()
//            val player = communicationManager.connectPlayer(details, "join")
//            if (player == null){
//                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "wrong info"))
//            }

        }
    }
}
suspend fun WebSocketSession.receiveDetails(): Player {
    val frame = incoming.receive()
    val byteArray = frame.readBytes()
    val jsonString = byteArray.decodeToString()
    val details = Json.decodeFromString<Player>(jsonString)
    return details
}