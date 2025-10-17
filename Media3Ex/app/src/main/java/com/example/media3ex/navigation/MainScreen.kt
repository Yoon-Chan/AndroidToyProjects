package com.example.media3ex.navigation

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object MainScreen : Screen {
    data class State(
        val id: Int,
        val eventSink: (MainEvent) -> Unit
    ): CircuitUiState
}

sealed interface MainEvent {
    data object Increase: MainEvent
    data object Decrease: MainEvent
}