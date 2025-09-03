package com.chan.chat_client.presentation.model.home

import com.chan.chat_client.domain.model.User

data class HomeState(
    val users: List<User> = emptyList(),
)
