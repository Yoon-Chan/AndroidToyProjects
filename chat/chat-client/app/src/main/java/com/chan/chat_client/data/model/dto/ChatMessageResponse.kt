package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    val roomId: Long,
    val message: String,
    val senderEmail: String,
)
