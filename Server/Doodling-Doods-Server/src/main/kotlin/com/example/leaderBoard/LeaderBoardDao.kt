package com.example.leaderBoard

interface LeaderBoardDao {

    suspend fun insertIntoLeaderBoard(user_name:String,matches_played:Int,trophies_won:Int):LeaderBoardDataClass?

    suspend fun getAllUsersFromLeaderBoard():List<LeaderBoardDataClass>
}