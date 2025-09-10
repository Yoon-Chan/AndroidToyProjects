package com.chan.chat_client.presentation.model.chatdetail

import com.chan.chat_client.domain.model.ChatMessage

data class ChatDetailState(
    val roomId: Long = 0,
    val email: String = "",
    val text: String = "",
    val messages: List<ChatMessage> = emptyList()
)
