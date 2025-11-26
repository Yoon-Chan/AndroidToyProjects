package com.example.mediaeditex.presentation.videocut

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaeditex.domain.RecordRepository
import com.example.mediaeditex.presentation.videocut.model.VideoCutEffect
import com.example.mediaeditex.presentation.videocut.model.VideoCutEvent
import com.example.mediaeditex.presentation.videocut.model.VideoCutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCutViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VideoCutState())
    val state = _state.asStateFlow()

    private val _effect = Channel<VideoCutEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: VideoCutEvent) {
        when(event) {
            VideoCutEvent.OnClickVideoSelect -> {
                viewModelScope.launch {
                    _effect.send(VideoCutEffect.MediaSelectClick)
                }
            }

            is VideoCutEvent.MediaSelect -> {
                viewModelScope.launch {
                    _state.update { it.copy(url = event.url, isLoading = true) }
                    recordRepository.getThumbnailInfo(event.url)
                        .onSuccess { videoCutInfo ->
                            Log.e("vsvx13", "onSuccess $videoCutInfo")
                            _state.update { it.copy(url = videoCutInfo.url, duration = videoCutInfo.duration, thumbnails = videoCutInfo.thumbnails, isLoading = false) }
                        }
                        .onFailure {
                            Log.e("vsvx13", "onFailure $it")
                        }
                }
            }
            else -> Unit
        }
    }
}