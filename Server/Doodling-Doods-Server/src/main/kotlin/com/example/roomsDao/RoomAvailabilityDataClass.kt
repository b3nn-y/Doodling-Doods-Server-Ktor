package com.example.roomsDao

import kotlinx.serialization.Serializable

//This class is used to send the client with data, when the request if the room is available, with its pass.
@Serializable
data class RoomAvailabilityDataClass(
    var roomAvailable: Boolean,
    var roomPass: String
)
