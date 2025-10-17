package com.example.media3ex.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.media3ex.navigation.SecondEvent
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.hilt.components.SingletonComponent

@CircuitInject(SecondScreen::class, SingletonComponent::class)
class SecondPresenter: Presenter<SecondScreen.State> {

    @Composable
    override fun present(): SecondScreen.State {
        var counter by remember {
            mutableStateOf(1)
        }
        return SecondScreen.State(id = counter) { event ->
            when(event) {
                SecondEvent.Decrease -> counter--
                SecondEvent.Increase -> counter++
            }
        }
    }
}