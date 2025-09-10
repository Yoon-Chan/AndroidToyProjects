package com.chan.chat_client.presentation.model.mypage

sealed interface MyPageEvent {
    object GetMyRoom : MyPageEvent
}