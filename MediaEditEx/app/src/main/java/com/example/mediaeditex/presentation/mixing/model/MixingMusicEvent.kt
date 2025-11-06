package com.example.mediaeditex.presentation.mixing.model

sealed interface MixingMusicEvent {
    data object OnClickMediaSelect: MixingMusicEvent
    data object OnClickAudioSelect: MixingMusicEvent
    data class OnChangeMedia(val mediaUri: String): MixingMusicEvent
    data class OnChangeMusic(val musicUri: String): MixingMusicEvent
    data object OnClickMixing: MixingMusicEvent
}