package com.ssu.project.domain.item;

import com.ssu.project.domain.category.ChildCategory;
import com.ssu.project.domain.category.ParentCategory;
import com.ssu.project.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String brand;

    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private long price;

    @Column(length = 500)
    private String code;

    @Column(length = 500)
    private String img_name;

    @Convert(converter = ItemImageListConverter.class)
    @Column(length = 2048)
    private List<String> urls = new ArrayList<>();

    @Convert(converter = ItemImageListConverter.class)
    @Column(length = 2048)
    private List<String> sizes = new ArrayList<>();

    private int liked;

    @ManyToOne(cascade = CascadeType.ALL)
    private ParentCategory parentCategory;

    @ManyToOne(cascade = CascadeType.ALL)
    private ChildCategory childCategory;

    @OneToMany(mappedBy = "item")
    private List<Review> review  = new ArrayList<>();;

    /*
        @Transient
            엔티티 객체의 데이터와 테이블의 컬럼(column)과
            매핑하고 있는 관계를 제외하기 위해 사용합니다.
     */
    @Transient
    private String mainUrl;
}

/*
    왜 Single_Tale ???
    슈퍼타입 서브타입 논리 모델 -> 물리모델 구현 방법
객체는 상속을 지원하므로 모델링과 구현이 똑같지만, DB는 상속을 지원하지 않으므로 논리 모델을 물리 모델로 구현할 방법이 필요하다.

DB의 슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법은 세가지 있다.

중요한건, DB입장에서 세가지로 구현하지만 JPA에서는 어떤 방식을 선택하던 매핑이 가능하다.

JPA가 이 세가지 방식과 매핑하려면

@Inheritance(strategy=InheritanceType.XXX)의 stategy를 설정해주면 된다.

default 전략은 SINGLE_TABLE(단일 테이블 전략)이다.

InheritanceType 종류

JOINED

SINGLE_TABLE

TABLE_PER_CLASS

@DiscriminatorColumn(name="DTYPE")

부모 클래스에 선언한다. 하위 클래스를 구분하는 용도의 컬럼이다. 관례는 default = DTYPE

@DiscriminatorValue("XXX")

하위 클래스에 선언한다. 엔티티를 저장할 때 슈퍼타입의 구분 컬럼에 저장할 값을 지정한다.

어노테이션을 선언하지 않을 경우 기본값으로 클래스 이름이 들어간다.

 */