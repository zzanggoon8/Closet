package com.ssu.project.domain.member;

import com.ssu.project.domain.address.Address;
import com.ssu.project.domain.BaseTimeEntity;
import com.ssu.project.domain.cody.Cody;
import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.order.Orders;
import com.ssu.project.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    /*
        id, email, password
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String email;

    @Column(length = 500)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberStatus type;

    @Enumerated
    private Address address;

    private boolean emailVerified;

    private String emailCheckToken; // email 인증 시 필요한 email Token value

    // MemberSignUpRequestDto
    @Builder
    public Member(String email, String password, MemberStatus type, boolean emailVerified, Address address) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.address = address;
        this.emailVerified = emailVerified;
    }

    // MemberUpdateRequestDto
    @Transactional
    public void update(String email) {
        this.email = email;
    }

    @Transactional
    public void generateEmailCheckToken() {
        /*
        @Transactional
        persistence Context 사용 시 transcation 위에서 수행해야 한다.
        따라서 JpaRepository를 상속받은 interface를 통해 수정하지 않고 값을 변경할 경우 해당 annotation을 사용한다.
        이 경우 method 시작 시점에 transaction이 begin되고, exception이 나지 않을 경우 commit된다(exception 발생 시 rollback)
         */
        //email token value 생성
        emailCheckToken = UUID.randomUUID().toString();
    }
//
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

}