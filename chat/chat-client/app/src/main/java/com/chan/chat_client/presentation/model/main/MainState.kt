package com.chan.chat_client.presentation.model.main

data class MainState(
    val isLoggedIn:Boolean = false,
    val isCheckingAuth: Boolean = false,
)
