package com.example.mediaeditex.presentation.videocut.model

data class VideoCutState(
    val url: String? = null,
    val duration: Long = 0L,
    val thumbnails: List<ByteArray> = emptyList(),
    val startPosition: Long = 0L,
    val endPosition: Long = 0L,
    val isLoading: Boolean = false
)
