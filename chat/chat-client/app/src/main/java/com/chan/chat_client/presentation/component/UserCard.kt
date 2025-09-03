package com.chan.chat_client.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chan.chat_client.domain.model.User
import com.chan.chat_client.presentation.ui.theme.ChatclientTheme

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: User,
    onClickChat: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "${user.id}",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = user.name,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(4f),
            text = user.email,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.weight(3f),
            onClick = onClickChat,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "채팅하기")
        }
    }
}

@Preview
@Composable
private fun UserCardPreview() {
    ChatclientTheme {
        UserCard(
            user = User(
                id = 1,
                name = "chan",
                email = "test123@naver.con"
            ),
            onClickChat = {}
        )
    }
}