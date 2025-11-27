package com.example.mediaeditex.presentation.videocut.model

sealed interface VideoCutEvent {
    data object OnClickVideoSelect: VideoCutEvent
    data class MediaSelect(val url: String): VideoCutEvent
    data class SetStartPosition(val ratio: Float): VideoCutEvent
    data class SetEndPosition(val ratio: Float): VideoCutEvent
    data object EditVideo: VideoCutEvent
}