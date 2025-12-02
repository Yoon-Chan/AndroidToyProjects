package com.example.mediaeditex.presentation.videocut

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme
import com.example.mediaeditex.presentation.util.ObserveAsEvents
import com.example.mediaeditex.presentation.videocut.model.VideoCutEffect
import com.example.mediaeditex.presentation.videocut.model.VideoCutEvent
import com.example.mediaeditex.presentation.videocut.model.VideoCutState
import kotlinx.coroutines.delay

@Composable
fun VideoCutScreenRoot(
    viewModel: VideoCutViewModel = hiltViewModel(),
    onNavigateResult: (String) -> Unit,
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

            is VideoCutEffect.ResultVideo -> {
                Log.e("vsvx13", "VideoCutEffect.ResultVideo ${effect.url}")
                onNavigateResult(effect.url)
            }
            else -> Unit
        }
    }

    state.url?.let {
        VideoCutScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    } ?: Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.onEvent(VideoCutEvent.OnClickVideoSelect)
        }) {
            Text("영상 선택하기")
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoCutScreen(
    modifier: Modifier = Modifier,
    state: VideoCutState,
    onEvent: (VideoCutEvent) -> Unit
) {
    val context = LocalContext.current
    var videoRatio by remember {
        mutableStateOf<Float?>(null)
    }

    val exoPlayer = remember(state.url) {
        val inputMediaItem = MediaItem.fromUri(state.url!!.toUri())
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
                    val width = videoSize.width // 비디오 너비 (픽셀)
                    val height = videoSize.height // 비디오 높이 (픽셀)
                    videoRatio = width.toFloat() / height.toFloat()
                    Log.e("vsvx13", "size : ${width}, ${height}")
                }
            }
            )
        }
    }

    val playerPauseStart = rememberPlayPauseButtonState(exoPlayer)
    LaunchedEffect(playerPauseStart.showPlay) {
        Log.e("vsvx13", "showPlay ${playerPauseStart.showPlay}")
        if (playerPauseStart.showPlay) {
            exoPlayer.pause()
            exoPlayer.seekTo(state.startPosition)
            return@LaunchedEffect
        }

        while (!playerPauseStart.showPlay) {
            if (exoPlayer.currentPosition >= state.endPosition) {
                if (!playerPauseStart.showPlay) {
                    playerPauseStart.onClick()
                    exoPlayer.seekTo(state.startPosition)
                }
                break
            }
            delay(33)
        }
//        while (exoPlayer.isPlaying) {
//            if(exoPlayer.currentPosition > state.endPosition) {
//                Log.e("vsvx13", "${exoPlayer.currentPosition}  ${state.endPosition}")
//                playerPauseStart.onClick()
//                exoPlayer.seekTo(state.startPosition)
//                break
//            }
//        }
    }

    LaunchedEffect(state.startPosition) {
        exoPlayer.seekTo(state.startPosition)
    }

    // ExoPlayer 생성
    if (!state.isLoading) {
        var boxHeight by remember {
            mutableStateOf(0.dp)
        }
        var boxWidth by remember {
            mutableStateOf(0.dp)
        }

        var leftWidth by remember {
            mutableStateOf(0.dp)
        }

        var rightWidth by remember {
            mutableStateOf(0.dp)
        }

        val density = LocalDensity.current

        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    TextButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        onClick = { onEvent(VideoCutEvent.EditVideo)}
                    ) {
                        Text(
                            text = "변환하기"
                        )
                    }
                }


                Box(
                    modifier = Modifier.weight(5f),
                    contentAlignment = Alignment.Center
                ) {
                    PlayerSurface(
                        player = exoPlayer,
                        modifier = Modifier
                            .padding(horizontal = 48.dp)
                            .fillMaxWidth()
                            .videoRatio(videoRatio)
                    )

                    IconButton(
                        onClick = playerPauseStart::onClick,
                        modifier = modifier,
                        enabled = playerPauseStart.isEnabled
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = if (playerPauseStart.showPlay) Icons.Default.PlayArrow else Icons.Filled.Close,
                            contentDescription =
                                if (playerPauseStart.showPlay) "play"
                                else "pause",
                            tint = Color.Red
                        )
                    }

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
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text("썸네일들")
                    Spacer(modifier = Modifier.height(16.dp))

                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val minWidth = this.minWidth
                        val minHeight = this.minHeight
                        val width = this.maxWidth
                        val height = this.maxHeight

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 8.dp)
                                .onSizeChanged {
                                    boxHeight = with(density) { it.height.toDp() }
                                    boxWidth = with(density) { it.width.toDp() }
                                }
                        ) {
                            state.thumbnails.forEach { thumbnail ->
                                Image(
                                    modifier = Modifier.weight(1f),
                                    bitmap = thumbnail.decodeToImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 1. 검은색 반투명 영역 (왼쪽에 위치, leftWidth만큼 차지)
                                // Row 안에서 첫 번째에 위치하므로 왼쪽 벽에 붙습니다.
                                Box(
                                    modifier = Modifier
                                        .width(leftWidth)
                                        .height(boxHeight)
                                        .background(color = Color.Black.copy(alpha = 0.5f))
                                )

                                // 2. 핸들 (오른쪽에 위치)
                                // 검은색 영역 바로 뒤에 오므로, leftWidth가 커지면 자동으로 오른쪽으로 밀립니다.
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(boxHeight)
                                        .background(color = Color.DarkGray)
                                        .pointerInput(Unit) {
                                            detectHorizontalDragGestures(
                                                onDragStart = {
                                                    Log.e("vsvx13", "left drag start")
                                                },
                                                onDragEnd = {
                                                    Log.e("vsvx13", "left drag end")
                                                },
                                                onDragCancel = {
                                                    Log.e("vsvx13", "left drag cancel")
                                                },
                                                onHorizontalDrag = { _, offset ->
                                                    val delta = with(density) { offset.toDp() }
                                                    val targetWidth =
                                                        (leftWidth + delta).coerceAtLeast(0.dp)

                                                    // 소수점 첫째 자리까지만 반영 (0.1dp 단위)
                                                    // 예: 12.3456.dp -> 12.3.dp
                                                    val roundedWidth =
                                                        (targetWidth.value * 10).toInt() / 10f

                                                    if (leftWidth.value != roundedWidth) {
                                                        leftWidth =
                                                            if ((roundedWidth.dp - rightWidth) > boxWidth) {
                                                                boxWidth + rightWidth
                                                            } else {
                                                                roundedWidth.dp
                                                            }
                                                    }
                                                    onEvent(VideoCutEvent.SetStartPosition(leftWidth.value / boxWidth.value))
                                                }
                                            )
                                        }
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 1. 핸들 (왼쪽에 위치)
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(boxHeight)
                                        .background(color = Color.DarkGray)
                                        .pointerInput(Unit) {
                                            detectHorizontalDragGestures(
                                                onDragStart = {
                                                    Log.e("vsvx13", "right drag start")
                                                },
                                                onDragEnd = {
                                                    Log.e("vsvx13", "right drag end")
                                                },
                                                onDragCancel = {
                                                    Log.e("vsvx13", "right drag cancel")
                                                },
                                                onHorizontalDrag = { inputEvent, offset ->
                                                    // offset은 왼쪽으로 갈 때 음수이므로 rightWidth도 음수로 누적됩니다.
//                                                        rightWidth = (rightWidth + with(density) { offset.toDp() }).coerceAtMost(0.dp)
//                                                        Log.e(
//                                                            "vsvx13",
//                                                            "right drag running $offset $rightWidth"
//                                                        )

                                                    val delta = with(density) { offset.toDp() }
                                                    // 오른쪽은 왼쪽으로 갈 때 width가 늘어나지 않으므로 로직 주의 (기존 로직 유지)
                                                    // offset은 왼쪽으로 갈 때 음수 -> rightWidth에 더하면(음수 누적) 됨
                                                    val targetWidth =
                                                        (rightWidth + delta).coerceAtMost(0.dp)

                                                    // 소수점 첫째 자리 반올림 로직 적용
                                                    val roundedWidth =
                                                        (targetWidth.value * 10).toInt() / 10f

                                                    Log.e(
                                                        "vsvx13",
                                                        "inputEvent ${inputEvent.previousPosition} roundedWidth $roundedWidth"
                                                    )
                                                    if (rightWidth.value != roundedWidth) {
                                                        rightWidth =
                                                            if ((leftWidth - roundedWidth.dp) > boxWidth) {
                                                                leftWidth - boxWidth
                                                            } else {
                                                                roundedWidth.dp
                                                            }
                                                    }
                                                    onEvent(VideoCutEvent.SetEndPosition(-rightWidth.value / boxWidth.value))
                                                }
                                            )
                                        }
                                )

                                // 2. 검은색 반투명 영역 (오른쪽에 위치)
                                // rightWidth가 음수이므로 -를 붙여 양수로 변환하여 너비 설정
                                Box(
                                    modifier = Modifier
                                        .width(-rightWidth)
                                        .height(boxHeight)
                                        .background(color = Color.Black.copy(alpha = 0.5f))
                                )
                            }
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