package com.chan.chat_client.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chan.chat_client.domain.repository.LoginRepository
import com.chan.chat_client.presentation.model.login.LoginEffect
import com.chan.chat_client.presentation.model.login.LoginEvent
import com.chan.chat_client.presentation.model.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.CreateMember -> {
                viewModelScope.launch {
                    loginRepository.onCreateUser(event.email, event.password)
                        .catch {
                            Timber.e("onEvent LoginEvent.CreateMember: $it")
                        }
                        .collect {
                            _effect.send(LoginEffect.Toast("회원가입이 완료되었습니다."))
                        }
                }
            }
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    loginRepository.doLogin(event.email, event.password)
                        .catch {
                            Timber.e("onEvent LoginEvent.Login: $it")
                        }
                        .collect {
                            _effect.send(LoginEffect.Home)
                        }
                }
            }
        }
    }
}