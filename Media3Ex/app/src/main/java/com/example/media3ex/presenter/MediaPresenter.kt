package com.example.media3ex.presenter

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.media3ex.domain.MediaController
import com.example.media3ex.navigation.MediaEvent
import com.example.media3ex.navigation.MediaScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class MediaPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val mediaController: MediaController
) : Presenter<MediaScreen.State> {

    @Composable
    override fun present(): MediaScreen.State {
        val currentId by mediaController.currentId.collectAsState()
        val activity = LocalActivity.current
        return MediaScreen.State(
            id = 0,
            currentId = currentId
        ) { event ->
            when (event) {
                MediaEvent.OnBack -> activity?.finish()
                MediaEvent.ClearCurrentId ->  mediaController.clear()
            }
        }
    }

    @CircuitInject(MediaScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): MediaPresenter
    }
}