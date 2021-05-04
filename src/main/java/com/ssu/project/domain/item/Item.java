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

    @Transient
    private String mainUrl;
}