package com.ssu.project.domain.cart;

import com.ssu.project.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cartCount;

    @ManyToOne
    private Member member;
}
