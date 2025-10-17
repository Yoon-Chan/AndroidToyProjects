package com.example.media3ex.di

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.example.media3ex.navigation.MainScreen
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.backstack.NavArgument
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.animation.AnimatedNavDecorator
import com.slack.circuit.foundation.animation.AnimatedNavEvent
import com.slack.circuit.foundation.animation.AnimatedNavState
import com.slack.circuit.foundation.animation.AnimatedScreenTransform
import com.slack.circuit.foundation.animation.NoOpAnimatedScreenTransform
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import kotlinx.collections.immutable.ImmutableList
import kotlin.reflect.KClass


@Module
@InstallIn(SingletonComponent::class)
abstract class CircuitModule {

    @Multibinds
    abstract fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds
    abstract fun uiFactories(): Set<Ui.Factory>

    companion object {
        @OptIn(ExperimentalCircuitApi::class)
        @Provides
        fun provideCircuit(
            presenterFactories: @JvmSuppressWildcards Set<Presenter.Factory>,
            uiFactories: @JvmSuppressWildcards Set<Ui.Factory>,
        ): Circuit = Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .build()
    }
}