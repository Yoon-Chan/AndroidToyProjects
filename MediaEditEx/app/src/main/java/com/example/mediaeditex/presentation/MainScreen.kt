package com.example.mediaeditex.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mediaeditex.domain.DrawType
import com.example.mediaeditex.presentation.model.MainEffect
import com.example.mediaeditex.presentation.model.MainEvent
import com.example.mediaeditex.presentation.model.MainState
import com.example.mediaeditex.presentation.util.ObserveAsEvents

@Composable
fun MainScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is MainEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    MainScreen(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    onEvent: (MainEvent) -> Unit,
) {
    var width by remember {
        mutableStateOf(0f)
    }
    var height by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(width, height) {
        Log.d("vsvx13", "width $width height $height")
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .height(IntrinsicSize.Min)) {
            SimpleVideoPlayer(videoUrl = state.url, progress = state.progress)

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                width = size.width
                height = size.height

                state.draws.forEach { type ->

                    when(type) {
                        is DrawType.Rect ->{
                            Log.d("vsvx13", "DrawRect")
                            drawRect(color = Color(type.bgColor), topLeft = Offset(type.x - type.size/2, type.y - type.size/2), size = Size(type.size.toFloat(), type.size.toFloat()))
                        }

                        is DrawType.Text -> TODO()
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                onEvent(MainEvent.OnClickTransfer(state.url, "히히 안녕하세요"))
            }) {
                Text(text = "변환하기")
            }

            TextButton(
                onClick = {
                    onEvent(
                        MainEvent.CreateRect(
                            x = width/2f,
                            y = height/2f,
                            size = 300,
                        )
                    )
                }
            ) {
                Text("도형 생성")
            }

            TextButton(
                onClick = {}
            ) {
                Text("글자 생성하기")
            }
        }

        state.downloadUrl?.let { download ->
            SimpleVideoPlayer(
                videoUrl = download
            )
        }
    }
}