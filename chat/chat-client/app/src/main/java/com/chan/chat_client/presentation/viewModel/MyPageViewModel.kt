package com.chan.chat_client.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chan.chat_client.domain.repository.ChatRepository
import com.chan.chat_client.presentation.model.mypage.MyPageEffect
import com.chan.chat_client.presentation.model.mypage.MyPageEvent
import com.chan.chat_client.presentation.model.mypage.MyPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    saveStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MyPageState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MyPageEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(MyPageEvent.GetMyRoom)
    }

    fun onEvent(event: MyPageEvent) {
        when (event) {
            MyPageEvent.GetMyRoom -> {
                viewModelScope.launch {
                    chatRepository.getMyChatRoom()
                        .collect { rooms ->
                            _state.update { it.copy(rooms = rooms) }
                        }
                }
            }
        }
    }
}