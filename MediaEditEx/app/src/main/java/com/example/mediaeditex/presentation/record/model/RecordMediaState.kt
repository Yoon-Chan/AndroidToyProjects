package com.example.mediaeditex.presentation.record.model

data class RecordMediaState(
    val isRecord: Boolean = false,
    val recordTime: Int = 0,
    val fileName: String? = null,
    val videos: List<String> = emptyList()
)
