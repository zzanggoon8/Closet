package com.ssu.project.dto.social.kakao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KakaoProfile {
    String id;
    String email;

    @Builder
    public KakaoProfile(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
