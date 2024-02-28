package com.example.usersDao

import kotlinx.serialization.Serializable

@Serializable
data class UsersDataClass(
    val id:Int,
    val user_name:String,
    val mail_id:String,
    val password:String
)