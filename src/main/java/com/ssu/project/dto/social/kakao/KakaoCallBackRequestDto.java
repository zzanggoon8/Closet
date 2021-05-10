package com.ssu.project.dto.social.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoCallBackRequestDto {
    String code;

    @Builder
    public KakaoCallBackRequestDto(String code) {
        this.code = code;
    }
}
