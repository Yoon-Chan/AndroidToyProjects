package com.chan.chat_client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateMemberRequest(
    val name: String,
    val email: String,
    val password: String
)
