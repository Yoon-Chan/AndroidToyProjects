package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import com.example.media3ex.navigation.MainScreen
import com.slack.circuit.runtime.presenter.Presenter

class MainPresenter: Presenter<MainScreen.State> {

    @Composable
    override fun present(): MainScreen.State {
        return MainScreen.State(id = 1)
    }
}