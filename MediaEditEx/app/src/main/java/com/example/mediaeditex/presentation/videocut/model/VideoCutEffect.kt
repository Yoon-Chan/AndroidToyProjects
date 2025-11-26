package com.example.mediaeditex.presentation.videocut.model

sealed interface VideoCutEffect {
    data object MediaSelectClick: VideoCutEffect
}