package com.example.mediaeditex.presentation.mixing

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaeditex.domain.MediaTransfer
import com.example.mediaeditex.presentation.mixing.model.MixingMusicEffect
import com.example.mediaeditex.presentation.mixing.model.MixingMusicEvent
import com.example.mediaeditex.presentation.mixing.model.MixingMusicState
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
class MixingMusicViewModel @Inject constructor(
    private val mediaTransfer: MediaTransfer
): ViewModel() {

    private val _state = MutableStateFlow(MixingMusicState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MixingMusicEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: MixingMusicEvent) {
        when(event) {

            is MixingMusicEvent.OnChangeMedia -> _state.update { it.copy(mediaUri = event.mediaUri) }
            is MixingMusicEvent.OnChangeMedia2 -> _state.update { it.copy(mediaUri2 = event.mediaUri2) }
            is MixingMusicEvent.OnChangeMusic -> _state.update { it.copy(musicUri = event.musicUri) }
            MixingMusicEvent.OnClickMixing -> {
                viewModelScope.launch {
                    val mediaUri = state.value.mediaUri
                    val mediaUri2 = state.value.mediaUri2
                    val musicUri = state.value.musicUri
                    if(mediaUri == null || musicUri == null || mediaUri2 == null) return@launch
                    mediaTransfer.startTransferMixingMusic(mediaUri, mediaUri2,musicUri)
                        .catch {
                            Log.e("vsvx13", "OnClickMixing error $it")
                        }
                        .collect {
                            _effect.send(MixingMusicEffect.ResultScreen(it))
                        }
                }
            }
            else -> Unit
        }
    }
}