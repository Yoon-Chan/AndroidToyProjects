package com.example.mediaeditex.presentation.model

import com.example.mediaeditex.domain.DrawType
import com.google.common.collect.ImmutableList

data class MainState(
    val url: String = "https://videos.pexels.com/video-files/6963395/6963395-hd_1080_1920_25fps.mp4",
    val progress: Int? = null,
    val downloadUrl: String? = null,
    val draws: List<DrawType> = emptyList()
)
