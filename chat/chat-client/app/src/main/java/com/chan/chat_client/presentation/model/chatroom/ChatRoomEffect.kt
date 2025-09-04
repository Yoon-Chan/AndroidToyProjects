package com.chan.chat_client.presentation.model.chatroom

sealed interface ChatRoomEffect {
    data object NewChatRoom: ChatRoomEffect
    data class ToastMessage(val message: String): ChatRoomEffect
    data class OnChatDetail(val roomId: Long): ChatRoomEffect
}