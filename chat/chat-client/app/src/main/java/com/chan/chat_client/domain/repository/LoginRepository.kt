package com.chan.chat_client.domain.repository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun onCreateUser(email: String, password: String): Flow<Long>
    fun doLogin(email: String, password: String): Flow<Long>
}