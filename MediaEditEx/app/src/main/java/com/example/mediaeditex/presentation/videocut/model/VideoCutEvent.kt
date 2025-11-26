package com.example.mediaeditex.presentation.videocut.model

sealed interface VideoCutEvent {
    data object OnClickVideoSelect: VideoCutEvent
    data class MediaSelect(val url: String): VideoCutEvent
}