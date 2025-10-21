package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.media3ex.navigation.MainEvent
import com.example.media3ex.navigation.MainScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(MainScreen::class, SingletonComponent::class)
class MainPresenter @Inject constructor(): Presenter<MainScreen.State> {

    @Composable
    override fun present(): MainScreen.State {
        var counter by rememberRetained {
            mutableStateOf(1)
        }
        return MainScreen.State(id = counter) { event ->
            when(event) {
                MainEvent.Decrease -> counter++
                MainEvent.Increase -> counter--
            }
        }
    }
}