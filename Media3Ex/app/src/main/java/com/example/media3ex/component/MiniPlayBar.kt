package com.example.media3ex.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniPlayBar(
    modifier: Modifier = Modifier,
    currentId: Long,
) {
    SharedElementTransitionScope {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Magenta)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .background(color = Color.LightGray)
                    .sharedBounds(
                        rememberSharedContentState(currentId),
                        requireAnimatedScope(SharedElementTransitionScope.AnimatedScope.Navigation)
                    )
            )
        }
    }
}