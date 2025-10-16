package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import com.example.media3ex.navigation.MainScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent

@CircuitInject(MainScreen::class, SingletonComponent::class)
class MainPresenter: Presenter<MainScreen.State> {

    @Composable
    override fun present(): MainScreen.State {
        return MainScreen.State(id = 1) { event ->
            when(event) {
                else -> Unit
            }
        }
    }
}