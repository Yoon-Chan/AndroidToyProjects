package com.chan.chatserver.chat.dto;

import com.chan.chatserver.chat.domain.ChatParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyChatListResDto {
    private Long roomId;
    private String roomName;
    private String isGroupChat;
    private Long unReadCount;

    public static MyChatListResDto from(ChatParticipant chatParticipant, Long count) {
        return MyChatListResDto.builder()
                .roomId(chatParticipant.getChatRoom().getId())
                .roomName(chatParticipant.getChatRoom().getName())
                .isGroupChat(chatParticipant.getChatRoom().getIsGroupChat())
                .unReadCount(count)
                .build();
    }
}
