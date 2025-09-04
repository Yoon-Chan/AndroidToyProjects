package com.chan.chat_client.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chan.chat_client.presentation.navigation.ChatRoom
import com.chan.chat_client.presentation.navigation.Destination
import com.chan.chat_client.presentation.navigation.Main
import com.chan.chat_client.presentation.navigation.MyPage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onChatDetail: (Long) -> Unit
) {
    val navController = rememberNavController()
    val startDestination = Destination.MAIN
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(
                                route = when (destination) {
                                    Destination.MAIN -> Main
                                    Destination.CHAT_ROOM -> ChatRoom
                                    Destination.MY_PAGE -> MyPage
                                }
                            ) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.destinationName
                            )
                        },
                        label = { Text(destination.destinationName) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Main
        ) {
            composable<Main> {
                MainScreenRoot()
            }

            composable<ChatRoom> {
                ChatRoomScreenRoot(
                    onChatDetail = onChatDetail
                )
            }

            composable<MyPage> {
                MyPageScreen()
            }
        }
    }
}