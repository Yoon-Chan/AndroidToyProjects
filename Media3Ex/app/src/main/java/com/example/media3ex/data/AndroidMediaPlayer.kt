package com.example.media3ex.data

import android.content.Context
import com.example.media3ex.domain.MediaController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AndroidMediaPlayer @Inject constructor(
    private val context: Context
): MediaController {

    private val _currentId: MutableStateFlow<Long?> = MutableStateFlow(null)
    override val currentId: StateFlow<Long?> = _currentId.asStateFlow()

    override fun play(id: Long) {
        _currentId.update { id }
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun clear() {
        _currentId.update { null }
    }
}