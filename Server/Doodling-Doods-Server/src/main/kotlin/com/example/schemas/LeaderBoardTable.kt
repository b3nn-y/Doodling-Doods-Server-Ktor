package com.example.schemas


import org.jetbrains.exposed.sql.Table

object LeaderBoardTable: Table() {
    val id = integer("id").autoIncrement()
    val user_name = varchar("user_name", 128)
    val matches_played = integer("matches_played")
    val trophies_count = integer("trophies_count")

    override val primaryKey =PrimaryKey(id)
}