package com.ssu.project.service.member;

import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.domain.member.MemberStatus;
import com.ssu.project.domain.member.MemberUser;
import com.ssu.project.dto.member.MemberListResponseDto;
import com.ssu.project.dto.member.MemberSignUpRequestDto;
import com.ssu.project.dto.member.MemberUpdateRequestDto;
import com.ssu.project.dto.member.MemberUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RestController
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public  Long save(MemberSignUpRequestDto requestDto) {
        return memberRepository.save(requestDto.toEntity()).getId();
    }

    // 전체조회
    @Transactional(readOnly = true)
    public List <MemberListResponseDto> findAllDesc() {
        return memberRepository.findAllDesc().stream()
                .map(MemberListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 수정조회
    public MemberUpdateResponseDto findById(Long id) {
        Member entity = memberRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("해당 아이디가 없습니다. id=" + id));
        return new MemberUpdateResponseDto(entity);
    }

    // 수정기능
    @Transactional
    public Long update(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("해당 아이디가 없습니다." + id));
        member.update(requestDto.getEmail());
        return id;
    }

    public Member createNewMember(MemberSignUpRequestDto memberSignUpRequestDto) {
        Member member = Member.builder()
                .email(memberSignUpRequestDto.getEmail())
                .password(memberSignUpRequestDto.getPassword())
                .type(MemberStatus.USER)
                .address(memberSignUpRequestDto.getAddress())
                .build();

        // email 검증 token 생성 -> DB에 저장
        member.generateEmailCheckToken();
//        member.encodePassword(passwordEncoder); // pw를 인코딩하여 저장

        memberRepository.save(member);
        return member;
    }

    public void autologin(Member member) {
        MemberUser memberUser = new MemberUser(member);
        // Certification Token create
        // SecurityContext는 인증 유저(현재 login status인 user들의 정보)를 관리한다.
        // user의 구별은 AuthenticationToken을 통해 이루어진다.
        // Token 구조 : username, password, role
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser, // username
                        memberUser.getMember().getPassword(), // password
                        memberUser.getAuthorities() // authorities(data type : Collection)
                );

        // SecurityContext에 token 저장
        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(token); // 인증 완료
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);

        if(member == null){
            throw new UsernameNotFoundException(username);
        }

        return new MemberUser(member);
    }
}
