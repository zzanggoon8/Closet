package com.ssu.project.dto.member;

import com.ssu.project.domain.address.Address;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@NoArgsConstructor
public class MemberSignUpRequestDto {

    @Email
    @NotBlank @Length(min = 5, max = 40)
    @NotNull(message = "이메일은 Null 일 수 없습니다!")
    private String email;

    @NotNull(message = "비밀번호는 Null 일 수 없습니다!")
    private String password;

    private MemberStatus type;


    private String agreePolicy;

    @Enumerated
    private Address address;
//    private String emailVerified;

    @Builder
    public MemberSignUpRequestDto(String email, String password, MemberStatus type, Address address) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.address = address;
    }



    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .address(address)
                .type(MemberStatus.USER)
                .build();
    }
}

/*
    signup.html의 form 내용을 SignUpForm DTO로 포장 --> Validator 객체에 전달
    즉, 유효성 검증을 위한 DTO
    따라서 physical DB에 들어가지 않음

    *Lombok @Data annotation => data 역할을 할 class에게 주어짐
    자동 getter, setter 생성

    @Length : 문자열 길이, @NotBlank : null 과 "" 과 " " 모두 허용하지 않음.
*/