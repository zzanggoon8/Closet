package com.ssu.project.dto.social.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoToken {
    String token;

    @Builder
    public KakaoToken(String token) {
        this.token = token;
    }
}

