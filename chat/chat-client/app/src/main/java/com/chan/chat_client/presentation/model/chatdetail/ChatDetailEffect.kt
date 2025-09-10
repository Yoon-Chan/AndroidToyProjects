package com.chan.chat_client.presentation.model.chatdetail

sealed interface ChatDetailEffect {
    data object ScrollToBottom : ChatDetailEffect
}