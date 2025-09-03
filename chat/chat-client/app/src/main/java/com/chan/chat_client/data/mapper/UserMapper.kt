package com.chan.chat_client.data.mapper

import com.chan.chat_client.data.model.UserDto
import com.chan.chat_client.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email
    )
}