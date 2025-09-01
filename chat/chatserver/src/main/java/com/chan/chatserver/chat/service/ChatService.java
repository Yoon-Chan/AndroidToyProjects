package com.chan.chatserver.chat.service;

import com.chan.chatserver.chat.domain.ChatMessage;
import com.chan.chatserver.chat.domain.ChatParticipant;
import com.chan.chatserver.chat.domain.ChatRoom;
import com.chan.chatserver.chat.domain.ReadStatus;
import com.chan.chatserver.chat.dto.ChatMessageReqDto;
import com.chan.chatserver.chat.repository.ChatMessageRepository;
import com.chan.chatserver.chat.repository.ChatParticipantRepository;
import com.chan.chatserver.chat.repository.ChatRoomRepository;
import com.chan.chatserver.chat.repository.ReadStatusRepository;
import com.chan.chatserver.member.domain.Member;
import com.chan.chatserver.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;

    public ChatService(
            ChatRoomRepository chatRoomRepository,
            ChatParticipantRepository chatParticipantRepository,
            ChatMessageRepository chatMessageRepository,
            ReadStatusRepository readStatusRepository,
            MemberRepository memberRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
        this.memberRepository = memberRepository;
    }

    public void saveMessage(Long roomId, ChatMessageReqDto message) {
        //채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));

        //보낸 사람이 누군지
        Member sender = memberRepository.findByEmail(message.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        //메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(message.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);

        //사용자별로 읽음 여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant chatParticipant : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .member(chatParticipant.getMember())
                    .chatMessage(chatMessage)
                    .isRead(chatParticipant.getMember().equals(sender))
                    .build();
            readStatusRepository.save(readStatus);
        }
    }
}
