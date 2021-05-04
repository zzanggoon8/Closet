package com.ssu.project.controller.member;

import com.ssu.project.dto.member.MemberSignUpRequestDto;
import com.ssu.project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/member")
    public Long memberSignUp(@RequestBody MemberSignUpRequestDto requestDto) {
        return memberService.save(requestDto);
    }
}
