package com.chan.chat_client.presentation.model.chatroom

import com.chan.chat_client.domain.model.ChatRoom

data class ChatRoomState(
    val rooms: List<ChatRoom> = emptyList(),
)
