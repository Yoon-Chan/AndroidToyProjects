package com.chan.chat_client.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NewRoomDialog(
    onDismissRequest: () -> Unit,
    onClick: (String) -> Unit
) {
    var roomName by remember {
        mutableStateOf("")
    }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "채팅방 이름"
            )

            BasicTextField(
                value = roomName,
                onValueChange = { roomName = it },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 40.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (roomName.isEmpty()) {
                            Text(
                                text = "채팅방 이름을 입력해주세요.",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if(roomName.isNotEmpty()) {
                            onClick(roomName)
                            onDismissRequest()
                        }
                    }
                ) {
                    Text("생성")
                }
            }
        }
    }
}