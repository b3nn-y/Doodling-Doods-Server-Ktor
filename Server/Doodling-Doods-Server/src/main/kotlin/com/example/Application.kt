package com.example

import com.example.playerManager.PlayerCommunicationManager
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

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
    val communicationManager = PlayerCommunicationManager()
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting(communicationManager)
}
