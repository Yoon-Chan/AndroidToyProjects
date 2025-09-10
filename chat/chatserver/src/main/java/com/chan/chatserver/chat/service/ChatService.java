package com.chan.chatserver.chat.service;

import com.chan.chatserver.chat.domain.ChatMessage;
import com.chan.chatserver.chat.domain.ChatParticipant;
import com.chan.chatserver.chat.domain.ChatRoom;
import com.chan.chatserver.chat.domain.ReadStatus;
import com.chan.chatserver.chat.dto.ChatMessageReqDto;
import com.chan.chatserver.chat.dto.ChatRoomListResDto;
import com.chan.chatserver.chat.dto.MyChatListResDto;
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

    public Long creatGroupRoom(String roomName) {
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
        return chatRoom.getId();
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

    public void messageRead(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));
        //보낸 사람이 누군지
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom, member);
        for (ReadStatus readStatus : readStatuses) {
            readStatus.updateIsRead(true);
        }
    }

    public List<MyChatListResDto> getMyChatRooms() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        return chatParticipants.stream().map(chatParticipant -> {
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(chatParticipant.getChatRoom(), member);
            return MyChatListResDto.from(chatParticipant, count);
        }).toList();
    }

    //채팅방 나가기
    public void leaveGroupChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room can't bean found"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        if (chatRoom.getIsGroupChat().equals("N")) throw new IllegalArgumentException("단체 채팅방이 아닙니다.");
        ChatParticipant participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).orElseThrow(() -> new EntityNotFoundException("participant can't bean found"));
        chatParticipantRepository.delete(participant);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if (chatParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public Long getOrCreatePrivateRoom(Long otherMemberId) {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(() -> new EntityNotFoundException("sender can't bean found"));

        //나와 상대방이 이미 1:1 채팅하고 있으면 해당 roomId return
        Optional<ChatRoom> chatRoom = chatParticipantRepository.findChatRoomIdExistingPrivateRoom(member.getId(), otherMember.getId());

        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        //없을 경우 채팅방 개설
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(member.getName() + " - " + otherMember.getName())
                .build();
        chatRoomRepository.save(newRoom);

        //두 사람 모두 참여자로 추가
        addParticipantToRoom(newRoom, member);
        addParticipantToRoom(newRoom, otherMember);
        return newRoom.getId();
    }
}
