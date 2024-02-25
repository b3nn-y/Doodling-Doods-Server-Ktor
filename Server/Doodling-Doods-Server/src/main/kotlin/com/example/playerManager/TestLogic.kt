//package com.example.playerManager
//
//import kotlinx.coroutines.delay
//
//class TestLogic(){
//    private var playersList = ArrayList<String>()
//    private var finishedPlayersList = ArrayList<String>()
//    private var numOfRounds = 0
//    fun getCurrentPlayer(): String? {
//        if (numOfRounds > 0){
//            if (playersList.size > 0){
//                playersList.forEach {p1 ->
//                    var isPlayersTurnOver = false
//                    finishedPlayersList.forEach{ p2 ->
//                        if (p1 == p2){
//                            isPlayersTurnOver = true
//                        }
//                    }
//                    if (!isPlayersTurnOver){
//                        finishedPlayersList.add(p1)
//                        return p1
//                    }
//                }
//                numOfRounds--
//                playersList = finishedPlayersList
//                finishedPlayersList = arrayListOf()
//                return null
//            }
////            else{
////                if (finishedPlayersList.size > 0){
////                    numOfRounds--
////                    playersList = finishedPlayersList.shuffled() as ArrayList<Player>
////                    finishedPlayersList = arrayListOf()
////                    getCurrentPlayer()
////                }
////                else{
////                    return null
////                }
////            }
//        }
//        else{
//            return null
//        }
//        return null
//    }
//
//    fun updatePlayerDetails(players: ArrayList<String>){
//        playersList = players.shuffled() as ArrayList<String>
//    }
//
//    fun noOfRounds(rounds: Int){
//        numOfRounds = rounds
//    }
//}
//
////suspend fun main(){
////    var testLogic = TestLogic()
////    var players = arrayListOf("ben", "sam", "dan", "sum", "jo", "Sand")
////    testLogic.updatePlayerDetails(players)
////    testLogic.noOfRounds(5)
////    for (i in 1..5){
////        testLogic.updatePlayerDetails(players)
////        do {
////            var currentPlayer = testLogic.getCurrentPlayer()
////            println("Current player is $currentPlayer and its round $i")
////            delay(1000)
////
////        }
////            while (currentPlayer != null)
////
////
////        println("Round $i is over")
////
////        if (i==3){
////            players.add("gary")
////            players.add("jose")
////            players.add("anston")
////        }
////        if (i==4){
////            players.remove("jo")
////            players.remove("Sand")
////            players.remove("sum")
////        }
////    }
////
////
////}