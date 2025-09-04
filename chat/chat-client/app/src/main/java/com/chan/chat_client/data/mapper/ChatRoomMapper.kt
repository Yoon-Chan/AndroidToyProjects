package com.chan.chat_client.data.mapper

import com.chan.chat_client.data.model.GroupChatRoomDto
import com.chan.chat_client.data.model.MyChatRoomDto
import com.chan.chat_client.domain.model.GroupChatRoom
import com.chan.chat_client.domain.model.MyChatRoom

fun MyChatRoomDto.toDomain(): MyChatRoom {
    return MyChatRoom(
        id = roomId,
        roomName = roomName,
        isGroupChat = isGroupChat == "Y",
        unReadCount = unReadCount
    )
}

fun GroupChatRoomDto.toDomain(): GroupChatRoom {
    return GroupChatRoom(
        roomId = roomId,
        roomName = roomName
    )
}