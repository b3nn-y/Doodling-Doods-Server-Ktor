package com.example.roomManager

import com.example.playerManager.Player
import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

object RoomModerator {
    var rooms = hashMapOf<String, Room>()
    private val roomJobs = mutableMapOf<String, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    //adds a room to the room list
    fun addRoom(name: String, room: Room) {
        rooms[name] = room
//        println(rooms)
        println("Room Added")
        val job = createBackgroundJob(name)
        roomJobs[name] = job

    }

    //THis creates a background loop to track the room, for active players
    private fun createBackgroundJob(name: String): Job {
        return coroutineScope.launch {
            while (isActive) {
                try {
                    while (rooms[name]?.players?.size!! > 0){
                        println("$name is running with ${rooms[name]?.players!!.size} players")
                        delay(10000)
                    }
                    for (i in 1..120){
                        if (rooms[name]?.players!!.size == 0){
                            println("The room $name has no players and is waiting for players to join in $i/120 seconds, If not the room will be closed")
                            delay(1000)
                        }
                    }

                    if (rooms[name]?.players!!.size == 0){
                        deleteRoom(name)
                        break
                    }
                }
                catch (e:Exception){
                    println(e.message)
                }
            }
        }
    }

    //this sends out info about a room based on its name
    fun getRoom(name: String): Room? {
        return try {
            rooms[name]
        } catch (e: Exception) {
            null
        }
    }

    //this function gives all the hashmap of all active rooms
    fun getAllRooms(): HashMap<String, Room> {
        return rooms
    }

    //this function removes the room from the list and cancels the background check
    fun deleteRoom(name: String) {
        try {
            println("Room $name is getting destroyed")
            rooms.remove(name)
            roomJobs[name]?.cancel()
            roomJobs.remove(name)
            println("Room $name is destroyed")
        } catch (e: Exception) {
            println("No Room present")
        }
    }


    //this function adds a player to a room based on name
    fun addPlayerToRoom(playerDetails: Player){
        rooms[playerDetails.roomName]?.players?.add(playerDetails)
    }
    fun removePlayer(playerDetails: Player){
        rooms[playerDetails.roomName]?.players?.remove(playerDetails)
    }

    fun sendRoomUpdates(player: String, room: String, playerSockets: ConcurrentHashMap<String, WebSocketSession>){
        val roomData = rooms[room]
        roomData?.players?.forEach {
            if (it.name != player){
                CoroutineScope(Dispatchers.IO).launch {
                    playerSockets[it.name]?.send(Gson().toJson(rooms[room]))
                }
            }
        }
    }


}


