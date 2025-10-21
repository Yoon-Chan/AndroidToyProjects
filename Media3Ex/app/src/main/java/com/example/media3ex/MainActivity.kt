package com.example.media3ex

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.media3ex.navigation.MainScreen
import com.example.media3ex.navigation.SecondScreen
import com.example.media3ex.ui.theme.Media3ExTheme
import com.slack.circuit.backstack.NavArgument
import com.slack.circuit.backstack.NavDecoration
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.LocalCircuit
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Media3ExTheme {
                val backStack = rememberSaveableBackStack(root = MainScreen)
                val navigator = rememberCircuitNavigator(backStack)

                var currentBottomScreen by remember {
                    mutableStateOf<Screen>(MainScreen)
                }
                val bottoms = listOf(
                    MainScreen,
                    SecondScreen
                )

                CircuitCompositionLocals(circuit = circuit) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar(
                                contentColor = Color.Black
                            ) {
                                bottoms.forEach { screen ->
                                    NavigationBarItem(
                                        selected = screen == currentBottomScreen,
                                        onClick = {
                                            if(currentBottomScreen != screen) {
                                                currentBottomScreen = screen
                                                navigator.resetRoot(
                                                    screen,
                                                )
                                            }
                                        },
                                        icon = {
                                            when (screen) {
                                                MainScreen -> {
                                                    Icon(
                                                        imageVector = Icons.Default.Home,
                                                        contentDescription = null
                                                    )
                                                }
                                                SecondScreen -> {
                                                    Icon(
                                                        imageVector = Icons.Default.Settings,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        },
                                        label = {
                                            when (screen) {
                                                MainScreen -> {
                                                    Text(text = "홈")
                                                }
                                                SecondScreen -> {
                                                    Text(text = "설정")
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                        )
                    }
                }
            }
        }
    }
}