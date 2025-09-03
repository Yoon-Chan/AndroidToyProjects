package com.chan.chat_client.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.chat_client.presentation.model.login.LoginEffect
import com.chan.chat_client.presentation.model.login.LoginEvent
import com.chan.chat_client.presentation.model.login.LoginState
import com.chan.chat_client.presentation.util.ObserveAsEvents
import com.chan.chat_client.presentation.viewModel.LoginViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    goHome: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is LoginEffect.Toast -> {
                Toast.makeText(context.applicationContext, effect.message, Toast.LENGTH_SHORT)
                    .show()
            }

            LoginEffect.Home -> goHome()
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .heightIn(min = 36.dp),
                state = state.email,
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = { innerTextField ->
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = Color.LightGray)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.email.text.isEmpty()) {
                            Text(
                                text = "email",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            BasicSecureTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .heightIn(min = 36.dp),
                state = state.password,
                textObfuscationMode = TextObfuscationMode.Hidden,
                decorator = { innerTextField ->
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = Color.LightGray)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.password.text.isEmpty()) {
                            Text(
                                text = "password",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = { onEvent(LoginEvent.Login(state.email.text.toString(), state.password.text.toString())) },
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("로그인")
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = { onEvent(LoginEvent.CreateMember(state.email.text.toString(), state.password.text.toString())) },
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("회원가입")
            }
        }
    }
}