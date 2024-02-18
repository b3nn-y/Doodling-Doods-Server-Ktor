package com.example.db

import org.jetbrains.exposed.sql.Table

object People : Table() {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstName", 50)
    val lastName = varchar("lastName", 50)
}