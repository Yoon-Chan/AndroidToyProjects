package com.example.mediaeditex.presentation.model

interface MainEffect {
    data class ShowToast(val message: String?) : MainEffect
}