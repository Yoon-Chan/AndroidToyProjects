package com.example.mediaeditex.presentation.videocut.model

sealed interface VideoCutEffect {
    data object MediaSelectClick: VideoCutEffect
    data class VideoSeek(val position: Long): VideoCutEffect
    data class ResultVideo(val url: String): VideoCutEffect
}