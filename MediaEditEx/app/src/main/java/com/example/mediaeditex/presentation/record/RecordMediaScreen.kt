package com.example.mediaeditex.presentation.record

import android.graphics.Bitmap
import android.graphics.Matrix
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mediaeditex.presentation.record.model.RecordMediaEvent
import com.example.mediaeditex.presentation.record.model.RecordMediaState
import com.example.mediaeditex.presentation.util.ObserveAsEvents
import java.io.File
import java.time.Instant

@Composable
fun RecordMediaScreenRoot(
    viewModel: RecordMediaViewModel = hiltViewModel()
) {
    var recording: Recording? by remember {
        mutableStateOf(null)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            else -> Unit
        }
    }

    val context = LocalContext.current
    RecordMediaScreen(
        state = state,
        onClickRecorder = { controller ->
            if (recording != null) {
                viewModel.onEvent(RecordMediaEvent.StopRecord)
                recording?.stop()
                recording = null
                return@RecordMediaScreen
            }

            val outputFile = File(context.cacheDir, "my-recording-${Instant.now().epochSecond}.mp4")
            recording = controller.startRecording(
                FileOutputOptions.Builder(outputFile).build(),
                AudioConfig.create(true),
                ContextCompat.getMainExecutor(context.applicationContext),
            ) { event ->
                when (event) {
                    is VideoRecordEvent.Finalize -> {
                        if (event.hasError()) {
                            Toast.makeText(context, "에러 발생", Toast.LENGTH_SHORT).show()
                            recording?.close()
                            recording = null
                        } else {
                            Toast.makeText(context, "녹화 성공", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is VideoRecordEvent.Start -> {
                        Toast.makeText(context, "녹화 시작", Toast.LENGTH_SHORT).show()
                        viewModel.onEvent(RecordMediaEvent.StartRecord)
                    }
                }
            }
        }
    )
}

@Composable
fun RecordMediaScreen(
    modifier: Modifier = Modifier,
    state: RecordMediaState,
    onClickRecorder: (LifecycleCameraController) -> Unit
) {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.controller = controller
                        controller.bindToLifecycle(lifecycleOwner)
                    }
                }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = if(state.isRecord) Color.Red else Color.White)
                    .clickable {
                        onClickRecorder(controller)
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "00:04 / 04:00"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "이미지 리스트들"
                    )
                }

                Button(
                    onClick = {}
                ) {
                    Text(
                        text = "완료"
                    )
                }
            }
        }
    }
}

private fun recordVideo(controller: LifecycleCameraController) {

}

@Preview
@Composable
fun RecordMediaScreenPreview() {
    RecordMediaScreen(
        state = RecordMediaState(),
        onClickRecorder = {}
    )
}