package com.example.playerManager

import com.example.roomManager.ChatMessages
import com.example.roomManager.Room
import com.example.roomManager.RoomModerator
import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

//This class is responsible for all the incoming request from the clients, this evaluates the message and does appropriate actions
object PlayerCommunicationManager {
    //This file holds all the player socket sessions, we use these to communicate back the client
    private val playerSockets = ConcurrentHashMap<String, WebSocketSession>()
    //This hasp map exists, because at first when a client sends a join request we assign a temp name to them and wait until they give us the proper details, until that this hash map contains their data.
    private val tempPlayerSockets = ConcurrentHashMap<String, WebSocketSession>()

//    private val state = MutableStateFlow("")
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private var delayGameJob: Job? = null

    //this has all the players that have connected to the server so far
    private var noOfPlayersConnected = 0


    //This functions assigns the proper username and other data, when the server receives proper input from the client
    fun assignTheirUserNameAndRoom(data: String, oldName:String): Player {
        val playerDetails = Gson().fromJson(data, Player::class.java)
        tempPlayerSockets.forEach{ (name, socket) ->
            if (name == oldName){
                playerSockets[playerDetails.name] = socket
                tempPlayerSockets.remove(oldName)
            }
        }
//        playerDetails.session = playerSockets[playerDetails.name]

        //this block creates or join them to a particular room, based on their join type
        when(playerDetails.joinType){
            "create" -> {
                RoomModerator.addRoom(playerDetails.roomName, Room(name = playerDetails.name, pass = playerDetails.roomPass, createdBy = playerDetails, players = arrayListOf(playerDetails), wordList = arrayListOf(), guessedPlayers = arrayListOf(), messages = arrayListOf() , iosCords = arrayListOf()))
            }
            "join" -> {
                RoomModerator.addPlayerToRoom(playerDetails)
            }
        }

        //This block sends back the client the current room data
        CoroutineScope(Dispatchers.IO).launch {
            playerSockets[playerDetails.name]?.send(Gson().toJson(RoomModerator.getRoom(playerDetails.roomName)))
        }

        return playerDetails
    }

//    fun encodeToStringWithMoshi(room: Room): String {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(Room::class.java)
//        return adapter.toJson(room)
//    }

    //This function moderates all the incoming requests, and if its valid, it performs actions
    fun incomingClientRequestModerator(player: String, room: String, request: String){
        println("Request by $player on room $room: $request")

        if (checkIfTheInputIsOfChatDataType(request)){
            println("\nChat Request $request")
            println("Chat REQUEST VALIDATED!!!")
            CoroutineScope(Dispatchers.Default).launch {
                RoomModerator.addChat(Gson().fromJson(request, Chat::class.java), room)
                println(Gson().fromJson(request, Chat::class.java))
            }
        }

        if (checkIfTheInputIsOfRoomDataType(request)){
            println("\nRequest $request")
            println("REQUEST VALIDATED!!!")
//            RoomModerator.rooms[room] = Gson().fromJson(request, Room::class.java)
            CoroutineScope(Dispatchers.Default).launch{
                RoomModerator.updateRoomData(room, Gson().fromJson(request, Room::class.java))
                RoomModerator.sendRoomUpdates(player, room, playerSockets)
            }

        }

    }

    //This functions connects the incoming client with a temp username and save their socket sessions
    fun connectPlayer(session: WebSocketSession): String {
        val tempPlayerName = "tempPlayer${noOfPlayersConnected++}"
        println("Connected $tempPlayerName")
        tempPlayerSockets[tempPlayerName] = session
        gameScope.launch {
            clientWelcomeMessage(session)
        }
        return tempPlayerName
    }


    //This function is executed when the client joins, with a welcome message
    private suspend fun clientWelcomeMessage(session: WebSocketSession) {
        session.send(
            "You have been successfully connected to the Doodling Doods Server!!"
        )

    }

    //This function removes the player from the respective room and removes its socket session, when the player disconnects
    fun disconnectPlayer(player: Player) {
        RoomModerator.removePlayer(player)
        playerSockets.remove(player.name)
    }

    //This function can be used to send a message to all the connected players
    suspend fun sendMessageToAllClients(message: String) {
        playerSockets.values.forEach { socket ->
            socket.send(message)
        }
    }

    //This function returns true, if the incoming data is of the type of Player data class.
    fun checkIfTheInputIsOfPlayerDataType(data: String): Boolean {
        try {
            Gson().fromJson(data, Player::class.java)
            println("Player data initialized")
            return true
        }
        catch (e:Exception){
            println("\n\n\nPlayer Details InCorrect, Waiting for correct details")
            return false
        }
    }
    //This function returns true, if the incoming data is of the type of Room data class.
    fun checkIfTheInputIsOfRoomDataType(data: String): Boolean {
        try {
            var roomData = Gson().fromJson(data, Room::class.java)

            if (roomData.name != null && roomData.pass != null && roomData.players != null && roomData.createdBy != null && roomData.cords != null  ){
                return true
            }
            else{
                return false
            }
        }
        catch (e:Exception){
            println(e.message)
            return false
        }
    }

    fun checkIfTheInputIsOfChatDataType(data: String): Boolean{
        try {
            var chat = Gson().fromJson(data, Chat::class.java)

            if ( chat.chat != null && chat.chat.msg is String && (chat.chat.msgColor in mutableListOf("green", "white", "black") && RoomModerator.getRoom(chat.chat.room) != null) ){
                return true
            }
            else{
                return false
            }
        }
        catch (e:Exception){
            println(e.message)
            return false
        }
    }

    fun getPlayerSockets(): ConcurrentHashMap<String, WebSocketSession> {
        return playerSockets
    }
}
