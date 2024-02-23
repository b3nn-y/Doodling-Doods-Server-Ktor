package com.example.playerManager

import com.example.roomManager.Room
import com.example.roomManager.RoomModerator
import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
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
        playerDetails.session = playerSockets[playerDetails.name]
        when(playerDetails.joinType){
            "create" -> {
                RoomModerator.addRoom(playerDetails.roomName, Room(name = playerDetails.name, pass = playerDetails.roomPass, createdBy = playerDetails, players = arrayListOf(playerDetails) ))
            }
            "join" -> {
                RoomModerator.addPlayerToRoom(playerDetails)
            }
        }

        return playerDetails
    }

    fun incomingClientRequestModerator(player: String, room: String, request: String){

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
}
