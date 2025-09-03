package com.chan.chat_client.data.repository

import com.chan.chat_client.data.mapper.toDomain
import com.chan.chat_client.data.model.ChatRoomDto
import com.chan.chat_client.data.model.dto.PostCreateGroupChatRoomRequest
import com.chan.chat_client.domain.model.ChatRoom
import com.chan.chat_client.domain.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    val httpClient: HttpClient
) : ChatRepository {
    override fun getMyChatRoom(): Flow<List<ChatRoom>> = flow {
        runCatching {
            val request = httpClient.get {
                url("http://10.0.2.2:8080/chat/my/rooms")
            }

            request.body<List<ChatRoomDto>>()
        }
            .onFailure {
                throw it
            }
            .onSuccess { rooms ->
                emit(rooms.map { it.toDomain() })
            }
    }

    override fun postCreateGroupChatRoom(roomName: String): Flow<ChatRoom> = flow {
        runCatching {
            val request = httpClient.post {
                url("http://10.0.2.2:8080/chat/room/group/create")
                contentType(ContentType.Application.Json)
                parameter("roomName", roomName)
            }

            request.body<Long>()
        }
            .onFailure {
                throw it
            }
            .onSuccess { id ->
                emit(ChatRoom(id = id, roomName = roomName, isGroupChat = true, unReadCount = 0))
            }
    }
}