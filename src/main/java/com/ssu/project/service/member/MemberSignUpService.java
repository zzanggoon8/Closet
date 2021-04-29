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

    // * Validator -> original Spring Framework interface
    // supports, validate를 override해줘야 함.

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberSignUpRequestDto.class);
        // paramerter가 SignUpForm의 class type을 소화할 수 있는지 검증
    }

    /*
    SignUpForm의 유효성 검사
    default : SignUpForm에 선언한 Annotation 내용
              @NotBlank, @Length, @Pattern 등의 annotation을 검사
    +) validate() method run(annotation으로 진행할 수 없는 추가적인 검사)
       DB를 거쳐야 하는 검사인 경우 validate()를 활용한다.
     */
    @Override
    public void validate(Object target, Errors errors) {
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
