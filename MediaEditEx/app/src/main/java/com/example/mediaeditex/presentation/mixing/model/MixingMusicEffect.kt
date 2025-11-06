package com.example.mediaeditex.presentation.mixing.model

sealed interface MixingMusicEffect {
    data class ResultScreen(val uri: String): MixingMusicEffect
}