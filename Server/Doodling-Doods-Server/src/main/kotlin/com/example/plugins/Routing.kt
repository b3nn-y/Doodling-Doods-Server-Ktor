package com.example.plugins

import com.example.DatabaseFactory
import com.example.dao.RoomImpl
import com.example.dao.RoomsDao
import com.example.models.JsonRoomObject
//import com.example.playerManager.PlayerCommunicationManager
import com.example.playerManager.TicTacToeGame
import com.example.playerManager.socket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Application.configureRouting(communicationManager: TicTacToeGame) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    install(DoubleReceive)

    routing {
        socket(communicationManager)
    }
    routing {
        webSocket("/chat") {
            send("You are connected!")
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                send("You said: $receivedText")
            }
        }
    }
    routing {
        get("/") {
            call.respondText("Hello, Welcome to the Doodling Doods Server!!")
        }
    }

    val dao: RoomsDao = RoomImpl()

    DatabaseFactory.init()

    routing {
        post("/addRooms") {
            val fromParameters = call.receiveParameters()
            val roomId = fromParameters.getOrFail("room_id")
            val createdBy = fromParameters.getOrFail("created_by")
            val password = fromParameters.getOrFail("password")

            dao.addRooms(room_id = roomId, create_by = createdBy, password = password)

            call.respond("Room added successfully")

        }

        get("/rooms") {
            val listOfRooms = dao.allRooms()

            if (listOfRooms.isEmpty()) {
                call.respond("empty database")
            } else {

                val listOfJsonRooms = listOfRooms.map { room ->
                    JsonRoomObject(
                        room.id,
                        room.room_id,
                        room.created_by,
                        room.password
                    )
                }

                call.respond(listOfJsonRooms)
            }
        }


    }

}
