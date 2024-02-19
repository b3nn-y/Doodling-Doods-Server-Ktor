package com.example

import com.example.demo.UserDao
import com.example.demo.UserDaoImpl
import com.example.plugins.*
import io.ktor.client.plugins.BodyProgress.Plugin.install
import io.ktor.http.*
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
<<<<<<< Updated upstream
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






=======
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
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
>>>>>>> Stashed changes


}

fun Application.module() {
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()


    val dao :UserDao = UserDaoImpl()
    DatabaseFactory.init()

    routing {


        post("/adduser") {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("names")
            val profilePic = formParameters.getOrFail("profilePic")
            val article = dao.adduser(name, profilePic)
            println("add user called")
            call.respond("User added successfully")
        }

        get("/users") {
            val listOfUsers = dao.allUsers()
            if(listOfUsers.isEmpty()){
                call.respond("emptyyyy")
            }else{
                call.respond(listOfUsers.toString())
            }
        }
    }

}
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}




