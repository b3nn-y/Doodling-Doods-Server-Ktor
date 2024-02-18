package com.example.schemas

import org.jetbrains.exposed.sql.Table

object RoomsTable:Table() {
    val id =integer("id").autoIncrement()
    val room_id = varchar("room_id",128)
    val created_by = varchar("created_by",128)
    val password =varchar("password",128)

    override val primaryKey =PrimaryKey(id)
}