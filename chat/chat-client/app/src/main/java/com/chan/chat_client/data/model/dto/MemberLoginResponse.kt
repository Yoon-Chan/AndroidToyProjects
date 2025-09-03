package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemberLoginResponse(
    val id: Long,
    val token: String
)
