package com.ssu.project.domain.member;

import com.ssu.project.domain.BaseTimeEntity;
import com.ssu.project.domain.address.Address;
import com.ssu.project.domain.cody.Cody;
import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.order.Orders;
import com.ssu.project.domain.review.Review;
//import com.ssu.project.domain.social.Social;
import com.ssu.project.domain.social.Social;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 500)
    private String email;

    @Column (length = 500)
    private String password;

    @Enumerated (EnumType.STRING)
    private MemberStatus type;

    @Enumerated private Address address;

    private boolean emailVerified;

    private String emailCheckToken;

    /**
     * 회원가입
     * @param email
     * @param password
     * @param type
     * @param emailVerified
     * @param address
     */
    @Builder
    public Member(String email, String password, MemberStatus type, boolean emailVerified, Address address) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.address = address;
        this.emailVerified = emailVerified;
    }

    /**
     * 회원정보 수정
    * @param email
     */
    @Transactional
    public void update(String email) {
        this.email = email;
    }

    @Transactional
    public void generateEmailCheckToken() {
        //email token value 생성
        emailCheckToken = UUID.randomUUID().toString();
    }

//    @Transactional
//    public void encodePassword(PasswordEncoder passwordEncoder){
//        password = passwordEncoder.encode(password);
//    }

    @OneToMany(mappedBy = "member") // Orders와 1:N 양방향
    private List<Orders> orders = new ArrayList<>();

    @OneToMany // Member : Item => 1 : N Relationship(단방향)
    private List<Item> likes = new ArrayList<>();
    // convention : NullPointerException 방지

    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Cody> codies = new ArrayList<>();

    @OneToMany // Member : Cody => 1 : N Relationship(단방향)
    private List<Cody> codyLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Social> socials = new ArrayList<>();
}