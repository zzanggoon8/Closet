package com.ssu.project.domain.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Setter
public class MemberUser extends User {
    private Member member;

    // constructor
    public MemberUser(Member member){
        super(member.getEmail(), member.getPassword(), List.of(member.getType()));
        this.member = member;
    }
}
