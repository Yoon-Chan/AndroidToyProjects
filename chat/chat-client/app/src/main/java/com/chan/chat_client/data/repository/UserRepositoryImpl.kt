package com.chan.chat_client.data.repository

import com.chan.chat_client.data.datastore.SessionStorage
import com.chan.chat_client.data.mapper.toDomain
import com.chan.chat_client.data.model.UserDto
import com.chan.chat_client.domain.model.User
import com.chan.chat_client.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): UserRepository {
    override fun getUsers(): Flow<List<User>> = flow {
        runCatching {
            val request = httpClient.get {
                url("http://10.0.2.2:8080/member/list")
            }

            request.body<List<UserDto>>()
        }
            .onFailure {
                throw it
            }
            .onSuccess {
                emit(it
                    .filter { sessionStorage.get()?.email != it.email }
                    .map { it.toDomain() })
            }
    }

    override fun getMyUserEmail(): Flow<String> = flow {
        sessionStorage.get()?.let {
            emit(it.email)
        } ?: throw IllegalArgumentException("not found user email")
    }
}