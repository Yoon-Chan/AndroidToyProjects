package com.example.mediaeditex.presentation.videocut

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaeditex.domain.MediaTransfer
import com.example.mediaeditex.domain.RecordRepository
import com.example.mediaeditex.presentation.videocut.model.VideoCutEffect
import com.example.mediaeditex.presentation.videocut.model.VideoCutEvent
import com.example.mediaeditex.presentation.videocut.model.VideoCutState
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
class VideoCutViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    private val mediaTransfer: MediaTransfer
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
                            _state.update { it.copy(url = videoCutInfo.url, duration = videoCutInfo.duration, thumbnails = videoCutInfo.thumbnails, isLoading = false, endPosition = videoCutInfo.duration) }
                        }
                        .onFailure {
                            Log.e("vsvx13", "onFailure $it")
                        }
                }
            }

            is VideoCutEvent.SetStartPosition -> {
//                Log.e("vsvx13", "SetStartPosition ${(state.value.duration * event.ratio).toLong().coerceIn(minimumValue = 0, maximumValue = state.value.duration)}")
                _state.update { it.copy(startPosition = (it.duration * event.ratio).toLong().coerceIn(minimumValue = 0, maximumValue = it.duration)) }
                viewModelScope.launch {
                    _effect.send(VideoCutEffect.VideoSeek(state.value.startPosition))
                }
            }
            is VideoCutEvent.SetEndPosition -> {
//                Log.e("vsvx13", "endPosition ${(state.value.duration - (state.value.duration * event.ratio).toLong()).coerceIn(minimumValue = 0, maximumValue = state.value.duration)}, ")
//                Log.e("vsvx13", "endPosition ${event.ratio}")
                _state.update { it.copy(endPosition = (it.duration - (it.duration * event.ratio).toLong()).coerceIn(minimumValue = 0, maximumValue = it.duration)) }
            }

            VideoCutEvent.EditVideo -> {
                viewModelScope.launch {
                    state.value.url?.let { url ->
                        mediaTransfer.editCutVideo(
                            url,
                            state.value.startPosition,
                            state.value.endPosition
                        )
                            .catch {
                                Log.e("vsvx13", "EditVideo Error $it")
                            }
                            .collect {
                                Log.e("vsvx13", "EditVideo Success $it")
                                _effect.send(VideoCutEffect.ResultVideo(it))
                            }
                    }
                }
            }
        }
    }
}