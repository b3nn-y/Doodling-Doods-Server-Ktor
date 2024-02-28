package com.example.usersDao

import kotlinx.serialization.Serializable

@Serializable
data class JsonUserObject(
    val id: Int,
    val user_name: String,
    val mail_by: String,
    val password: String
)
@Serializable
data class AuthenticationDataClass(val isAuthorized:Boolean)
