package com.chan.chatserver.chat.service;

import com.chan.chatserver.chat.dto.ChatMessageReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisPubSubService implements MessageListener {

    private final SimpMessageSendingOperations mssageTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisPubSubService(SimpMessageSendingOperations mssageTemplate, @Qualifier("chatPubSub") StringRedisTemplate stringRedisTemplate) {
        this.mssageTemplate = mssageTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    @Override
    //pattern에는 topic의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatMessageReqDto chatMessageReqDto = objectMapper.readValue(payload, ChatMessageReqDto.class);
            mssageTemplate.convertAndSend("/topic/" + chatMessageReqDto.getRoomId(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
