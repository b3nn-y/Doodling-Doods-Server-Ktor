package com.example
import RoomModerator
import com.example.playerManager.Player

import com.example.playerManager.TicTacToeGame
import com.example.plugins.*
import com.example.roomManager.Room
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import java.net.ServerSocket
import java.util.ArrayList

fun main(args: Array<String>) {
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

    try {
       EngineMain.main(args)
    } catch (e: Exception) {
        println(e)

    }

}

fun Application.module() {
    val communicationManager = TicTacToeGame()
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting(communicationManager)
}
