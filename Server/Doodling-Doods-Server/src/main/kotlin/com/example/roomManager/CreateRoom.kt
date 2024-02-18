package com.example.roomManager
import RoomModerator
import com.example.playerManager.Player

class CreateRoom {
    fun createRoom(roomName: String, roomPass: String){
        val room = RoomModerator()
        room.addRoom(roomName,Room("", "", arrayListOf(Player(""))) )
    }
}
//}