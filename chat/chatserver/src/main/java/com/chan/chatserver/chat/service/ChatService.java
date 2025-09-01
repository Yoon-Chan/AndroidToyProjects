package com.chan.chatserver.chat.service;

import com.chan.chatserver.chat.repository.ChatMessageRepository;
import com.chan.chatserver.chat.repository.ChatParticipantRepository;
import com.chan.chatserver.chat.repository.ChatRoomRepository;
import com.chan.chatserver.chat.repository.ReadStatusRepository;
import com.chan.chatserver.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
