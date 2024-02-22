package com.example.playerManager

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

class PlayerSocketRoutes {
}
fun Route.socket(communicationManager: PlayerCommunicationManager){
    route("/connect"){
        webSocket {
            var isPlayerSuccessfullyConnected = false
            var player = communicationManager.connectPlayer(this)
            var room = ""
            if(player == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid"))
                return@webSocket
            }

            try {
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        val incomingMessage = (frame.readText())
                        if (!isPlayerSuccessfullyConnected){
                            val playerDetails = communicationManager.assignTheirUserName(incomingMessage, player)
                            player = playerDetails.name
                            room = playerDetails.roomName
                            isPlayerSuccessfullyConnected = true
                        }
                        else{
                            communicationManager.incomingClientRequestModerator(player, room, incomingMessage)
                        }
                        println("\n\n\n")
                        println(incomingMessage)
                        println("\n\n\n")

                    }
                }
            } catch(e: Exception) {
                e.printStackTrace()
            } finally {
                communicationManager.disconnectPlayer(player)
            }
        }
    }
}



