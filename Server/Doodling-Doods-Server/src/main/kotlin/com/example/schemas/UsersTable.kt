package com.example.schemas


import org.jetbrains.exposed.sql.Table


object UsersTable: Table(){
    val id = integer("id").autoIncrement()
    val user_name = varchar("user_name",128)
    val mail_id = varchar("mail_id",128)
    val password = varchar("password",128)

    override val primaryKey =PrimaryKey(id)
}