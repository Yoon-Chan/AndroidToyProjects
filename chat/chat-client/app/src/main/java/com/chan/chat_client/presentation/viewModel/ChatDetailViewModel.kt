package com.chan.chat_client.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chan.chat_client.domain.repository.ChatRepository
import com.chan.chat_client.domain.repository.StompMessageClient
import com.chan.chat_client.domain.repository.UserRepository
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailEffect
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailEvent
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailState
import com.chan.chat_client.presentation.navigation.ChatDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val stompMessageClient: StompMessageClient,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ChatDetailState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ChatDetailEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        val info = savedStateHandle.toRoute<ChatDetail>()


        Timber.e("ChatDetailViewModel stompMessageClient start ${info.roomId}")
        stompMessageClient.getMessageStream(info.roomId)
            .onEach { message ->
                Timber.e("ChatDetailViewModel stompMessageClient success $message")
                _state.update {
                    it.copy(
                        messages = state.value.messages + message
                    )
                }
                _effect.send(ChatDetailEffect.ScrollToBottom)
            }
            .catch {
                Timber.e("ChatDetailViewModel stompMessageClient error : $it")
            }
            .launchIn(viewModelScope)



        onEvent(ChatDetailEvent.GetHistoryMessage(info.roomId))
    }

    fun onEvent(event: ChatDetailEvent) {
        when (event) {
            is ChatDetailEvent.GetHistoryMessage -> {
                viewModelScope.launch {
                    combine(
                        chatRepository.historyRoomMessage(event.roomId),
                        userRepository.getMyUserEmail()
                    ) { messages, email ->
                        ChatDetailState(
                            roomId = event.roomId,
                            email = email,
                            messages = messages
                        )
                    }
                        .catch {
                            Timber.e("ChatDetailViewModel onEvent historyRoomMessage error : $it")
                        }
                        .collect { chatDetailState ->
                            _state.update { chatDetailState }
//                            _effect.send(ChatDetailEffect.ScrollToBottom)
                        }
                }
            }

            is ChatDetailEvent.SendMessage -> {
                viewModelScope.launch {
                    stompMessageClient.sendMessage(state.value.roomId, event.message)
                    _state.update { it.copy(text = "") }
                }
            }

            is ChatDetailEvent.OnValueChange -> {
                _state.update { it.copy(text = event.text) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            stompMessageClient.close()
        }
    }
}