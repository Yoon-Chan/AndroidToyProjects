package com.chan.chat_client.domain.repository

import com.chan.chat_client.domain.model.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMyChatRoom(): Flow<List<ChatRoom>>
    fun postCreateGroupChatRoom(roomName: String): Flow<ChatRoom>
}