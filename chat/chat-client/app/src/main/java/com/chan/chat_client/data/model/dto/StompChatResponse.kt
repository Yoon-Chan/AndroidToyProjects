package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class StompChatResponse(
    val channel: String,
    val body: String,
)
