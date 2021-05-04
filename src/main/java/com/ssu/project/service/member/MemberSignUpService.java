package com.ssu.project.service.member;

import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.dto.member.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequiredArgsConstructor
public class MemberSignUpService implements Validator {
    private MemberRepository memberRepository;

    @Override public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberSignUpRequestDto.class);
    }

    @Override public void validate(Object target, Errors errors) {
//        MemberSignUpRequestDto signUpRequestDto = (MemberSignUpRequestDto) target;
//
//        if(memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
//            errors.rejectValue("email", // filed : 문제가 있는 field의 이름
//                    "duplicated.email", // errorCode : customizing(code를 보고 view에서 판단)
//                    new Object[]{signUpRequestDto.getEmail()}, // MessageFormat에 binding할 error messages
//                    "using email address. please enter another"); // defaultMessage
//        }
    }
}
