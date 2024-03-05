package com.example.leaderBoard

import com.example.DatabaseFactory.dbQuery
import com.example.schemas.LeaderBoardTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class LeaderBoardImpl : LeaderBoardDao {

    override suspend fun insertIntoLeaderBoard(
        user_name: String,
        matches_played: Int,
        trophies_count:Int
    ): LeaderBoardDataClass?= dbQuery {
        val insertStatement = LeaderBoardTable.insert {
            it[LeaderBoardTable.user_name] = user_name
            it[LeaderBoardTable.matches_played] = matches_played
            it[LeaderBoardTable.trophies_count] = trophies_count
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToLeaderBoardDataClass)
    }

    override suspend fun getAllUsersFromLeaderBoard(): List<LeaderBoardDataClass> = dbQuery {
        LeaderBoardTable.selectAll().map(::resultRowToLeaderBoardDataClass)
    }

    private fun resultRowToLeaderBoardDataClass(row: ResultRow) = LeaderBoardDataClass(
        id = row[LeaderBoardTable.id],
        user_name = row[LeaderBoardTable.user_name],
        matches_played = row[LeaderBoardTable.matches_played],
        trophies_count = row[LeaderBoardTable.trophies_count],

        )
}