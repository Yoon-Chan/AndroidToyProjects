package com.chan.chat_client.data.repository

import com.chan.chat_client.data.datastore.SessionStorage
import com.chan.chat_client.data.mapper.toDomain
import com.chan.chat_client.data.model.AuthInfoEntity
import com.chan.chat_client.data.model.UserDto
import com.chan.chat_client.data.model.dto.CreateMemberRequest
import com.chan.chat_client.data.model.dto.MemberLoginRequest
import com.chan.chat_client.data.model.dto.MemberLoginResponse
import com.chan.chat_client.domain.model.User
import com.chan.chat_client.domain.repository.LoginRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : LoginRepository {
    override fun onCreateUser(email: String, password: String): Flow<Long> = flow {
        runCatching {
            val request = httpClient.post {
                url("http://10.0.2.2:8080/member/create")
                setBody(
                    CreateMemberRequest(
                        name = "User${UUID.randomUUID().toString().substring(0, 8)}",
                        email = email,
                        password = password
                    )
                )
            }

            request.body<Long>()
        }
            .onFailure {
                throw it
            }
            .onSuccess {
                emit(it)
            }
    }

    override fun doLogin(email: String, password: String): Flow<Long> = flow {
        runCatching {
            val request = httpClient.post {
                url("http://10.0.2.2:8080/member/doLogin")
                setBody(
                    MemberLoginRequest(
                        email = email,
                        password = password
                    )
                )
            }

            request.body<MemberLoginResponse>()
        }
            .onFailure {
                throw it
            }
            .onSuccess {
                sessionStorage.set(
                    AuthInfoEntity(
                        accessToken = it.token,
                        email = email,
                        password = password
                    )
                )
                emit(it.id)
            }
    }

    override fun isLogin(): Flow<Boolean> = flow {
        emit(sessionStorage.get() != null)
    }

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
}