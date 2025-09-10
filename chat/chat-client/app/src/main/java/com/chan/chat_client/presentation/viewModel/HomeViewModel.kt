package com.chan.chat_client.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.chat_client.domain.repository.UserRepository
import com.chan.chat_client.presentation.model.home.HomeEffect
import com.chan.chat_client.presentation.model.home.HomeEvent
import com.chan.chat_client.presentation.model.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            userRepository.getUsers().collect { users ->
                _state.update {
                    it.copy(users = users)
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            else -> {}
        }
    }
}