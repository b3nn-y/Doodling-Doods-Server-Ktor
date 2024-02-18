package com.example
import RoomModerator
import com.example.playerManager.Player
import com.example.playerManager.PlayerCommunicationManager
import com.example.plugins.*
import com.example.roomManager.Room
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import java.util.ArrayList

fun main() {
//    val roomCreator = RoomModerator()
//    roomCreator.addRoom("TestRoom1", Room("","", arrayListOf(Player("","","")),false))
//    roomCreator.addRoom("TestRoom2", Room("","", arrayListOf(Player("","","")),false))
//    roomCreator.addRoom("TestRoom3", Room("","", arrayListOf(Player("","","")),false))
//    roomCreator.addRoom("TestRoom4", Room("","", arrayListOf(Player("","","")),false))
//    roomCreator.addRoom("TestRoom5", Room("","", ArrayList(),false))
//    CoroutineScope(Job()).launch {
//        delay(4000)
//        roomCreator.addRoom("TestRoom6", Room("","", arrayListOf(Player("","","")),false))
//        roomCreator.addPlayerToRoom("TestRoom5")
//    }
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)


}

fun Application.module() {
    val communicationManager = PlayerCommunicationManager()
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting(communicationManager)
}
