package com.example

import kotlinx.serialization.Serializable

@Serializable
data class RoomDataClass (
    val id : Int,
    val room_id:String,
    val created_by :String,
    val room_pass:String,
)