package com.example.roomsHanlder

import com.example.RoomDataClass

interface RoomsDao {
    suspend fun allUsers(): List<RoomDataClass>
    suspend fun user(id: Int): RoomDataClass?
    suspend fun addNewUser(name: String, profilePic: String?): RoomDataClass?
    suspend fun editUser(id: Int, name: String, profilePic: String?): Boolean
    suspend fun deleteUser(id: Int): Boolean
}