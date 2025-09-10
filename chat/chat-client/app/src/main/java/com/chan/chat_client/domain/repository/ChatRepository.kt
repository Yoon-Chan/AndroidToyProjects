package com.chan.chat_client.domain.repository

import com.chan.chat_client.domain.model.ChatMessage
import com.chan.chat_client.domain.model.GroupChatRoom
import com.chan.chat_client.domain.model.MyChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMyChatRoom(): Flow<List<MyChatRoom>>
    fun getGroupChatRoom(): Flow<List<GroupChatRoom>>
    fun postCreateGroupChatRoom(roomName: String): Flow<GroupChatRoom>
    fun joinGroupChatRoom(roomId: Long): Flow<Long>
    fun historyRoomMessage(roomId: Long): Flow<List<ChatMessage>>
}