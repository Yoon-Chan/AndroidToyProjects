package com.chan.chat_client.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.chat_client.domain.repository.LoginRepository
import com.chan.chat_client.presentation.model.main.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = loginRepository.isLogin().first()
            )
            state = state.copy(isCheckingAuth = false)
        }
    }
}