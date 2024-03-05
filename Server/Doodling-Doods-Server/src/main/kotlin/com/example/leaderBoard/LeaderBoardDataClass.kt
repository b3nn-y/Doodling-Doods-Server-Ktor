package com.example.leaderBoard

import kotlinx.serialization.Serializable

@Serializable
data class LeaderBoardDataClass(
    val id:Int,
    val user_name:String,
    val matches_played:Int,
    val trophies_count :Int,


)