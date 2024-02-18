package com.example.demo

interface UserDao {

    suspend fun allUsers(): List<Player>
    suspend fun user(id: Int): Player?
    suspend fun adduser(name: String, profilePic: String?): Player?
    suspend fun editUser(id: Int, name: String, profilePic: String?): Boolean
    suspend fun deleteUser(id: Int): Boolean
}