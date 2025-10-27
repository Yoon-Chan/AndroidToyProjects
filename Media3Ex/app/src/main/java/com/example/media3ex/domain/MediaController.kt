package com.example.media3ex.domain

import kotlinx.coroutines.flow.StateFlow

interface MediaController {
    val currentId: StateFlow<Long?>

    fun play(id: Long)

    fun pause()

    fun resume()

    fun clear()
}