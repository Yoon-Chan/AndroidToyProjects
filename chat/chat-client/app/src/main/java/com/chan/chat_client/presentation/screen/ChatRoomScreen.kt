package com.chan.chat_client.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.chat_client.presentation.component.NewRoomDialog
import com.chan.chat_client.presentation.model.chatroom.ChatRoomEffect
import com.chan.chat_client.presentation.model.chatroom.ChatRoomEvent
import com.chan.chat_client.presentation.model.chatroom.ChatRoomState
import com.chan.chat_client.presentation.ui.theme.ChatclientTheme
import com.chan.chat_client.presentation.util.ObserveAsEvents
import com.chan.chat_client.presentation.viewModel.ChatRoomViewModel

@Composable
fun ChatRoomScreenRoot(
    viewModel: ChatRoomViewModel = hiltViewModel(),
    onChatDetail: (Long) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newRoomDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            ChatRoomEffect.NewChatRoom -> newRoomDialog = true
            is ChatRoomEffect.ToastMessage -> {
                Toast.makeText(context.applicationContext, effect.message, Toast.LENGTH_SHORT)
                    .show()
            }

            is ChatRoomEffect.OnChatDetail -> onChatDetail(effect.roomId)
        }
    }

    ChatRoomScreen(
        state = state,
        onEvent = viewModel::onEvent
    )

    if (newRoomDialog) {
        NewRoomDialog(
            onDismissRequest = { newRoomDialog = false },
            onClick = { viewModel.onEvent(ChatRoomEvent.NewGroupChatRoom(it)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    state: ChatRoomState,
    onEvent: (ChatRoomEvent) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            title = {
                Text(
                    text = "채팅 목록",
                    fontWeight = FontWeight.ExtraBold
                )
            },
            actions = {
                Button(
                    onClick = { onEvent(ChatRoomEvent.NewChatRoom) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "채팅 생성",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(3f),
                text = "Room"
            )

            Text(
                modifier = Modifier.weight(1f),
                text = "Action"
            )
        }
        HorizontalDivider()
        state.rooms.forEach { room ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(3f),
                    text = room.roomName
                )

                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onEvent(ChatRoomEvent.OnChatDetail(room.roomId)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("입장")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatRoomScreenPreview() {
    ChatclientTheme {
        ChatRoomScreen(
            state = ChatRoomState(),
            onEvent = {}
        )
    }
}