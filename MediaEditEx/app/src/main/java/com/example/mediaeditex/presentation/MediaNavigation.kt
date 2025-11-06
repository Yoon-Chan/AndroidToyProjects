package com.example.mediaeditex.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mediaeditex.presentation.mixing.MixingMusicScreenRoot
import com.example.mediaeditex.presentation.navigation.Main
import com.example.mediaeditex.presentation.navigation.MixingMusic
import com.example.mediaeditex.presentation.navigation.ResultMedia
import com.example.mediaeditex.presentation.result.ResultScreenRoot

@Composable
fun MediaNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Main
    ) {
        composable<Main> {
            StartScreenRoot(
                onClickMixingMusic = { navController.navigate(MixingMusic)}
            )
        }

        composable<MixingMusic> {
            MixingMusicScreenRoot(
                onResult = {
                    navController.navigate(ResultMedia(it))
                }
            )
        }

        composable<ResultMedia> {
            val data = it.toRoute<ResultMedia>()
            ResultScreenRoot(
                uri = data.uri
            )
        }
    }
}