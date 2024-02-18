package com.example.demo

import org.jetbrains.exposed.sql.Table

object Players : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val profile_pic = varchar("profilePic", 1024).nullable()

    override val primaryKey = PrimaryKey(id)
}