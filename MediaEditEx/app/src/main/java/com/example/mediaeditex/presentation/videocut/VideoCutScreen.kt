package com.example.mediaeditex.presentation.videocut

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme
import com.example.mediaeditex.presentation.util.ObserveAsEvents
import com.example.mediaeditex.presentation.videocut.model.VideoCutEffect
import com.example.mediaeditex.presentation.videocut.model.VideoCutEvent
import com.example.mediaeditex.presentation.videocut.model.VideoCutState

@Composable
fun VideoCutScreenRoot(
    viewModel: VideoCutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.e("vsvx13", "video $uri")
        uri?.let {
            viewModel.onEvent(VideoCutEvent.MediaSelect(it.toString()))
        }
    }


    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            VideoCutEffect.MediaSelectClick -> {
                mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }
        }
    }

    VideoCutScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun VideoCutScreen(
    modifier: Modifier = Modifier,
    state: VideoCutState,
    onEvent: (VideoCutEvent) -> Unit
) {
    val context = LocalContext.current

    if (state.url == null) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                onEvent(VideoCutEvent.OnClickVideoSelect)
            }) {
                Text("영상 선택하기")
            }
        }
    } else {
        // ExoPlayer 생성
        if (!state.isLoading) {
            val inputMediaItem = MediaItem.fromUri(state.url.toUri())
            var videoRatio by remember {
                mutableStateOf<Float?>(null)
            }
            val exoPlayer = remember(state.url) {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(inputMediaItem)
                    prepare()
                    playWhenReady = false
                    Log.e(
                        "vsvx13",
                        "size : ${this.videoSize.width}, ${this.videoSize.height}, ${this.videoSize.pixelWidthHeightRatio}"
                    )
                    addListener(object : Player.Listener {
                        override fun onVideoSizeChanged(videoSize: VideoSize) {
                            super.onVideoSizeChanged(videoSize)
                            // 비디오 크기 정보가 변경될 때 호출됩니다.
                            val width = videoSize.width // 비디오 너비 (픽셀)
                            val height = videoSize.height // 비디오 높이 (픽셀)
                            videoRatio = width.toFloat() / height.toFloat()
                            // 오리지널 비율 계산 (예: 16:9, 4:3 등)
                            // aspectRatio는 width / height 값입니다.
                            // 정수 비율로 표현하고 싶다면 GCD(최대공약수) 등을 활용하여 계산할 수 있습니다.

                            Log.e("vsvx13", "size : ${width}, ${height}")

                            // 가져온 크기 정보를 사용하여 UI 레이아웃을 동적으로 조정할 수 있습니다.
                        }
                    })
                }
            }

            Scaffold(
                modifier = modifier
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Box(
                        modifier = Modifier.weight(5f),
                        contentAlignment = Alignment.Center
                    ) {
                        PlayerSurface(
                            player = exoPlayer,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .videoRatio(videoRatio)
                        )

                        if (videoRatio == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {
                            state.thumbnails.forEach { thumbnail ->
                                Image(
                                    modifier = Modifier.weight(1f),
                                    bitmap = thumbnail.decodeToImageBitmap(),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

fun Modifier.videoRatio(ratio: Float?): Modifier {
    return ratio?.let {
        if (ratio == 0f) this.height(1.dp) else
            this.aspectRatio(ratio)
    } ?: this.height(1.dp)
}

@Preview
@Composable
private fun VideoCutScreenPreview() {
    MediaEditExTheme {
        VideoCutScreen(
            state = VideoCutState(),
            onEvent = {}
        )
    }
}