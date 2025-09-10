package com.chan.chat_client.data.repository

import com.chan.chat_client.data.datastore.SessionStorage
import com.chan.chat_client.data.mapper.toDomain
import com.chan.chat_client.data.model.dto.ChatMessageRequest
import com.chan.chat_client.data.model.dto.ChatMessageResponse
import com.chan.chat_client.data.model.dto.StompChatResponse
import com.chan.chat_client.domain.model.ChatMessage
import com.chan.chat_client.domain.repository.StompMessageClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import timber.log.Timber
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class DefaultStompMessageClient @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : StompMessageClient {
    private var session: StompSession? = null

    @OptIn(ExperimentalEncodingApi::class)
    override fun getMessageStream(roomId: Long): Flow<ChatMessage> = flow {
        Timber.e("getMessageStream connect")
        try {
            val wsClient = KtorWebSocketClient(httpClient)
            val stompClient = StompClient(wsClient)
            session = stompClient.connect(
                url = "ws://10.0.2.2:8080/connect",
                customStompConnectHeaders = mapOf(
                    "Authorization" to "Bearer ${sessionStorage.get()?.accessToken}",
                    "Content-Type" to "application/json"
                )
            )

            val message = session!!.subscribe(
                headers = StompSubscribeHeaders(
                    destination = "/topic/${roomId}", customHeaders = mapOf(
                        "Authorization" to "Bearer ${sessionStorage.get()?.accessToken}",
                        "Content-Type" to "application/json"
                    )
                )
            )
                .mapNotNull {
                    val response = Json.decodeFromString<StompChatResponse>(it.bodyAsText)
                    val decodeString = Base64.decode(response.body)
                    Json.decodeFromString<ChatMessageResponse>(
                        String(
                            decodeString,
                            Charsets.UTF_8
                        )
                    ).toDomain()
                }
            emitAll(message)
        } catch (e: Exception) {
            Timber.e("getMessageStream Error $e")
        }
    }

    override suspend fun sendMessage(roomId: Long, message: String) {
        val request = ChatMessageRequest(
            roomId = roomId,
            message = message,
            senderEmail = sessionStorage.get()?.email ?: ""
        )
        session?.send(
            headers = StompSendHeaders(
                destination = "/publish/${roomId}", customHeaders = mapOf(
                    "Authorization" to "Bearer ${sessionStorage.get()?.accessToken}",
                    "Content-Type" to "application/json"
                )
            ),
            body = FrameBody.Text(Json.encodeToString(request))
        )
    }

    override suspend fun close() {
        session?.disconnect()
        session = null
    }
}