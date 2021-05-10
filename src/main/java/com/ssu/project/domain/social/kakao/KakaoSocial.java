package com.ssu.project.domain.social.kakao;

import com.ssu.project.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class KakaoSocial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String kakaoSocialId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Member member;

    @Column(length = 500)
    private String type;

    @Builder
    public KakaoSocial(String kakaoSocialId, Member member, String type) {
        this.kakaoSocialId = kakaoSocialId;
        this.member = member;
        this.type = type;
    }
}
