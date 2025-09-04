package com.chan.chat_client.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data class ChatDetail(val roomId: Long)

@Serializable
data object Home


@Serializable
data object Main

@Serializable
data object ChatRoom

@Serializable
data object MyPage