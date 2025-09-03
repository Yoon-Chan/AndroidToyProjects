package com.chan.chat_client.presentation.model.login

sealed interface LoginEvent {
    data class CreateMember(val email: String, val password: String): LoginEvent
    data class Login(val email: String, val password: String): LoginEvent
}