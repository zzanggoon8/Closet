package com.ssu.project.dto.social.google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleToken {
    String token;

    @Builder
    public GoogleToken(String token) {
        this.token = token;
    }
}
