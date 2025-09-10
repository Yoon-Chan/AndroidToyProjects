package com.chan.chat_client.presentation.screen

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailEffect
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailEvent
import com.chan.chat_client.presentation.model.chatdetail.ChatDetailState
import com.chan.chat_client.presentation.util.ObserveAsEvents
import com.chan.chat_client.presentation.viewModel.ChatDetailViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ChatDetailScreenRoot(
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val isAtBottom by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            val lastIndex = info.totalItemsCount - 1
            lastIndex - lastVisible <= 1 // 바닥 근처면 true
        }
    }

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            ChatDetailEffect.ScrollToBottom -> {
                if (state.messages.isNotEmpty() && isAtBottom) {
                    Timber.e("LaunchedEffect state.messages ${state.messages.last()}")
                    scope.launch {
                        snapshotFlow { listState.layoutInfo.totalItemsCount }
                            .first { it >= state.messages.size }
                        listState.scrollToItem(state.messages.lastIndex , scrollOffset = 0)
                    }
                }
            }
        }
    }

    ChatDetailScreen(
        state = state,
        lazyListState = listState,
        onEvent = viewModel::onEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatDetailScreen(
    modifier: Modifier = Modifier,
    state: ChatDetailState,
    lazyListState: LazyListState,
    onEvent: (ChatDetailEvent) -> Unit
) {
    var maxHeight by remember {
        mutableStateOf(0)
    }
    var changeHeight by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(changeHeight) {
        Timber.e("LaunchedEffect changeHeight $changeHeight")
        val delta = (maxHeight - changeHeight).toFloat()
        lazyListState.scroll(MutatePriority.PreventUserInput) {
            val consumed = scrollBy((delta).toFloat())
            maxHeight = (changeHeight + (delta.toInt() - consumed.toInt()))
            Timber.e("delta $delta consumed $consumed maxHeight $maxHeight")
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("채팅 디테일방")
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onSizeChanged { size ->
                        if (maxHeight == 0) maxHeight = size.height
                        changeHeight = size.height
                        Timber.e("onSizeChange changeHeight $changeHeight")
                    },
                state = lazyListState
            ) {
                items(state.messages.size) {
                    val message = state.messages[it]
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        text = "${message.senderEmail} : ${message.message}",
                        textAlign = if (message.senderEmail == state.email) TextAlign.End else TextAlign.Start
                    )
                }
            }

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .imePadding(),
                value = state.text,
                onValueChange = { onEvent(ChatDetailEvent.OnValueChange(it)) },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 36.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }

                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = { onEvent(ChatDetailEvent.SendMessage(state.text)) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    }
}