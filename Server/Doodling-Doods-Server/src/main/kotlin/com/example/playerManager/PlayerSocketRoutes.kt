package com.example.playerManager

import io.ktor.server.routing.*
import io.ktor.server.websocket.*

class PlayerSocketRoutes {

    fun Route.socket(communicationManager: PlayerCommunicationManager){
        route("/create"){
            webSocket {

            }
        }

        route("/join"){
            webSocket {

            }
        }
    }
}