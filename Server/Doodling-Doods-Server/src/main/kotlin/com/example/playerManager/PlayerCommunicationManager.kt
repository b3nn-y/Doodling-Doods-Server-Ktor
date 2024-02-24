package com.example.playerManager

import com.example.roomManager.Room
import com.example.roomManager.RoomModerator
import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class PlayerCommunicationManager {
    private val playerSockets = ConcurrentHashMap<String, WebSocketSession>()
    private val tempPlayerSockets = ConcurrentHashMap<String, WebSocketSession>()

    private val state = MutableStateFlow("")
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var delayGameJob: Job? = null

    private var noOfPlayersConnected = 0

    init {
        gameScope.launch {
            clientWelcomeMessage()
        }

    }

    fun assignTheirUserNameAndRoom(data: String, oldName:String): Player {
        val playerDetails = Gson().fromJson(data, Player::class.java)
        tempPlayerSockets.forEach{ (name, socket) ->
            if (name == oldName){
                playerSockets[playerDetails.name] = socket
                tempPlayerSockets.remove(oldName)
            }
        }
//        playerDetails.session = playerSockets[playerDetails.name]
        when(playerDetails.joinType){
            "create" -> {
                RoomModerator.addRoom(playerDetails.roomName, Room(name = playerDetails.name, pass = playerDetails.roomPass, createdBy = playerDetails, players = arrayListOf(playerDetails) ))
            }
            "join" -> {
                RoomModerator.addPlayerToRoom(playerDetails)
            }
        }


//        var d = TRoom(
//            name = roomData.name,
//            pass = roomData.pass,
//            players = roomData.players.toString(),
//            noOfPlayersInRoom =  roomData.noOfPlayersInRoom,
//            noOfGuessedAnswersInCurrentRound =  roomData.noOfGuessedAnswersInCurrentRound,
//            createdBy = roomData.createdBy.toString(),
//            maxPlayers = roomData.maxPlayers,
//            cords = roomData.cords,
//            visibility = roomData.visibility,
//            currentPlayer = roomData.currentPlayer,
//            rounds = roomData.rounds,
//            currentWordToGuess = roomData.currentWordToGuess)
//        println("Sent Room Data to Player ${playerDetails.name}")
        CoroutineScope(Dispatchers.IO).launch {
            playerSockets[playerDetails.name]?.send(Json.encodeToString(RoomModerator.getRoom(playerDetails.roomName)))
        }

        return playerDetails
    }

//    fun encodeToStringWithMoshi(room: Room): String {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(Room::class.java)
//        return adapter.toJson(room)
//    }

    fun incomingClientRequestModerator(player: String, room: String, request: String){
        println("Request by $player on room $room: $request")
        if (checkIfTheInputIsOfRoomDataType(request)){
            println("\nRequest $request")
//            RoomModerator.rooms[room] = Gson().fromJson(request, Room::class.java)
            CoroutineScope(Dispatchers.Default).launch{
                RoomModerator.updateRoomData(room, Gson().fromJson(request, Room::class.java))
                RoomModerator.sendRoomUpdates(player, room, playerSockets)
            }

        }

    }

    fun connectPlayer(session: WebSocketSession): String {
        val tempPlayerName = "tempPlayer${noOfPlayersConnected++}"
        println("Connected $tempPlayerName")
        tempPlayerSockets[tempPlayerName] = session
        return tempPlayerName
    }



    private suspend fun clientWelcomeMessage() {
        playerSockets.values.forEach { socket ->
            socket.send(
                "You have been successfully connected to the Doodling Doods Server!!"
            )
        }
    }

    fun disconnectPlayer(player: Player) {
        RoomModerator.removePlayer(player)
        playerSockets.remove(player.name)
//        state.update {
//            it.copy(
////                connectedPlayers = it.connectedPlayers - 'p'
//            )
//        }
    }

    suspend fun sendMessageToAllClients(message: String) {
        playerSockets.values.forEach { socket ->
            socket.send(message)
        }
    }

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
            return false
        }
    }
}
