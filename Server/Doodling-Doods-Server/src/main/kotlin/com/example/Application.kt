package com.example

import com.example.playerManager.Player
import com.example.playerManager.PlayerCommunicationManager
import com.example.plugins.*
import com.example.roomManager.Room
import com.example.roomManager.RoomModerator
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    RoomModerator.addRoom("TestRoom1", Room("TestRoom1","ben", arrayListOf(Player("testBot","","","")),0,1,Player("","", "",""),9,
        "", false,Player("","", "","",),8,"", wordList = arrayListOf(), guessedPlayers = arrayListOf(), messages = arrayListOf(), iosCords = arrayListOf(), wordType = "ZohoProducts"))
//    val roomCreator = com.example.roomManager.RoomModerator()
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
//    CoroutineScope(Dispatchers.Default).launch {
//        delay(10000)
//        RoomModerator.startGame("TestRoom1")
//    }

    try {
       EngineMain.main(args)
    } catch (e: Exception) {
        println(e)

    }

}

fun Application.module() {
    val communicationManager = PlayerCommunicationManager
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting(communicationManager)
}
