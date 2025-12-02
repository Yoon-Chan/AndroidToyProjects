package com.example.mediaeditex.presentation.record.model

import com.example.mediaeditex.domain.VideoInfo
import java.io.File

sealed interface RecordMediaEvent {
    data class StartRecord(val url: String): RecordMediaEvent
    data object StopRecord: RecordMediaEvent
    data class DeleteVideo(val videoInfo: VideoInfo): RecordMediaEvent
}