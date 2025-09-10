package com.chan.chatserver.chat.controller;

import com.chan.chatserver.chat.dto.ChatMessageReqDto;
import com.chan.chatserver.chat.service.ChatService;
import com.chan.chatserver.chat.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class StompController {

    private final RedisPubSubService pubSubService;
    private final ChatService chatService;

    public StompController(RedisPubSubService pubSubService, ChatService chatService) {
        this.pubSubService = pubSubService;
        this.chatService = chatService;
    }

    //    @MessageMapping("/{roomId}") //클라이언트에서 특정 /publish/roomId 형태로 메시지를 발행 시 MessageMapping 수신
//    @SendTo("/topic/{roomId}") //해당 RoomId에 메시지를 발행하여, 구독중인 클라이언트에게 메시지 전송
//    // @DestinationVariable : @MessageMapping 어노테이션으로 정의된 Websocket Controller 내에서만 적용
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        log.info("sendMessage: {}", message);
//        return message;
//    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto message) throws JsonProcessingException {
        log.info("sendMessage: {}", message.getMessage());
        chatService.saveMessage(roomId, message);
//        mssageTemplate.convertAndSend("/topic/" + roomId, message);
        message.setRoomId(roomId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(message);
        pubSubService.publish("chat", jsonMessage);
    }
}
