package com.chan.chat_client.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupChatRoomDto(
    val roomId: Long,
    val roomName: String
)
