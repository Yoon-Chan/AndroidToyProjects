package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemberLoginRequest(
    val email: String,
    val password: String,
)
