package com.example.mediaeditex.presentation.record

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaeditex.domain.RecordRepository
import com.example.mediaeditex.domain.VideoInfo
import com.example.mediaeditex.presentation.record.model.RecordMediaEffect
import com.example.mediaeditex.presentation.record.model.RecordMediaEvent
import com.example.mediaeditex.presentation.record.model.RecordMediaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordMediaViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : ViewModel() {

    private var hasLoadedInitialData: Boolean = false

    private val _state = MutableStateFlow(RecordMediaState())
    val state = _state.asStateFlow()
        .onStart {
            if (!hasLoadedInitialData) {
                observeIsRecord()
                hasLoadedInitialData = true
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), RecordMediaState())

    private val _effect = Channel<RecordMediaEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RecordMediaEvent) {
        when (event) {
            is RecordMediaEvent.StartRecord -> {
                _state.update {
                    it.copy(
                        isRecord = true,
                        filePath = event.url
                    )
                }
            }

            is RecordMediaEvent.StopRecord -> {
                val fileName = state.value.filePath
                _state.update { it.copy(isRecord = false, filePath = null) }
                viewModelScope.launch {
                    if (fileName == null) {
                        //TODO: 오류 메세지 출력
                        return@launch
                    }
                    recordRepository.getVideoInfo(fileName)
                        .catch {
                            Log.e("vsvx13", "getVideoInfo error $it")
                        }
                        .collect {
                            when (it) {
                                is VideoInfo.Done -> {
                                    _state.update { state ->
                                        state.copy(
                                            videos = state.videos.map { video ->
                                                if (video is VideoInfo.Loading && video.url == it.video.url) it else video
                                            },
                                            resTime = (state.resTime - it.video.duration).coerceAtLeast(
                                                0L
                                            )
                                        )
                                    }
                                }

                                is VideoInfo.Loading -> {
                                    _state.update { state -> state.copy(videos = state.videos + it) }
                                }
                            }
                        }
                }
            }

            is RecordMediaEvent.DeleteVideo -> {
                if (event.videoInfo !is VideoInfo.Done) return

                _state.update {
                    it.copy(
                        videos = it.videos.filter { video ->
                            video != event.videoInfo
                        },
                        resTime = it.resTime + event.videoInfo.video.duration
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeIsRecord() {
        Log.e("vsvx13", "observeIsRecord")
        state
            .map { it.isRecord }
            .distinctUntilChanged()
            .onEach { isRecord ->
                if (!isRecord) {
                    _state.update { it.copy(recordTime = 0) }
                }
            }
            .flatMapLatest { isRecord ->
                if (isRecord) {
                    flow {
                        while (true) {
                            delay(1000L)
                            emit(1000)
                        }
                    }
                } else flowOf()
            }
            .onEach { time ->
                _state.update { it.copy(recordTime = it.recordTime + time) }
            }
            .launchIn(viewModelScope)
    }
}