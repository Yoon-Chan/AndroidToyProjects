package com.example.media3ex.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.media3ex.navigation.MainEvent
import com.example.media3ex.navigation.MainScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(MainScreen::class, ActivityRetainedComponent::class)
class MainUi : Ui<MainScreen.State> {

    @Composable
    override fun Content(
        state: MainScreen.State,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Yellow)
                .clickable {
                    state.eventSink(MainEvent.Increase)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${state.id}"
            )
        }
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    val state = MainScreen.State(id = 1) {}
    MainUi().Content(state, modifier = Modifier)
}