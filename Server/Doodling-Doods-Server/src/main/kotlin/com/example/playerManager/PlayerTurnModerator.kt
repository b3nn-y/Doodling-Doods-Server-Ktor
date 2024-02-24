package com.example.playerManager

class PlayerTurnModerator {

    private var playersList = ArrayList<Player>()
    private var finishedPlayersList = ArrayList<Player>()
    private var numOfRounds = 0
    fun getCurrentPlayer(): Player?{
        if (numOfRounds > 0){
            if (playersList.size > 0){
                playersList.forEach {p1 ->
                    var isPlayersTurnOver = false
                    finishedPlayersList.forEach{ p2 ->
                        if (p1 == p2){
                            isPlayersTurnOver = true
                        }
                    }
                    if (!isPlayersTurnOver){
                        finishedPlayersList.add(p1)
                        return p1
                    }
                }
                numOfRounds--
                playersList = finishedPlayersList.shuffled() as ArrayList<Player>
                finishedPlayersList = arrayListOf()
                getCurrentPlayer()
            }
//            else{
//                if (finishedPlayersList.size > 0){
//                    numOfRounds--
//                    playersList = finishedPlayersList.shuffled() as ArrayList<Player>
//                    finishedPlayersList = arrayListOf()
//                    getCurrentPlayer()
//                }
//                else{
//                    return null
//                }
//            }
        }
        else{
            return null
        }
        return null
    }

    fun updatePlayerDetails(players: ArrayList<Player>){
        playersList = players
    }

    fun noOfRounds(rounds: Int){
        numOfRounds = rounds
    }


}