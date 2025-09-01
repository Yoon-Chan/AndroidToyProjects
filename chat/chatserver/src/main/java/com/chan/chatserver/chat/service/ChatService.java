package com.chan.chatserver.chat.service;

import com.chan.chatserver.chat.domain.ChatMessage;
import com.chan.chatserver.chat.domain.ChatParticipant;
import com.chan.chatserver.chat.domain.ChatRoom;
import com.chan.chatserver.chat.domain.ReadStatus;
import com.chan.chatserver.chat.dto.ChatMessageReqDto;
import com.chan.chatserver.chat.dto.ChatRoomListResDto;
import com.chan.chatserver.chat.repository.ChatMessageRepository;
import com.chan.chatserver.chat.repository.ChatParticipantRepository;
import com.chan.chatserver.chat.repository.ChatRoomRepository;
import com.chan.chatserver.chat.repository.ReadStatusRepository;
import com.chan.chatserver.member.domain.Member;
import com.chan.chatserver.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        for (ChatParticipant chatParticipant : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .member(chatParticipant.getMember())
                    .chatMessage(chatMessage)
                    .isRead(chatParticipant.getMember().equals(sender))
                    .build();
            readStatusRepository.save(readStatus);
        }
    }

    public void creatGroupRoom(String roomName) {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("member can't bean found"));

        //채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .isGroupChat("Y")
                .build();
        chatRoomRepository.save(chatRoom);

        //채팅참여자로 개설자 추가
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatRoomListResDto> getGroupChatRooms() {
        return chatRoomRepository.findByIsGroupChat("Y").stream().map(ChatRoomListResDto::from).toList();
    }

    public void addParticipantToGroupChat(Long roomId) {
        //채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));

        //Member조회
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("member can't bean found"));

        //이미 참여자인지 검증
        //ChatParticipant 객체 생성 후 저장
        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);
        if (participant.isEmpty()) {
            addParticipantToRoom(chatRoom, member);
        }
    }

    public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageReqDto> getChatHistory(Long roomId) {
        //내가 해당 채팅방의 참여자가 아닐 경우 에러 발생
        //채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));

        //보낸 사람이 누군지
        Member sender = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for (ChatParticipant chatParticipant : chatParticipants) {
            if (chatParticipant.getMember().equals(sender)) {
                check = true;
                break;
            }
        }
        if (!check) throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
        return chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom).stream().map(ChatMessageReqDto::from).toList();
    }

    public boolean isRoomParticipant(String email, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));

        //보낸 사람이 누군지
        Member sender = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (ChatParticipant chatParticipant : chatParticipants) {
            if (chatParticipant.getMember().equals(sender)) {
                return true;
            }
        }
        return false;
    }
}
