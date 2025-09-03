package com.chan.chat_client.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.chat_client.domain.model.User
import com.chan.chat_client.presentation.component.UserCard
import com.chan.chat_client.presentation.model.home.HomeState
import com.chan.chat_client.presentation.ui.theme.ChatclientTheme
import com.chan.chat_client.presentation.viewModel.HomeViewModel

@Composable
fun MainScreenRoot(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    MainScreen(
        state = state.value
    )
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: HomeState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "회원 목록",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "ID",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(2f),
                text = "이름",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(4f),
                text = "email",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(2f),
                text = "채팅",
                textAlign = TextAlign.Center
            )
        }
        HorizontalDivider()
        state.users.forEach { user ->
            UserCard(
                user = User(
                    id = user.id,
                    name = user.name,
                    email = user.email
                ),
                onClickChat = {}
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    ChatclientTheme {
        MainScreen(
            state = HomeState()
        )
    }
}