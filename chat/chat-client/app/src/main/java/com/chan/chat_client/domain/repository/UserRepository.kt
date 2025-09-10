package com.chan.chat_client.domain.repository

import com.chan.chat_client.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun getMyUserEmail(): Flow<String>
}