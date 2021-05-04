package com.ssu.project.dto.member;

import com.ssu.project.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberListResponseDto {
    private Long id;
    private String email;
    private LocalDateTime modifiedDate;

    public MemberListResponseDto(Member entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.modifiedDate = entity.getModifiedDate();
    }
}
