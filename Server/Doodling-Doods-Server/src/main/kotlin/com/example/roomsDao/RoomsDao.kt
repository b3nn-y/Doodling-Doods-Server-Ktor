package com.example.roomsDao

interface RoomsDao {

    suspend fun allRooms():List<RoomDataClass>

    suspend fun findRoom(id: Int): RoomDataClass?

    suspend fun addRooms(room_id:String,create_by:String,password:String): RoomDataClass?

    suspend fun deleteRooms(id:Int):Boolean



}