package com.chan.chat_client.domain.repository

import com.chan.chat_client.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface StompMessageClient {
    fun getMessageStream(roomId: Long): Flow<ChatMessage>
    suspend fun sendMessage(roomId: Long, message: String)
    suspend fun close()
}