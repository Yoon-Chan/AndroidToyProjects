package com.chan.chat_client.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.chat_client.domain.repository.ChatRepository
import com.chan.chat_client.presentation.model.chatroom.ChatRoomEffect
import com.chan.chat_client.presentation.model.chatroom.ChatRoomEffect.*
import com.chan.chat_client.presentation.model.chatroom.ChatRoomEvent
import com.chan.chat_client.presentation.model.chatroom.ChatRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ChatRoomState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ChatRoomEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            chatRepository.getGroupChatRoom()
                .collect { rooms ->
                    _state.update {
                        it.copy(
                            rooms = rooms
                        )
                    }
                }
        }
    }

    fun onEvent(event: ChatRoomEvent) {
        when (event) {
            ChatRoomEvent.NewChatRoom -> {
                viewModelScope.launch {
                    _effect.send(ChatRoomEffect.NewChatRoom)
                }
            }

            is ChatRoomEvent.NewGroupChatRoom -> {
                viewModelScope.launch {
                    chatRepository.postCreateGroupChatRoom(event.roomName)
                        .catch {
                            Timber.e("onEvent ChatRoomEvent.NewGroupChatRoom: $it")
                        }
                        .collect { room ->
                            _state.update {
                                it.copy(
                                    rooms = it.rooms + room
                                )
                            }
                            _effect.send(ToastMessage("그룹 방이 생성되었습니다."))
                        }
                }
            }

            is ChatRoomEvent.OnChatDetail -> {
                viewModelScope.launch {
                    chatRepository.joinGroupChatRoom(event.roomId)
                        .catch {
                            Timber.e("onEvent ChatRoomEvent.OnChatDetail: $it")
                        }
                        .collect {
                            _effect.send(ChatRoomEffect.OnChatDetail(event.roomId))
                        }
                }
            }
        }
    }
}