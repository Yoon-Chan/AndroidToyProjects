package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostCreateGroupChatRoomRequest(
    val roomName: String
)
