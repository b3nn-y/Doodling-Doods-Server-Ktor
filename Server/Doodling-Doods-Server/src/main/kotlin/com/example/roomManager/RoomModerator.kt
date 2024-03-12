package com.example.roomManager

import com.example.gameModes.GuessTheWord
import com.example.playerManager.Chat
import com.example.playerManager.Player
import com.example.playerManager.PlayerChats
import com.example.playerManager.PlayerCommunicationManager
import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
//This is a static object, This manages the entire room
object RoomModerator {
    var rooms = hashMapOf<String, Room>()
    private val roomJobs = mutableMapOf<String, Job>()
    private val ongoingGames = mutableMapOf<String, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val coroutineSupervisorScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var chatHashMap = HashMap<String, PlayerChats>()

    var isWordChosen = HashMap<String, Boolean>()


    private var listOfOngoingGames = ArrayList<String>()
    //adds a room to the room list
    fun addRoom(name: String, room: Room) {
        rooms[name] = room
        chatHashMap[name] = PlayerChats(arrayListOf(),HashMap())
//        println(rooms)
        println("Room Added")
        isWordChosen[name] = false
        val job = createBackgroundJob(name)
        roomJobs[name] = job

    }

    //THis creates a background loop to track the room, for active players
    private fun createBackgroundJob(name: String): Job {
        return coroutineScope.launch {
            while (isActive) {
                try {
                    while (rooms[name]?.players?.size!! > 0){
                        println("$name is running with ${rooms[name]?.players!!.size} players")
                        delay(10000)
                    }
                    for (i in 1..120){
                        if (rooms[name]?.players!!.size == 0){
                            println("The room $name has no players and is waiting for players to join in $i/120 seconds, If not the room will be closed")
                            delay(1000)
                        }
                    }

                    if (rooms[name]?.players!!.size == 0){
                        deleteRoom(name)
                        break
                    }
                }
                catch (e:Exception){
                    println(e.message)
                }
            }
        }
    }

    //this sends out info about a room based on its name
    fun getRoom(name: String): Room? {
        return try {
            rooms[name]
        } catch (e: Exception) {
            null
        }
    }

    fun addChat(chat: Chat, room: String){
        coroutineScope.launch {
            chatHashMap[room]?.chats?.add(chat.chat)
            var score = HashMap<String, Int>()


            for (i in RoomModerator.getRoom(room)?.players?: arrayListOf()){
                for (j in chatHashMap[room]?.chats?: arrayListOf()){
                    if (score.containsKey(i.name)){
                        if (i.name == j.player){
                            if (j.msgColor == "green"){
                                score[j.player]?.plus(5)
                            }
                        }
                    }
                    else{
                        score[i.name] = 0
                        if (i.name == j.player){
                            if (j.msgColor == "green"){
                                score[j.player]?.plus(5)
                            }
                        }
                    }
                }
            }

            chatHashMap[room]?.score = score

//            chatHashMap[room]?.score?.toList()
//                ?.sortedByDescending { it.second }
//                ?.toMap()

            sendChats(room)
        }
    }
    fun sendChats( room: String){
        coroutineScope.launch {
            val playerSockets = PlayerCommunicationManager.getPlayerSockets()
            rooms[room]?.players?.forEach{
                playerSockets[it.name]?.send(Gson().toJson(chatHashMap[room]))
//                println("This is the message being sent"+ Gson().toJson(rooms[roomName]))
            }
        }
    }

    //this function gives all the hashmap of all active rooms
    fun getAllRooms(): HashMap<String, Room> {
        return rooms
    }

    //this function removes the room from the list and cancels the background check
    fun deleteRoom(name: String) {
        try {
            println("Room $name is getting destroyed")
            rooms.remove(name)
            roomJobs[name]?.cancel()
            roomJobs.remove(name)
            listOfOngoingGames.remove(name)
            chatHashMap.remove(name)
            isWordChosen.remove(name)
            println("Room $name is destroyed")

        } catch (e: Exception) {
            println("No Room present")
        }
    }


    //this function adds a player to a room based on name
    fun addPlayerToRoom(playerDetails: Player){
        coroutineScope.launch {
            rooms[playerDetails.roomName]?.players?.add(playerDetails)
            sendRoomUpdates(playerDetails.name, playerDetails.roomName, PlayerCommunicationManager.getPlayerSockets())
        }
    }
    //remove a player
    fun removePlayer(playerDetails: Player){
        coroutineScope.launch {
            rooms[playerDetails.roomName]?.players?.remove(playerDetails)
            sendRoomUpdates(playerDetails.name, playerDetails.roomName, PlayerCommunicationManager.getPlayerSockets())
        }
    }

