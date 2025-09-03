package com.chan.chat_client.presentation.model.login

sealed interface LoginEffect {
    data class Toast(val message: String): LoginEffect
    data object Home: LoginEffect
}