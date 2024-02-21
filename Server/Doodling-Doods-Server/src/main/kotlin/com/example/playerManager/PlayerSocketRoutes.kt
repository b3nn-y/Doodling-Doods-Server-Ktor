package com.example.playerManager

import com.google.gson.Gson
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

class PlayerSocketRoutes {
}
fun Route.socket(communicationManager: TicTacToeGame){
    route("/connect"){
        webSocket {
            val player = communicationManager.connectPlayer(this, "")
            if(player == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid"))
                return@webSocket
            }

            try {
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text) {
                        val action = (frame.readText())
                        println("\n\n\n")
                        println(action)
                        println("\n\n\n")
                        send(Gson().toJson(Message("Hello")))
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

//    route("/join"){
//        webSocket {
////            val details = receiveDetails()
////            val player = communicationManager.connectPlayer(details, "join")
////            if (player == null){
////                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "wrong info"))
////            }
//
//        }
//    }



data class Message(
    val data: String
)