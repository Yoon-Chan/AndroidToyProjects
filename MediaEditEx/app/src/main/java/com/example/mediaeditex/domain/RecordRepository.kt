package com.example.mediaeditex.domain

import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    fun getVideoInfo(url: String): Flow<VideoInfo>
}