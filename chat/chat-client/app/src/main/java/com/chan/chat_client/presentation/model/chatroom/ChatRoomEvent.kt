package com.chan.chat_client.presentation.model.chatroom

sealed interface ChatRoomEvent {
    data object NewChatRoom: ChatRoomEvent
    data class NewGroupChatRoom(val roomName: String): ChatRoomEvent
}