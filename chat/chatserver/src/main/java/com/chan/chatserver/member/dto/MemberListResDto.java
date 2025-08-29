package com.chan.chatserver.member.dto;

import com.chan.chatserver.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResDto {
    private Long id;
    private String name;
    private String email;

    public static MemberListResDto fromMember(Member member) {
        return new MemberListResDto(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
