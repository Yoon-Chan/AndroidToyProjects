package com.example.mediaeditex.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaeditex.domain.DrawType
import com.example.mediaeditex.domain.MediaTransfer
import com.example.mediaeditex.domain.TransferState
import com.example.mediaeditex.presentation.model.MainEffect
import com.example.mediaeditex.presentation.model.MainEvent
import com.example.mediaeditex.presentation.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaTransfer: MediaTransfer
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.OnClickTransfer -> {
                viewModelScope.launch {
                    mediaTransfer.startTransfer(event.url, event.text)
                        .collect { transfer ->
                            when(transfer) {
                                is TransferState.Done -> {
                                    Log.e("vsvx13", "Done")
                                    _state.update {
                                        it.copy(
                                            downloadUrl = transfer.path,
                                            progress = null
                                        )
                                    }
                                }
                                is TransferState.Error -> {
                                    Log.e("vsvx13", "Error")
                                    _effect.send(MainEffect.ShowToast(transfer.errorMessage))
                                }
                                is TransferState.Progress -> {
                                    Log.e("vsvx13", "Progress")
                                    state.value.downloadUrl?.let {
                                        _state.update { it.copy(progress = transfer.progress) }
                                    }
                                }
                            }
                        }
                }
            }

            is MainEvent.CreateRect -> {
                _state.update { it.copy(draws = it.draws + DrawType.Rect(
                    x = event.x,
                    y = event.y,
                    size = event.size,
                    bgColor = 0xFFFF00FF,
                    start = 0,
                    end = 0
                )) }
            }
            is MainEvent.CreateText -> {}
        }
    }

}