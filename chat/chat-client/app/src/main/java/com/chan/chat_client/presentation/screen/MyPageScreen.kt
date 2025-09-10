package com.chan.chat_client.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.chat_client.presentation.model.mypage.MyPageState
import com.chan.chat_client.presentation.ui.theme.ChatclientTheme
import com.chan.chat_client.presentation.viewModel.MyPageViewModel

@Composable
fun MyPageScreenRoot(
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyPageScreen(
        state = state
    )
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    state: MyPageState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "내 채팅 목록",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        }

        state.rooms.forEach { room ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = room.roomName
                    )

                    if (room.unReadCount != 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color = Color.Magenta),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${room.unReadCount}",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text("입장")
                    }

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (room.isGroupChat) Color.Red else Color.Red.copy(
                                alpha = 0.5f
                            )
                        )
                    ) {
                        Text("나가기")
                    }
                }
            }
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MyPageScreenPreview() {
    ChatclientTheme {
        MyPageScreen(
            state = MyPageState()
        )
    }
}