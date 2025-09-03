package com.chan.chat_client.domain.model

data class ChatRoom(
    val id: Long,
    val roomName: String,
    val isGroupChat: Boolean,
    val unReadCount: Int,
)
