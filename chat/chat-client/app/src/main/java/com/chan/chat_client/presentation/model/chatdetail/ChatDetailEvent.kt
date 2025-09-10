package com.chan.chat_client.presentation.model.chatdetail

sealed interface ChatDetailEvent {
    data class GetHistoryMessage(val roomId: Long): ChatDetailEvent
    data class SendMessage(val message: String): ChatDetailEvent
    data class OnValueChange(val text: String): ChatDetailEvent
}