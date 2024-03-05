package com.example.plugins

import com.example.DatabaseFactory
import com.example.leaderBoard.LeaderBoardDao
import com.example.leaderBoard.LeaderBoardDataClass
import com.example.leaderBoard.LeaderBoardImpl


import com.example.roomsDao.RoomImpl
import com.example.roomsDao.RoomsDao
import com.example.roomsDao.JsonRoomObject
import com.example.playerManager.PlayerCommunicationManager
import com.example.playerManager.socket
import com.example.roomManager.RoomModerator
import com.example.roomsDao.RoomAvailabilityDataClass
import com.example.usersDao.AuthenticationDataClass
import com.example.usersDao.UsersDao
import com.example.usersDao.UsersImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting(communicationManager: PlayerCommunicationManager) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(DoubleReceive)

    routing {
        socket(communicationManager)
    }

    routing {
        get("/") {
            call.respondText("Hello, Welcome to the Doodling Doods Server!!")
        }
    }

    val roomsDao: RoomsDao = RoomImpl()
    val usersDao: UsersDao = UsersImpl()
    val leaderBoardDao: LeaderBoardDao = LeaderBoardImpl()

    DatabaseFactory.init()

    routing {
        post("/addRooms") {
            val fromParameters = call.receiveParameters()
            val roomId = fromParameters.getOrFail("room_id")
            val createdBy = fromParameters.getOrFail("created_by")
            val password = fromParameters.getOrFail("password")

            roomsDao.addRooms(room_id = roomId, create_by = createdBy, password = password)

            call.respond("Room added successfully")

        }
        //This post method is being used by the client to check if a room exists with the name the user gives, and this returns back the details
        post("/room") {
            val fromParameters = call.receiveParameters()
            val roomId = fromParameters.getOrFail("room_id")
            val roomData = RoomModerator.getRoom(roomId)
            if (roomData == null) {
                call.respond(RoomAvailabilityDataClass(false, ""))

            } else {
                call.respond(RoomAvailabilityDataClass(true, roomData.pass))
            }
        }
        get("/rooms") {
            val listOfRooms = roomsDao.allRooms()

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

        // below methods are for adding and retrieve users

        post("/signup") {
            val fromParameters = call.receiveParameters()
            val user_name = fromParameters.getOrFail("user_name")
            val mail_id = fromParameters.getOrFail("mail_id")
            val password = fromParameters.getOrFail("password")


            val isSignUpSuccess =
                if (usersDao.userInputFilter(user_name, mail_id)) {
                    //creating row in leaderboard

                    usersDao.signUp(user_name, mail_id, password)


                } else {
                    false
                }

            if (isSignUpSuccess)
                leaderBoardDao.insertIntoLeaderBoard(user_name = user_name, 0, 0)
//


            call.respond(
                if (isSignUpSuccess) AuthenticationDataClass(true)
                else AuthenticationDataClass(false)
            )

        }

        get("/users") {
            val listOfUsers = usersDao.allUsers()

            if (listOfUsers.isNotEmpty()) {
                val listOfJsonRooms = listOfUsers.map { user ->
                    JsonRoomObject(
                        user.id,
                        user.user_name,
                        user.mail_id,
                        user.password
                    )
                }
                call.respond(listOfJsonRooms)

            } else {

                call.respond("empty database")
            }
        }

        post("/signin") {
            val fromParameters = call.receiveParameters()
            val mail_id = fromParameters.getOrFail("mail_id")
            val password = fromParameters.getOrFail("password")


            val user = usersDao.signIn(mail_id, password)


            println("hash $user")

            if (user) call.respond(AuthenticationDataClass(true))
            else call.respond(AuthenticationDataClass(false))


        }

        get("/leaderboard") {
            val leaderBoardScores = leaderBoardDao.getAllUsersFromLeaderBoard()

            if (leaderBoardScores.isNotEmpty()) {

                val scoreList = mutableListOf<LeaderBoardDataClass>()

                leaderBoardScores.forEach {
                    scoreList.add(
                        LeaderBoardDataClass(
                            id = it.id, user_name = it.user_name,
                            matches_played = it.matches_played, trophies_count = it.trophies_count
                        )
                    )
                }

                call.respond(scoreList)

            } else {
                call.respond("Empty leaderBoard")
            }


        }


    }


}
