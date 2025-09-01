package com.chan.chatserver.chat.dto;

import com.chan.chatserver.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomListResDto {
    private Long roomId;
    private String roomName;

    public static ChatRoomListResDto from(ChatRoom room) {
        return new ChatRoomListResDto(
                room.getId(),
                room.getName()
        );
    }
}
