package com.ssu.project.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String email;

    @Builder
    public MemberUpdateRequestDto(String email) {
        this.email = email;
    }
}
