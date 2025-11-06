package com.example.mediaeditex.domain

sealed interface TransferState {
    data class Progress(val progress: Int): TransferState
    data class Done(val path: String): TransferState
    data class Error(val errorMessage: String?): TransferState
}