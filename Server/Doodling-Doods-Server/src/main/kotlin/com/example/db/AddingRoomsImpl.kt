package com.example.db

//
//class AddingRoomsImpl: AddingRooms {
//
//    override suspend fun registerTable(params: CreateRoomDataClass): RoomDataClass? {
//        var insertStatement :InsertStatement<Number>?=null
//
//        dbQuery {
//            insertStatement =GameTables.insert {
//                it[room_id]=params.room_id
//                it[room_pass]=params.password
//                it[created_by]=params.created_by
//            }
//        }
//
//        return insertStatement
//    }
//}