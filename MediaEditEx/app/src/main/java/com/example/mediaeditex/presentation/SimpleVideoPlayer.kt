package com.example.mediaeditex.presentation

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import kotlinx.coroutines.delay


@OptIn(UnstableApi::class)
@Composable
fun SimpleVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    progress: Int? = null
) {
    val context = LocalContext.current
    var isVisible by remember {
        mutableStateOf(false)
    }
    var size by remember {
        mutableStateOf(VideoSize(0, 0))
    }
    val inputMediaItem = MediaItem.fromUri(videoUrl.toUri())

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000L)
            isVisible = false
        }
    }

    // ExoPlayer 생성
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(inputMediaItem)
            prepare()
            playWhenReady = true
            size = videoSize
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(size.pixelWidthHeightRatio)
    ) {
        PlayerSurface(
            player = exoPlayer,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isVisible = !isVisible
                }
        )

        PlayPauseButton(
            isVisible = isVisible,
            player = exoPlayer,
            modifier = Modifier.align(Alignment.Center)
        )

        progress?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                Text(text = "변환 중... $progress%")
            }
        }
    }

    // Composable이 사라질 때 리소스 해제
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}