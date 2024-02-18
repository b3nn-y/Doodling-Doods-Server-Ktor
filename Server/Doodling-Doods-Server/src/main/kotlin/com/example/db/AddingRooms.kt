package com.example.db

interface AddingRooms {
    suspend fun registerTable(params:CreateRoomDataClass):RoomDataClass1?
}