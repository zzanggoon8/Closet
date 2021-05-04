package com.ssu.project.domain.social;

import com.ssu.project.domain.BaseTimeEntity;
import com.ssu.project.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Social  extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 500)
    private String socialId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Member member;

    @Column(length = 500)
    private String type;

    @Builder
    public Social(String socialId, Member member, String type) {
        this.socialId = socialId;
        this.member = member;
        this.type = type;
    }
}
