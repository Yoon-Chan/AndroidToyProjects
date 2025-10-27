package com.example.media3ex.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.media3ex.localprovider.ChromeController
import com.example.media3ex.localprovider.LocalChromeController
import com.example.media3ex.navigation.MainScreen
import com.example.media3ex.navigation.MediaBottomNavigation
import com.example.media3ex.navigation.MediaEvent
import com.example.media3ex.navigation.MediaScreen
import com.example.media3ex.navigation.SecondScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(MediaScreen::class, ActivityRetainedComponent::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MediaUi(state: MediaScreen.State, modifier: Modifier = Modifier) {
    val backStack = rememberSaveableBackStack(root = MainScreen)
    val navigator = rememberCircuitNavigator(backStack)

    var currentBottomScreen by remember {
        mutableStateOf<Screen>(MainScreen)
    }
    val bottoms = listOf(
        MainScreen,
        SecondScreen
    )
    val chrome = remember { ChromeController() }

    BackHandler {
        Log.e("vsvx13", "backHandler ${state.currentId}")
        if(state.currentId != null) {
            state.eventSink(MediaEvent.ClearCurrentId)
        } else {
            state.eventSink(MediaEvent.OnBack)
        }
    }

    LaunchedEffect(state.currentId) {
        if (state.currentId != null) {
            chrome.hideBottomBar()
        } else {
            chrome.showBottomBar()
        }
    }

    CompositionLocalProvider(LocalChromeController provides chrome) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                MediaBottomNavigation(
                    isBottomBarVisible = chrome.bottomBarVisible,
                    bottoms = bottoms,
                    currentBottomScreen = currentBottomScreen,
                    onClickNavigation = { screen ->
                        currentBottomScreen = screen
                        navigator.resetRoot(screen)
                    }
                )
            }
        ) {
            NavigableCircuitContent(
                modifier = Modifier,
                navigator = navigator,
                backStack = backStack,
            )

            AnimatedVisibility(
                visible = state.currentId != null,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .background(color = Color.Magenta),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Click Item ${state.currentId ?: -1}")
                }
            }
        }
    }
}