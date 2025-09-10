package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    val roomId: Long,
    val message: String,
    val senderEmail: String,
)
