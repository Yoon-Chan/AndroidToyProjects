package com.example.mediaeditex.domain

import kotlinx.coroutines.flow.Flow

interface MediaTransfer {
    suspend fun startTransfer(url: String, text: String): Flow<TransferState>
    suspend fun startTransferMixingMusic(mediaUrl: String,mediaUrl2: String, musicUrl: String): Flow<String>
    suspend fun editCutVideo(url: String, startPosition: Long, endPosition: Long): Flow<String>
}