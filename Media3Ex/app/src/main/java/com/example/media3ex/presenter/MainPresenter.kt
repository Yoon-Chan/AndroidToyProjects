package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.media3ex.domain.MediaController
import com.example.media3ex.navigation.MainEvent
import com.example.media3ex.navigation.MainScreen
import com.example.media3ex.navigation.MediaScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class MainPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val mediaController: MediaController
): Presenter<MainScreen.State> {

    @Composable
    override fun present(): MainScreen.State {

        var counter by rememberRetained {
            mutableStateOf(1)
        }

        return MainScreen.State(id = counter) { event ->
            when(event) {
                MainEvent.Decrease -> counter++
                MainEvent.Increase -> counter--
                MainEvent.OnBack -> navigator.pop()
                is MainEvent.OnDetail -> mediaController.play(event.id)
            }
        }
    }

    @CircuitInject(MainScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): MainPresenter
    }
}