package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent

@CircuitInject(SecondScreen::class, SingletonComponent::class)
class SecondPresenter: Presenter<SecondScreen.State> {

    @Composable
    override fun present(): SecondScreen.State {
        return SecondScreen.State(id = 1) { event ->
            when(event) {
                else -> Unit
            }
        }
    }
}