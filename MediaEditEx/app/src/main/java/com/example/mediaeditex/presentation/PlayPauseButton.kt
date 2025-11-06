package com.example.mediaeditex.presentation

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(player: Player, isVisible: Boolean, modifier: Modifier = Modifier) {
    val state = rememberPlayPauseButtonState(player)

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = if (state.showPlay) Icons.Default.PlayArrow else Icons.Filled.Close,
                contentDescription =
                    if (state.showPlay) "play"
                    else "pause",
                tint = Color.Red
            )
        }
    }
}