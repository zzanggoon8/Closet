package com.ssu.project.dto.social;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Profile {
    String id;
    String email;

    @Builder
    public Profile(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
