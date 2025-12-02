package com.example.mediaeditex.domain

sealed interface VideoInfo {
    data class Loading(val url: String): VideoInfo
    data class Done(val video: Video) : VideoInfo
}