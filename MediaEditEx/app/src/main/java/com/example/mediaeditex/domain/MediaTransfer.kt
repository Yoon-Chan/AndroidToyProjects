package com.example.mediaeditex.domain

import kotlinx.coroutines.flow.Flow

interface MediaTransfer {
    suspend fun startTransfer(url: String, text: String): Flow<TransferState>
    suspend fun startTransferMixingMusic(mediaUrl: String, musicUrl: String): Flow<String>
}