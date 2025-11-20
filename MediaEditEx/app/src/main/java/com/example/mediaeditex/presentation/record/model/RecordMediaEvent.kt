package com.example.mediaeditex.presentation.record.model

import java.io.File

sealed interface RecordMediaEvent {
    data object StartRecord: RecordMediaEvent
    data object StopRecord: RecordMediaEvent
    data class SaveThumbnail(val file: File): RecordMediaEvent
}