package com.chan.chat_client.data.repository

import com.chan.chat_client.data.mapper.toDomain
import com.chan.chat_client.data.model.GroupChatRoomDto
import com.chan.chat_client.data.model.MyChatRoomDto
import com.chan.chat_client.data.model.dto.ChatMessageResponse
import com.chan.chat_client.domain.model.ChatMessage
import com.chan.chat_client.domain.model.GroupChatRoom
import com.chan.chat_client.domain.model.MyChatRoom
import com.chan.chat_client.domain.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    val httpClient: HttpClient
) : ChatRepository {
    override fun getMyChatRoom(): Flow<List<MyChatRoom>> = flow {
        runCatching {
            val request = httpClient.get {
                url("http://10.0.2.2:8080/chat/my/rooms")
            }

            request.body<List<MyChatRoomDto>>()
        }
            .onFailure {
                throw it
            }
            .onSuccess { rooms ->
                emit(rooms.map { it.toDomain() })
            }
    }

    override fun postCreateGroupChatRoom(roomName: String): Flow<GroupChatRoom> = flow {
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
                emit(GroupChatRoom(roomId = id, roomName = roomName))
            }
    }

    override fun getGroupChatRoom(): Flow<List<GroupChatRoom>> = flow {
        runCatching {
            val request = httpClient.get {
                url("http://10.0.2.2:8080/chat/room/group/list")
            }

            request.body<List<GroupChatRoomDto>>()
        }
            .onFailure {
                throw it
            }
            .onSuccess { groupRooms ->
                emit(groupRooms.map { it.toDomain() })
            }
    }

    override fun historyRoomMessage(roomId: Long): Flow<List<ChatMessage>> = flow {
        runCatching {
            val request = httpClient.get {
                url("http://10.0.2.2:8080/chat/history/${roomId}")
            }

            request.body<List<ChatMessageResponse>>()
        }
            .onFailure {
                throw it
            }
            .onSuccess { chatResponse ->
                emit(chatResponse.map { it.toDomain() })
            }
    }

    override fun joinGroupChatRoom(roomId: Long): Flow<Long> = flow {
        runCatching {
            val request = httpClient.post {
                url("http://10.0.2.2:8080/chat/room/group/${roomId}/join")
            }
            request.body<Unit>()
        }
            .onFailure {
                throw it
            }
            .onSuccess {
                emit(roomId)
            }
    }
}