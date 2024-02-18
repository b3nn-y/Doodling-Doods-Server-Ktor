package com.example.demo

import org.jetbrains.exposed.sql.selectAll
import com.example.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {

    override suspend fun allUsers(): List<Player> = dbQuery{
        Players.selectAll().map(::resultRowToPlayer)
    }

    override suspend fun user(id: Int): Player?= dbQuery {
        Players.select{
            Players.id eq id
        }
            .map (::resultRowToPlayer)
            .singleOrNull()
    }

    override suspend fun adduser(name: String, profilePic: String?): Player? = dbQuery{
        val insertStatement = Players.insert {
            it[Players.name] = name
            it[Players.profile_pic] = "sample img"
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPlayer)
    }

    override suspend fun editUser(id: Int, name: String, profilePic: String?): Boolean = dbQuery {
        Players.update({ Players.id eq id }) {
            it[Players.name] = name
            it[Players.profile_pic] = profilePic
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery{
        Players.deleteWhere { Players.id eq id } > 0
    }

    private fun resultRowToPlayer(row : ResultRow) = Player(
        id = row[Players.id],
        name = row[Players.name],
        profilePic = row[Players.profile_pic]
    )
}