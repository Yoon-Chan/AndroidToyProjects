package com.chan.chat_client.data.mapper

import com.chan.chat_client.data.model.ChatRoomDto
import com.chan.chat_client.domain.model.ChatRoom

fun ChatRoomDto.toDomain(): ChatRoom {
    return ChatRoom(
        id = roomId,
        roomName = roomName,
        isGroupChat = isGroupChat == "Y",
        unReadCount = unReadCount
    )
}