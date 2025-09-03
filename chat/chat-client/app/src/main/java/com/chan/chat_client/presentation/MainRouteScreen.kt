package com.chan.chat_client.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chan.chat_client.presentation.navigation.ChatDetail
import com.chan.chat_client.presentation.navigation.Home
import com.chan.chat_client.presentation.navigation.Login
import com.chan.chat_client.presentation.screen.ChatDetailScreen
import com.chan.chat_client.presentation.screen.HomeScreen
import com.chan.chat_client.presentation.screen.LoginScreenRoot

@Composable
fun MainRouteScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreenRoot(
                goHome = {
                    navController.navigate(Home) {
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen()
        }

        composable<ChatDetail> {
            ChatDetailScreen()
        }
    }
}