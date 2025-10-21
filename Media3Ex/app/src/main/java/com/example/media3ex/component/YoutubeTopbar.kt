package com.example.media3ex.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.media3ex.ui.theme.Media3ExTheme

@Composable
fun YoutubeTopbar(
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(height),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "유튜브 클론 코딩"
        )
    }
}

@Preview
@Composable
private fun YoutubeTopbarPreview() {
    Media3ExTheme {
        YoutubeTopbar(
            modifier = Modifier
        )
    }
}