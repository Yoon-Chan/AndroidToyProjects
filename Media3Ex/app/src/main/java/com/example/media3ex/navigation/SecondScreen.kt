package com.example.media3ex.navigation

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SecondScreen : Screen {
    data class State(
        val id: Int,
    ): CircuitUiState
}