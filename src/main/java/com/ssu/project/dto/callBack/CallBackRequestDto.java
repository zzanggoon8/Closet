package com.ssu.project.dto.callBack;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CallBackRequestDto {
    String code;
    String state;
    String error;
    String error_description;

    @Builder
    public CallBackRequestDto(String code, String state, String error, String error_description) {
        this.code = code;
        this.state = state;
        this.error = error;
        this.error_description = error_description;
    }
}
