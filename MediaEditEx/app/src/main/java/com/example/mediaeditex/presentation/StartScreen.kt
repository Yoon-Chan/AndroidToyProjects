package com.example.mediaeditex.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediaeditex.presentation.ui.theme.MediaEditExTheme

@Composable
fun StartScreenRoot(
    onClickMixingMusic: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onClickMixingMusic) {
            Text(
                text = "음향 합성하기"
            )
        }

    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    MediaEditExTheme {
        StartScreenRoot(
            onClickMixingMusic = {}
        )
    }
}