package com.example.playerManager

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

class PlayerSocketRoutes {
}

//This file contains, the websockets routes, by which the client connects.
fun Route.socket(communicationManager: PlayerCommunicationManager){
    route("/connect"){
        webSocket {
            var isPlayerSuccessfullyConnected = false
            var player = communicationManager.connectPlayer(this)
            var room = ""
            var details: Player? = null
            if(player == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid"))
                return@webSocket
            }

            try {
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        val incomingMessage = (frame.readText())
                        if (!isPlayerSuccessfullyConnected){
                            if (communicationManager.checkIfTheInputIsOfPlayerDataType(incomingMessage)){
                                val playerDetails = communicationManager.assignTheirUserNameAndRoom(incomingMessage, player)
                                player = playerDetails.name
                                room = playerDetails.roomName
                                details = playerDetails
                                println("\n$player is being assigned to $room")
                                isPlayerSuccessfullyConnected = true
                            }

                        }
                        else{
                            communicationManager.incomingClientRequestModerator(player, room, incomingMessage)
                        }
                        println("\n\n\n")
                        println("$player: $incomingMessage")
                        println("\n\n\n")

                    }
                }
            } catch(e: Exception) {
                e.printStackTrace()
            } finally {
                if (details != null) {
                    communicationManager.disconnectPlayer(details!!)
                }
            }
        }
    }

}



