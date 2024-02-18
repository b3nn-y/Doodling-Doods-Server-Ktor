package com.example


import com.example.dao.RoomImpl
import com.example.dao.RoomsDao
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*


import java.net.ServerSocket


fun main() {
    val port =8081
    var serverSocket:ServerSocket?=null
    try {
        serverSocket =ServerSocket(port)
        DatabaseFactory.init()
        embeddedServer(Netty, port = 8081, host = "127.0.0.1", module = Application::module)
            .start(wait = true)
    }catch (e:Exception){
        println(e)
    }finally {
        serverSocket?.close()
    }

}

fun Application.module() {
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()

    val dao: RoomsDao = RoomImpl()

    DatabaseFactory.init()

    routing {
        post("/addRooms"){
            val fromParameters = call.receiveParameters()
            val roomId = fromParameters.getOrFail("room_id")
            val createdBy = fromParameters.getOrFail("created_by")
            val password = fromParameters.getOrFail("password")

            dao.addRooms(room_id = roomId, create_by = createdBy, password = password)

            call.respond("Room added successfully")

        }

        get("/rooms"){
            val listOfRooms = dao.allRooms()

            if (listOfRooms.isEmpty()){
                call.respond("empty database")
            }else{
                call.respond(listOfRooms.toString())
            }
        }


    }



}
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}




