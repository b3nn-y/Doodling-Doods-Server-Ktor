package com.example.dao
import com.example.DatabaseFactory.dbQuery
import com.example.models.RoomDataClass
import com.example.schemas.RoomsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RoomImpl: RoomsDao {
    override suspend fun allRooms(): List<RoomDataClass> = dbQuery {
        RoomsTable.selectAll().map(::resultRowToRoomDataClass)
    }

    override suspend fun findRoom(id: Int): RoomDataClass? = dbQuery {
        RoomsTable.select{
            RoomsTable.id eq id
        }
            .map(::resultRowToRoomDataClass)
            .singleOrNull()

    }


    override suspend fun addRooms(room_id: String, created_by: String, password: String): RoomDataClass? = dbQuery{
        val insertStatement = RoomsTable.insert {
            it[RoomsTable.room_id]=room_id
            it[RoomsTable.created_by]= created_by
            it[RoomsTable.password]=password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToRoomDataClass)
    }

    override suspend fun deleteRooms(id: Int): Boolean= dbQuery {
       RoomsTable.deleteWhere { RoomsTable.id eq id} >0
    }

    private fun resultRowToRoomDataClass(row : ResultRow) = RoomDataClass(
        id = row[RoomsTable.id],
        room_id = row[RoomsTable.room_id],
        created_by = row[RoomsTable.created_by],
        password = row[RoomsTable.password]

    )
}