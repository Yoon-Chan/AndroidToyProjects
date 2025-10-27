package com.example.media3ex.navigation

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object MediaScreen : Screen {
    data class State(
        val id: Int,
        val currentId: Long?,
        val eventSink: (MediaEvent) -> Unit
    ): CircuitUiState
}

sealed interface MediaEvent {
    data object OnBack: MediaEvent
    data object ClearCurrentId: MediaEvent
}