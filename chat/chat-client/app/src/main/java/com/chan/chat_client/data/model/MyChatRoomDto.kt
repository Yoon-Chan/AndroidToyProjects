package com.chan.chat_client.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MyChatRoomDto(
    val roomId: Long,
    val roomName: String,
    val isGroupChat: String,
    val unReadCount: Int,
)
