package com.example.mediaeditex.presentation.mixing

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mediaeditex.presentation.mixing.model.MixingMusicEffect
import com.example.mediaeditex.presentation.mixing.model.MixingMusicEvent
import com.example.mediaeditex.presentation.mixing.model.MixingMusicState
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme
import com.example.mediaeditex.presentation.util.ObserveAsEvents

@Composable
fun MixingMusicScreenRoot(
    viewModel: MixingMusicViewModel = hiltViewModel(),
    onResult: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.e("vsvx13", "video $uri")
        uri?.let {
            viewModel.onEvent(MixingMusicEvent.OnChangeMedia(it.toString()))
        }
    }

    val musicPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        Log.e("vsvx13", "music $uri")
        uri?.let {
            viewModel.onEvent(MixingMusicEvent.OnChangeMusic(it.toString()))
        }
    }

    ObserveAsEvents(viewModel.effect) { effect ->
        when(effect) {
            is MixingMusicEffect.ResultScreen -> onResult(effect.uri)
        }
    }

    MixingMusicScreen(
        state = state,
        onEvent = {
            when(it) {
                MixingMusicEvent.OnClickAudioSelect -> {
                    musicPickerLauncher.launch("audio/*")
                }
                MixingMusicEvent.OnClickMediaSelect -> {
                    mediaPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                }
                else -> Unit
            }
            viewModel.onEvent(it)
        }
    )
}

@Composable
fun MixingMusicScreen(
    modifier: Modifier = Modifier,
    state: MixingMusicState,
    onEvent: (MixingMusicEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onEvent(MixingMusicEvent.OnClickMediaSelect)}) {
            Text(text = "영상 선택")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = state.mediaUri ?: "미디어를 선택하지 않았습니다.")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onEvent(MixingMusicEvent.OnClickAudioSelect) }) {
            Text(text = "음악 선택")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = state.musicUri ?: "음악을 선택하지 않았습니다.")

        Spacer(modifier = Modifier.height(36.dp))
        Button(onClick = { onEvent(MixingMusicEvent.OnClickMixing) }) {
            Text(text = "합성하기")
        }
    }
}

@Preview
@Composable
private fun MixingMusicScreenPreview() {
    MediaEditExTheme {
        MixingMusicScreen(
            state = MixingMusicState(),
            onEvent = {}
        )
    }
}