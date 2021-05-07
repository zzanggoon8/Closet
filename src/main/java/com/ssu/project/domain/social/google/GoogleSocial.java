package com.ssu.project.domain.social.google;

import com.ssu.project.domain.BaseTimeEntity;
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
public class GoogleSocial  extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 500)
    private String googleSocialId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Member member;

    @Column(length = 500)
    private String type;

    @Builder
    public GoogleSocial(String googleSocialId, Member member, String type) {
        this.googleSocialId = googleSocialId;
        this.member = member;
        this.type = type;
    }
}
