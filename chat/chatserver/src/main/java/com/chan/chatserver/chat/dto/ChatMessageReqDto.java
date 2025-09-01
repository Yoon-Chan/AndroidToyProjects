package com.chan.chatserver.chat.dto;

import com.chan.chatserver.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReqDto {
    private String message;
    private String senderEmail;

    public static ChatMessageReqDto from(ChatMessage chatMessage) {
        return new ChatMessageReqDto(
                chatMessage.getContent(),
                chatMessage.getMember().getEmail()
        );
    }
}
