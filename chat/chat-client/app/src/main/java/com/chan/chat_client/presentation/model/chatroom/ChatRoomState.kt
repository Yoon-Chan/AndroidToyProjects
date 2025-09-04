package com.chan.chat_client.presentation.model.chatroom

import com.chan.chat_client.domain.model.GroupChatRoom

data class ChatRoomState(
    val rooms: List<GroupChatRoom> = emptyList(),
)
