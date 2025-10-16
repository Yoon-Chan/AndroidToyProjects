package com.example.media3ex.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent

@CircuitInject(SecondScreen::class, SingletonComponent::class)
class SecondUi(): Ui<SecondScreen.State> {
    @Composable
    override fun Content(
        state: SecondScreen.State,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Magenta),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${state.id}"
            )
        }
    }
}