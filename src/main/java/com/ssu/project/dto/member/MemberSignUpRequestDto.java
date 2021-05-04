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

    @Enumerated
    private Address address;

//    private String emailVerified;
    private MemberStatus type;
    private String agreePolicy;

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