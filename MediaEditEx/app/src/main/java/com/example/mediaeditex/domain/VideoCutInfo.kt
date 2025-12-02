package com.example.mediaeditex.domain

data class VideoCutInfo(
    val url: String,
    val thumbnails: List<ByteArray>,
    val duration: Long
)
