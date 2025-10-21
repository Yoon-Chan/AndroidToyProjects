package com.example.media3ex.di

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.example.media3ex.navigation.MainScreen
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.animation.AnimatedNavEvent
import com.slack.circuit.foundation.animation.AnimatedNavState
import com.slack.circuit.foundation.animation.AnimatedScreenTransform
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds


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
            .addAnimatedScreenTransforms(
                MainScreen::class to CustomScreenNoneAnimatedTransform,
                SecondScreen::class to CustomScreenNoneAnimatedTransform
            )
            .build()
    }
}

@ExperimentalCircuitApi
object CustomScreenNoneAnimatedTransform : AnimatedScreenTransform {

    override fun AnimatedContentTransitionScope<AnimatedNavState>.enterTransition(
        animatedNavEvent: AnimatedNavEvent
    ): EnterTransition? {
        return EnterTransition.None
    }

    override fun AnimatedContentTransitionScope<AnimatedNavState>.exitTransition(
        animatedNavEvent: AnimatedNavEvent
    ): ExitTransition? {
        return ExitTransition.None
    }
}

@ExperimentalCircuitApi
object CustomScreenSlideHorizonAnimatedTransform : AnimatedScreenTransform {

    override fun AnimatedContentTransitionScope<AnimatedNavState>.enterTransition(
        animatedNavEvent: AnimatedNavEvent
    ): EnterTransition? {
        return slideInHorizontally()
    }

    override fun AnimatedContentTransitionScope<AnimatedNavState>.exitTransition(
        animatedNavEvent: AnimatedNavEvent
    ): ExitTransition? {
        return slideOutHorizontally { it }
    }
}