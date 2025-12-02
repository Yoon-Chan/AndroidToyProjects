package com.example.mediaeditex.presentation.record.model

import com.example.mediaeditex.domain.VideoInfo

data class RecordMediaState(
    val isRecord: Boolean = false,
    val recordTime: Long = 0L,
    val resTime: Long = 1000L * 60 * 4,
    val filePath: String? = null,
    val videos: List<VideoInfo> = emptyList()
)
