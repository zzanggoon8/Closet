package com.ssu.project.service.member;

import com.ssu.project.domain.email.EmailCheckStatus;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberEmailService {

    private final MemberRepository memberRepository;

    @Transactional
    public EmailCheckStatus processSignUp(String email, String token) {
        Member member = memberRepository.findByEmail(email);

        // member에 값이 할당되지 않은 경우
        if(member == null){
            return EmailCheckStatus.WRONG_EMAIL;
        }

        if(member.getEmailCheckToken().equals(token)){

            if(member.isEmailVerified()){
                return EmailCheckStatus.MODIFIED;
            }

            return EmailCheckStatus.COMPLETE;
        }
        return EmailCheckStatus.WRONG_TOKEN;
    }

    public boolean checkEmailToken(String email, String token) {
        if(email == null || token == null){
            return false;
        }

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            return false;
        }

        return token.equals(member.getEmailCheckToken());
    }

}
