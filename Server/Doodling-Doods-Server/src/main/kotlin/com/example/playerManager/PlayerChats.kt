package com.example.playerManager

import com.example.roomManager.ChatMessages
import kotlinx.serialization.Serializable

@Serializable
data class PlayerChats(
    var chats: ArrayList<ChatMessages>,
    var score: HashMap<String, Int>
)

@Serializable
data class Chat(
    var chat: ChatMessages
)
