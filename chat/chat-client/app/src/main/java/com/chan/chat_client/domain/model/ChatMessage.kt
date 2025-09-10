package com.chan.chat_client.domain.model

data class ChatMessage(
    val roomId: Long,
    val message: String,
    val senderEmail: String,
)
