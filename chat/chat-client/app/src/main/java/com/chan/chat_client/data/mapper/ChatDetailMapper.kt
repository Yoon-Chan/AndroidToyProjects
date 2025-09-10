package com.chan.chat_client.data.mapper

import com.chan.chat_client.data.model.dto.ChatMessageRequest
import com.chan.chat_client.data.model.dto.ChatMessageResponse
import com.chan.chat_client.domain.model.ChatMessage

fun ChatMessage.toDto(): ChatMessageRequest {
    return ChatMessageRequest(
        roomId = roomId,
        message = message,
        senderEmail = senderEmail
    )
}

fun ChatMessageResponse.toDomain(): ChatMessage {
    return ChatMessage(
        roomId = roomId,
        message = message,
        senderEmail = senderEmail
    )
}