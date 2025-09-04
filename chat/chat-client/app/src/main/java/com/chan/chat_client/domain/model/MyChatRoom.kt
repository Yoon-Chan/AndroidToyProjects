package com.chan.chat_client.domain.model

data class MyChatRoom(
    val id: Long,
    val roomName: String,
    val isGroupChat: Boolean,
    val unReadCount: Int,
)
