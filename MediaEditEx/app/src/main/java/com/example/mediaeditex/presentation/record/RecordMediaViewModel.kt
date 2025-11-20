package com.example.mediaeditex.presentation.record

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mediaeditex.presentation.record.model.RecordMediaEffect
import com.example.mediaeditex.presentation.record.model.RecordMediaEvent
import com.example.mediaeditex.presentation.record.model.RecordMediaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class RecordMediaViewModel @Inject constructor(

): ViewModel() {

    private val _state = MutableStateFlow(RecordMediaState())
    val state = _state.asStateFlow()

    private val _effect = Channel<RecordMediaEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RecordMediaEvent) {
        when(event) {
            is RecordMediaEvent.StartRecord -> {
                _state.update {
                    it.copy(
                        isRecord = true,
                        fileName = "my-record-${Instant.now().epochSecond}.mp4"
                    )
                }
            }

            is RecordMediaEvent.StopRecord -> {
                _state.update { it.copy(isRecord = false) }
            }

            is RecordMediaEvent.SaveThumbnail -> {
                Log.e("vsvx13", "saveThumbnail ${event.file.path}")
            }
        }
    }
}