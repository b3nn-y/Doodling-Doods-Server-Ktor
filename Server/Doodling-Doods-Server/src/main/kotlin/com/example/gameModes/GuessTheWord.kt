package com.example.gameModes

import com.example.playerManager.PlayerTurnModerator
import com.example.roomManager.RoomModerator
import kotlinx.coroutines.*

//aka scribble
class GuessTheWord : PlayerTurnModerator() {

    var tempListOfWords = mutableListOf("pen", "eraser", "compass", "ball", "sward")
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var time = 0
    fun playGuessTheWord(room: String) {
        gameScope.launch {
            val roomData = RoomModerator.getRoom(room)
            noOfRounds(5)
            for (i in 1..5) {
                RoomModerator.getRoom(room)?.let { updatePlayerDetails(it.players) }

                do {
                    val player = getCurrentPlayer()
                    println("Current player in $room is ${player?.name}")
                    if (player != null) {
                        roomData?.currentWordToGuess = tempListOfWords.random()
                        roomData?.currentPlayer = player
                        if (roomData != null) {
                            RoomModerator.updateRoomDataAndSend(room, roomData)
                        }
                        println(roomData)
                        println("\n\n\n\ncurrent PLayer: ${roomData?.currentPlayer?.name}\n\n\n\n")
                        if (roomData != null) {
                            for (i in roomData.players) {
                                println(i.name)
                            }
                        }
                        while (time < 10) {
                            time++
                            delay(1000)
                            println("Timer "+time)
                        }
                        time = 0
                        roomData?.cords = ""
                    }

                } while (player != null)
                println("Round $i over\n")
            }

        }
        val roomData = RoomModerator.getRoom(room)
        if (roomData != null) {
            roomData.currentPlayer?.name = ""
            RoomModerator.updateRoomDataAndSend(room, roomData)
        }

    }
}