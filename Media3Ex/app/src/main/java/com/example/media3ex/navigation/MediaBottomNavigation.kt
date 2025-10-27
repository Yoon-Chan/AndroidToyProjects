package com.example.media3ex.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.media3ex.ui.theme.Media3ExTheme
import com.slack.circuit.runtime.screen.Screen

@Composable
fun MediaBottomNavigation(
    modifier: Modifier = Modifier,
    isBottomBarVisible: Boolean,
    bottoms: List<Screen>,
    currentBottomScreen: Screen,
    onClickNavigation: (Screen) -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isBottomBarVisible,//chrome.bottomBarVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        NavigationBar(
            contentColor = Color.Black
        ) {
            bottoms.forEach { screen ->
                NavigationBarItem(
                    selected = screen == currentBottomScreen,
                    onClick = {
                        if (currentBottomScreen != screen) {
                            onClickNavigation(screen)
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
}

@Preview
@Composable
private fun MediaBottomNavigationPreview() {
    Media3ExTheme {
        MediaBottomNavigation(
            modifier = Modifier,
            isBottomBarVisible = true,
            bottoms = listOf(MainScreen, SecondScreen),
            currentBottomScreen = MainScreen,
            onClickNavigation = {}
        )
    }
}