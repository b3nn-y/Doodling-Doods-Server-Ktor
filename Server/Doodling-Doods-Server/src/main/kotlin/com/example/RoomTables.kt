package com.example

import org.jetbrains.exposed.sql.Table

object GameTables :Table("room") {
    val id =integer("id").autoIncrement()
    val room_id = text("room_id")
    val created_by =text("created_by")
    val room_pass = text("password")

    override val primaryKey = PrimaryKey(id)

}