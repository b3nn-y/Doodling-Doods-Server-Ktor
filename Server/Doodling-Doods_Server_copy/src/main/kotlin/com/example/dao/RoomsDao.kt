package com.example.dao

import com.example.models.RoomDataClass

interface RoomsDao {

    suspend fun allRooms():List<RoomDataClass>

    suspend fun findRoom(id: Int): RoomDataClass?

    suspend fun addRooms(room_id:String,create_by:String,password:String): RoomDataClass?

    suspend fun deleteRooms(id:Int):Boolean

}