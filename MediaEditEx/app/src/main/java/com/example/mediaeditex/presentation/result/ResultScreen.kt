package com.example.mediaeditex.presentation.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediaeditex.presentation.SimpleVideoPlayer
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme

@Composable
fun ResultScreenRoot(modifier: Modifier = Modifier, uri: String) {
    ResultScreen(modifier = modifier, uri= uri)
}

@Composable
fun ResultScreen(modifier: Modifier = Modifier, uri: String) {
    SimpleVideoPlayer(modifier = modifier, videoUrl = uri)
}

@Preview
@Composable
private fun ResultScreenPreview() {
    MediaEditExTheme {
        ResultScreen(
            uri = ""
        )
    }
}