    //this function, sends all the clients when a user sends and updated room data
    fun sendRoomUpdates(player: String, room: String, playerSockets: ConcurrentHashMap<String, WebSocketSession>){
        val roomData = rooms[room]
        roomData?.players?.forEach {
            if (it.name != player){
                CoroutineScope(Dispatchers.IO).launch {
                    playerSockets[it.name]?.send(Gson().toJson(rooms[room]))
                }
            }
        }
    }

    fun startGame(room: String){
        coroutineScope.launch {
            sendUpdatesToEveryoneInARoom(room)
            GuessTheWord().playGuessTheWord(room)

        }
    }

    //This function updates the room's data, that is being managed
    fun updateRoomData(roomName: String, data: Room){
        rooms[roomName]?.noOfPlayersInRoom = data.noOfPlayersInRoom
        rooms[roomName]?.noOfGuessedAnswersInCurrentRound = data.noOfGuessedAnswersInCurrentRound
        rooms[roomName]?.maxPlayers = data.maxPlayers
        rooms[roomName]?.cords = data.cords
        rooms[roomName]?.noOfPlayersInRoom = data.noOfPlayersInRoom
        rooms[roomName]?.visibility = data.visibility
        rooms[roomName]?.rounds = data.rounds
        rooms[roomName]?.guessedPlayers = data.guessedPlayers
        rooms[roomName]?.wordList = data.wordList
        rooms[roomName]?.rounds = data.rounds
        rooms[roomName]?.currentWordToGuess = data.currentWordToGuess
        rooms[roomName]?.gameStarted = data.gameStarted
        rooms[roomName]?.messages = data.messages
        rooms[roomName]?.numberOfRoundsOver = data.noOfGuessedAnswersInCurrentRound
        rooms[roomName]?.gameOver = data.gameOver
        rooms[roomName]?.iosCords = data.iosCords
        rooms[roomName]?.isWordChosen = data.isWordChosen
        isWordChosen[roomName] = data.isWordChosen
//        println("This is the data message ${data.messages}")

        if (!(roomName in listOfOngoingGames) && data.gameStarted ){
            listOfOngoingGames.add(roomName)
            startGame(roomName)
        }
    }

    fun updateRoomDataAndSend(roomName: String, data: Room){
        coroutineScope.launch {
            rooms[roomName]?.noOfPlayersInRoom = data.noOfPlayersInRoom
            rooms[roomName]?.noOfGuessedAnswersInCurrentRound = data.noOfGuessedAnswersInCurrentRound
            rooms[roomName]?.maxPlayers = data.maxPlayers
            rooms[roomName]?.cords = data.cords
            rooms[roomName]?.noOfPlayersInRoom = data.noOfPlayersInRoom
            rooms[roomName]?.visibility = data.visibility
            rooms[roomName]?.rounds = data.rounds
            rooms[roomName]?.currentWordToGuess = data.currentWordToGuess
            rooms[roomName]?.gameStarted = data.gameStarted
            rooms[roomName]?.guessedPlayers = data.guessedPlayers
            rooms[roomName]?.wordList = data.wordList
            rooms[roomName]?.messages = data.messages
            rooms[roomName]?.numberOfRoundsOver = data.noOfGuessedAnswersInCurrentRound
            rooms[roomName]?.gameOver = data.gameOver
            rooms[roomName]?.iosCords = data.iosCords
            sendUpdatesToEveryoneInARoom(roomName)
        }

    }

    fun sendUpdatesToEveryoneInARoom(roomName: String){
        coroutineScope.launch {
            val playerSockets = PlayerCommunicationManager.getPlayerSockets()
            rooms[roomName]?.players?.forEach{
                try {
                    playerSockets[it.name]?.send(Gson().toJson(rooms[roomName]))
                }
                catch (e:Exception){
                    println(e.message)
                }
//                println("This is the message being sent"+ Gson().toJson(rooms[roomName]))
            }
        }
    }
    fun checkIfAWordIsSent(room: String, tempListOfWords: ArrayList<String>){
        coroutineScope.launch {
            delay(6000)
            if (isWordChosen[room] != true){
                RoomModerator.getRoom(room)?.currentWordToGuess = tempListOfWords.random()
                isWordChosen[room] = true

            }

        }
    }



}


