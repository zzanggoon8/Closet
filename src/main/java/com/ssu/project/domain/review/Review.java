package com.ssu.project.domain.review;

import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.order.Orders;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Orders order;

    private String title;
    private String content;
    private String img;
    private Long parentId;
    private String createDate;
    private String updateDate;
}
