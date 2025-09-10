package com.chan.chat_client.presentation.model.mypage

import com.chan.chat_client.domain.model.MyChatRoom

data class MyPageState(
    val rooms: List<MyChatRoom> = emptyList<MyChatRoom>()
)
