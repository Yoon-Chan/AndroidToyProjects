package com.chan.chat_client.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoEntity(
    val accessToken: String,
    val email: String,
    val password: String
)
