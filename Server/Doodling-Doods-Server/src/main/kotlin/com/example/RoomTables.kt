package com.example

import org.jetbrains.exposed.sql.Table

object RoomTables :Table("rooms") {
    val id =integer("id").autoIncrement()
    val room_id = varchar("room_id",10)
    val created_by =varchar("created_by",128)
    val room_pass = varchar("password",128)

    override val primaryKey = PrimaryKey(id)

}