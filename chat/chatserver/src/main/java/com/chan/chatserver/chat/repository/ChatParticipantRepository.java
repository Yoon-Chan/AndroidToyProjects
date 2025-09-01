package com.chan.chatserver.chat.repository;

import com.chan.chatserver.chat.domain.ChatParticipant;
import com.chan.chatserver.chat.domain.ChatRoom;
import com.chan.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    Optional<ChatParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
