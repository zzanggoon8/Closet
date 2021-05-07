package com.ssu.project.dto.social.google;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GoogleProfile {
    String id;
    String email;

    @Builder
    public GoogleProfile(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
